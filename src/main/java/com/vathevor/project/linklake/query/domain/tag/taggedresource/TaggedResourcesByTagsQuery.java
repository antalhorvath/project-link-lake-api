package com.vathevor.project.linklake.query.domain.tag.taggedresource;

import com.vathevor.shared.util.LogicalOperator;
import com.vathevor.shared.util.ShortUUID;

import java.util.Set;

public record TaggedResourcesByTagsQuery(
        ShortUUID userId,
        Set<ShortUUID> tagIds,
        LogicalOperator operator
) {
}
