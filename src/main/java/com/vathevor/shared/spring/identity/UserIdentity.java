package com.vathevor.shared.spring.identity;

import java.util.UUID;

public record UserIdentity(
        UUID uuid,
        String idpSub
) {
}
