package org.meena.treeforum.hierarchical_forum.dto;

import org.meena.treeforum.hierarchical_forum.model.User;

public class JwtAuthenticationResponse {
    private String accessToken;
    private Long id;
    private String username;
    private String email;

    public JwtAuthenticationResponse(String accessToken, User user) {
        this.accessToken = accessToken;
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
