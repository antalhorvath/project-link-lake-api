package com.vathevor.project.linklake.query.domain.tag;

import com.vathevor.shared.util.ShortUUID;

public record SimpleTagView(
        ShortUUID tagId,
        String name
) {
}
