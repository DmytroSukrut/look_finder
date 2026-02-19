package com.look_finder.components.new_yorker;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class CategoryIdFinderNewYorker {

    private final ObjectMapper mapper = new ObjectMapper();

    public String CreateUrl(String category, String sex) {

        InputStream json = null;
        try {
            json = new ClassPathResource("jsons_for_url/new_yorker.json").getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try{
            JsonNode root = mapper.readTree(json);

            for(JsonNode sex_ : root) {
                if(sex_.get("sex").asText().equals(sex)) {
                    for(JsonNode category_ : sex_.get("category_urls")) {
                        if(category_.get("name").asText().equals(category)) {
                            return category_.get("category_id").asText() + "\\" +
                                    category_.get("orientation").asText();
                        }
                    }
                }
            }

        } catch (IOException e){
            throw new RuntimeException(e);
        }



        return null;
    }

}
