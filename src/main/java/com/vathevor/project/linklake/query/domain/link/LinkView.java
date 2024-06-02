package com.vathevor.project.linklake.query.domain.link;

import com.vathevor.shared.util.ShortUUID;
import lombok.Builder;

@Builder
public record LinkView(
        ShortUUID linkId,
        String name,
        String link
) {
}
