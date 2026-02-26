package com.look_finder.components;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Component
public class ShuffleAndDivideComponent {

    private final ObjectMapper mapper = new ObjectMapper();

    public Object shuffle_and_divide(List<List<Map<String, Object>>> unprepared_) throws IOException {

        JsonNode root = mapper.valueToTree(unprepared_);

        List<Map<String, Object>> error_list = new ArrayList<>();
        List<String> errors_origin_list = new ArrayList<>();

        for (JsonNode jsons_from_shops : root) {
            Map<String, Object> error = new HashMap<>();
            String origin = jsons_from_shops.get(0).get("origin").asText();
            String error_ = jsons_from_shops.get(0).get("error").asText();

            if(!errors_origin_list.contains(origin)) {
                error.put("origin", origin);
                error.put("error", error_);
                error_list.add(error);

                errors_origin_list.add(origin);
            }
        }

        List<List<Map<String, Object>>> unprepared = new ArrayList<>();

        for (JsonNode jsons_from_shops : root) {
            List<Map<String, Object>> json_from_shop = new ArrayList<>();
            boolean skip_first = true;
            for (JsonNode product :  jsons_from_shops) {
                if (!skip_first) {
                    Map<String, Object> productMap = mapper.convertValue(product, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
                    json_from_shop.add(productMap);
                    continue;
                }
                skip_first = false;
            }
            unprepared.add(json_from_shop);
        }



        List<Map<String, Object>> shuffled_json = new ArrayList<>();
        List<Integer> memory_int = new ArrayList<>();

        if (unprepared.size() > 1) {
            //shuffle function
            List<Integer> random_positions_by_seed = new ArrayList<>();

            // 1. Build a flat list of JSON indices based on sizes.
            for (int i = 0; i < unprepared.size(); i++) {
                // Repeat index 'i' as many times as JSON i has elements.
                for (int j = 0; j < unprepared.get(i).size(); j++) {
                    random_positions_by_seed.add(i);
                }
            }

            // 2. Shuffle these indices using a deterministic seed.
            Collections.shuffle(random_positions_by_seed, new Random(1406200820));

            // 3. Memory will remember here on which position we are in each JSON in jsons_for_shufle
            // initialize memory with zeros for each JSON file
            for (int i = 0; i < unprepared.size(); i++) {
                memory_int.add(0);
            }

            // 4. Build final shuffled JSON
            for (int rand : random_positions_by_seed) {

                int current_position_in_json = memory_int.get(rand);

                // Add the next element from the selected source JSON.
                shuffled_json.add(unprepared.get(rand).get(current_position_in_json));
                memory_int.set(rand, current_position_in_json + 1);
            }

        } else {
            // If there's only one JSON, return it as-is (no shuffle needed).
            shuffled_json = unprepared.get(0);
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

        //PAGE DIVIDER MECHANISM

        List<Map<String, Object>> divided_json = new ArrayList<>();

        float float_shuffled_length_divided = (float) shuffled_json.size() / 24;


        int page_quantity = (int) Math.ceil(float_shuffled_length_divided);


        for (int i = 1; i <= page_quantity; i++) {
            Map<String, Object> one_page = new HashMap<>();
            List<Map<String, Object>> one_page_products = new ArrayList<>();

            one_page.put("Page", i);

            int iterations = 24;

            if ((iterations * i) > shuffled_json.size()) {
                iterations = shuffled_json.size() - ((i - 1) * 24);
            }

            for (int n = 0; n < iterations; n++) {
                one_page_products.add(shuffled_json.get(n + (24 * (i - 1))));
            }
            one_page.put("positions", one_page_products);
            divided_json.add(one_page);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("errors", error_list);
        result.put("products", divided_json);

        Path jsonDir = Path.of("src/main/resources/json");
        Files.createDirectories(jsonDir);

        Path filePath = jsonDir.resolve("divided_all.json");

        String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
        Files.writeString(filePath, prettyJson);

        return result;
    }

}
