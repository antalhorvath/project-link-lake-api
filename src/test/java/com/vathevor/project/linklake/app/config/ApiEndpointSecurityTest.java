package com.vathevor.project.linklake.app.config;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("feature")
@WebMvcTest
@AutoConfigureMockMvc
class ApiEndpointSecurityTest {

    @Autowired
    MockMvc mockMvc;

    @ParameterizedTest(name = "Allows authenticated request on {0} endpoint")
    @MethodSource("applicationEndpoints")
    void allows_authenticated_request_on_endpoint(String endpoint) throws Exception {
        mockMvc.perform(
                        get(endpoint)
                                .with(SecurityMockMvcRequestPostProcessors.oauth2Login())
                )
                .andExpect(status().isOk());
    }

    @ParameterizedTest(name = "Prevents unauthenticated request on {0} endpoint")
    @MethodSource("applicationEndpoints")
    void prevents_unauthenticated_request_on_endpoint(String endpoint) throws Exception {
        mockMvc.perform(get(endpoint))
                .andExpect(status().isUnauthorized());
    }

    static Stream<String> applicationEndpoints() {
        return Stream.of(
                "/links"
        );
    }
}
