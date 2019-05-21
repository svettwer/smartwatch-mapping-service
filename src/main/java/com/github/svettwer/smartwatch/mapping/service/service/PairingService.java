package com.github.svettwer.smartwatch.mapping.service.service;

import com.github.svettwer.smartwatch.mapping.service.database.Pairing;
import com.github.svettwer.smartwatch.mapping.service.database.PairingRepository;
import com.github.svettwer.smartwatch.mapping.service.dto.PairingRequest;
import com.github.svettwer.smartwatch.mapping.service.exception.NoSuchDeviceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PairingService {

    private KafkaTemplate<String, PairingRequest> kafkaTemplate;
    private final PairingRepository pairingRepository;

    @Autowired
    public PairingService(final KafkaTemplate<String, PairingRequest> kafkaTemplate,
                          final PairingRepository pairingRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.pairingRepository = pairingRepository;
    }

    public void createNewPairing(final PairingRequest pairingRequest){
        pairingRepository.save(new Pairing(
                pairingRequest.getDeviceId(),
                pairingRequest.getCustomerId(),
                true));
        kafkaTemplate.send("pairing.temporary", pairingRequest);
    }

    public List<Pairing> getPairings(){
        return pairingRepository.findAll();
    }

    public Pairing deletePairing(final UUID deviceId) throws NoSuchDeviceException {
        final Optional<Pairing> byId = pairingRepository.findById(deviceId);
        pairingRepository.deleteById(deviceId);
        return byId.orElseThrow(() -> new NoSuchDeviceException(deviceId));
    }
}
