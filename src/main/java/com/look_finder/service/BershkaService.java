package com.look_finder.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.look_finder.components.bershka.BershkaParcer;
import com.look_finder.components.bershka.UrlCreatorBershka;
import com.look_finder.components.bershka.CategoryIdFinderBershka;
import com.look_finder.components.bershka.BershkaSizeSelector;
import com.look_finder.position.PositionEntity;
import com.look_finder.position.PositionRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class BershkaService {

    private final BershkaParcer parcer;
    private final UrlCreatorBershka urlCreator;
    private final CategoryIdFinderBershka idFinder;
    private final BershkaSizeSelector  sizeSelector;
    private final PositionRepository repository;
    private final ObjectMapper mapper = new ObjectMapper();

    public BershkaService(BershkaParcer parcer, UrlCreatorBershka urlCreator, CategoryIdFinderBershka idFinder, BershkaSizeSelector sizeSelector, PositionRepository repository) {
        this.parcer = parcer;
        this.urlCreator = urlCreator;
        this.idFinder = idFinder;
        this.sizeSelector = sizeSelector;
        this.repository = repository;
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
    public List<List<Map<String, Object>>> getAndParseBershkaJSON(String category_, String sex, int bust, int waist, int hip) throws IOException {

        List<String> categorys = new ArrayList<>(Arrays.asList(category_.split("\\+")));
        List<List<Map<String, Object>>> all_bershka = new ArrayList<>();

        String error_msg = "none";
        Map<String, Object> error_map = new HashMap<>();

        for (int i = 0; i < categorys.size(); i++) {
            String category = categorys.get(i);
            String orientation = idFinder.findCategoryId(category, sex);

            String temp = sizeSelector.SelectSize(sex, orientation, bust, waist, hip);
            String[] temp_ = temp.split("\\+");

            String error = temp_[0];
            if(!error.equals("none")) {
                error_msg = error;
            }
            if(i == categorys.size() - 1) {
                error_map.put("origin", "Bershka");
                error_map.put("error", error_msg);
            }
        }

        for (String category : categorys) {
            System.out.println("category: " + category);
            String orientation = idFinder.findCategoryId(category, sex);
            System.out.println("orientation: " + orientation);

            String temp = sizeSelector.SelectSize(sex, orientation, bust, waist, hip);
            String[] temp_ = temp.split("\\+");

            String sizeD = temp_[1];
            String sizeS = temp_[2];

            List<Map<String, Object>> category_positions = new ArrayList<>();

            category_positions.add(error_map);

            for (String size : List.of(sizeD, sizeS)) {
                if(!Objects.equals(size, "error")) {
                    repository.findByCategoryAndSexAndSize(category, sex, size)
                            .stream()
                            .map(this::from_entity_to_map)
                            .forEach(category_positions::add);
                }
            }

            all_bershka.add(category_positions);
        }

        return all_bershka;
    }

    private Map<String, Object> from_entity_to_map(PositionEntity entity) {
        Map<String, Object> position = new HashMap<>();

        position.put("id", entity.getId());
        position.put("origin", entity.getOrigin());
        position.put("name", entity.getName());

        if(!Objects.equals(entity.getNameEn(), "not found")) {
            position.put("name_en", entity.getNameEn());
        }

        position.put("position_color", entity.getPositionsColor());
        position.put("size", entity.getSize());
        position.put("price", entity.getPrice());

        List<Map<String, Object>> photos = new ArrayList<>();
        Map<String,Object> photo = new HashMap<>();

        photo.put("display", entity.getDisplay());
        photos.add(photo);

        if(!Objects.equals(entity.getPhoto0(), "not found")) {
            photo = new HashMap<>();
            photo.put("0", entity.getPhoto0());
            photos.add(photo);
        }
        if(!Objects.equals(entity.getPhoto1(), "not found")) {
            photo = new HashMap<>();
            photo.put("1", entity.getPhoto1());
            photos.add(photo);
        }
        if(!Objects.equals(entity.getPhoto2(), "not found")) {
            photo = new HashMap<>();
            photo.put("2", entity.getPhoto2());
            photos.add(photo);
        }
        if(!Objects.equals(entity.getPhoto3(), "not found")) {
            photo = new HashMap<>();
            photo.put("3", entity.getPhoto3());
            photos.add(photo);
        }

        position.put("photos", photos);

        return position;
    }

    public String putAndParseBershkaJson(JsonNode json, String category, String sex){

        String orientation = idFinder.findCategoryId(category, sex);

        int iteration_count = 1;
        String orientation_for_loop = orientation;

        if (Objects.equals(sex, "m") && Objects.equals(orientation, "bottom")) {
            orientation_for_loop = orientation + "D";
            System.out.println(orientation_for_loop);
            iteration_count = 2;
        }

        for (int i = 1; i <= iteration_count; i++) {
            if (i > 1) orientation_for_loop = orientation + "S";
            List<String> sizes = getSizes(orientation_for_loop, sex);

            assert sizes != null;
            for (String size : sizes) {
                System.out.println(size);
                parcer.parse(json, size, "none", category, sex);
            }

        }

        return "done";
    }

    private List<String> getSizes(String orientation, String sex){

        List<String> sizes = new ArrayList<>();

        InputStream json = null;
        try {
            json = new ClassPathResource("jsons_for_size/bershka_sizes.json").getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            JsonNode root = mapper.readTree(json);

            for (JsonNode sex_plus_orientation : root) {
                if(Objects.equals(sex_plus_orientation.get("sex").asText(), sex) &&
                        Objects.equals(sex_plus_orientation.get("orientation").asText(), orientation)){
                    for(JsonNode size : sex_plus_orientation.path("sizes")){
                        sizes.add(size.get("size").asText());
                    }
                    System.out.println(sizes);
                    return sizes;
                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void clear_repo(){
        repository.deleteAll();
    }

}
