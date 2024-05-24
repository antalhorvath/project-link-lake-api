package com.vathevor.project.linklake.command.domain.link;

import com.vathevor.shared.util.ShortUUID;

import java.util.Optional;

public interface LinkRepository {

    Optional<LinkEntity> findUsersLinkById(ShortUUID linkId, ShortUUID userId);

    void save(LinkEntity link);
}
