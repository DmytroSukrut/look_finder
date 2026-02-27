package com.look_finder.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.look_finder.components.ShuffleAndDivideComponent;
import com.look_finder.service.BershkaService;
import com.look_finder.service.NewYorkerService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/put")
public class PutClothesController {

    private final BershkaService bershkaService;

    public PutClothesController(BershkaService bershkaService) {
        this.bershkaService = bershkaService;
    }

    /**
     * Creating here our endpoint for bershka
     * @return Fully parsed bershka JSON
     * */
    @PostMapping("/clothes")
    public String putClothes(
            @RequestParam String origin,
            @RequestParam String category,
            @RequestParam String sex,
            @RequestBody JsonNode json
    ) throws IOException, InterruptedException {

        System.out.println("PutClothesController");

        return bershkaService.putAndParseBershkaJson(json, category, sex);
    }

    @DeleteMapping("/clear")
    public String clearClothes() throws IOException, InterruptedException
    {

        bershkaService.clear_repo();

        return "clear";
    }
}
