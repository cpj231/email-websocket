package com.example.websocket;

import com.alibaba.fastjson2.JSONObject;
import com.example.entity.UserPo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;

@Slf4j
public class SocketTest extends CustomWebSocketHandler{

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // 处理客户端发来的消息
        String payload =(String) message.getPayload();
        UserPo userPo = JSONObject.parseObject(payload, UserPo.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("user_info","小米, 22, 女");
        map.put("type","user_info");
        log.info("123:{}",userPo);
        // 解析前端发送的账号密码信息
        // 假设这里直接解析为一个用户对象，并且返回用户信息给前端
//        UserPo userInfo = parseUserInfo(payload);
        // 假设处理完用户信息后，返回给前端的用户对象
        String responseUserInfo = "小米, 22, 女";
        session.sendMessage(new TextMessage(new JSONObject(map).toString(),true));

    }
}
