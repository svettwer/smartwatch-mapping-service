package com.github.svettwer.smartwatch.mapping.service.controller;

import com.github.svettwer.smartwatch.mapping.service.database.PairingRepository;
import com.github.svettwer.smartwatch.mapping.service.dto.PairingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class PairingController {

    private KafkaTemplate<String, PairingRequest> kafkaTemplate;
    private final PairingRepository pairingRepository;

    @Autowired
    public PairingController(final KafkaTemplate<String, PairingRequest> kafkaTemplate,
                             final PairingRepository pairingRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.pairingRepository = pairingRepository;
    }

    @PostMapping("/pairing")
    public ResponseEntity postPairing(@RequestBody final PairingRequest pairingRequest) throws SQLException{
        pairingRepository.saveTemporaryPairing(pairingRequest);
        kafkaTemplate.send("pairing.temporary", pairingRequest);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
