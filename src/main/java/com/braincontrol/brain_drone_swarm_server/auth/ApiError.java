package com.braincontrol.brain_drone_swarm_server.auth;

import java.time.Instant;

public record ApiError(String message, int status, Instant timestamp) {
    public static ApiError of(String message, int status) {
        return new ApiError(message, status, Instant.now());
    }
}
