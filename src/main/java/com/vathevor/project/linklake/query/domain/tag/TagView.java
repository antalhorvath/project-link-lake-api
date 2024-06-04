package com.vathevor.project.linklake.query.domain.tag;

import com.vathevor.shared.util.ShortUUID;
import lombok.Builder;

@Builder
public record TagView(
        ShortUUID tagId,
        String name,
        int numberOfTaggedResources
) {

}
