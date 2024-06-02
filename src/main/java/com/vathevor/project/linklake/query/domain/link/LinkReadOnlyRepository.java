package com.vathevor.project.linklake.query.domain.link;

import com.vathevor.shared.util.ShortUUID;

import java.util.List;

public interface LinkReadOnlyRepository {

    List<LinkView> findLinks(ShortUUID userId);
}
