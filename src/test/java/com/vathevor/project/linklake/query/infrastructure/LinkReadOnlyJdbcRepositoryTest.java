package com.vathevor.project.linklake.query.infrastructure;

import com.vathevor.project.linklake.query.domain.link.LinkView;
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
@ContextConfiguration(classes = LinkReadOnlyJdbcRepository.class)
class LinkReadOnlyJdbcRepositoryTest {

    @Autowired
    LinkReadOnlyJdbcRepository repository;

    @Sql("classpath:/test_data/user_identity/insert_user_identities.sql")
    @Sql("classpath:/test_data/link/insert-links_uf_user_01.sql")
    @Sql("classpath:/test_data/link/insert-links_uf_user_02.sql")
    @Test
    void finds_user_1s_links_in_descending_order_of_modified_at() {
        List<LinkView> links = repository.findLinks(USER_1_ID);
        assertThat(links).isEqualTo(
                List.of(
                        LinkView.builder()
                                .linkId(ShortUUID.fromString("f518b691d2314f00abad4cfe46f63697"))
                                .name("user1 link3")
                                .link("https://link3.com")
                                .build(),
                        LinkView.builder()
                                .linkId(ShortUUID.fromString("e5bc7046193042e7b6fd561ac425d312"))
                                .name("user1 link2")
                                .link("https://link2.com")
                                .build(),
                        LinkView.builder()
                                .linkId(ShortUUID.fromString("9231bc7b95d84c50b1499b8a548204e3"))
                                .name("user1 link1")
                                .link("https://link1.com")
                                .build()
                )
        );
    }

    @Sql("classpath:/test_data/user_identity/insert_user_identities.sql")
    @Sql("classpath:/test_data/link/insert-links_uf_user_01.sql")
    @Sql("classpath:/test_data/link/insert-links_uf_user_02.sql")
    @Test
    void finds_user_2s_links_in_descending_order_of_modified_at() {
        List<LinkView> links = repository.findLinks(USER_2_ID);
        assertThat(links).isEqualTo(
                List.of(
                        LinkView.builder()
                                .linkId(ShortUUID.fromString("6eb0204faa1046aab7602cda41b48aea"))
                                .name("user2 link1")
                                .link("https://link1.com")
                                .build(),
                        LinkView.builder()
                                .linkId(ShortUUID.fromString("d645220d3a89415580a1b9160aabfa15"))
                                .name("user2 link3")
                                .link("https://link3.com")
                                .build(),
                        LinkView.builder()
                                .linkId(ShortUUID.fromString("07bbf65ae56f48bcba645678cbbe1d3e"))
                                .name("user2 link2")
                                .link("https://link2.com")
                                .build()
                )
        );
    }
}
