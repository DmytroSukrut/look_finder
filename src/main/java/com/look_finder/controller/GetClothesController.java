package com.look_finder.controller;


import com.look_finder.components.ShuffleAndDivideComponent;
import com.look_finder.service.BershkaService;
import com.look_finder.service.NewYorkerService;
import com.look_finder.service.SimilarService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clothes")
public class GetClothesController {

    private final BershkaService service_bershka;
    private final NewYorkerService service_newyorker;
    private final SimilarService service_similar;

    private final ShuffleAndDivideComponent controller_shuffle;

    public GetClothesController(BershkaService service_bershka, NewYorkerService service_newyorker, SimilarService serviceSimilar, ShuffleAndDivideComponent controller_shuffle) {
        this.service_bershka = service_bershka;
        this.service_newyorker = service_newyorker;
        this.service_similar = serviceSimilar;
        this.controller_shuffle = controller_shuffle;
    }

    @GetMapping("/filter")
    public Object getClothes(
            @RequestParam String category,
            @RequestParam(defaultValue = "f") String sex,
            @RequestParam(defaultValue = "0") String bust,
            @RequestParam(defaultValue = "0") String waist,
            @RequestParam(defaultValue = "0") String hip
    ) throws IOException, InterruptedException {

        int bust_ =  Integer.parseInt(bust);
        int waist_ =  Integer.parseInt(waist);
        int hip_ =  Integer.parseInt(hip);

        List<List<Map<String, Object>>> jsons_for_shuffle = new ArrayList<>();

        List<List<Map<String, Object>>> temp_bershka  = new ArrayList<>();

        try {
            System.out.println("WORKING ON BERSHKA _______________");
            temp_bershka = service_bershka.getAndParseBershkaJSON(category, sex, bust_, waist_, hip_);
            jsons_for_shuffle.addAll(temp_bershka);
        } catch (Exception e) {
            System.out.println("EXCEPTION");
            System.out.println(e);
        }

        System.out.println("WORKING ON NEW YORKER _______________");
        List<Map<String, Object>> temp_new_yorker = service_newyorker.getAndParseNewYorkerJSON(category, sex, bust_, waist_, hip_);

        jsons_for_shuffle.add(temp_new_yorker);

        return controller_shuffle.shuffle_and_divide(jsons_for_shuffle, "get_clothes", null);
    }

    @GetMapping("/similar")
    public Object getClothesSimilar(
            @RequestParam(defaultValue = "standard") long id
    ) throws IOException, InterruptedException {
        return service_similar.getSimilar(id);
    }
}
