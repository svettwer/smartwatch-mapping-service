package com.github.svettwer.smartwatch.mapping.service;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.kafka.endpoint.KafkaEndpoint;
import com.consol.citrus.message.MessageType;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import javax.sql.DataSource;


public class MappingServiceIT extends TestNGCitrusTestRunner {

    @Autowired
    private HttpClient smartphoneClient;

    @Autowired
    @Qualifier("temporaryPairingEndpoint")
    private KafkaEndpoint temporaryPairingEndpoint;

    @Autowired
    private DataSource dataSource;

    private final Resource pairingRequestTemplate = new ClassPathResource(
            "com/github/svettwer/smartwatch/mapping/service/payloads/pairing-request.json");

    @CitrusTest
    @Test
    public void testNewMappingIsPropagated(){
        //WHEN
        http(http -> http
                .client(smartphoneClient)
                .send()
                .post("/pairing")
                .messageType(MessageType.JSON)
                .contentType(ContentType.APPLICATION_JSON.toString())
                .payload(pairingRequestTemplate));

        //THEN
        query(executeSQLQueryBuilder -> executeSQLQueryBuilder.dataSource(dataSource)
                .statement("SELECT * FROM pairing")
                .validate("device_id", "${deviceId}")
                .validate("customer_id", "${customerId}")
                .validate("temporary", "true"));

        receive(receiveMessageBuilder -> receiveMessageBuilder
                .endpoint(temporaryPairingEndpoint)
                .messageType(MessageType.JSON)
                .payload(pairingRequestTemplate));

        http(http -> http
                .client("smartphoneClient")
                .receive()
                .response(HttpStatus.OK));
    }

}