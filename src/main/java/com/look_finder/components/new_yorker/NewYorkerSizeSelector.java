package com.look_finder.components.new_yorker;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.internal.util.JsonUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Component
public class NewYorkerSizeSelector {

    private final ObjectMapper mapper = new ObjectMapper();

    public String select_size(String sex, String orientation, int bust, int waist, int hip){
        String error = "none";
        String sizeD = "-20";
        String sizeS = "A";

        InputStream json = null;
        try {
            json = new ClassPathResource("jsons_for_size/new_yorker_sizes.json").getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (Objects.equals(orientation, "bottom")) {
            orientation = "bottomD";
        }

        try {
            JsonNode root = mapper.readTree(json);

            for(JsonNode node : root){

                String sex_ = node.get("sex").asText();
                String orientation_ = node.get("orientation").asText();

                if(Objects.equals(orientation, orientation_) && Objects.equals(sex, sex_)){
                    JsonNode sizes = node.path("sizes");
                    String size = "";
                    switch (sex + "_" + orientation) {
                        case "f_top" -> {
                            size = size_founder(sizes, true, 54, 80, 72, waist, hip, bust);

                            if (Objects.equals(size, "less_min")) {
                                error = size;
                                sizeS = "XXS";
                                System.out.println("got less min ||  " + sex + "_" + orientation);
                            } else if (Objects.equals(size, "over_max")) {
                                error = size;
                                sizeS = "XXL";
                                System.out.println("got over max ||  " + sex + "_" + orientation);
                            } else {
                                sizeS = size;
                                System.out.println("SIZE S: " + sizeS);
                            }
                        }
                        case "f_bottomD" -> {
                            size = size_founder(sizes, false, 53, 80, 0, waist, hip, bust);

                            if (Objects.equals(size, "less_min")) {
                                error = size;
                                sizeD = "32";
                                System.out.println("got less min ||  " + sex + "_" + orientation);
                            } else if (Objects.equals(size, "over_max")) {
                                error = size;
                                sizeD = "44";
                                System.out.println("got over max ||  " + sex + "_" + orientation);
                            } else {
                                sizeD = size;
                                System.out.println("SIZE D: " + sizeD);
                            }
                            orientation = "bottomS";
                            System.out.println(orientation + " ||  " + sex + "_" + orientation);
                        }
                        case "f_bottomS" -> {
                            size = size_founder(sizes, false, 51, 78, 0, waist, hip, bust);

                            if (Objects.equals(size, "less_min")) {
                                error = size;
                                sizeS = "XXS";
                                System.out.println("got less min ||  " + sex + "_" + orientation);
                            } else if (Objects.equals(size, "over_max")) {
                                error = size;
                                sizeS = "XXL";
                                System.out.println("got over max ||  " + sex + "_" + orientation);
                            } else {
                                sizeS = size;
                                System.out.println("SIZE S: " + sizeS);
                            }
                        }
                        case "m_top" -> {
                            size = size_founder(sizes, true, 79, 92, 89, waist, hip, bust);

                            if (Objects.equals(size, "less_min")) {
                                error = size;
                                sizeS = "XXS";
                                System.out.println("got less min ||  " + sex + "_" + orientation);
                            } else if (Objects.equals(size, "over_max")) {
                                error = size;
                                sizeS = "XXL";
                                System.out.println("got over max ||  " + sex + "_" + orientation);
                            } else {
                                sizeS = size;
                                System.out.println("SIZE S: " + sizeS);
                            }
                        }
                        case "m_bottomD" -> {
                            size = size_founder(sizes, false, 68, 90, 0, waist, hip, bust);

                            if (Objects.equals(size, "less_min")) {
                                error = size;
                                sizeD = "28";
                                System.out.println("got less min ||  " + sex + "_" + orientation);
                            } else if (Objects.equals(size, "over_max")) {
                                error = size;
                                sizeD = "38";
                                System.out.println("got over max ||  " + sex + "_" + orientation);
                            } else {
                                sizeD = size;
                                System.out.println("SIZE D: " + sizeD);
                            }
                            orientation = "bottomS";
                            System.out.println(orientation + " ||  " + sex + "_" + orientation);
                        }
                        case "m_bottomS" -> {
                            size = size_founder(sizes, false, 79, 92, 0, waist, hip, bust);

                            if (Objects.equals(size, "less_min")) {
                                error = size;
                                sizeS = "XXS";
                                System.out.println("got less min ||  " + sex + "_" + orientation);
                            } else if (Objects.equals(size, "over_max")) {
                                error = size;
                                sizeS = "XXL";
                                System.out.println("got over max ||  " + sex + "_" + orientation);
                            } else {
                                sizeS = size;
                                System.out.println("SIZE D: " + sizeS);
                            }
                        }
                    }
                }


            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }



        return error + "+" + sizeD + "+" + sizeS;
    }

    private boolean inRange(int value, int min, int max) {
        System.out.printf("inRange(%d value, %d min, %d max)\n", value, min, max);
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
                            System.out.println("FOUND");
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
                            System.out.println("FOUND");
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
                            System.out.println("FOUND");
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
