package com.vathevor.project.linklake.query.domain.tag.taggedresource;

import com.vathevor.project.linklake.query.domain.tag.SimpleTagView;
import com.vathevor.shared.util.ShortUUID;
import lombok.Builder;
import lombok.Singular;

import java.util.List;

@Builder
public record TaggedResourceView(
        ShortUUID resourceId,
        String name,
        @Singular
        List<SimpleTagView> tags
) {
}
