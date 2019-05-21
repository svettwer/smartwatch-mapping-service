package com.github.svettwer.smartwatch.mapping.service.controller;

import com.github.svettwer.smartwatch.mapping.service.service.PairingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiController {
    @Value("${spring.application.name}")
    String appName;

    private PairingService pairingService;

    @Autowired
    public UiController(final PairingService pairingService) {
        this.pairingService = pairingService;
    }

    @GetMapping("/")
    public String homePage(final Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("pairings", pairingService.getPairings());
        return "home";
    }
}
