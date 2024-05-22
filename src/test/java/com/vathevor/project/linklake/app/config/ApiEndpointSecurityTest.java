package com.vathevor.project.linklake.app.config;

import com.vathevor.project.linklake.command.domain.link.LinkCommandService;
import com.vathevor.shared.util.ShortUUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static com.vathevor.shared.test.CustomResultMatchers.statusIsEither;
import static com.vathevor.shared.test.CustomResultMatchers.statusIsNot;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;

@Tag("feature")
@WebMvcTest
@AutoConfigureMockMvc
class ApiEndpointSecurityTest {

    @MockBean
    LinkCommandService linkCommandService;

    @Autowired
    MockMvc mockMvc;

    @ParameterizedTest(name = "Allows authenticated request on {0} {1} endpoint")
    @MethodSource("applicationEndpoints")
    void allows_authenticated_request_on_endpoint(HttpMethod method, String endpoint) throws Exception {
        mockMvc.perform(request(method, endpoint)
                        .with(SecurityMockMvcRequestPostProcessors.oauth2Login())
                        .with(csrf())
                )
                .andExpect(statusIsNot(UNAUTHORIZED, FORBIDDEN));
    }

    @ParameterizedTest(name = "Prevents unauthenticated request on {0} {1} endpoint")
    @MethodSource("applicationEndpoints")
    void prevents_unauthenticated_request_on_endpoint(HttpMethod method, String endpoint) throws Exception {
        mockMvc.perform(request(method, endpoint))
                .andExpect(statusIsEither(UNAUTHORIZED, FORBIDDEN));
    }

    static Stream<Arguments> applicationEndpoints() {
        return Stream.of(
                Arguments.of(HttpMethod.GET, "/links"),
                Arguments.of(HttpMethod.PUT, "/links/" + ShortUUID.randomUUID())
        );
    }
}
