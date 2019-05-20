package com.github.svettwer.smartwatch.mapping.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.svettwer.smartwatch.mapping.service.database.PairingRepository;
import com.github.svettwer.smartwatch.mapping.service.dto.PairingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;

@Component
public class PairingResultListener {

    private final PairingRepository pairingRepository;

    @Autowired
    public PairingResultListener(final PairingRepository pairingRepository){
        this.pairingRepository = pairingRepository;
    }

    @KafkaListener(id = "PairingResultListener", topics = "pairing.result", autoStartup = "false")
    public void listen(final String message) throws IOException, SQLException {
        final PairingResult pairingResult = new ObjectMapper().readValue(message, PairingResult.class);

        if(pairingResult.isSuccessful()){
            pairingRepository.persistTemporaryPairing(pairingResult);
        }
    }


}
