package com.braincontrol.brain_drone_swarm_server.auth;

import com.braincontrol.brain_drone_swarm_server.user.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "邮箱不能为空")
        @Email(message = "邮箱格式不正确")
        @Size(max = 254, message = "邮箱过长")
        String email,

        @NotBlank(message = "密码不能为空")
        @Size(min = 8, max = 72, message = "密码长度必须为 8 到 72 位")
        String password,

        @NotBlank(message = "显示名称不能为空")
        @Size(max = 80, message = "显示名称不能超过 80 个字符")
        String displayName,

        UserRole role
) {
}
