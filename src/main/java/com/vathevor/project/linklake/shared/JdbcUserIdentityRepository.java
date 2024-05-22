package com.vathevor.project.linklake.shared;

import com.vathevor.shared.spring.identity.UserIdentity;
import com.vathevor.shared.spring.identity.UserIdentityRepository;
import com.vathevor.shared.util.ShortUUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcUserIdentityRepository implements UserIdentityRepository {

    private static final RowMapper<UserIdentity> USER_IDENTITY_ROW_MAPPER = (rs, rowNum) -> new UserIdentity(
            ShortUUID.fromString(rs.getString("user_id")),
            rs.getString("idp_sub")
    );

    private final JdbcClient jdbcClient;

    @Override
    public Optional<UserIdentity> findUserIdentityByIdpSub(String idpSub) {
        log.info("Find userIdentity by idpSub: {}", idpSub);
        return jdbcClient.sql("SELECT user_id, idp_sub FROM linklake.user_identity WHERE idp_sub = :idp_sub")
                .param("idp_sub", idpSub)
                .query(USER_IDENTITY_ROW_MAPPER)
                .optional();
    }

    @Override
    public void save(UserIdentity userIdentity) {
        log.info("Save: {}", userIdentity);
        jdbcClient.sql("INSERT INTO linklake.user_identity (user_id, idp_sub) VALUES (:user_id, :idp_sub)")
                .param("user_id", userIdentity.userId().value())
                .param("idp_sub", userIdentity.idpSub())
                .update();
    }
}
