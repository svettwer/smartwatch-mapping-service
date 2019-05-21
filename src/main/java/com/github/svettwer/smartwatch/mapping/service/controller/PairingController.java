package com.github.svettwer.smartwatch.mapping.service.controller;

import com.github.svettwer.smartwatch.mapping.service.database.Pairing;
import com.github.svettwer.smartwatch.mapping.service.database.PairingRepository;
import com.github.svettwer.smartwatch.mapping.service.dto.PairingRequest;
import com.github.svettwer.smartwatch.mapping.service.exception.NoSuchDeviceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/pairing")
public class PairingController {

    private KafkaTemplate<String, PairingRequest> kafkaTemplate;
    private final PairingRepository pairingRepository;

    @Autowired
    public PairingController(final KafkaTemplate<String, PairingRequest> kafkaTemplate,
                             final PairingRepository pairingRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.pairingRepository = pairingRepository;
    }

    @PostMapping
    public ResponseEntity postPairing(@RequestBody final PairingRequest pairingRequest) {
        pairingRepository.save(new Pairing(
                pairingRequest.getDeviceId(),
                pairingRequest.getCustomerId(),
                true));
        kafkaTemplate.send("pairing.temporary", pairingRequest);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping
    public List<Pairing> getPairings(){
        return pairingRepository.findAll();
    }

    @DeleteMapping
    public Pairing deletePairing(@RequestBody final UUID deviceId) throws NoSuchDeviceException {
        final Optional<Pairing> byId = pairingRepository.findById(deviceId);
        pairingRepository.deleteById(deviceId);
        return byId.orElseThrow(() -> new NoSuchDeviceException(deviceId));
    }

    @PostMapping("/retry")
    public ResponseEntity retryPostPairing(@RequestBody final PairingRequest pairingRequest) {
        pairingRepository.deleteById(pairingRequest.getDeviceId());
        return postPairing(pairingRequest);
    }
}
