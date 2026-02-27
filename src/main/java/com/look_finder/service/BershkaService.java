package com.look_finder.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.look_finder.components.bershka.BershkaParcer;
import com.look_finder.components.bershka.UrlCreatorBershka;
import com.look_finder.components.bershka.CategoryIdFinderBershka;
import com.look_finder.components.bershka.BershkaSizeSelector;
import com.look_finder.position.PositionEntity;
import com.look_finder.position.PositionRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class BershkaService {

    private final BershkaParcer parcer;
    private final UrlCreatorBershka urlCreator;
    private final CategoryIdFinderBershka idFinder;
    private final BershkaSizeSelector  sizeSelector;
    private final PositionRepository repository;
    private final ObjectMapper mapper = new ObjectMapper();

    public BershkaService(BershkaParcer parcer, UrlCreatorBershka urlCreator, CategoryIdFinderBershka idFinder, BershkaSizeSelector sizeSelector, PositionRepository repository) {
        this.parcer = parcer;
        this.urlCreator = urlCreator;
        this.idFinder = idFinder;
        this.sizeSelector = sizeSelector;
        this.repository = repository;
    }

    /**
     * This function gets all Bershkas available products and then sends this
     * information back to our website.
     * First, it fetches Bershkas stocks and parses the response using "UrlCreatorBershka".
     * Second, it fetches real Bershkas endpoints sending all productIds to get all the info and then parses it using "BershkaParcer".
     * @param category_ string representing the category our user is looking for. Example: "jeans_w" - jeans woman
     * @return parsed JSON with all necessary information for display
     * @throws IOException if we have a problem fetching bershka data throws exception
     * */
    public List<List<Map<String, Object>>> getAndParseBershkaJSON(String category_, String sex, int bust, int waist, int hip) throws IOException {

        List<String> categorys = new ArrayList<>(Arrays.asList(category_.split("\\+")));
        List<List<Map<String, Object>>> all_bershka = new ArrayList<>();

        String error_msg = "none";
        Map<String, Object> error_map = new HashMap<>();

        for (int i = 0; i < categorys.size(); i++) {
            String category = categorys.get(i);
            String orientation = idFinder.findCategoryId(category, sex);

            String temp = sizeSelector.SelectSize(sex, orientation, bust, waist, hip);
            String[] temp_ = temp.split("\\+");

            String error = temp_[0];
            if(!error.equals("none")) {
                error_msg = error;
            }
            if(i == categorys.size() - 1) {
                error_map.put("origin", "Bershka");
                error_map.put("error", error_msg);
            }
        }

        for (String category : categorys) {
            System.out.println("category: " + category);
            String orientation = idFinder.findCategoryId(category, sex);
            System.out.println("orientation: " + orientation);

            String temp = sizeSelector.SelectSize(sex, orientation, bust, waist, hip);
            String[] temp_ = temp.split("\\+");

            String sizeD = temp_[1];
            String sizeS = temp_[2];

            List<Map<String, Object>> category_positions = new ArrayList<>();

            category_positions.add(error_map);

            for (String size : List.of(sizeD, sizeS)) {
                if(!Objects.equals(size, "error")) {
                    repository.findByCategoryAndSexAndSize(category, sex, size)
                            .stream()
                            .map(this::from_entity_to_map)
                            .forEach(category_positions::add);
                }
            }

            all_bershka.add(category_positions);
        }

        return all_bershka;

//        List<List<Map<String, Object>>> jsons_for_shuffle = new ArrayList<>();
//
//        List<String> categorys = new ArrayList<>(Arrays.asList(category_.split("\\+")));
//
//        warmUpSession();
//
//        ObjectMapper mapper = new ObjectMapper();
//        for (String category : categorys) {
//            String temp = idFinder.findCategoryId(category, sex);
//            String[] parts = temp.split("\\+");
//            String cat_id = parts[0];
//            String orientation = parts[1];
//
//            // Create url to correct stock
//            String url_stock = createUrlStock(cat_id);
//            // Create an HTTP client (This client can send requests to endpoints)
//            OkHttpClient client = new OkHttpClient();
//
//            // Create a request on our endpoint in Internet to get all available positions from bershka
//            // We are adding headers so the endpoint doesn't understand that we are making this request from code
//            Request stock_req = new Request.Builder()
//                    .url(url_stock)
//                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
//                            + "AppleWebKit/537.36 (KHTML, like Gecko) "
//                            + "Chrome/118.0.0.0 Safari/537.36") // Header to tell that we are Google Chrome
//                    .header("Accept", "application/json, text/plain, */*") // We are excepting all information from the endpoint
//                    .header("Accept-Language", "en-US,en;q=0.9")
//                    .header("Referer", "https://www.bershka.com/") // We are "coming" from Bershka website
//                    .header("Origin", "https://www.bershka.com") // We are "coming" from Bershka website
//                    .header("Connection", "keep-alive")
//                    .header("DNT", "1") // Don't track me!
//                    // These 3 further headers just need to be (-_-)
//                    .header("Sec-Ch-Ua", "\"Google Chrome\";v=\"118\", \"Chromium\";v=\"118\", \"Not=A?Brand\";v=\"99\"")
//                    .header("Sec-Ch-Ua-Mobile", "?0")
//                    .header("Sec-Fetch-Site", "same-origin")
//                    .build(); // End building a request
//
//            String response_body;
//            // Our Client sends our response, and we are waiting for the answer
//            try (Response stock_resp = client.newCall(stock_req).execute()) {
//                if (!stock_resp.isSuccessful()) { // Check if we got "200" any other = error
//                    throw new IOException("Unexpected code " + stock_resp.code()); // Tels me if we got bad response code
//                } else {
//                    ResponseBody body = stock_resp.body(); // Get what our request got
//                    if (body == null)
//                        throw new IOException("Empty response body"); // Check if the response body is empty (doesn't really need it, I think)
//
//                    // Interesting thing! If we don't say in the header that we except gzip, it won't send any! :)
//                    response_body = body.string();
//                }
//            }
//
//            /* We got our JSON from Bershka with stocks
//             * Then we send it our component urlCreator
//             * (for more explanation how it works, see the component "components/parcers/UrlCreatorBershka")
//             * In return from it, we get url (unexpected, yes? :)) with NO duplicates of product! How cool is that!!!
//             * */
//            String url_products = urlCreator.CreateUrl(response_body, cat_id);
//
//
//            // Same creating request, but to get all info for products we extracted previously
//            Request products_req = new Request.Builder()
//                    .url(url_products)
//                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
//                            + "AppleWebKit/537.36 (KHTML, like Gecko) "
//                            + "Chrome/118.0.0.0 Safari/537.36") // Header to tell that we are Google Chrome
//                    .header("Accept", "application/json, text/plain, */*") // We are excepting all information from the endpoint
//                    .header("Accept-Language", "en-US,en;q=0.9")
//                    .header("Referer", "https://www.bershka.com/") // We are "coming" from Bershka website
//                    .header("Origin", "https://www.bershka.com") // We are "coming" from Bershka website
//                    .header("Connection", "keep-alive")
//                    .header("DNT", "1") // Don't track me!
//                    // These 3 further headers just need to be (-_-)
//                    .header("Sec-Ch-Ua", "\"Google Chrome\";v=\"118\", \"Chromium\";v=\"118\", \"Not=A?Brand\";v=\"99\"")
//                    .header("Sec-Ch-Ua-Mobile", "?0")
//                    .header("Sec-Fetch-Site", "same-origin")
//                    .build(); // End building a request
//
//            // Same as before. Sends a request and gets a response
//            try (Response products_resp = client.newCall(products_req).execute()) {
//                if (!products_resp.isSuccessful()) {
//                    throw new IOException("Unexpected code " + products_resp.code());
//                } else {
//                    ResponseBody body = products_resp.body();
//                    if (body == null) throw new IOException("Empty response body");
//
//                    response_body = body.string();
//                }
//            }
//
//            // Saving JSON just for a check
//            Path jsonDir = Path.of("src/main/resources/json");
//            Files.createDirectories(jsonDir);
//
//            Path filePath = jsonDir.resolve("original_bershka.json");
//
//            // save JSON
//            mapper = new ObjectMapper();
//            Object jsonObj = mapper.readValue(response_body, Object.class);
//            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
//            Files.writeString(filePath, writer.writeValueAsString(jsonObj));
//
//            temp = sizeSelector.SelectSize(sex, orientation, bust, waist, hip);
//            String[] sizes = temp.split("\\+");
//            String error = sizes[0];
//            String sizeD = sizes[1];
//            String sizeS = sizes[2];
//
//            List<Map<String, Object>> parsed_json = parcer.parse(response_body, sizeD, sizeS, error);
//
//            jsons_for_shuffle.add(parsed_json);
//        }
//
//        return jsons_for_shuffle;
    }

    private Map<String, Object> from_entity_to_map(PositionEntity entity) {
        Map<String, Object> position = new HashMap<>();

        position.put("id", entity.getId());
        position.put("origin", entity.getOrigin());
        position.put("name", entity.getName());

        if(!Objects.equals(entity.getNameEn(), "not found")) {
            position.put("name_en", entity.getNameEn());
        }

        position.put("position_color", entity.getPositionsColor());
        position.put("size", entity.getSize());
        position.put("price", entity.getPrice());

        List<Map<String, Object>> photos = new ArrayList<>();
        Map<String,Object> photo = new HashMap<>();

        photo.put("display", entity.getDisplay());
        photos.add(photo);

        if(!Objects.equals(entity.getPhoto0(), "not found")) {
            photo = new HashMap<>();
            photo.put("0", entity.getPhoto0());
            photos.add(photo);
        }
        if(!Objects.equals(entity.getPhoto1(), "not found")) {
            photo = new HashMap<>();
            photo.put("1", entity.getPhoto1());
            photos.add(photo);
        }
        if(!Objects.equals(entity.getPhoto2(), "not found")) {
            photo = new HashMap<>();
            photo.put("2", entity.getPhoto2());
            photos.add(photo);
        }
        if(!Objects.equals(entity.getPhoto3(), "not found")) {
            photo = new HashMap<>();
            photo.put("3", entity.getPhoto3());
            photos.add(photo);
        }

        return position;
    }

    public String putAndParseBershkaJson(JsonNode json, String category, String sex){

        String orientation = idFinder.findCategoryId(category, sex);

        int iteration_count = 1;
        String orientation_for_loop = orientation;

        if (Objects.equals(sex, "m") && Objects.equals(orientation, "bottom")) {
            orientation_for_loop = orientation + "D";
            System.out.println(orientation_for_loop);
            iteration_count = 2;
        }

        for (int i = 1; i <= iteration_count; i++) {
            if (i > 1) orientation_for_loop = orientation + "S";
            List<String> sizes = getSizes(orientation_for_loop, sex);

            assert sizes != null;
            for (String size : sizes) {
                System.out.println(size);
                parcer.parse(json, size, "none", category, sex);
            }

        }

        return "put";
    }

    private List<String> getSizes(String orientation, String sex){

        List<String> sizes = new ArrayList<>();

        InputStream json = null;
        try {
            json = new ClassPathResource("jsons_for_size/bershka_sizes.json").getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            JsonNode root = mapper.readTree(json);

            for (JsonNode sex_plus_orientation : root) {
                if(Objects.equals(sex_plus_orientation.get("sex").asText(), sex) &&
                        Objects.equals(sex_plus_orientation.get("orientation").asText(), orientation)){
                    for(JsonNode size : sex_plus_orientation.path("sizes")){
                        sizes.add(size.get("size").asText());
                    }
                    System.out.println(sizes);
                    return sizes;
                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void clear_repo(){
        repository.deleteAll();
    }

}
