package com.example.service;

import com.example.entity.EmailPo;
import com.example.entity.UserPo;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.MimeUtility;
import jakarta.mail.search.FlagTerm;

import jakarta.mail.Session;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


@Service
@Slf4j
public class ReceiveService {
    @Autowired
    private JavaMailSenderImpl javaMailSmtp;
    @Autowired
    private JavaMailSenderImpl javaMailImap;
    private static final String OUT_PUT_FOLDER="D:\\pdf\\";
    protected String test;

    public List<EmailPo> checkForNewEmail(UserPo userPo) {
        System.out.println("---------");
        javaMailImap.setUsername(userPo.getUsername());
        javaMailImap.setPassword(userPo.getPassword());
        Session session = Session.getInstance(javaMailImap.getJavaMailProperties());
        try {
            Store store = session.getStore("imap");
            store.connect(javaMailImap.getHost(), userPo.getUsername(), userPo.getPassword());
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Message[] message = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), true));
            log.info("message--{}", message.toString());
//            if (message.length > 0) {
//                System.out.println("有新邮件到达！");
//            }
            List<EmailPo> emailPos = new ArrayList<>();
            int count =0;
            for (Message message1 : message) {
                count++;
                if (count>5) break;
                String content = getTextFromMessage(message1);
                String subject = message1.getSubject();
                String from = MimeUtility.decodeText(message1.getFrom()[0].toString());
                EmailPo emailPo = new EmailPo();
                emailPo.setFrom(from);
                emailPo.setSubject(subject);
                log.info("类型{}",message1.getContentType());
                emailPo.setContent(content);
                emailPos.add(emailPo);
            }
            inbox.close(false);
            store.close();
            return emailPos;
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<>();
    }

    // 定时任务，每分钟检查一次新邮件
//    @Scheduled(fixedRate = 60000)
    public void checkForNewEmail(){
        System.out.println("---------");
        javaMailImap.setUsername("userPo.getUsername()");
        javaMailImap.setPassword("userPo.getPassword()");
        Session session = Session.getInstance(javaMailImap.getJavaMailProperties());
        try {
            Store store = session.getStore("imap");
            store.connect(javaMailImap.getHost(),javaMailImap.getUsername(),javaMailImap.getPassword());
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Message[] message = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
//            if (message.length > 0) {
//                System.out.println("有新邮件到达！");
//            }
            for (Message message1 : message) {
//                System.out.println("From: "+message1.getFrom()[0]);
//                System.out.println("Subject: "+message1.getSubject());
//                System.out.println("ContentType: "+message1.getContentType());
//                System.out.println("Description: "+message1.getDescription());
//                System.out.println("Disposition: "+message1.getDisposition());
//                System.out.println("FileName: "+message1.getFileName());
//                System.out.println("Content: "+message1.getContent());
//                System.out.println("Send Data: "+message1.getSentDate());
                String content = getTextFromMessage(message1);
                String subject = message1.getSubject();
                String from = MimeUtility.decodeText(message1.getFrom()[0].toString());
                File file = new File(OUT_PUT_FOLDER);
                if (!file.exists()) {
                    file.mkdirs();
                }
                convertToPDF(from,subject,content,OUT_PUT_FOLDER+"email_"+ System.currentTimeMillis()+".pdf");
                log.info("subject:{}",subject);
                log.info("content:{}",content);
                log.info("from:{}",from);
                log.info("{}","----------------------");
                message1.setFlag(Flags.Flag.SEEN,true);
            }
            inbox.close(false);
            store.close();
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    // 从邮件中获取文本内容
    private String getTextFromMessage(Message message){
        try {
            if (message.isMimeType("text/plain")) {
                return message.getContent().toString();
            }
            if (message.isMimeType("text/html")) {
                return (String) message.getContent();
            }
            if (message.isMimeType("multipart/*")) {
                MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
                return getTextFromMimeMultipart(mimeMultipart);
            }
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return "";
    }
    // 从 MIME 多部分内容中获取文本内容
    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart){
        StringBuilder result = new StringBuilder();
        try {
            int count = mimeMultipart.getCount();
            for (int i = 0; i < count; i++) {
                BodyPart bodyPart = mimeMultipart.getBodyPart(i);
                if (bodyPart.isMimeType("text/plain")) {
                    result.append(bodyPart.getContent());
                }
                if (bodyPart.isMimeType("text/html")) {
                    String html = (String) bodyPart.getContent();
                    result.append(html);
                }else if (bodyPart.getContent() instanceof MimeMultipart) {
                    result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
                }
            }
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result.toString();
    }
    // 将邮件内容转换为 PDF 文件并保存到本地
    private void convertToPDF(String from,String subject,String content,String fileName) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                PDType0Font font = PDType0Font.load(document, new File("C:\\Windows\\Fonts\\simkai.ttf"));
//            PDFont font = PDType1Font.HELVETICA;
                contentStream.setFont(font, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(0, 750);
                // 设置行距
                float leading = 1.5f * 12; // 1.5 倍字体大小
                contentStream.setLeading(leading);
                contentStream.showText("发件人:"+from);
                contentStream.newLine();
                contentStream.showText("主题:" + subject);
                contentStream.newLine();
                contentStream.showText("内容:");

                // 将邮件内容写入 PDF 文件
                content = content.replaceAll("\t", " ");
                content = content.replaceAll("©", "(版权)");
                contentStream.setFont(font, 8);
                for (String s : content.split("\r\n")) {
                    contentStream.showText(s);
                    contentStream.newLine();
                }

                contentStream.endText();
            }
            // 保存 PDF 文件到本地文件系统
            document.save(fileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public static void test(){
        // 邮箱账号
        String username = "2317613629@qq.com";

        // 邮箱密码或授权码
        String password = "wyunxiygzyhedigd";

        // 邮箱服务器地址
        String host = "imap.qq.com";

        // 获取系统属性
        Properties properties = System.getProperties();

        // 设置IMAP服务器
        properties.setProperty("mail.store.protocol", "imap");
        properties.setProperty("mail.imap.host", host);
        properties.setProperty("mail.imap.port", "993");
        properties.setProperty("mail.imap.ssl.enable", "true");

        // 获取会话对象
        Session session = Session.getDefaultInstance(properties);

        try {
            // 获取Store对象
            Store store = session.getStore("imap");

            // 连接到邮件服务器
            store.connect(host, username, password);

            // 获取收件箱文件夹
            Folder inbox = store.getFolder("INBOX");

            // 以只读方式打开收件箱
            inbox.open(Folder.READ_ONLY);

            // 获取收件箱中的邮件数
            int messageCount = inbox.getMessageCount();
            System.out.println("收件箱中共有 " + messageCount + " 封邮件");

            // 遍历收件箱中的邮件
            Message[] messages = inbox.getMessages();
            for (int i = 0; i < messages.length; i++) {
                System.out.println("邮件 " + (i + 1) + ": " + messages[i].getSubject());
            }

            // 关闭收件箱
            inbox.close(false);

            // 关闭Store连接
            store.close();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(System.getProperties().toString());
    }
}
