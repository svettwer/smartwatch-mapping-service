package com.github.svettwer.smartwatch.mapping.service.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface PairingRepository extends JpaRepository<Pairing, UUID> {
}
