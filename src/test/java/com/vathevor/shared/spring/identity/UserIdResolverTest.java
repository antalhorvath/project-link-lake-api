package com.vathevor.shared.spring.identity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Method;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserIdResolverTest {

    @Mock
    UserIdentityRepository userIdentityRepository;

    @InjectMocks
    UserIdResolver resolver;

    @ParameterizedTest(name = "supports the type {1} provided in {0} method: {2}")
    @MethodSource("methodParams")
    void supports_the_type_provided_in_method(String methodName, Class<?> paramType, boolean expectedToSupport) throws NoSuchMethodException {
        class DummyController {
            public void userIdUuidMethod(@UserId UUID userId) {
            }

            public void userIdStringMethod(@UserId String userId) {
            }

            public void uuidMethod(UUID userId) {
            }

            public void stringMethod(String userId) {
            }
        }
        Method method = DummyController.class.getMethod(methodName, paramType);
        MethodParameter methodParameter = new MethodParameter(method, 0);
        boolean isSupported = resolver.supportsParameter(methodParameter);
        assertThat(isSupported).isEqualTo(expectedToSupport);
    }

    static Stream<Arguments> methodParams() {
        return Stream.of(
                Arguments.of("userIdUuidMethod", UUID.class, true),
                Arguments.of("userIdStringMethod", String.class, false),
                Arguments.of("uuidMethod", UUID.class, false),
                Arguments.of("stringMethod", String.class, false)
        );
    }

    @Test
    void saves_user_identity_upon_first_interaction() {
        var idpSub = "dummyIdpSub";
        when(userIdentityRepository.findUserIdentityByIdpSub(idpSub))
                .thenReturn(Optional.empty());

        Object userId = resolver.resolveArgument(
                mock(MethodParameter.class),
                mock(ModelAndViewContainer.class),
                mockWebRequest(idpSub),
                mock(WebDataBinderFactory.class));

        var captor = ArgumentCaptor.forClass(UserIdentity.class);
        verify(userIdentityRepository).save(captor.capture());
        var savedUserIdentity = captor.getValue();
        assertThat(savedUserIdentity.uuid())
                .isNotNull()
                .isEqualTo(userId);
        assertThat(savedUserIdentity.idpSub()).isEqualTo(idpSub);
    }

    private NativeWebRequest mockWebRequest(String idpSub) {
        NativeWebRequest webRequest = mock(NativeWebRequest.class);
        when(webRequest.getUserPrincipal()).thenReturn(buildPrincipal(idpSub));
        return webRequest;
    }

    private Principal buildPrincipal(String idpSub) {
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        String nameAttributeKey = "sub";
        Map<String, Object> attributes = Map.of(nameAttributeKey, idpSub);
        OAuth2User principal = new DefaultOAuth2User(authorities, attributes, nameAttributeKey);
        return new OAuth2AuthenticationToken(principal, authorities, "clientRegistrationId");
    }

    @Test
    void retrieves_existing_user_identity_without_saving_it_again() {
        var idpSub = "dummyIdpSub";
        var userIdentity = new UserIdentity(UUID.randomUUID(), idpSub);
        when(userIdentityRepository.findUserIdentityByIdpSub(idpSub))
                .thenReturn(Optional.of(userIdentity));

        Object userId = resolver.resolveArgument(
                mock(MethodParameter.class),
                mock(ModelAndViewContainer.class),
                mockWebRequest(idpSub),
                mock(WebDataBinderFactory.class));

        assertThat(userId).isEqualTo(userIdentity.uuid());
        verifyNoMoreInteractions(userIdentityRepository);
    }
}
