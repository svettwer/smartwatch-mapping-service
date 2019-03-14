package com.github.svettwer.smartwatch.mapping.service.configuration.jdbc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

@Configuration
public class JdbcApplicationConfig {

    @Value(value = "${mapping.service.jdbc.url}")
    private String url;

    @Value(value = "${mapping.service.jdbc.username}")
    private String username;

    @Value(value = "${mapping.service.jdbc.password}")
    private String password;

    @Value(value = "${mapping.service.jdbc.driverClassName}")
    private String driverClassName;


    @Bean
    public SingleConnectionDataSource dataSource() {
        final SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }
}
