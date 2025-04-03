package org.meena.treeforum.hierarchical_forum.repository;

import org.junit.jupiter.api.Test;
import org.meena.treeforum.hierarchical_forum.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindUser() {
        // 准备测试数据
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");

        // 执行保存
        User savedUser = userRepository.save(user);

        // 验证保存结果
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("testUser");
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");

        // 验证查询方法
        Optional<User> foundByUsername = userRepository.findByUsername("testUser");
        assertThat(foundByUsername).isPresent();
        assertThat(foundByUsername.get().getEmail()).isEqualTo("test@example.com");

        Optional<User> foundByEmail = userRepository.findByEmail("test@example.com");
        assertThat(foundByEmail).isPresent();
        assertThat(foundByEmail.get().getUsername()).isEqualTo("testUser");
    }

    @Test
    void testFindByUsername_WhenUserNotExists() {
        Optional<User> result = userRepository.findByUsername("nonExistingUser");
        assertThat(result).isEmpty();
    }

    @Test
    void testFindByEmail_WhenUserNotExists() {
        Optional<User> result = userRepository.findByEmail("nonExisting@example.com");
        assertThat(result).isEmpty();
    }

    @Test
    void testExistsByUsername() {
        // 准备测试数据
        User user = new User();
        user.setUsername("existingUser");
        user.setEmail("existing@example.com");
        userRepository.save(user);

        // 验证存在检查
        assertThat(userRepository.existsByUsername("existingUser")).isTrue();
        assertThat(userRepository.existsByUsername("nonExistingUser")).isFalse();
    }

    @Test
    void testExistsByEmail() {
        // 准备测试数据
        User user = new User();
        user.setUsername("user1");
        user.setEmail("user1@example.com");
        userRepository.save(user);

        // 验证存在检查
        assertThat(userRepository.existsByEmail("user1@example.com")).isTrue();
        assertThat(userRepository.existsByEmail("nonExisting@example.com")).isFalse();
    }

    @Test
    void testUniqueUsernameConstraint() {
        // 准备第一个用户
        User user1 = new User();
        user1.setUsername("commonUsername");
        user1.setEmail("user1@example.com");
        userRepository.save(user1);

        // 尝试创建同名用户
        User user2 = new User();
        user2.setUsername("commonUsername"); // 相同的用户名
        user2.setEmail("user2@example.com");

        // 这里应该抛出异常，测试验证约束
        org.junit.jupiter.api.Assertions.assertThrows(
                org.springframework.dao.DataIntegrityViolationException.class,
                () -> userRepository.save(user2)
        );
    }

    @Test
    void testUniqueEmailConstraint() {
        // 准备第一个用户
        User user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("common@example.com");
        userRepository.save(user1);

        // 尝试创建同邮箱用户
        User user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("common@example.com"); // 相同的邮箱

        // 这里应该抛出异常，测试验证约束
        org.junit.jupiter.api.Assertions.assertThrows(
                org.springframework.dao.DataIntegrityViolationException.class,
                () -> userRepository.save(user2)
        );
    }

    @Test
    void testDeleteUser() {
        // 准备测试数据
        User user = new User();
        user.setUsername("userToDelete");
        user.setEmail("delete@example.com");
        User savedUser = userRepository.save(user);

        // 验证保存成功
        assertThat(userRepository.findById(savedUser.getId())).isPresent();

        // 执行删除
        userRepository.delete(savedUser);

        // 验证删除结果
        assertThat(userRepository.findById(savedUser.getId())).isEmpty();
        assertThat(userRepository.existsByUsername("userToDelete")).isFalse();
    }
}
