package com.vathevor.shared.spring.identity;

import java.util.Optional;

public interface UserIdentityRepository {

    Optional<UserIdentity> findUserIdentityByIdpSub(String idpSub);

    void save(UserIdentity userIdentity);
}
