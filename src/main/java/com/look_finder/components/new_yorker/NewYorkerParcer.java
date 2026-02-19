package com.look_finder.components.new_yorker;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Component
public class NewYorkerParcer {
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Map<String, Object>> parse(String json, String sizeD, String sizeS, String error) throws IOException {

        List<Map<String, Object>> positions = new ArrayList<>();

        Map<String, Object> error_map = new HashMap<>();
        error_map.put("error", error);
        error_map.put("origin", "New Yorker");
        positions.add(error_map);

        try {
            JsonNode root = mapper.readTree(json);
            for(JsonNode item : root.get("items")) {

                String name_en = "";


                for(JsonNode description : item.get("descriptions")) {
                    if(description.get("language").asText().equals("EN")) {
                        name_en = description.get("description").asText();
                    }
                }

                for(JsonNode variant : item.get("variants")) {
                    double price = 0;
                    String size = "";
                    String color = "";
                    String id = "";
                    Map<String, Object> position = new HashMap<>();

                    //Check if there is size for position
                    boolean got_correct_size = false;
                    for(JsonNode size_ : variant.get("sizes")) {
                        if (size_.get("size_name").asText().equals(sizeD) || size_.get("size_name").asText().equals(sizeS)) {
                            size = size_.get("size_name").asText();
                            got_correct_size = true;
                        }
                    }

                    if(!got_correct_size) {
                        continue;
                    }

                    price = variant.get("current_price").asDouble();
                    color = variant.get("color_group").asText();
                    id = variant.get("product_id").asText();

                    List<Map<String, Object>> photos = new LinkedList<>();
                    boolean has_display = false;
                    String pre_url = "https://nyerblobstoreprod.blob.core.windows.net/product-images-public/";

                    for(JsonNode image :  variant.get("images")) {
                        if(image.get("type").asText().equals("OUTFIT_IMAGE") &&
                        image.get("position").asText().equals("0")) {
                            String key = image.get("key").asText();
                            has_display = true;
                            Map<String, Object> photo = new HashMap<>();
                            photo.put("display", pre_url + key);
                            photos.add(photo);
                        }
                    }

                    int photo_counter = 0;
                    for (JsonNode image : variant.get("images")) {
                        String key = image.get("key").asText();
                        if(!has_display &&
                                image.get("type").asText().equals("CUTOUT")) {
                            has_display = true;
                            Map<String, Object> photo = new HashMap<>();
                            photo.put("display", pre_url + key);
                            photos.add(photo);
                        } else {
                            Map<String, Object> photo = new HashMap<>();
                            photo.put(Integer.toString(photo_counter), pre_url + key);
                            photos.add(photo);
                            photo_counter++;
                        }
                    }
                    position.put("origin", "new_yorker");
                    position.put("positions_color",  color);
                    position.put("size", size);
                    position.put("price", price);
                    position.put("name_en", name_en);
                    position.put("photos", photos);
                    position.put("id", id);
                    positions.add(position);
                }
            }

            Path jsonDir = Path.of("src/main/resources/json");
            Files.createDirectories(jsonDir);

            Path filePath = jsonDir.resolve("parsed_new_yorker.json");

            String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(positions);
            Files.writeString(filePath, prettyJson);

            return positions;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        return null;
    };
}
