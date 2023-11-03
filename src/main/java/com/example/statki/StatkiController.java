package com.example.statki;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

public class StatkiController {
    @RestController
    public class StatekController
    {
        @GetMapping("/")
        public String index()
        {
            return "index.html";
        }
    }
}
