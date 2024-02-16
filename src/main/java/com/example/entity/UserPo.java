package com.example.entity;

import io.swagger.v3.oas.annotations.media.PatternProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "用户")
public class UserPo {
    @Schema(description = "账号")
    private String username;
    @Schema(description = "密码")
    private String password;
}
