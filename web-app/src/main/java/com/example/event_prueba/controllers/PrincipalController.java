package com.example.event_prueba.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PrincipalController {
    @GetMapping
    public String principal(){
        return "pages/index";
    }
}
