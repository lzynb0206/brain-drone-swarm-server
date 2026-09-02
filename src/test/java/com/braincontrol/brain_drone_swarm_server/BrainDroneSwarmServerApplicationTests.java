package com.braincontrol.brain_drone_swarm_server;

import com.braincontrol.brain_drone_swarm_server.security.JwtService;
import com.braincontrol.brain_drone_swarm_server.user.AppUser;
import com.braincontrol.brain_drone_swarm_server.user.UserRole;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BrainDroneSwarmServerApplicationTests {

    @Test
    void bcryptUsesRandomSaltAndStillVerifiesThePassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

        String firstHash = encoder.encode("correct-horse-battery-staple");
        String secondHash = encoder.encode("correct-horse-battery-staple");

        assertNotEquals(firstHash, secondHash);
        assertTrue(encoder.matches("correct-horse-battery-staple", firstHash));
        assertTrue(encoder.matches("correct-horse-battery-staple", secondHash));
    }

    @Test
    void jwtContainsTheAuthenticatedUserAndRole() {
        JwtService jwtService = new JwtService(
                "test-only-secret-that-is-longer-than-thirty-two-bytes",
                60
        );
        AppUser user = new AppUser(
                "researcher@example.com",
                "测试科研人员",
                "unused-test-hash",
                UserRole.RESEARCHER
        );

        String token = jwtService.createAccessToken(user);
        Claims claims = jwtService.parse(token);

        assertEquals(user.getId().toString(), claims.getSubject());
        assertEquals("researcher@example.com", claims.get("email", String.class));
        assertEquals("researcher", claims.get("role", String.class));
        assertEquals(3600, jwtService.expiresInSeconds());
    }
}
