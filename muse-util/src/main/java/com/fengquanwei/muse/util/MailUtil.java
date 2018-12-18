package com.fengquanwei.muse.util;

import com.sun.mail.util.MailSSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

/**
 * 邮件工具类
 *
 * @author fengquanwei
 * @create 2018/12/18 19:32
 **/
public class MailUtil {
    private static Logger logger = LoggerFactory.getLogger(MailUtil.class);

    // 用户名
    private static String USERNAME = "from@163.com";
    // 密码
    private static String PASSWORD = "******";
    // 发件地址
    private static String FROM = "from@163.com";

    private static Properties properties = null;

    static {
        properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.host", "smtp.163.com");
        properties.setProperty("mail.smtp.auth", "true");

        // 开启安全协议
        MailSSLSocketFactory mailSSLSocketFactory = null;
        try {
            mailSSLSocketFactory = new MailSSLSocketFactory();
            mailSSLSocketFactory.setTrustAllHosts(true);
        } catch (GeneralSecurityException e1) {
            e1.printStackTrace();
        }

        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", mailSSLSocketFactory);
    }

    /**
     * 发送邮件
     *
     * @param recipients 收件人列表
     * @param subject    主题
     * @param html       正文
     * @param filePaths  附件文件路径列表
     */
    public static void send(String[] recipients, String subject, String html, String[] filePaths) {
        try {
            // 消息体
            MimeMultipart mimeMultipart = new MimeMultipart("mixed");

            // 正文
            MimeBodyPart body = new MimeBodyPart();
            body.setContent(html, "text/html;charset=utf-8");
            mimeMultipart.addBodyPart(body);

            // 附件
            if (filePaths != null && filePaths.length > 0) {
                for (String filepath : filePaths) {
                    File file = new File(filepath);
                    if (file.exists()) {
                        MimeBodyPart attachment = new MimeBodyPart();
                        DataSource attachmentDataSource = new FileDataSource(file);
                        DataHandler attachmentDataHandler = new DataHandler(attachmentDataSource);
                        attachment.setDataHandler(attachmentDataHandler);
                        attachment.setFileName(MimeUtility.encodeText(file.getName(), "UTF-8", null));
                        mimeMultipart.addBodyPart(attachment);
                    }
                }
            }

            // 服务器认证
            Session session = Session.getDefaultInstance(properties, new MyAuthenticator(USERNAME, PASSWORD));
            session.setDebug(true);

            // 邮件
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(FROM));
            if (recipients != null && recipients.length > 0) {
                for (String recipient : recipients) {
                    mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
                }
            }
            mimeMessage.setSubject(subject, "UTF-8");
            mimeMessage.setSentDate(new Date());

            // 设置邮件消息体
            mimeMessage.setContent(mimeMultipart);

            // 生成邮件
            mimeMessage.saveChanges();

            // 发送
            Transport.send(mimeMessage);
        } catch (Exception e) {
            logger.error("send mail error, recipients: {}, subject: {}, html: {}, filePaths: {}", recipients, subject, html, filePaths, e);
        }
    }

    // 认证
    public static class MyAuthenticator extends Authenticator {
        private String username;
        private String password;

        public MyAuthenticator(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        MailUtil.send(new String[]{"to@163.com"}, "hello world", "<h1>hello world</h1>", new String[]{"/Users/xxx/abc.txt"});
    }
}
