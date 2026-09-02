package com.braincontrol.brain_drone_swarm_server.auth;

import com.braincontrol.brain_drone_swarm_server.security.JwtService;
import com.braincontrol.brain_drone_swarm_server.user.AppUser;
import com.braincontrol.brain_drone_swarm_server.user.AppUserRepository;
import com.braincontrol.brain_drone_swarm_server.user.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class AuthService {
    private final AppUserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final boolean allowRoleSelection;

    public AuthService(
            AppUserRepository users,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            @Value("${app.security.allow-role-selection-on-registration:true}") boolean allowRoleSelection
    ) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.allowRoleSelection = allowRoleSelection;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.email());
        if (users.existsByEmail(email)) {
            throw new ApiException(HttpStatus.CONFLICT, "该邮箱已经注册，请直接登录");
        }

        UserRole selectedRole = request.role() == null ? UserRole.SOLDIER : request.role();
        UserRole role = allowRoleSelection ? selectedRole : UserRole.SOLDIER;
        AppUser user = new AppUser(
                email,
                request.displayName().trim(),
                passwordEncoder.encode(request.password()),
                role
        );
        users.saveAndFlush(user);
        return responseFor(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String email = normalizeEmail(request.email());
        AppUser user = users.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "邮箱或密码错误"));
        if (!user.isEnabled() || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "邮箱或密码错误");
        }
        return responseFor(user);
    }

    @Transactional(readOnly = true)
    public UserResponse currentUser(String userId) {
        return users.findById(java.util.UUID.fromString(userId))
                .map(UserResponse::from)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "登录账号不存在或已停用"));
    }

    private AuthResponse responseFor(AppUser user) {
        return new AuthResponse(
                jwtService.createAccessToken(user),
                "Bearer",
                jwtService.expiresInSeconds(),
                UserResponse.from(user)
        );
    }

    private static String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
