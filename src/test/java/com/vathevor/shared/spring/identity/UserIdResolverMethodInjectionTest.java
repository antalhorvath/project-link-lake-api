package com.vathevor.shared.spring.identity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vathevor.shared.util.ShortUUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

record ResolvedUserId(ShortUUID userId) {
    static ResolvedUserId empty() {
        return new ResolvedUserId(null);
    }
}

@Import({
        UserIdResolverConfig.class
})
@RestController
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
class DummyController {

    @GetMapping("/dummyEndpointWithUuidType")
    public ResolvedUserId dummy(@UserId ShortUUID userId) {
        return new ResolvedUserId(userId);
    }

    @GetMapping("/dummyEndpointWithUnsupportedType")
    public ResolvedUserId dummy(@UserId String userIdWithNotSupportedType) {
        return Optional.ofNullable(userIdWithNotSupportedType)
                .map(ShortUUID::fromUUIDString)
                .map(ResolvedUserId::new)
                .orElseGet(ResolvedUserId::empty);
    }
}

@SpringBootTest(classes = {DummyController.class})
@AutoConfigureMockMvc
class UserIdResolverMethodInjectionTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    void injects_user_id_with_uuid_type_into_controller() throws Exception {
        var resolvedUserId = performGET("/dummyEndpointWithUuidType");
        assertThat(resolvedUserId).isPresent();
    }

    @Test
    void does_not_inject_user_id_with_invalid_type_into_controller() throws Exception {
        var resolvedUserId = performGET("/dummyEndpointWithUnsupportedType");
        assertThat(resolvedUserId).isEmpty();
    }

    Optional<UUID> performGET(String endpoint) throws Exception {
        String responseBody = mockMvc.perform(
                        get(endpoint)
                                .with(SecurityMockMvcRequestPostProcessors.oauth2Login()
                                        .attributes(attr -> attr.put("sub", "dummySub"))))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        ResolvedUserId resolvedUserId = objectMapper.readValue(responseBody, ResolvedUserId.class);
        return Optional.ofNullable(resolvedUserId.userId())
                .map(ShortUUID::toUUID);
    }
}
