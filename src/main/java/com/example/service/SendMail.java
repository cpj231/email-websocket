package com.example.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Component;

import java.util.Properties;
@Component
public class SendMail {
    public static void test() {
        // 收件人邮箱地址
        String to = "panjie_chen@163.com";

        // 发件人邮箱地址
        String from = "2317613629@qq.com";

        // 设置发件人的用户名和密码
        String username = "2317613629@qq.com";
        String password = "wyunxiygzyhedigd";
        // SMTP服务器地址
        String host = "smtp.qq.com";

        // 创建属性对象，用于配置邮件会话
        Properties properties = System.getProperties();

        // 设置SMTP服务器
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.starttls","true");
        properties.setProperty("mail.smtp.auth","true");
        properties.setProperty("mail.smtp.port","587"); // SMTP端口号，一般为587或者465
        // 获取默认的Session对象
        Session session = Session.getInstance(properties,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // 创建一个默认的MimeMessage对象
            MimeMessage message = new MimeMessage(session);

            // 设置发件人
            message.setFrom(new InternetAddress(from));

            // 设置收件人
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // 设置邮件主题
            message.setSubject("这是邮件主题");

            // 设置邮件内容
            message.setText("这是邮件内容");

            // 发送消息
            Transport.send(message);
            System.out.println("邮件已发送。");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
