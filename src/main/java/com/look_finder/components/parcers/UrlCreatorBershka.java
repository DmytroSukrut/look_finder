package com.look_finder.components.parcers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UrlCreatorBershka {

    private final ObjectMapper mapper = new ObjectMapper();


    /**
     * This function creates url to get all necessary information from bershka endpoints
     * @param json JSON of all stocks (all product ids)
     * @param category depending on category adds correct category id
     * @return url to fetch in "BershkaService" later
     * */
    public String CreateUrl(String json, String category){

        StringBuilder sb = new StringBuilder();
        StringBuilder url = new StringBuilder("https://www.bershka.com/itxrest/3/catalog/store/45109545/40259564/productsArray?categoryId=");

        switch(category){
            case "jeans_w":
                url.append("1010276029");
                break;
            case "jackets_m":
                url.append("1010193546");
                break;
            default:
                return "ERROR_INVALID_CATEGORY";
        }

        List<String> ids = new ArrayList<>();

        try {
            JsonNode root = mapper.readTree(json);
            List<String> restrictedSecondIds = new ArrayList<>();

            /*
            * This for loop finds all the ids of products
            * BUT, there is a problem. Bershka can send different products ids for the same product!
            * The solution is to check second ids because even if a product has different ids,
            * it has the same second ids.
            * So we add the first second id to the restricted list, and then we are checking if the product has this
            * second id or no. If it has, we scip it.
            * In result, we don't have duplicates of the product on our website.
            * Here is the example:
            *
            * {
            *   "productId": 206171991, // Product id
            *   "stocks": [
            *       {
            *         "id": 206162347, // Second id
            *         "availability": "in_stock",
            *         "typeThreshold": "UNDEFINED_UMBRAL_STOCK"
            *       },
            * }
            *
            * */

            for(JsonNode id : root.path("stocks")){
                if (!id.isEmpty()) {
                    if (ids.isEmpty()) {
                        ids.add(id.path("productId").asText());
                    } else {
                        String secondId = id.path("stocks").get(0).path("id").asText();
                        if(!restrictedSecondIds.contains(secondId)) {
                            sb.append("%2C");
                            sb.append(id.path("productId").asText());
                            ids.add(sb.toString());
                            sb.delete(0, sb.length());
                            restrictedSecondIds.add(secondId);
                        }
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        url.append("&productIds=");

        for (String id : ids){
            url.append(id);
        }

        url.append("&appId=1&languageId=-28&locale=sk_SK");

        return url.toString();
    }

}
