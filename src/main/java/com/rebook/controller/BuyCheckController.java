package com.rebook.controller;

import com.rebook.dto.request.BuyCheckRequest;
import com.rebook.dto.response.BuyCheckResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/buy-check")
public class BuyCheckController {

    @PostMapping
    public BuyCheckResponse checkBuyable(@RequestBody BuyCheckRequest request) {
        return new BuyCheckResponse(true, "매입 가능합니다.", "");
    }
}
