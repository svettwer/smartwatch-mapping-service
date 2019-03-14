package com.github.svettwer.smartwatch.mapping.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;


public class PairingResult implements Serializable {

    private UUID deviceId;
    private UUID customerId;
    private boolean wasSuccessful;

    public PairingResult(final UUID customerId, final UUID deviceId, final Boolean wasSuccessful) {
        this.customerId = customerId;
        this.deviceId = deviceId;
        this.wasSuccessful = wasSuccessful;
    }

    public UUID getCustomerId() {
        return customerId;
    }


    public UUID getDeviceId() {
        return deviceId;
    }


    public boolean getWasSuccessful() {
        return wasSuccessful;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof PairingResult)) return false;
        final PairingResult pairingResult = (PairingResult) o;
        return Objects.equals(customerId, pairingResult.customerId) &&
                Objects.equals(deviceId, pairingResult.deviceId) &&
                Objects.equals(wasSuccessful, pairingResult.wasSuccessful);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, deviceId, wasSuccessful);
    }
}
