package com.github.svettwer.smartwatch.mapping.service.database;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Pairing {

    @Id
    private UUID deviceId;
    private UUID customerId;
    private boolean temporary;

    public Pairing(){}

    public Pairing(final UUID deviceId, final UUID customerId, final boolean temporary) {
        this.deviceId = deviceId;
        this.customerId = customerId;
        this.temporary = temporary;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(final UUID deviceId) {
        this.deviceId = deviceId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(final UUID customerId) {
        this.customerId = customerId;
    }

    public boolean isTemporary() {
        return temporary;
    }

    public void setTemporary(final boolean temporary) {
        this.temporary = temporary;
    }
}
