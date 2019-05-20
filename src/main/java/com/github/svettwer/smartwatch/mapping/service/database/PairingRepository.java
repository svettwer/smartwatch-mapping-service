package com.github.svettwer.smartwatch.mapping.service.database;

import com.github.svettwer.smartwatch.mapping.service.dto.PairingRequest;
import com.github.svettwer.smartwatch.mapping.service.dto.PairingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class PairingRepository {

    private final DataSource dataSource;

    @Autowired
    public PairingRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveTemporaryPairing(final PairingRequest pairingRequest) throws SQLException {
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

    public void persistTemporaryPairing(final PairingResult pairingResult) throws SQLException {
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
