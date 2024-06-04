package com.vathevor.project.linklake.query.infrastructure;

import com.vathevor.project.linklake.query.domain.tag.TagReadOnlyRepository;
import com.vathevor.project.linklake.query.domain.tag.TagView;
import com.vathevor.shared.util.ShortUUID;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TagReadOnlyJdbcRepository implements TagReadOnlyRepository {

    private final JdbcClient jdbcClient;

    @Override
    public List<TagView> findTags(ShortUUID shortUUID) {
        return jdbcClient.sql("""
                        SELECT t.tag_id, name, count(*) AS number_of_tagged_resources
                        FROM linklake.tag t
                        INNER JOIN linklake.resource_tag rt ON t.tag_id = rt.tag_id
                        WHERE t.user_id = :user_id
                        GROUP BY t.tag_id, t.name
                        ORDER BY number_of_tagged_resources DESC, t.name
                        """)
                .param("user_id", shortUUID.value())
                .query((rs, r) -> TagView.builder()
                        .tagId(ShortUUID.fromString(rs.getString("tag_id")))
                        .name(rs.getString("name"))
                        .numberOfTaggedResources(rs.getInt("number_of_tagged_resources"))
                        .build()).list();
    }
}
