package com.vathevor.project.linklake.command.domain.tag.entity;

import com.vathevor.shared.util.ShortUUID;
import lombok.Builder;

@Builder
public record TagEntity(
        ShortUUID userId,
        ShortUUID tagId,
        String name
) {

}
