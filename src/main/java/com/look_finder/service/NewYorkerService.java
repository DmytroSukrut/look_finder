package com.look_finder.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.look_finder.components.new_yorker.CategoryIdFinderNewYorker;
import com.look_finder.components.new_yorker.NewYorkerParcer;
import com.look_finder.components.new_yorker.NewYorkerSizeSelector;
import okhttp3.*;
import org.springframework.stereotype.Service;

import javax.print.DocFlavor;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class NewYorkerService {

    private final NewYorkerParcer parcer;
    private final CategoryIdFinderNewYorker categoryFinder;
    private final NewYorkerSizeSelector  sizeSelector;

    private final ObjectMapper mapper;

    public NewYorkerService(NewYorkerParcer newYorkerParcer,
                            CategoryIdFinderNewYorker categoryFinder,
                            ObjectMapper mapper,
                            NewYorkerSizeSelector  sizeSelector) {
        this.parcer = newYorkerParcer;
        this.categoryFinder = categoryFinder;
        this.mapper = mapper;
        this.sizeSelector = sizeSelector;
    }

    public List<Map<String, Object>> getAndParseNewYorkerJSON(String category_, String sex, int bust, int waist, int hip) throws IOException {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        //category combiner
        switch (category_) {
            case "jackets+coats" -> category_ = "jackets";
            case "sweaters_and_cardigans+knitwear" -> category_ = "knitwear";
            case "jackets+puffer_jackets" -> category_ = "jackets";
            case "trousers+baggy_trousers" -> category_ = "trousers";
            case "shirts+polos" -> category_ = "shirts";
        }

        String s = categoryFinder.CreateUrl(category_, sex);

        String[] parts1 = s.split("\\\\");
        String orientation =  parts1[1];
        String parts = parts1[0];

        String[] parts_of_url = parts.split("\\+");


        String url_products_start = "";
        if(Objects.equals(sex, "f")){
            url_products_start = "https://api.newyorker.de/csp/products/public/query?limit=30&offset=0&filters%5Bcountry%5D=sk&filters%5Bgender%5D=FEMALE&filters%5Bbrand%5D=&filters%5Bcolor%5D=&filters%5Bweb_category%5D=";
        } else {
            url_products_start = "https://api.newyorker.de/csp/products/public/query?limit=30&offset=0&filters%5Bcountry%5D=sk&filters%5Bgender%5D=MALE&filters%5Bbrand%5D=&filters%5Bcolor%5D=&filters%5Bweb_category%5D=";
        }

        String url_products_end = "filters%5Blikes%5D=&filters%5Bcollections%5D=&filters%5Beditorials%5D=";

        String json_for_parser = "";

        List<String> unpolished_responses = new ArrayList<>();

        System.out.println(Arrays.toString(parts_of_url));

        for(int i = 0; i < parts_of_url.length; i++) {

            String url_products = url_products_start +
                    parts_of_url[i] +
                    url_products_end;

            Request products_req = new Request.Builder()
                    .url(url_products)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                            + "AppleWebKit/537.36 (KHTML, like Gecko) "
                            + "Chrome/118.0.0.0 Safari/537.36") // Header to tell that we are Google Chrome
                    .header("Accept", "application/json, text/plain, */*") // We are excepting all information from the endpoint
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .header("Referer", "https://www.bershka.com/") // We are "coming" from Bershka website
                    .header("Origin", "https://www.bershka.com") // We are "coming" from Bershka website
                    .header("Connection", "keep-alive")
                    .header("DNT", "1") // Don't track me!
                    // These 3 further headers just need to be (-_-)
                    .header("Sec-Ch-Ua", "\"Google Chrome\";v=\"118\", \"Chromium\";v=\"118\", \"Not=A?Brand\";v=\"99\"")
                    .header("Sec-Ch-Ua-Mobile", "?0")
                    .header("Sec-Fetch-Site", "same-origin")
                    .build();

            try (Response products_resp = client.newCall(products_req).execute()) {
                if (!products_resp.isSuccessful()) {
                    throw new IOException("Unexpected code " + products_resp.code());
                } else {
                    ResponseBody body = products_resp.body();
                    if (body == null) throw new IOException("Empty response body");

                    json_for_parser = body.string();
                    unpolished_responses.add(json_for_parser);
                }
            }
        }

        List<Map<String, Object>> products = new ArrayList<>();

        if (unpolished_responses.isEmpty()) {
            json_for_parser = "{\\\"items\\\":[]}";
        } else {
            ObjectNode root = (ObjectNode) mapper.readTree(unpolished_responses.get(0));

            ArrayNode mergedItems;
            JsonNode firstItemsNode = root.get("items");
            mergedItems = (ArrayNode) firstItemsNode;

            for (int i = 1; i < unpolished_responses.size(); i++) {

                JsonNode n = mapper.readTree(unpolished_responses.get(i));
                JsonNode items = n.get("items");
                mergedItems.addAll((ArrayNode) items);

            }

            json_for_parser = mapper.writeValueAsString(root);

        }

        String temp = sizeSelector.select_size(sex, orientation, bust, waist, hip);
        String[] temp_parts = temp.split("\\+");
        String error = temp_parts[0];
        String sizeD = temp_parts[1];
        String sizeS = temp_parts[2];


        products = parcer.parse(json_for_parser, sizeD, sizeS, error);

        Path jsonDir = Path.of("src/main/resources/json");
        Files.createDirectories(jsonDir);

        Path filePath = jsonDir.resolve("original_new_yorker.json");

        mapper = new ObjectMapper();
        Object jsonObj = mapper.readValue(json_for_parser, Object.class);
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        Files.writeString(filePath, writer.writeValueAsString(jsonObj));

        return products;
    }

}
