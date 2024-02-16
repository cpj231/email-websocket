package com.example.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.example.entity.EmailPo;
import com.example.entity.UserPo;
import com.example.service.ReceiveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/email")
@Tag(name = "示例控制器", description = "用于示例的API")
@ResponseBody
public class EmailController {

    @Autowired
    private ReceiveService receiveService;
    @Operation(summary = "获取邮件")
    @PostMapping
    public List<EmailPo> getEmail(@RequestBody UserPo user) {
        log.info("{}",user);
        List<EmailPo> emailPos = receiveService.checkForNewEmail(user);
        String json = JSON.toJSONString(receiveService.checkForNewEmail(user));

        return emailPos;
    }
}
