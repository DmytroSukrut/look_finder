package com.look_finder.components.parcers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Component
public class BershkaParcer {

    private final ObjectMapper mapper = new ObjectMapper();


    /**
     * This function parses down the original bershka JSON to a smaller version
     * @param json Original bershka JSON, which we get in "BershkaService" on endpoint
     * @param sizeD Size in digits
     * @param sizeS Size as Text
     * */
    public Object parse(String json, String sizeD, String sizeS) {
        String[] temp = {"a4o", "b1", "p1", "a2d", "a1t"}; //array of photo codes we need to get
        Set<String> photos_originalName = new HashSet<>(Arrays.asList(temp)); // Same array but as Set

        try {
            JsonNode root = mapper.readTree(json);
            List<Map<String, Object>> positions = new ArrayList<>();
            for (JsonNode product : root.path("products")) {
                /*
                * This code there loops through all the products.
                * But further code which will be marked down loops through all colors available for the product
                * And creating a "product" for all colors available depending on can you buy them right now
                * */
                String name = product.path("name").asText(); // Saving product name

                Map<Integer, String> colors_ids = new HashMap<>(); // Map for saving all colors available for the product

                // for loop to save all the available colors
                for (JsonNode c : product.path("bundleColors")) {
                    int id = Integer.parseInt(c.get("id").asText());
                    String color = c.get("name").asText();
                    colors_ids.put(id, color);
                }


                //----------------PRICE----------------
                JsonNode firstBundle = product.path("bundleProductSummaries").get(0);
                if (firstBundle == null) {
                    System.out.println("null");
                    continue;
                }
                // Getting the wrong price. Bershka send price as "2399" not 23,99
                String price_wrong = firstBundle
                        .path("detail")
                        .path("colors").get(0)
                        .path("sizes").get(0)
                        .path("price").asText();
                System.out.println("price_wrong: " + price_wrong);
                double price = Double.parseDouble(price_wrong) / 100; //Getting the correct price

                String size = "";
                //-----------SIZE AND COLORS-----------
                boolean is_correct_found = false;//Check if there is a customers size
                List<Map<String, Object>> colors_and_types = new ArrayList<>(); //Save necessary colors with customers size
                List<Integer> restricted_colors = new ArrayList<>(); //If there is no size for color, then color gets there and
                                                                    //code will not look at photos for this color further
                for (JsonNode color : firstBundle
                        .path("detail")
                        .path("colors")) {
                    boolean found_correct_size = false;
                    String last_size = null;
                    for (JsonNode size_finder : color.path("sizes")) {
                        String found_size = size_finder.path("name").asText();
                        /*
                        * Just a sign to speed up a process.
                        * If we read information about the requested size, we won't look further
                        * */
                        if (found_correct_size && !found_size.equals(last_size)) {
                            break;
                        }
                        last_size = found_size;

                        //Check if we found the requested size and decide it is a Digits or String size parameter
                        String foundSize = null;
                        if (found_size.contains(sizeD)) {
                            is_correct_found = true; // Set that size exists!
                            foundSize = sizeD;
                        } else if (found_size.contains(sizeS)) {
                            is_correct_found = true; // Set that size exists!
                            foundSize = sizeS;
                        }

                        //Check if size is available
                        if (foundSize != null && size_finder.path("visibilityValue").asText().equals("SHOW")) {
                            found_correct_size = true;
                            size = foundSize;

                            // Save the available size
                            Map<String, Object> colorName = new HashMap<>();
                            colorName.put("color", color.path("name").asText());
                            colorName.put("sizeType", size_finder.path("sizeType").asText());
                            colors_and_types.add(colorName);
                        }
                    }
                    // Colors gose to restrict List if it doesn't have available customer size
                    if (!found_correct_size) {
                        restricted_colors.add(Integer.parseInt(color.path("id").asText()));
                    }
                    System.out.println("restricted_colors: " + restricted_colors);
                }

                //If there is no such size, no sense to look further
                if (!is_correct_found) {
                    System.out.println("No size found for: " + name);
                    continue;
                }

                //Create position for all the colors with available sizes
                int n = colors_ids.size();
                for (int i = 0; i < n; i++) {
                    Map<String, Object> position = new HashMap<>();
                    //----------------NAMES----------------
                    position.put("name", name);
                    System.out.println(name);
                    position.put("name_en", product.path("nameEn").asText());

                    //----------------PRICE----------------
                    position.put("price", price);
                    System.out.println(price);

                    //-----------SIZE AND COLORS-----------
                    position.put("size", size);
                    position.put("colors_for_size", colors_and_types);
                    System.out.println(colors_and_types);

                    //----------NEEDED PHOTOS URL----------
                    List<Map<String, Object>> photo_urls = new ArrayList<>();
                    JsonNode detail = firstBundle
                            .path("detail");

                    boolean should_be_added_to_postions = true;
                    for (JsonNode xmedia : detail.path("xmedia")) {
                        String path = xmedia.path("path").asText();
                        String[] parts = path.split("/");
                        int color_id = Integer.parseInt(parts[parts.length - 1]);

                        System.out.println("color_id: " + color_id);
                        System.out.println("colors_ids: " + colors_ids);
                        System.out.println("restricted_colors: " + restricted_colors);
                        System.out.println("path: " + path);

                        //Finds photos for each color
                        if (colors_ids.containsKey(color_id) && !restricted_colors.contains(color_id)) {
                            System.out.println("colors_ids.get(color_id):  " + colors_ids.get(color_id));

                            position.put("positions_color", colors_ids.get(color_id));
                            colors_ids.remove(color_id); //Remove the color that we found

                            JsonNode xmediaItems = xmedia.path("xmediaItems");
                            JsonNode firstMedias = xmediaItems.get(0);
                            for (JsonNode photo_find : firstMedias.path("medias")) { //find all necessary photos
                                String photo_name = photo_find.path("extraInfo").path("originalName").asText();
                                if (photos_originalName.contains(photo_name)) {
                                    System.out.println(photo_name);
                                    Map<String, Object> photo_map = new HashMap<>();
                                    photo_map.put(photo_name, photo_find.path("url"));
                                    photo_urls.add(photo_map);
                                }
                            }
                            position.put("id", product.path("id").asInt() + "_" + color_id);
                            position.put("photos", photo_urls);
                            break;
                        } else if(colors_ids.containsKey(color_id) && restricted_colors.contains(color_id)) { //Remove unneeded color
                            colors_ids.remove(color_id);
                            should_be_added_to_postions = false;
                            break;
                        }
                    }
                    //-------------ADD POSITION------------
                    if (should_be_added_to_postions) {
                        positions.add(position);
                    }
                }
            }

            //Save parsed JSON for checks
            Path jsonDir = Path.of("src/main/resources/json");
            Files.createDirectories(jsonDir);

            Path filePath = jsonDir.resolve("parsed_bershka.json");

            String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(positions);
            Files.writeString(filePath, prettyJson);

            System.out.println("✅ Parsed JSON saved to: " + filePath.toAbsolutePath());

            return positions;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
