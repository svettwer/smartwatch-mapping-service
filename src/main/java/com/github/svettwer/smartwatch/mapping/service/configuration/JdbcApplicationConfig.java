package com.github.svettwer.smartwatch.mapping.service.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

@Configuration
@EnableConfigurationProperties(JdbcConfigurationProperties.class)
public class JdbcApplicationConfig {

    @Bean
    public SingleConnectionDataSource dataSource(final JdbcConfigurationProperties configurationProperties) {
        final SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setDriverClassName(configurationProperties.getDriverClassName());
        dataSource.setUrl(configurationProperties.getUrl());
        dataSource.setUsername(configurationProperties.getUsername());
        dataSource.setPassword(configurationProperties.getPassword());

        return dataSource;
    }
}
