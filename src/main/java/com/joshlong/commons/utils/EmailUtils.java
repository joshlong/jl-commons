package com.joshlong.commons.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.DateTool;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This code knows how to send an email that has parameters swapped out in the body. Can be used to correctly send
 * multipart mime messages and handles correctly authenticating against a lot of SMTP servers (like gmail's)
 *
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
public class EmailUtils {

    private volatile VelocityEngine velocityEngine;
    private volatile JavaMailSender mailSender;

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public EmailUtils(JavaMailSender mailSender, VelocityEngine velocityEngine) {
        this.mailSender = mailSender;
        this.velocityEngine = velocityEngine;
    }

    public InternetAddress[] getInternetAddresses(final String... emails) throws Throwable {
        final List<InternetAddress> addys = new ArrayList<InternetAddress>();
        for (final String e : emails) {
            addys.add(new InternetAddress(e));
        }
        return addys.toArray(new InternetAddress[addys.size()]);
    }

    VelocityContext fromVariables(Map<String, Object> vars) {
        VelocityContext context1 = new VelocityContext();
        for (String k : vars.keySet()) {
            context1.put(k, vars.get(k));
        }
        return context1;
    }

    public String mergeTemplate(final String template, Map<String, Object> macros) throws Throwable {
        if (StringUtils.isEmpty(template)) {
            return StringUtils.EMPTY;
        }
        if (null == macros) {
            macros = new HashMap<String, Object>();
        }

        String answer = StringUtils.EMPTY;

        Map<String, Object> vals = new HashMap<String, Object>(macros);
        vals.put("dateTool", new DateTool());

        final VelocityContext context = fromVariables(vals);

        final StringWriter writer = new StringWriter();

        boolean eval = velocityEngine.evaluate(context, writer, "LOG", template);

        if (eval) {
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

    public void setMailSender(final JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

}
