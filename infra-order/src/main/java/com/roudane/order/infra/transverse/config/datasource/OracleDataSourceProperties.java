package com.roudane.order.infra.transverse.config.datasource;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@ConfigurationProperties(prefix = "spring.datasource")
public class OracleDataSourceProperties {
    private String url;
    private String username;
    private String password;
    private String driverClassName;


}
