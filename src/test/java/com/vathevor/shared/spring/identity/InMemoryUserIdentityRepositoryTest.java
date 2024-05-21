package com.vathevor.shared.spring.identity;

class InMemoryUserIdentityRepositoryTest extends UserIdentityRepositoryTest {

    @Override
    protected UserIdentityRepository createRepository() {
        return new InMemoryUserIdentityRepository();
    }
}
