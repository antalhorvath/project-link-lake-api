package com.vathevor.project.linklake;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;

import javax.sql.DataSource;

@TestConfiguration
public class TestJdbcClientConfig {

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .username("sa")
                .password("password")
                .url("jdbc:hsqldb:mem:testdb")
                .driverClassName("org.hsqldb.jdbcDriver")
                .build();
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public JdbcClient jdbcClient(NamedParameterJdbcTemplate jdbcTemplate) {
        return JdbcClient.create(jdbcTemplate);
    }
}
