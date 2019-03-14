package com.github.svettwer.smartwatch.mapping.service.dto;

import java.util.Objects;
import java.util.UUID;

public class PairingRequest {

    private UUID customerId;
    private UUID deviceId;

    public PairingRequest(final UUID customerId, final UUID deviceId) {
        this.customerId = customerId;
        this.deviceId = deviceId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof PairingRequest)) return false;
        final PairingRequest that = (PairingRequest) o;
        return Objects.equals(customerId, that.customerId) &&
                Objects.equals(deviceId, that.deviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, deviceId);
    }
}
