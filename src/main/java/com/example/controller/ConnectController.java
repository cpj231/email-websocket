package com.example.controller;

import com.example.entity.EmailPo;
import com.example.websocket.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class ConnectController {
    @MessageMapping("/info")
    @SendTo("/topic/info")
    public Message send(String message) {
        return new Message("Server", "Echo: " + message, LocalDateTime.now());
    }

    @MessageMapping("/sendEmail")
    public void sendEmail(EmailPo emailPo) {
        log.info("send{}",12321);
    }

    @GetMapping("/unread")
    public List<EmailPo> getUnreadEmails() {
        return new ArrayList<EmailPo>(List.of(new EmailPo("12","21","121")));
    }
}
