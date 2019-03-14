package com.github.svettwer.smartwatch.mapping.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;


public class PairingResult implements Serializable {

    private UUID deviceId;
    private UUID customerId;
    private boolean successful;

    @JsonCreator
    public PairingResult(@JsonProperty("customerId") final UUID customerId,
                         @JsonProperty("deviceId")final UUID deviceId,
                         @JsonProperty("successful")final Boolean successful) {
        this.customerId = customerId;
        this.deviceId = deviceId;
        this.successful = successful;
    }

    public UUID getCustomerId() {
        return customerId;
    }


    public UUID getDeviceId() {
        return deviceId;
    }


    public boolean isSuccessful() {
        return successful;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof PairingResult)) return false;
        final PairingResult pairingResult = (PairingResult) o;
        return Objects.equals(customerId, pairingResult.customerId) &&
                Objects.equals(deviceId, pairingResult.deviceId) &&
                Objects.equals(successful, pairingResult.successful);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, deviceId, successful);
    }
}
