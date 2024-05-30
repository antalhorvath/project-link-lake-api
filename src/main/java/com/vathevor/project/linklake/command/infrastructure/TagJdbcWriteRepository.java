package com.vathevor.project.linklake.command.infrastructure;

import com.vathevor.project.linklake.command.domain.tag.entity.TagEntity;
import com.vathevor.project.linklake.command.domain.tag.repository.TagRepository;
import com.vathevor.shared.util.ShortUUID;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class TagJdbcWriteRepository implements TagRepository {

    private static final int BATCH_SIZE_INSERT_TAGS = 10;

    private final JdbcClient jdbcClient;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Set<ShortUUID> findExistingTagIds(ShortUUID userId, Set<ShortUUID> tagIds) {
        return jdbcClient.sql("SELECT tag_id FROM linklake.tag WHERE user_id = :user_id AND tag_id IN (:tag_ids)")
                .param("user_id", userId.value())
                .param("tag_ids", tagIds.stream().map(ShortUUID::value).toList())
                .query((rs, rowNum) -> ShortUUID.fromString(rs.getString("tag_id")))
                .set();
    }

    @Override
    public void saveAll(Collection<TagEntity> tags) {
        jdbcTemplate.batchUpdate("INSERT INTO linklake.tag (tag_id, user_id, name) VALUES (?, ?, ?)",
                tags,
                BATCH_SIZE_INSERT_TAGS,
                (ps, tag) -> {
                    ps.setString(1, tag.tagId().value());
                    ps.setString(2, tag.userId().value());
                    ps.setString(3, tag.name());
                });
    }
}
