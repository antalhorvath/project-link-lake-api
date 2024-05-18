package com.vathevor.shared.spring.identity;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class InMemoryUserIdentityRepository implements UserIdentityRepository {

    private final Map<String, UserIdentity> identityMap;

    public InMemoryUserIdentityRepository() {
        this.identityMap = new HashMap<>();
    }

    @Override
    public Optional<UserIdentity> findUserIdentityByIdpSub(String idpSub) {
        log.info("Find userIdentity by idpSub: {}", idpSub);
        return Optional.ofNullable(identityMap.get(idpSub));
    }

    @Override
    public void save(UserIdentity userIdentity) {
        log.info("Save: {}", userIdentity);
        identityMap.put(userIdentity.idpSub(), userIdentity);
    }
}
