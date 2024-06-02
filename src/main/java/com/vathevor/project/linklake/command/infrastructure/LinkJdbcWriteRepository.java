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

    private static final RowMapper<LinkEntity> LINK_MAPPER = (rs, num) -> LinkEntity.builder()
            .linkId(ShortUUID.fromUUIDString(rs.getString("link_id")))
            .userId(ShortUUID.fromUUIDString(rs.getString("user_id")))
            .name(rs.getString("name"))
            .link(rs.getString("link"))
            .modifiedAt(LocalDate.parse(rs.getString("modified_at")))
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
                .param("link_id", linkId.value())
                .param("user_id", userId.value())
                .query(LINK_MAPPER)
                .optional();
    }

    @Override
    public void save(LinkEntity link) {
        jdbcClient.sql("""
                        INSERT INTO linklake.link (link_id, user_id, name, link, modified_at)
                        VALUES (:link_id, :user_id, :name, :link, :modified_at)
                        """)
                .param("link_id", link.linkId().value())
                .param("user_id", link.userId().value())
                .param("name", link.name())
                .param("link", link.link())
                .param("modified_at", Date.valueOf(link.modifiedAt()))
                .update();
    }
}
