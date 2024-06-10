package com.vathevor.project.linklake.query.infrastructure;

import com.vathevor.project.linklake.query.domain.tag.SimpleTagView;
import com.vathevor.project.linklake.query.domain.tag.taggedresource.TaggedResourceReadOnlyRepository;
import com.vathevor.project.linklake.query.domain.tag.taggedresource.TaggedResourceView;
import com.vathevor.shared.util.ShortUUID;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class TaggedResourceReadOnlyJdbcRepository implements TaggedResourceReadOnlyRepository {

    private static final String USER_ID = "user_id";
    private static final String TAG_IDS = "tag_ids";

    private static final ResultSetExtractor<List<TaggedResourceView>> TAGGED_RESOURCE_VIEW_EXTRACTOR = rs -> {
        Map<ShortUUID, TaggedResourceView.TaggedResourceViewBuilder> result = new HashMap<>();
        while (rs.next()) {
            ShortUUID resourceId = ShortUUID.fromString(rs.getString("resource_id"));
            result.putIfAbsent(resourceId, TaggedResourceView.builder());
            result.get(resourceId)
                    .resourceId(resourceId)
                    .name(rs.getString("resource_name"))
                    .tag(new SimpleTagView(
                            ShortUUID.fromString(rs.getString("tag_id")),
                            rs.getString("tag_name")
                    ));
        }
        return result.values()
                .stream()
                .map(TaggedResourceView.TaggedResourceViewBuilder::build)
                .toList();
    };

    private final JdbcClient jdbcClient;

    @Override
    public List<TaggedResourceView> queryResources(ShortUUID userId) {
        return jdbcClient.sql("""
                        SELECT r.resource_id AS resource_id,
                               r.name        AS resource_name,
                               t.tag_id      AS tag_id,
                               t.name        AS tag_name
                        FROM linklake.resource r
                                 INNER JOIN linklake.resource_tag rt on r.resource_id = rt.resource_id
                                 INNER JOIN linklake.tag t on t.tag_id = rt.tag_id
                        WHERE r.user_id = :user_id;
                        """)
                .param(USER_ID, userId.value())
                .query(TAGGED_RESOURCE_VIEW_EXTRACTOR);
    }

    @Override
    public List<TaggedResourceView> queryResourcesHavingAllTags(ShortUUID userId, Set<ShortUUID> tagIds) {
        return jdbcClient.sql("""
                        SELECT r.resource_id AS resource_id,
                               r.name        AS resource_name,
                               t.tag_id      AS tag_id,
                               t.name        AS tag_name
                        FROM linklake.resource r
                                 INNER JOIN linklake.resource_tag rt on r.resource_id = rt.resource_id
                                 INNER JOIN linklake.tag t on t.tag_id = rt.tag_id
                        WHERE r.user_id = :user_id
                          AND r.resource_id IN (SELECT resource_id
                                                FROM linklake.resource_tag
                                                WHERE tag_id IN (:tag_ids)
                                                GROUP BY resource_id
                                                HAVING COUNT(*) = :tag_ids_length);
                        """)
                .param(USER_ID, userId.value())
                .param(TAG_IDS, tagIds.stream().map(ShortUUID::value).toList())
                .param("tag_ids_length", tagIds.size())
                .query(TAGGED_RESOURCE_VIEW_EXTRACTOR);
    }

    @Override
    public List<TaggedResourceView> queryResourcesHavingEitherTags(ShortUUID userId, Set<ShortUUID> tagIds) {
        return jdbcClient.sql("""
                        SELECT r.resource_id AS resource_id,
                               r.name        AS resource_name,
                               t.tag_id      AS tag_id,
                               t.name        AS tag_name
                        FROM linklake.resource r
                                 INNER JOIN linklake.resource_tag rt on r.resource_id = rt.resource_id
                                 INNER JOIN linklake.tag t on t.tag_id = rt.tag_id
                        WHERE r.user_id = :user_id
                          AND r.resource_id IN (SELECT resource_id
                                                FROM linklake.resource_tag
                                                WHERE tag_id IN (:tag_ids));
                        """)
                .param(USER_ID, userId.value())
                .param(TAG_IDS, tagIds.stream().map(ShortUUID::value).toList())
                .query(TAGGED_RESOURCE_VIEW_EXTRACTOR);
    }
}
