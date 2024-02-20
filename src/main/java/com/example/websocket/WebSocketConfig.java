package com.example.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
/**
 * 注册websocket处理器
 * */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册自定义的 WebSocket 处理器，并设置连接路径
        registry.addHandler(customWebSocketHandler(), "/custom-websocket")
                .setAllowedOrigins("*"); // 允许所有来源的连接 如果需要跨域
    }

    @Bean
    public CustomWebSocketHandler customWebSocketHandler() {
        return new SocketTest();
    }
}
