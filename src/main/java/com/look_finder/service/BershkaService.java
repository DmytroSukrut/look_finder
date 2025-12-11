package com.look_finder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.look_finder.components.parcers.BershkaParcer;
import com.look_finder.components.parcers.UrlCreatorBershka;
import com.look_finder.components.parcers.CategoryIdFinderBershka;
import com.look_finder.components.selector.BershkaSizeSelector;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import okhttp3.*;

@Service
public class BershkaService {

    private final BershkaParcer parcer;
    private final UrlCreatorBershka urlCreator;
    private final CategoryIdFinderBershka idFinder;
    private final BershkaSizeSelector  sizeSelector;

    public BershkaService(BershkaParcer parcer,  UrlCreatorBershka urlCreator,   CategoryIdFinderBershka idFinder, BershkaSizeSelector sizeSelector) {
        this.parcer = parcer;
        this.urlCreator = urlCreator;
        this.idFinder = idFinder;
        this.sizeSelector = sizeSelector;
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
    public Object getAndParseJSON(String category_, String sex, int bust, int waist, int hip) throws IOException {

        List<List<Map<String, Object>>> jsons_for_shuffle = new ArrayList<>();

        List<String> categorys = new ArrayList<>(Arrays.asList(category_.split("\\+")));


        ObjectMapper mapper = new ObjectMapper();
        for (String category : categorys) {
            String temp = idFinder.findCategoryId(category, sex);
            String[] parts = temp.split("\\+");
            String cat_id = parts[0];
            String orientation = parts[1];

            // Create url to correct stock
            String url_stock = createUrlStock(cat_id);
            // Create an HTTP client (This client can send requests to endpoints)
            OkHttpClient client = new OkHttpClient();

            // Create a request on our endpoint in Internet to get all available positions from bershka
            // We are adding headers so the endpoint doesn't understand that we are making this request from code
            Request stock_req = new Request.Builder()
                    .url(url_stock)
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
                    .build(); // End building a request

            String response_body;
            // Our Client sends our response, and we are waiting for the answer
            try (Response stock_resp = client.newCall(stock_req).execute()) {
                if (!stock_resp.isSuccessful()) { // Check if we got "200" any other = error
                    throw new IOException("Unexpected code " + stock_resp.code()); // Tels me if we got bad response code
                } else {
                    ResponseBody body = stock_resp.body(); // Get what our request got
                    if (body == null)
                        throw new IOException("Empty response body"); // Check if the response body is empty (doesn't really need it, I think)

                    // Interesting thing! If we don't say in the header that we except gzip, it won't send any! :)
                    response_body = body.string();
                }
            }

            /* We got our JSON from Bershka with stocks
             * Then we send it our component urlCreator
             * (for more explanation how it works, see the component "components/parcers/UrlCreatorBershka")
             * In return from it, we get url (unexpected, yes? :)) with NO duplicates of product! How cool is that!!!
             * */
            String url_products = urlCreator.CreateUrl(response_body, cat_id);

            System.out.println(url_products);

            // Same creating request, but to get all info for products we extracted previously
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
                    .build(); // End building a request

            // Same as before. Sends a request and gets a response
            try (Response products_resp = client.newCall(products_req).execute()) {
                if (!products_resp.isSuccessful()) {
                    throw new IOException("Unexpected code " + products_resp.code());
                } else {
                    ResponseBody body = products_resp.body();
                    if (body == null) throw new IOException("Empty response body");

                    response_body = body.string();
                }
            }

            // Saving JSON just for a check
            Path jsonDir = Path.of("src/main/resources/json");
            Files.createDirectories(jsonDir);

            Path filePath = jsonDir.resolve("original_bershka.json");

            // save JSON
            mapper = new ObjectMapper();
            Object jsonObj = mapper.readValue(response_body, Object.class);
            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
            Files.writeString(filePath, writer.writeValueAsString(jsonObj));

            temp = sizeSelector.SelectSize(sex, orientation, bust, waist, hip);
            String[] sizes = temp.split("\\+");
            String sizeD = sizes[0];
            String sizeS = sizes[1];

            List<Map<String, Object>> parsed_json = parcer.parse(response_body, sizeD, sizeS);

            jsons_for_shuffle.add(parsed_json);
        }

        /*
         * INTERLEAVING SHUFFLE MECHANISM
         * -------------------------------
         * This algorithm takes multiple JSON lists (jsons_for_shuffle) and mixes their
         * elements together randomly while preserving the original order *inside* each list.
         *
         * Example:
         *   JSON0: B1 B2 B3 B4
         *   JSON1: C1 C2 C3
         *
         * After shuffling:
         *   B1 C1 B2 B3 C2 B4 C3
         *
         * How it works:
         * 1. Build a flat list of JSON indices where each index appears as many times
         *    as there are items in that JSON (this controls the final proportions).
         *
         * 2. Shuffle this index list with a seeded Random — this defines the interleaving order.
         *
         * 3. Use a "memory" array to track which element of each JSON should be taken next.
         *
         * 4. Read through the shuffled indices and pull elements from each JSON in order.
         *
         * The result is a deterministic, stable interleaving shuffle.
         */

        List<Map<String, Object>> shuffled_json = new ArrayList<>();
        List<Integer> memory_int = new ArrayList<>();

        if (jsons_for_shuffle.size() > 1) {
            //shuffle function
            List<Integer> random_positions_by_seed = new ArrayList<>();

            // 1. Build a flat list of JSON indices based on sizes.
            for (int i = 0; i < jsons_for_shuffle.size(); i++) {
                // Repeat index 'i' as many times as JSON i has elements.
                for (int j = 0; j < jsons_for_shuffle.get(i).size(); j++) {
                    random_positions_by_seed.add(i);
                }
            }

            // 2. Shuffle these indices using a deterministic seed.
            Collections.shuffle(random_positions_by_seed, new Random(1406200820));

            // 3. Memory will remember here on which position we are in each JSON in jsons_for_shufle
            // initialize memory with zeros for each JSON file
            for (int i = 0; i < jsons_for_shuffle.size(); i++) {
                memory_int.add(0);
            }

            // 4. Build final shuffled JSON
            for (int rand : random_positions_by_seed) {

                int current_position_in_json = memory_int.get(rand);

                // Add the next element from the selected source JSON.
                shuffled_json.add(jsons_for_shuffle.get(rand).get(current_position_in_json));
                memory_int.set(rand, current_position_in_json + 1);
            }

        } else {
            // If there's only one JSON, return it as-is (no shuffle needed).
            shuffled_json = jsons_for_shuffle.get(0);
        }

        //Need to add a duplicate checker because apparently Bershka can add some positions in both of their categorys

        Set<String> memory_str = new HashSet<>();
        Iterator<Map<String,Object>> shuffled_json_iterator = shuffled_json.iterator();

        while(shuffled_json_iterator.hasNext()) {
            Map<String,Object> current = shuffled_json_iterator.next();
            String id = current.get("id").toString();

//          IF YOU HAVE TIME TRY TO ADD HASHSET IN ORIGINAL CODE
//          memory_str.add(id)
//          returns:
//          true  → id was NOT in the set (new item)
//          false → id already existed (duplicate!)
            if(!memory_str.add(id)){
                shuffled_json_iterator.remove();
            }
        }

        Path jsonDir = Path.of("src/main/resources/json");
        Files.createDirectories(jsonDir);

        Path filePath = jsonDir.resolve("shuffled_bershka.json");

        String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(shuffled_json);
        Files.writeString(filePath, prettyJson);


        return shuffled_json;
    }

    /**
     * This private function creates url to bershka stocks
     * @param category_id Specific category id
     * @return url to fetch or that we send bad data
     * */
    private String createUrlStock(String category_id) {
        StringBuilder url = new StringBuilder("https://www.bershka.com/itxrest/2/catalog/store/45109545/40259564/category/");

        url.append(category_id);

        url.append("/stock");
        System.out.println(url);
        return url.toString();
    }
}
