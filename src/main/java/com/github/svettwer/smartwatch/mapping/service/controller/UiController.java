package com.github.svettwer.smartwatch.mapping.service.controller;

import com.github.svettwer.smartwatch.mapping.service.database.Pairing;
import com.github.svettwer.smartwatch.mapping.service.exception.NoSuchDeviceException;
import com.github.svettwer.smartwatch.mapping.service.service.PairingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        model.addAttribute("newPairing", new Pairing());
        return "home";
    }

    @GetMapping("/pairing/csv")
    @ResponseBody
    public void getCsv(final HttpServletResponse response) throws IOException {
        final File pairingCsv = pairingService.getPairingsAsCsv();
        response.setHeader("Content-type", "text/csv");
        response.setHeader("Content-disposition", "attachment;filename="+pairingCsv.getName());
        Files.copy(Paths.get(pairingCsv.toURI()), response.getOutputStream());
        response.getOutputStream().flush();
    }

    @DeleteMapping(path = "/pairing/{deviceId}")
    public String homePage(@PathVariable() final UUID deviceId, final Model model) throws NoSuchDeviceException {
        pairingService.deletePairing(deviceId);
        return homePage(model);
    }

    @PostMapping("/pairing")
    public String greetingSubmit(@ModelAttribute Pairing pairing, final Model model) {
        pairingService.savePairing(pairing);
        return homePage(model);
    }
}
