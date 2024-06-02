package com.vathevor.project.linklake.query.infrastructure;

import com.vathevor.project.linklake.query.domain.link.LinkReadOnlyRepository;
import com.vathevor.project.linklake.query.domain.link.LinkView;
import com.vathevor.shared.util.ShortUUID;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LinkReadOnlyJdbcRepository implements LinkReadOnlyRepository {

    private static final String SELECT_USERS_LINKS_ORDERED_BY_DATE = """
            SELECT link_id, name, link
            FROM linklake.link
            WHERE user_id = :user_id
            ORDER BY modified_at DESC
            """;

    private final JdbcClient jdbcClient;

    @Override
    public List<LinkView> findLinks(ShortUUID userId) {
        return jdbcClient.sql(SELECT_USERS_LINKS_ORDERED_BY_DATE)
                .param("user_id", userId.value())
                .query((rs, r) -> LinkView.builder()
                        .linkId(ShortUUID.fromString(rs.getString("link_id")))
                        .name(rs.getString("name"))
                        .link(rs.getString("link"))
                        .build())
                .list();
    }
}
