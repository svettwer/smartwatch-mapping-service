package com.github.svettwer.smartwatch.mapping.service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiController {
    @Value("${spring.application.name}")
    String appName;



    @GetMapping("/")
    public String homePage(final Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }
}
