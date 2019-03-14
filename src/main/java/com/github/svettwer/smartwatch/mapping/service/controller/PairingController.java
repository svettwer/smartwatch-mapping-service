package com.github.svettwer.smartwatch.mapping.service.controller;

import com.github.svettwer.smartwatch.mapping.service.dto.PairingRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PairingController {

    @PostMapping("/pairing")
    public ResponseEntity postPairing(@RequestBody final PairingRequest pairingRequest){
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
