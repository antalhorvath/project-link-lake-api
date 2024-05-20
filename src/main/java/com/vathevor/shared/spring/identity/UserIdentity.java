package com.vathevor.shared.spring.identity;

import com.vathevor.shared.util.ShortUUID;

public record UserIdentity(
        ShortUUID userId,
        String idpSub
) {
}
