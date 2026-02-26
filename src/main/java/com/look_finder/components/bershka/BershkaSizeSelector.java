package com.look_finder.components.bershka;

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
        String error = "none";
        String sizeD = "error";
        String sizeS = "error";

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
                    String size = "";
                    switch (sex + "_" + orientation) {
                        case "f_top" -> {
                            size = size_founder(sizes, true, 54, 80, 72, waist, hip, bust);

                            if (Objects.equals(size, "less_min")) {
                                error = size;
                                sizeS = "XXS";
//                                System.out.println("got less min ||  " + sex + "_" + orientation);
                            } else if (Objects.equals(size, "over_max")) {
                                error = size;
                                sizeS = "XL";
//                                System.out.println("got over max ||  " + sex + "_" + orientation);
                            } else {
                                sizeS = size;
//                                System.out.println("SIZE S: " + sizeS);
                            }
                        }
                        case "f_bottom" -> {
                            size = size_founder(sizes, false, 56, 82, 0, waist, hip, bust);

                            if (Objects.equals(size, "less_min")) {
                                error = size;
                                sizeD = "32";
//                                System.out.println("got less min ||  " + sex + "_" + orientation);
                            } else if (Objects.equals(size, "over_max")) {
                                error = size;
                                sizeD = "44";
//                                System.out.println("got over max ||  " + sex + "_" + orientation);
                            } else {
                                sizeD = size;
//                                System.out.println("SIZE D: " + sizeD);
                            }
                        }
                        case "m_top" -> {
                            size = size_founder(sizes, true, 75, 75, 75, waist, hip, bust);

                            if (Objects.equals(size, "less_min")) {
                                error = size;
                                sizeS = "XXS";
//                                System.out.println("got less min ||  " + sex + "_" + orientation);
                            } else if (Objects.equals(size, "over_max")) {
                                error = size;
                                sizeS = "XXL";
//                                System.out.println("got over max ||  " + sex + "_" + orientation);
                            } else {
                                sizeS = size;
//                                System.out.println("SIZE S: " + sizeS);
                            }
                        }
                        case "m_bottomD" -> {
                            size = size_founder(sizes, false, 66, 83, 0, waist, hip, bust);

                            if (Objects.equals(size, "less_min")) {
                                error = size;
                                sizeD = "34";
//                                System.out.println("got less min ||  " + sex + "_" + orientation);
                            } else if (Objects.equals(size, "over_max")) {
                                error = size;
                                sizeD = "48";
//                                System.out.println("got over max ||  " + sex + "_" + orientation);
                            } else {
                                sizeD = size;
//                                System.out.println("SIZE D: " + sizeD);
                            }
                            orientation = "bottomS";
//                            System.out.println(orientation + " ||  " + sex + "_" + orientation);
                        }
                        case "m_bottomS" -> {
                            size = size_founder(sizes, false, 64, 81, 0, waist, hip, bust);

                            if (Objects.equals(size, "less_min")) {
                                error = size;
                                sizeS = "XS";
//                                System.out.println("got less min ||  " + sex + "_" + orientation);
                            } else if (Objects.equals(size, "over_max")) {
                                error = size;
                                sizeS = "XL";
//                                System.out.println("got over max ||  " + sex + "_" + orientation);
                            } else {
                                sizeS = size;
//                                System.out.println("SIZE S: " + sizeS);
                            }
                        }
                    }
                }
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return error + "+" + sizeD + "+" + sizeS;
    }

    private boolean inRange(int value, int min, int max) {
//        System.out.printf("inRange(%d value, %d min, %d max)\n", value, min, max);
        return value >= min && value <= max;
    }

    private String size_founder(JsonNode sizes, boolean has_bust, int prev_waist, int prev_hip, int prev_bust, int waist, int hip, int bust) {
        int iteration = 0;

        int waist_iteration_count = 0;
        int hip_iteration_count = 0;
        int bust_iteration_count = 0;

        String waist_selected_size = "";
        String hip_selected_size = "";
        String bust_selected_size = "";

        int current_waist = 0;
        int current_hip = 0;
        int current_bust = 0;

        int param_count = 2;
        if(has_bust) param_count = 3;

        if ((waist < prev_waist) || (hip < prev_hip) || (bust < prev_bust)) {
            return "less_min";
        }

        for (int current_param = 0; current_param < param_count; current_param++) {
            iteration = 0;
            for (JsonNode size_node : sizes) {
                boolean break_for_loop = false;
                switch(current_param) {
                    case 0 -> {
                        //waist
                        current_waist = size_node.get("waist").asInt(); //max for the current size

                        if(inRange(waist, prev_waist, current_waist)){
                            waist_selected_size = size_node.get("size").asText();
                            waist_iteration_count = iteration;
//                            System.out.println("FOUND");
                            break_for_loop = true;
                            break;
                        }

                        prev_waist = current_waist;
                        iteration++;
                    }
                    case 1 -> {
                        //hip
                        current_hip = size_node.get("hip").asInt();
                        if(inRange(hip, prev_hip, current_hip)){
                            hip_selected_size = size_node.get("size").asText();
                            hip_iteration_count = iteration;
//                            System.out.println("FOUND");
                            break_for_loop = true;
                            break;
                        }
                        prev_hip = current_hip;
                        iteration++;
                    }
                    case 2 -> {
                        //bust
                        current_bust = size_node.get("bust").asInt();
                        if(inRange(bust, prev_bust, current_bust)){
                            bust_selected_size = size_node.get("size").asText();
                            bust_iteration_count = iteration;
//                            System.out.println("FOUND");
                            break_for_loop = true;
                            break;
                        }
                        prev_bust = current_bust;
                        iteration++;
                    }
                }
                if(break_for_loop) {
                    break;
                }
            }
        }

        if((Objects.equals(waist_selected_size, "")) ||
                (Objects.equals(hip_selected_size, "")) ||
                (Objects.equals(bust_selected_size, "") && has_bust)) {
            return "over_max";
        }

        return sizes.get(Math.max(waist_iteration_count, Math.max(hip_iteration_count, bust_iteration_count))).get("size").asText();
    }
}
