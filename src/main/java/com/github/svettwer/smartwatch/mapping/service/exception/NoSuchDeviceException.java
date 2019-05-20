package com.github.svettwer.smartwatch.mapping.service.exception;

import java.util.UUID;

public class NoSuchDeviceException extends Exception {
    public NoSuchDeviceException(final UUID deviceId) {
        super("No device found with device id " +  deviceId);
    }
}
