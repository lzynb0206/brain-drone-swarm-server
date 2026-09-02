package com.braincontrol.brain_drone_swarm_server.auth;

import com.braincontrol.brain_drone_swarm_server.user.AppUser;
import com.braincontrol.brain_drone_swarm_server.user.UserRole;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String displayName,
        UserRole role
) {
    public static UserResponse from(AppUser user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getDisplayName(), user.getRole());
    }
}
