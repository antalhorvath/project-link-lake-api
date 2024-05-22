package com.vathevor.project.linklake.command.domain.link;

import com.vathevor.shared.util.ShortUUID;
import lombok.Builder;

@Builder
public record LinkEntity(
        ShortUUID userId,
        ShortUUID linkId,
        String name,
        String link
) {
}
