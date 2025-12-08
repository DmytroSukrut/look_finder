package com.look_finder.controller;


import com.look_finder.service.BershkaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/clothes")
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
            @RequestParam(defaultValue = "f") String sex,
            @RequestParam(defaultValue = "0") String bust,
            @RequestParam(defaultValue = "0") String waist,
            @RequestParam(defaultValue = "0") String hip
    ) throws IOException, InterruptedException {

        int bust_ =  Integer.parseInt(bust);
        int waist_ =  Integer.parseInt(waist);
        int hip_ =  Integer.parseInt(hip);

        return service.getAndParseJSON(category, sex,  bust_, waist_, hip_);
    }
}
