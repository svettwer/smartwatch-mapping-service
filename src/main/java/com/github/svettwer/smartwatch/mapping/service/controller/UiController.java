package com.github.svettwer.smartwatch.mapping.service.controller;

import com.github.svettwer.smartwatch.mapping.service.exception.NoSuchDeviceException;
import com.github.svettwer.smartwatch.mapping.service.service.PairingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

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

    @DeleteMapping(path = "/pairing/{deviceId}")
    public String homePage(@PathVariable() final UUID deviceId) throws NoSuchDeviceException {
        pairingService.deletePairing(deviceId);
        return "home";
    }
}
