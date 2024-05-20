package com.vathevor.project.linklake;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestJdbcClientConfig.class) // TODO - Spring JdbcClient should be autoconfigured, why not working? Should be closed by now https://github.com/spring-projects/spring-boot/issues/36579
@SpringBootTest
class LinkLakeApplicationTests {

    @Test
    void contextLoads() {
    }

}
