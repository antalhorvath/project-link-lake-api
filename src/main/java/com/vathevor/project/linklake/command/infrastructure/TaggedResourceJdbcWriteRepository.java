package com.vathevor.project.linklake.command.infrastructure;

import com.vathevor.project.linklake.command.domain.tag.entity.TagEntity;
import com.vathevor.project.linklake.command.domain.tag.entity.TaggedResourceEntity;
import com.vathevor.project.linklake.command.domain.tag.repository.TaggedResourceRepository;
import com.vathevor.shared.util.ShortUUID;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TaggedResourceJdbcWriteRepository implements TaggedResourceRepository {

    private static final int BATCH_SIZE_INSERT_TAGS = 10;

    private static final String NAME = "name";
    private static final String USER_ID = "user_id";
    private static final String RESOURCE_ID = "resource_id";
    private static final String RESOURCE_NAME = "resource_name";
    private static final String TAG_ID = "tag_id";
    private static final String TAG_NAME = "tag_name";

    private final JdbcClient jdbcClient;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<TaggedResourceEntity> findUserResourceById(ShortUUID userId, ShortUUID resourceId) {
        return jdbcClient.sql("""
                        SELECT
                            r.resource_id AS resource_id,
                            r.user_id AS user_id,
                            r.name AS resource_name,
                            t.tag_id AS tag_id,
                            t.name AS tag_name
                        FROM linklake.resource r
                            INNER JOIN linklake.resource_tag rt ON r.resource_id = rt.resource_id
                            INNER JOIN linklake.tag t ON t.tag_id = rt.tag_id
                        WHERE r.resource_id = :resource_id AND r.user_id = :user_id
                        """)
                .param(USER_ID, userId.value())
                .param(RESOURCE_ID, resourceId.value())
                .query(rs -> {
                    if (rs.next()) {
                        return Optional.of(buildEntityFrom(rs));
                    } else {
                        return Optional.empty();
                    }
                });
    }

    private static TaggedResourceEntity buildEntityFrom(ResultSet rs) throws SQLException {
        var entityBuilder = TaggedResourceEntity.builder();
        do {
            entityBuilder
                    .resourceId(ShortUUID.fromString(rs.getString(RESOURCE_ID)))
                    .userId(ShortUUID.fromString(rs.getString(USER_ID)))
                    .name(rs.getString(RESOURCE_NAME))
                    .tag(TagEntity.builder()
                            .userId(ShortUUID.fromString(rs.getString(USER_ID)))
                            .tagId(ShortUUID.fromString(rs.getString(TAG_ID)))
                            .name(rs.getString(TAG_NAME))
                            .build());
        } while (rs.next());
        return entityBuilder.build();
    }

    @Override
    public void saveTaggedResource(TaggedResourceEntity taggedResource) {
        saveResource(taggedResource);
        saveResourceTags(taggedResource);
    }

    private void saveResource(TaggedResourceEntity taggedResource) {
        jdbcClient.sql("INSERT INTO linklake.resource (resource_id, user_id, name) VALUES (:resource_id, :user_id, :name)")
                .param(RESOURCE_ID, taggedResource.resourceId().value())
                .param(USER_ID, taggedResource.userId().value())
                .param(NAME, taggedResource.name())
                .update();
    }

    private void saveResourceTags(TaggedResourceEntity taggedResource) {
        jdbcTemplate.batchUpdate("INSERT INTO linklake.resource_tag (user_id, resource_id, tag_id) VALUES (?, ?, ?)",
                taggedResource.tags(),
                BATCH_SIZE_INSERT_TAGS,
                (ps, tag) -> {
                    ps.setString(1, tag.userId().value());
                    ps.setString(2, taggedResource.resourceId().value());
                    ps.setString(3, tag.tagId().value());
                });
    }
}
