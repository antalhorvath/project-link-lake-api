package com.vathevor.project.linklake.query.infrastructure;

import com.vathevor.project.linklake.query.domain.tag.TagReadOnlyRepository;
import com.vathevor.project.linklake.query.domain.tag.TagView;
import com.vathevor.shared.util.ShortUUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.vathevor.project.linklake.shared.SharedTestConstants.USER_1_ID;
import static com.vathevor.project.linklake.shared.SharedTestConstants.USER_2_ID;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ContextConfiguration(classes = TagReadOnlyJdbcRepository.class)
class TagReadOnlyJdbcRepositoryTest {

    @Autowired
    TagReadOnlyRepository repository;

    @Sql("classpath:/test_data/user_identity/insert_user_identities.sql")
    @Sql("classpath:/test_data/tag/insert_tagged_resources_of_user_01.sql")
    @Sql("classpath:/test_data/tag/insert_tagged_resources_of_user_02.sql")
    @Test
    void finds_user_1s_tags_in_descending_number_of_tagged_resources() {
        List<TagView> links = repository.findTags(USER_1_ID);
        assertThat(links).isEqualTo(
                List.of(
                        TagView.builder()
                                .tagId(ShortUUID.fromString("d931a3391a714ceb99f7054a289a7cf8"))
                                .name("learning")
                                .numberOfTaggedResources(4)
                                .build(),
                        TagView.builder()
                                .tagId(ShortUUID.fromString("a430b6dbbdc848cb8766a3c33ceec941"))
                                .name("java")
                                .numberOfTaggedResources(2)
                                .build(),
                        TagView.builder()
                                .tagId(ShortUUID.fromString("a17f3f8ddcbb45c2a6409129d357ff63"))
                                .name("work")
                                .numberOfTaggedResources(1)
                                .build()
                )
        );
    }

    @Sql("classpath:/test_data/user_identity/insert_user_identities.sql")
    @Sql("classpath:/test_data/tag/insert_tagged_resources_of_user_01.sql")
    @Sql("classpath:/test_data/tag/insert_tagged_resources_of_user_02.sql")
    @Test
    void finds_user_2s_tags_in_descending_number_of_tagged_resources() {
        List<TagView> links = repository.findTags(USER_2_ID);
        assertThat(links).isEqualTo(
                List.of(
                        TagView.builder()
                                .tagId(ShortUUID.fromString("4f5c18dce5f6423087593a91a0e68d00"))
                                .name("learning")
                                .numberOfTaggedResources(2)
                                .build(),
                        TagView.builder()
                                .tagId(ShortUUID.fromString("d3435d8a3c8d4836939a0f882728cb72"))
                                .name("exercise")
                                .numberOfTaggedResources(1)
                                .build(),
                        TagView.builder()
                                .tagId(ShortUUID.fromString("f393ce3e5da640b58c10aa0cd39cc77d"))
                                .name("fun")
                                .numberOfTaggedResources(1)
                                .build(),
                        TagView.builder()
                                .tagId(ShortUUID.fromString("b7156e08bbe04e7abcfb213a0248a60a"))
                                .name("work")
                                .numberOfTaggedResources(1)
                                .build()
                )
        );
    }
}
