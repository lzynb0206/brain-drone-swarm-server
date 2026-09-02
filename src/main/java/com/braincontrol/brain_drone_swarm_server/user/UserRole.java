package com.braincontrol.brain_drone_swarm_server.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum UserRole {
    SOLDIER,
    COMMANDER,
    RESEARCHER,
    ENGINEER,
    ADMIN;

    @JsonCreator
    public static UserRole fromJson(String value) {
        if (value == null || value.isBlank()) {
            return SOLDIER;
        }
        return valueOf(value.trim().toUpperCase(Locale.ROOT));
    }

    @JsonValue
    public String toJson() {
        return name().toLowerCase(Locale.ROOT);
    }
}
