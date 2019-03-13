package com.github.svettwer.smartwatch.mapping.service.steps;

import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.dsl.runner.TestRunner;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class MappingStepDefinitions {

    /** Test runner to execute the test actions with*/
    @CitrusResource
    private TestRunner runner;

    @Given("a new paring is initiated")
    public void initializePairing() {
        runner.echo("initialize pairing");
    }

    @When("the pairing was successful")
    public void thePairingWasSuccessful() {
        runner.echo("pairing successful");
    }

    @Then("the pairing is persisted in the database")
    public void thePairingIsPersistedInTheDatabase() {
        runner.echo("pairing persisted");
    }
}
