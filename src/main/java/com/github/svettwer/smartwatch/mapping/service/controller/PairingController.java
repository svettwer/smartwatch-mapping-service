package com.github.svettwer.smartwatch.mapping.service.controller;

import com.github.svettwer.smartwatch.mapping.service.database.Pairing;
import com.github.svettwer.smartwatch.mapping.service.dto.PairingRequest;
import com.github.svettwer.smartwatch.mapping.service.exception.NoSuchDeviceException;
import com.github.svettwer.smartwatch.mapping.service.service.PairingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pairing")
public class PairingController {

    private PairingService pairingService;

    @Autowired
    public PairingController(final PairingService pairingService) {
        this.pairingService = pairingService;
    }

    @PostMapping
    public ResponseEntity postPairing(@RequestBody final PairingRequest pairingRequest) {
        pairingService.createNewPairing(pairingRequest);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping
    public List<Pairing> getPairings(){
        return pairingService.getPairings();
    }

    @DeleteMapping
    public Pairing deletePairing(@RequestBody final UUID deviceId) throws NoSuchDeviceException {
        return pairingService.deletePairing(deviceId);
    }

    @PostMapping("/retry")
    public ResponseEntity retryPostPairing(@RequestBody final PairingRequest pairingRequest) throws NoSuchDeviceException {
        pairingService.deletePairing(pairingRequest.getDeviceId());
        pairingService.createNewPairing(pairingRequest);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
