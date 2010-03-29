package com.joshlong.commons.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.tools.generic.DateTool;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This code knows how to send an email that has parameters swapped out in the body. Can be used to correctly send
 * multipart mime messages and handles correctly authenticating against a lot of SMTP servers (like gmail's)
 *
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
public class EmailUtils {

    private JavaMailSender mailSender;

    public EmailUtils(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public EmailUtils() {
    }

    private InternetAddress[] getInternetAddresses(final String... emails) throws Throwable {
        final List<InternetAddress> addys = new ArrayList<InternetAddress>();
        for (final String e : emails) {
            addys.add(new InternetAddress(e));
        }
        return addys.toArray(new InternetAddress[0]);
    }

    public String mergeTemplate(final String template, final Map<String, Object> macros) throws Throwable {
        if (StringUtils.isEmpty(template)) {
            return StringUtils.EMPTY;
        }
        String answer = null;
        final VelocityContext context = new VelocityContext();
        context.put("dateTool", new DateTool());
        if (null != macros) {
            for (final String key : macros.keySet()) {
                context.put(key, macros.get(key));
            }
        }
        final StringWriter writer = new StringWriter();
        Velocity.init();
        if (Velocity.evaluate(context, writer, "LOG", template)) {
            IOUtils.closeQuietly(writer);
            answer = writer.toString();
        }
        return answer;
    }

    public void sendEmailMessage(final String from,
                                 final String[] to,
                                 final String subject,
                                 final String textBody,
                                 final String htmlBody,
                                 final Map<String, Object> params) throws Throwable {
        final MimeMessage msg = mailSender.createMimeMessage();
        msg.setFrom(new InternetAddress(from));
        msg.setSubject(subject);
        msg.setRecipients(Message.RecipientType.TO, getInternetAddresses(to));
        final MimeMultipart content = new MimeMultipart("alternative");
        if (!StringUtils.isEmpty(textBody)) {
            final MimeBodyPart text = new MimeBodyPart();
            text.setText(mergeTemplate(textBody, params));
            content.addBodyPart(text);
        }
        if (!StringUtils.isEmpty(htmlBody)) {
            final MimeBodyPart html = new MimeBodyPart();
            html.setContent(mergeTemplate(htmlBody, params), "text/html");
            content.addBodyPart(html);
        }
        msg.setContent(content);
        msg.saveChanges();

        mailSender.send(msg);

    }

    public void setMailSender(final JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

}
