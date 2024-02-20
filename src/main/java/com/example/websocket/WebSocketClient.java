package com.example.websocket;

import com.alibaba.fastjson2.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class WebSocketClient {

    public static void sendToTopic(String topic, String message) throws URISyntaxException, IOException, InterruptedException {
        // 构建 POST 请求的参数
        Map<Object, Object> data = new HashMap<>();
        data.put("topic", topic);
        data.put("message", message);

        // 构建 POST 请求的 URI
        URI uri = new URI("http://localhost:8080/sendToTopic");

        // 构建 POST 请求的内容
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(JSONObject.toJSONString(data)))
                .build();

        // 发送 POST 请求
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // 打印响应结果
        System.out.println("HTTP Response Code: " + response.statusCode());
        System.out.println("HTTP Response Body: " + response.body());
    }

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        // 调用 sendToTopic 方法发送消息到指定主题
        sendToTopic("topicName", "Hello, world!");
    }
}
