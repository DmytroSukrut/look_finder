package com.look_finder.components.bershka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class CategoryIdFinderBershka {

    private final ObjectMapper mapper = new ObjectMapper();

    public String findCategoryId(String category, String sex) {

        InputStream json = null;
        try {
            json = new ClassPathResource("jsons_for_url/bershka.json").getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            JsonNode root = mapper.readTree(json);

            for(JsonNode sex_ : root){
                if (sex_.path("sex").asText().equals(sex)) {
                    for(JsonNode category_ : sex_.path("category_urls")){
                        if(category_.path("name").asText().equals(category)){
                            return category_.path("category_id").asText() + "+" + category_.path("orientation").asText();
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
