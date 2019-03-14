package com.github.svettwer.smartwatch.mapping.service.steps;

import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.dsl.runner.TestRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.jdbc.message.JdbcMessage;
import com.consol.citrus.kafka.message.KafkaMessageHeaders;
import com.consol.citrus.message.MessageType;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;

public class MappingStepDefinitions {

    /** Test runner to execute the test actions with*/
    @CitrusResource
    private TestRunner runner;

    private final Resource pairingRequestTemplate = new ClassPathResource(
            "com/github/svettwer/smartwatch/mapping/service/payloads/pairing-request.json");

    private final Resource pairingResultTemplate = new ClassPathResource(
            "com/github/svettwer/smartwatch/mapping/service/payloads/pairing-result.json");

    @Given("a new paring is initiated")
    public void initializePairing() {
        runner.http(http -> http
                .client("smartphoneClient")
                .send()
                .post("/pairing")
                .messageType(MessageType.JSON)
                .payload(pairingRequestTemplate));

        runner.http(http -> http
                .client("smartphoneClient")
                .receive()
                .response(HttpStatus.OK));

        runner.receive(receiveMessageBuilder -> receiveMessageBuilder
                .endpoint("smartwatchMappingDatabase")
                .messageType(MessageType.JSON)
                .message(JdbcMessage.execute(
                        "INSERT INTO pairings (customer_id, device_id, temporary) " +
                                "VALUES (${customerId}, ${deviceId}, TRUE)")));

        runner.send(sendMessageBuilder -> sendMessageBuilder
                .endpoint("smartwatchMappingDatabase")
                .message(JdbcMessage.success()));

        runner.receive(receiveMessageBuilder -> receiveMessageBuilder
                .endpoint("temporaryPairingEndpoint")
                .messageType(MessageType.JSON)
                .payload(pairingRequestTemplate));
    }

    @When("the pairing was successful")
    public void thePairingWasSuccessful() {
        runner.send(receiveMessageBuilder -> receiveMessageBuilder
                .endpoint("pairingResultEndpoint")
                .messageType(MessageType.JSON)
                .payload(pairingResultTemplate));
    }

    @Then("the pairing is persisted in the database")
    public void thePairingIsPersistedInTheDatabase() {
        runner.receive(receiveMessageBuilder -> receiveMessageBuilder
                .endpoint("smartwatchMappingDatabase")
                .messageType(MessageType.JSON)
                .message(JdbcMessage.execute(
                        "UPDATE pairings" +
                                "SET temporary=FALSE " +
                                "WHERE customer_id=${customerId} and device_id=${deviceId}")));

        runner.send(sendMessageBuilder -> sendMessageBuilder
                .endpoint("smartwatchMappingDatabase")
                .message(JdbcMessage.success().rowsUpdated(1)));
    }
}
