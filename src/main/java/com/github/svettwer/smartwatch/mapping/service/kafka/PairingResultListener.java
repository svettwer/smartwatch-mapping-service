package com.github.svettwer.smartwatch.mapping.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.svettwer.smartwatch.mapping.service.dto.PairingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class PairingResultListener {

    private final DataSource dataSource;

    @Autowired
    public PairingResultListener(final DataSource dataSource){
        this.dataSource = dataSource;
    }

    @KafkaListener(id = "PairingResultListener", topics = "pairing.result", autoStartup = "false")
    public void listen(final String message) throws IOException, SQLException {
        final PairingResult pairingResult = new ObjectMapper().readValue(message, PairingResult.class);

        if(pairingResult.isSuccessful()){
            persistPairing(pairingResult);
        }
    }

    private void persistPairing(final PairingResult pairingResult) throws SQLException {
        try(final Connection connection = dataSource.getConnection()){
            try(final PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE pairings SET temporary=false " +
                            "WHERE customer_id=? and device_id=?")){

                preparedStatement.setString(1, pairingResult.getCustomerId().toString());
                preparedStatement.setString(2, pairingResult.getDeviceId().toString());

                preparedStatement.execute();
            }
        }
    }


}
