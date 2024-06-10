package com.vathevor.project.linklake.command.infrastructure;

import com.vathevor.project.linklake.command.domain.link.LinkEntity;
import com.vathevor.project.linklake.command.domain.link.LinkRepository;
import com.vathevor.shared.util.ShortUUID;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LinkJdbcWriteRepository implements LinkRepository {

    private static final String USER_ID = "user_id";
    private static final String LINK_ID = "link_id";
    private static final String NAME = "name";
    private static final String LINK = "link";
    private static final String MODIFIED_AT = "modified_at";

    private static final RowMapper<LinkEntity> LINK_MAPPER = (rs, num) -> LinkEntity.builder()
            .linkId(ShortUUID.fromUUIDString(rs.getString(LINK_ID)))
            .userId(ShortUUID.fromUUIDString(rs.getString(USER_ID)))
            .name(rs.getString(NAME))
            .link(rs.getString(LINK))
            .modifiedAt(LocalDate.parse(rs.getString(MODIFIED_AT)))
            .build();

    private final JdbcClient jdbcClient;

    @Override
    public Optional<LinkEntity> findUsersLinkById(ShortUUID linkId, ShortUUID userId) {
        return jdbcClient.sql("""
                        SELECT link_id, user_id, name, link, modified_at
                        FROM linklake.link
                        WHERE link_id = :link_id
                          AND user_id = :user_id
                        """)
                .param(LINK_ID, linkId.value())
                .param(USER_ID, userId.value())
                .query(LINK_MAPPER)
                .optional();
    }

    @Override
    public void save(LinkEntity link) {
        jdbcClient.sql("""
                        INSERT INTO linklake.link (link_id, user_id, name, link, modified_at)
                        VALUES (:link_id, :user_id, :name, :link, :modified_at)
                        """)
                .param(LINK_ID, link.linkId().value())
                .param(USER_ID, link.userId().value())
                .param(NAME, link.name())
                .param(LINK, link.link())
                .param(MODIFIED_AT, Date.valueOf(link.modifiedAt()))
                .update();
    }

    @Override
    public void delete(LinkEntity linkEntity) {
        jdbcClient.sql("DELETE FROM linklake.link WHERE user_id = :user_id AND link_id = :link_id")
                .param(USER_ID, linkEntity.userId().value())
                .param(LINK_ID, linkEntity.linkId().value())
                .update();
    }
}
