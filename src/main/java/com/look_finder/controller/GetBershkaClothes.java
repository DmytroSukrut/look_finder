package com.look_finder.controller;


import com.look_finder.service.BershkaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/clothes/bershka")
public class GetBershkaClothes {

    private final BershkaService service;
    public GetBershkaClothes(BershkaService service) {
        this.service = service;
    }

    /**
     * Creating here our endpoint for bershka
     * @return Fully parsed bershka JSON
     * */
    @GetMapping("/filter")
    public Object getClothes(
            @RequestParam String category,
            @RequestParam(defaultValue = "36") String sizeD,
            @RequestParam(defaultValue = "M") String sizeS
    ) throws IOException, InterruptedException {
//        try {
//            service.getAndParseJSON(category, size);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return service.getAndParseJSON(category, sizeD, sizeS);
    }

}
