package com.example.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Schema(description = "邮件")
@NoArgsConstructor
public class EmailPo {

    @Schema(description = "寄件人")
    private String from;
    @Schema(description = "主题")
    private String subject;
    @Schema(description = "内容")
    private String content;
}
