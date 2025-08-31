package com.roudane.inventory.infra.transverse.config.datasource;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = "spring.datasource")
public class OracleDataSourceProperties {
    private String url;
    private String username;
    private String password;
    private String driverClassName;


}
