package com.vathevor.project.linklake.query.domain.tag;

import com.vathevor.shared.util.ShortUUID;

import java.util.List;

public interface TagReadOnlyRepository {

    List<TagView> findTags(ShortUUID shortUUID);
}
