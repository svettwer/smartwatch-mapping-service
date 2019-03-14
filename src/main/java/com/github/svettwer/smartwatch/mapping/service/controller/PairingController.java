package com.github.svettwer.smartwatch.mapping.service.controller;

import com.github.svettwer.smartwatch.mapping.service.dto.PairingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@RestController
public class PairingController {

    private final DataSource dataSource;
    private KafkaTemplate<String, PairingRequest> kafkaTemplate;
    private final KafkaListenerEndpointRegistry registry;

    @Autowired
    public PairingController(final DataSource dataSource,
                             final KafkaTemplate<String, PairingRequest> kafkaTemplate,
                             final KafkaListenerEndpointRegistry registry) {
        this.dataSource = dataSource;
        this.kafkaTemplate = kafkaTemplate;
        this.registry = registry;
    }

    @PostMapping("/pairing")
    public ResponseEntity postPairing(@RequestBody final PairingRequest pairingRequest) throws SQLException{

        startKafkaListener(registry);
        saveTemporaryPairing(pairingRequest);
        kafkaTemplate.send("pairing.temporary", pairingRequest);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    private void startKafkaListener(final KafkaListenerEndpointRegistry registry) {
        final MessageListenerContainer listenerContainer = registry.getListenerContainer("PairingResultListener");
        listenerContainer.start();
    }

    private void saveTemporaryPairing(final PairingRequest pairingRequest) throws SQLException {
        try(final Connection connection = dataSource.getConnection()){
            try(final PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO pairings (customer_id, device_id, temporary) " +
                            "VALUES (?, ?, ?)")){

                preparedStatement.setString(1, pairingRequest.getCustomerId().toString());
                preparedStatement.setString(2, pairingRequest.getDeviceId().toString());
                preparedStatement.setBoolean(3, true);

                preparedStatement.execute();
            }
        }
    }
}
