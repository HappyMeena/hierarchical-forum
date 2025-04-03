package org.meena.treeforum.hierarchical_forum.service;

import org.meena.treeforum.hierarchical_forum.model.User;
import org.meena.treeforum.hierarchical_forum.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
