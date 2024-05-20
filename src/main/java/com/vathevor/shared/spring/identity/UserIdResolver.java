package com.vathevor.shared.spring.identity;

import com.vathevor.shared.util.ShortUUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.security.Principal;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserIdResolver implements HandlerMethodArgumentResolver {

    private final UserIdentityRepository userIdentityRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(UserId.class)) {
            if (parameter.getParameterType().equals(ShortUUID.class)) {
                return true;
            } else {
                log.error("@UserId argument requires type com.vathevor.shared.util.ShortUUID, but found: {}", parameter.getParameterType());
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        var principalName = Optional.ofNullable(webRequest.getUserPrincipal())
                .map(Principal::getName);

        return principalName.flatMap(userIdentityRepository::findUserIdentityByIdpSub)
                .or(() -> principalName.map(this::initialiseUserIdentity))
                .map(UserIdentity::userId)
                .orElseThrow();
    }

    private UserIdentity initialiseUserIdentity(String idpSub) {
        log.info("Initialise user identity for: {}", idpSub);
        var userIdentity = new UserIdentity(ShortUUID.randomUUID(), idpSub);
        userIdentityRepository.save(userIdentity);
        log.info("Initialised new user identity: {}", userIdentity);
        return userIdentity;
    }
}
