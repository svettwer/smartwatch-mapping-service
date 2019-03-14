package com.github.svettwer.smartwatch.mapping.service.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mapping.service.jdbc")
public class JdbcConfigurationProperties {

    private String url;
    private String username;
    private String password;
    private String driverClassName;


    public String getDriverClassName() {
        return driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setDriverClassName(final String driverClassName) {
        this.driverClassName = driverClassName;
    }
}
