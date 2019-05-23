package com.github.svettwer.smartwatch.mapping.service.service;

import com.github.svettwer.smartwatch.mapping.service.database.Pairing;
import com.github.svettwer.smartwatch.mapping.service.database.PairingRepository;
import com.github.svettwer.smartwatch.mapping.service.dto.PairingRequest;
import com.github.svettwer.smartwatch.mapping.service.exception.NoSuchDeviceException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PairingService {

    private Logger logger = LoggerFactory.getLogger(PairingService.class);

    private KafkaTemplate<String, PairingRequest> kafkaTemplate;
    private final PairingRepository pairingRepository;

    @Autowired
    public PairingService(final KafkaTemplate<String, PairingRequest> kafkaTemplate,
                          final PairingRepository pairingRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.pairingRepository = pairingRepository;
    }

    public void createNewPairing(final PairingRequest pairingRequest){
        logger.trace("creating new pairing from request: {}", pairingRequest);
        pairingRepository.save(new Pairing(
                pairingRequest.getDeviceId(),
                pairingRequest.getCustomerId(),
                true));
        kafkaTemplate.send("pairing.temporary", pairingRequest);
    }

    public List<Pairing> getPairings(){
        logger.trace("requesting pairings...");
        return pairingRepository.findAll();
    }

    public Pairing deletePairing(final UUID deviceId) throws NoSuchDeviceException {
        logger.trace("delete pairing: {}", deviceId);
        final Optional<Pairing> byId = pairingRepository.findById(deviceId);
        pairingRepository.deleteById(deviceId);
        return byId.orElseThrow(() -> new NoSuchDeviceException(deviceId));
    }

    public void savePairing(final Pairing pairing){
        logger.trace("creating new pairing: {}", pairing);
        pairingRepository.save(pairing);
        if(pairing.isTemporary()){
            kafkaTemplate.send(
                    "pairing.temporary",
                    new PairingRequest(pairing.getCustomerId(),
                            pairing.getDeviceId()));
        }
    }

    public File getPairingsAsCsv() throws IOException {
        final File file = new File("pairings.csv");
        final FileWriter out = new FileWriter(file);
        try(final CSVPrinter printer = getCsvPrinter(out)){
            for (final Pairing pairing : getPairings()){
                printer.printRecord(
                        pairing.getDeviceId(),
                        pairing.getCustomerId(),
                        pairing.isTemporary());
            }
        }
        return file;
    }

    private CSVPrinter getCsvPrinter(final FileWriter out) throws IOException {
        return new CSVPrinter(out, CSVFormat
                .DEFAULT
                .withHeader("deviceId", "customerId", "is temporary")
                .withDelimiter(';'));
    }
}
