package com.vathevor.project.linklake.query.infrastructure;

import com.vathevor.project.linklake.query.domain.tag.SimpleTagView;
import com.vathevor.project.linklake.query.domain.tag.taggedresource.TaggedResourceView;
import com.vathevor.shared.util.ShortUUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Set;

import static com.vathevor.project.linklake.shared.SharedTestConstants.USER_1_ID;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ContextConfiguration(classes = TaggedResourceReadOnlyJdbcRepository.class)
class TaggedResourceReadOnlyJdbcRepositoryTest {

    SimpleTagView TAG_JAVA = new SimpleTagView(ShortUUID.fromString("a430b6dbbdc848cb8766a3c33ceec941"), "java");
    SimpleTagView TAG_LEARNING = new SimpleTagView(ShortUUID.fromString("d931a3391a714ceb99f7054a289a7cf8"), "learning");
    SimpleTagView TAG_WORK = new SimpleTagView(ShortUUID.fromString("a17f3f8ddcbb45c2a6409129d357ff63"), "work");

    @Autowired
    TaggedResourceReadOnlyJdbcRepository repository;

    @Sql("classpath:/test_data/user_identity/insert_user_identities.sql")
    @Sql("classpath:/test_data/tag/insert_tagged_resources_of_user_01.sql")
    @Sql("classpath:/test_data/tag/insert_tagged_resources_of_user_02.sql")
    @Test
    void queries_all_resources_of_user() {
        List<TaggedResourceView> result = repository.queryResources(USER_1_ID);
        assertThat(result).containsExactlyInAnyOrderElementsOf(
                List.of(
                        TaggedResourceView.builder()
                                .resourceId(ShortUUID.fromString("323007fa053745b6a63a89ce76372869"))
                                .name("InfoQ")
                                .tag(TAG_JAVA)
                                .build(),
                        TaggedResourceView.builder()
                                .resourceId(ShortUUID.fromString("02b349ed70434609b1c53f72ef874b7f"))
                                .name("Baeldung")
                                .tag(TAG_JAVA)
                                .tag(TAG_LEARNING)
                                .build(),
                        TaggedResourceView.builder()
                                .resourceId(ShortUUID.fromString("b57693dbb18e4c25bf01a57cfe45b201"))
                                .name("Voxxeddays")
                                .tag(TAG_LEARNING)
                                .build(),
                        TaggedResourceView.builder()
                                .resourceId(ShortUUID.fromString("48c7c5ee32424bfeb9660a4d1915ac3e"))
                                .name("Pluralsight")
                                .tag(TAG_LEARNING)
                                .build(),
                        TaggedResourceView.builder()
                                .resourceId(ShortUUID.fromString("07cd5b428f944817b43ec0c35269b4db"))
                                .name("Udemy")
                                .tag(TAG_LEARNING)
                                .build(),
                        TaggedResourceView.builder()
                                .resourceId(ShortUUID.fromString("37bdae5015814c359acd976a28a051c7"))
                                .name("Workday")
                                .tag(TAG_WORK)
                                .build()
                )
        );
    }

    @Sql("classpath:/test_data/user_identity/insert_user_identities.sql")
    @Sql("classpath:/test_data/tag/insert_tagged_resources_of_user_01.sql")
    @Sql("classpath:/test_data/tag/insert_tagged_resources_of_user_02.sql")
    @Test
    void queries_resources_that_is_tagged_with_all_specified_tags() {
        List<TaggedResourceView> result = repository.queryResourcesHavingAllTags(USER_1_ID, Set.of(
                TAG_JAVA.tagId(),
                TAG_LEARNING.tagId()
        ));
        assertThat(result).containsOnly(
                TaggedResourceView.builder()
                        .resourceId(ShortUUID.fromString("02b349ed70434609b1c53f72ef874b7f"))
                        .name("Baeldung")
                        .tag(TAG_JAVA)
                        .tag(TAG_LEARNING)
                        .build()
        );
    }

    @Sql("classpath:/test_data/user_identity/insert_user_identities.sql")
    @Sql("classpath:/test_data/tag/insert_tagged_resources_of_user_01.sql")
    @Sql("classpath:/test_data/tag/insert_tagged_resources_of_user_02.sql")
    @Test
    void queries_resources_that_is_tagged_with_either_of_the_specified_tags() {
        List<TaggedResourceView> result = repository.queryResourcesHavingEitherTags(USER_1_ID, Set.of(
                TAG_JAVA.tagId(),
                TAG_WORK.tagId()
        ));
        assertThat(result).containsExactlyInAnyOrderElementsOf(
                List.of(
                        TaggedResourceView.builder()
                                .resourceId(ShortUUID.fromString("323007fa053745b6a63a89ce76372869"))
                                .name("InfoQ")
                                .tag(TAG_JAVA)
                                .build(),
                        TaggedResourceView.builder()
                                .resourceId(ShortUUID.fromString("02b349ed70434609b1c53f72ef874b7f"))
                                .name("Baeldung")
                                .tag(TAG_JAVA)
                                .tag(TAG_LEARNING)
                                .build(),
                        TaggedResourceView.builder()
                                .resourceId(ShortUUID.fromString("37bdae5015814c359acd976a28a051c7"))
                                .name("Workday")
                                .tag(TAG_WORK)
                                .build()
                )
        );
    }
}
