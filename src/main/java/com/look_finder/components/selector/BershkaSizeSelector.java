package com.look_finder.components.selector;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Component
public class BershkaSizeSelector {

    private final ObjectMapper mapper = new ObjectMapper();

    public String SelectSize(String sex, String orientation, int bust, int waist, int hip) {

        String sizeD = "-20";
        String sizeS = "A";

        InputStream json = null;
        try {
            json = new ClassPathResource("jsons_for_size/bershka_sizes.json").getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(Objects.equals(sex, "m") && Objects.equals(orientation, "bottom")) {
            orientation = "bottomD";
        }

        try {
            JsonNode root = mapper.readTree(json);

            for(JsonNode sex_plus_orientation : root){
                String sex_json = sex_plus_orientation.path("sex").asText();
                String orientation_json = sex_plus_orientation.path("orientation").asText();

                if(Objects.equals(sex_json, sex) && Objects.equals(orientation_json, orientation)) {
                    JsonNode sizes = sex_plus_orientation.path("sizes");
                    switch (sex_json + "_" + orientation_json) {
                        case "f_top" -> {
                            int prev_bust = 72;
                            int prev_waist = 54;
                            int prev_hip = 80;
                            for (JsonNode size_json : sizes) {
                                int bust_json = size_json.get("bust").asInt();
                                int waist_json = size_json.get("waist").asInt();
                                int hip_json = size_json.get("hip").asInt();

                                //Think about what to do if is smaller but not a lot on the bottom border
                                if (inRange(bust, prev_bust, bust_json) &&
                                        inRange(waist, prev_waist, waist_json) &&
                                        inRange(hip, prev_hip, hip_json)) {
                                    sizeS = size_json.get("size").asText();
                                    break;
                                } else {
                                    prev_bust = bust_json;
                                    prev_waist = waist_json;
                                    prev_hip = hip_json;
                                }
                            }
                        }
                        case "f_bottom" -> sizeD = finder_waist_hip(sizes, 56, 82, waist, hip);
                        case "m_top" -> sizeS = finder_waist_hip(sizes, 75, 75, waist, hip);
                        case "m_bottomD" -> {
                            sizeD = finder_waist_hip(sizes, 66, 83, waist, hip);
                            orientation =  "bottomS";
                        }
                        case "m_bottomS" -> sizeS = finder_waist_hip(sizes, 66, 83, waist, hip);
                    }
                }
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        String return_value = sizeD + "+" + sizeS;
        System.out.println(return_value);
        return return_value;
    }

    private boolean inRange(int value, int min, int max) {
        System.out.printf("inRange(%d value, %d min, %d max)\n", value, min, max);
        System.out.println(value >= min && value <= max);
        return value >= min && value <= max;
    }

    private String finder_waist_hip(JsonNode root, int prev_waist, int prev_hip, int waist, int hip) {
        System.out.println("lala");

        for (JsonNode size_json : root) {
            System.out.println("k");
            int waist_json = size_json.get("waist").asInt();
            int hip_json = size_json.get("hip").asInt();

            //Think about what to do if is smaller but not a lot on the bottom border
            if (inRange(waist, prev_waist, waist_json) &&
                    inRange(hip, prev_hip, hip_json)) {
                return size_json.get("size").asText();
            } else {
                prev_waist = waist_json;
                prev_hip = hip_json;
            }
        }
        return "size_not_found";
    }
}
