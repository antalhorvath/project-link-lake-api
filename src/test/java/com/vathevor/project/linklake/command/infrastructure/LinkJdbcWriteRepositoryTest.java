package com.vathevor.project.linklake.command.infrastructure;

import com.vathevor.project.linklake.command.domain.link.LinkEntity;
import com.vathevor.shared.util.ShortUUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.Optional;

import static com.vathevor.project.linklake.shared.SharedTestConstants.USER_1_ID;
import static com.vathevor.project.linklake.shared.SharedTestConstants.USER_2_ID;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ContextConfiguration(classes = LinkJdbcWriteRepository.class)
class LinkJdbcWriteRepositoryTest {

    @Autowired
    LinkJdbcWriteRepository repository;

    @Sql("classpath:/test_data/user_identity/insert_user_identities.sql")
    @Test
    void saves_link() {
        LinkEntity link = LinkEntity.builder()
                .linkId(ShortUUID.randomUUID())
                .userId(USER_1_ID)
                .name("example")
                .link("https://example.com")
                .modifiedAt(LocalDate.now())
                .build();

        repository.save(link);

        Optional<LinkEntity> savedLink = repository.findUsersLinkById(link.linkId(), link.userId());
        assertThat(savedLink)
                .isPresent()
                .contains(link);
    }

    @Sql("classpath:/test_data/user_identity/insert_user_identities.sql")
    @Sql("classpath:/test_data/link/insert-links_uf_user_01.sql")
    @Sql("classpath:/test_data/link/insert-links_uf_user_02.sql")
    @Nested
    class Delete {

        @Test
        void deletes_link_of_owner() {
            LinkEntity linkOfOwner = LinkEntity.builder()
                    .linkId(ShortUUID.fromString("9231bc7b95d84c50b1499b8a548204e3"))
                    .userId(USER_1_ID)
                    .build();

            repository.delete(linkOfOwner);

            assertThat(repository.findUsersLinkById(linkOfOwner.linkId(), linkOfOwner.userId())).isEmpty();
        }

        @Test
        void does_not_delete_link_of_someone_else() {
            ShortUUID linkIdBelongingToUser02 = ShortUUID.fromString("07bbf65ae56f48bcba645678cbbe1d3e");
            LinkEntity linkOfOwner = LinkEntity.builder()
                    .linkId(linkIdBelongingToUser02)
                    .userId(USER_1_ID)
                    .build();

            repository.delete(linkOfOwner);

            assertThat(repository.findUsersLinkById(linkIdBelongingToUser02, USER_2_ID)).isPresent();
        }
    }
}
