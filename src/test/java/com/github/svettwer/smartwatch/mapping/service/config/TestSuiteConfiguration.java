package com.github.svettwer.smartwatch.mapping.service.config;

import com.consol.citrus.dsl.endpoint.CitrusEndpoints;
import com.consol.citrus.dsl.runner.TestRunner;
import com.consol.citrus.dsl.runner.TestRunnerAfterTestSupport;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.kafka.endpoint.KafkaEndpoint;
import com.consol.citrus.variable.GlobalVariables;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class TestSuiteConfiguration {

    @Bean
    public GlobalVariables globalVariables(){
        final GlobalVariables globalVariables = new GlobalVariables();
        globalVariables.getVariables().put("customerId", "citrus:randomUUID()");
        globalVariables.getVariables().put("deviceId", "citrus:randomUUID()");
        return globalVariables;
    }

    @Bean
    public HttpClient smartphoneClient(){
        return CitrusEndpoints.http()
                .client()
                .requestUrl("http://localhost:8080/api")
                .build();
    }

    @Bean("temporaryPairingEndpoint")
    public KafkaEndpoint temporaryPairingEndpoint() {
        return CitrusEndpoints
                .kafka()
                .asynchronous()
                .server("localhost:9092")
                .topic("pairing.temporary")
                .consumerGroup("pairings")
                .build();
    }

    @Bean("pairingResultEndpoint")
    public KafkaEndpoint pairingResultEndpoint() {
        return CitrusEndpoints
                .kafka()
                .asynchronous()
                .server("localhost:9092")
                .topic("pairing.result")
                .consumerGroup("pairings")
                .build();
    }

    @Bean
    public DriverManagerDataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/testdb");
        dataSource.setUsername("postgres");
        dataSource.setPassword("");

        return dataSource;
    }

    @Bean
    public TestRunnerAfterTestSupport cleanUp(){
        return new TestRunnerAfterTestSupport() {
            @Override
            public void afterTest(final TestRunner testRunner) {
                testRunner.sql(executeSQLBuilder -> executeSQLBuilder
                        .dataSource(dataSource())
                        .statement("DELETE FROM pairing"));

                testRunner.purgeEndpoints(purgeEndpointsBuilder ->
                        purgeEndpointsBuilder.endpoint(pairingResultEndpoint()));

                testRunner.purgeEndpoints(purgeEndpointsBuilder ->
                        purgeEndpointsBuilder.endpoint(temporaryPairingEndpoint()));
            }
        };
    }
}
