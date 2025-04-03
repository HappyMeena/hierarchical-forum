package org.meena.treeforum.hierarchical_forum;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class HierarchicalForumApplicationTest {

    @Test
    void contextLoads(ApplicationContext context) {
        // This test will fail if the application context cannot start
        assertThat(context).isNotNull();
    }
}