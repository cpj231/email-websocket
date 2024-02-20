package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSenderImpl javaMailSmtp() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // 邮箱服务器地址
        mailSender.setHost("smtp.qq.com");
        mailSender.setPort(587);
        // 邮箱账号
        mailSender.setUsername("123123@qq.com");
        // 邮箱密码或授权码
        mailSender.setPassword("123123123123");

        Properties props = mailSender.getJavaMailProperties();
        // 指定邮件传输协议
        props.put("mail.transport.protocol", "smtp");
        // 指定是否需要进行SMTP身份验证
        props.put("mail.smtp.auth","true");
        // 指定是否启用STARTTLS加密
        props.put("mail.smtp.starttls.enable","true");
        // 指定是否启用调试模式
        props.put("mail.debug","true");
        return mailSender;
    }

    @Bean
    public JavaMailSenderImpl javaMailImap() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // 邮箱服务器地址
        mailSender.setHost("imap.qq.com");
        mailSender.setPort(993);
        // 邮箱账号
        mailSender.setUsername("123@qq.com");
        // 邮箱密码或授权码
        mailSender.setPassword("123123123");

        Properties props = mailSender.getJavaMailProperties();
        // 指定邮件传输协议
        props.put("mail.transport.protocol", "imap");
        // 指定是否启用STARTTLS加密
        props.put("mail.imap.ssl.enable","true");
        // 指定是否启用调试模式
//        props.put("mail.debug","true");
        return mailSender;
    }

    public static void main(String[] args) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        System.out.println(mailSender.getJavaMailProperties().toString());
        System.out.println(27/2);
    }

}
