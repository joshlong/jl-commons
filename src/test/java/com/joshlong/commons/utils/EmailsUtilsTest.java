package com.joshlong.commons.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
public class EmailsUtilsTest {
    private JavaMailSender javaMailSender;
    private VelocityEngine velocityEngine;
    private EmailUtils emailUtils;
    private Mockery context = new Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    @Test
    public void testMergeTemplate() throws Throwable {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("a", "josh");

        this.context.checking(new Expectations() {
            {
                one(velocityEngine).evaluate(with(any(VelocityContext.class)), with(any(Writer.class)), with(any(
                        String.class)), with(any(String.class)));
                will(returnValue(true));
            }
        });
        this.emailUtils.mergeTemplate("Hello ${a}", map);

    }

    @Test
    public void testSendingEmail() throws Throwable {

        final String from = "from", subject = "subject", txt = "txt", html = "html";
        final String[] to = "a,b".split(",");
        Map<String, Object> params = new HashMap<String, Object>();

        final MimeMessage msg = this.context.mock(MimeMessage.class);

        this.context.checking(new Expectations() {
            {

                one(javaMailSender).createMimeMessage();
                will(returnValue(msg));

                one(msg).setFrom(with(any(Address.class)));
                one(msg).setSubject(with(any(String.class)));
                one(msg).setRecipients(with(any(MimeMessage.RecipientType.class)), with(any(Address[].class)));
                one(velocityEngine).evaluate(with(any(VelocityContext.class)), with(any(StringWriter.class)), with(any(
                        String.class)), with(any(String.class)));
                one(velocityEngine).evaluate(with(any(VelocityContext.class)), with(any(StringWriter.class)), with(any(
                        String.class)), with(any(String.class)));

                one(msg).setContent(with(any(MimeMultipart.class)));
                one(msg).saveChanges();
                one(javaMailSender).send(with(any(MimeMessage.class)));

            }});

        this.emailUtils.sendEmailMessage(from, to, subject, txt, html, params);

    }

    @Test
    public void testSendingEmailWithEmptyText() throws Throwable {

        final String from = "from", subject = "subject", txt = "txt", html = "html";
        final String[] to = "a,b".split(",");
        Map<String, Object> params = new HashMap<String, Object>();

        final MimeMessage msg = this.context.mock(MimeMessage.class);

        this.context.checking(new Expectations() {
            {

                one(javaMailSender).createMimeMessage();
                will(returnValue(msg));

                one(msg).setFrom(with(any(Address.class)));
                one(msg).setSubject(with(any(String.class)));
                one(msg).setRecipients(with(any(MimeMessage.RecipientType.class)), with(any(Address[].class)));
                one(velocityEngine).evaluate(with(any(VelocityContext.class)), with(any(StringWriter.class)), with(any(
                        String.class)), with(any(String.class)));


                one(msg).setContent(with(any(MimeMultipart.class)));
                one(msg).saveChanges();
                one(javaMailSender).send(with(any(MimeMessage.class)));

            }});

        this.emailUtils.sendEmailMessage(from, to, subject,  StringUtils.EMPTY,html, params);

    }

    @Test
    public void testSendingEmailWithEmptyHtml() throws Throwable {

        final String from = "from", subject = "subject", txt = "txt", html = "html";
        final String[] to = "a,b".split(",");
        Map<String, Object> params = new HashMap<String, Object>();

        final MimeMessage msg = this.context.mock(MimeMessage.class);

        this.context.checking(new Expectations() {
            {

                one(javaMailSender).createMimeMessage();
                will(returnValue(msg));

                one(msg).setFrom(with(any(Address.class)));
                one(msg).setSubject(with(any(String.class)));
                one(msg).setRecipients(with(any(MimeMessage.RecipientType.class)), with(any(Address[].class)));
                one(velocityEngine).evaluate(with(any(VelocityContext.class)), with(any(StringWriter.class)), with(any(
                        String.class)), with(any(String.class)));
              

                one(msg).setContent(with(any(MimeMultipart.class)));
                one(msg).saveChanges();
                one(javaMailSender).send(with(any(MimeMessage.class)));

            }});

        this.emailUtils.sendEmailMessage(from, to, subject, txt, StringUtils.EMPTY, params);

    }

    @Test
    public void testMergeTemplateWithEmptyTemplate() throws Throwable {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("a", "josh");
        String x = this.emailUtils.mergeTemplate("", map);
        Assert.assertTrue(x.equals(""));
    }

    @Test
    public void testVelocityEvaluation() throws Throwable {
        final Map<String, Object> map = this.context.mock(Map.class);
        final String key = "key", value = "val";

        final Set<String> keys = new HashSet<String>(Arrays.asList(key));

        this.context.checking(new Expectations() {
            {

                one(velocityEngine).evaluate(with(any(VelocityContext.class)), with(any(Writer.class)), with(any(
                        String.class)), with(any(String.class)));

            }
        });
        this.emailUtils.mergeTemplate("x", new HashMap<String, Object>());

    }

    @Test
    public void testMergeTemplateWithNullMapMacros() throws Throwable {
        final Map<String, Object> map = this.context.mock(Map.class);
        final String key = "key", value = "val";
        final Set<String> keys = new HashSet<String>(Arrays.asList(key));
        this.context.checking(new Expectations() {
            {
                one(velocityEngine).evaluate(with(any(VelocityContext.class)), with(any(Writer.class)), with(any(
                        String.class)), with(any(String.class)));

            }
        });
        this.emailUtils.mergeTemplate("x", null);

    }

    @Test
    public void testMergeTemplateWithNonNullMapMacros() throws Throwable {
        final Map<String, Object> map = this.context.mock(Map.class);
        final String key = "key", value = "val";
        final Set<String> keys = new HashSet<String>(Arrays.asList(key));
        this.context.checking(new Expectations() {
            {

                one(map).size();
                one(map).entrySet();
                one(velocityEngine).evaluate(with(any(VelocityContext.class)), with(any(Writer.class)), with(any(
                        String.class)), with(any(String.class)));
            }
        });
        this.emailUtils.mergeTemplate("x", map);

    }

    @Test
    public void testMergeTemplateWithEmptyTemplateMap() throws Throwable {

        String x = this.emailUtils.mergeTemplate("", null);
        Assert.assertTrue(x.equals(""));
    }

    @Test
    public void testGetInternetAddresses() throws Throwable {
        InternetAddress[] addys = this.emailUtils.getInternetAddresses("a", "b");

        Assert.assertEquals(addys.length, 2);

    }

    @Before
    public void init() {
        this.javaMailSender = this.context.mock(JavaMailSender.class);
        this.velocityEngine = this.context.mock(VelocityEngine.class);
        this.emailUtils = new EmailUtils(this.javaMailSender, this.velocityEngine);
        this.emailUtils.setMailSender(this.javaMailSender);
        this.emailUtils.setVelocityEngine(velocityEngine);
    }

    @After
    public void destroy() {
        this.context.assertIsSatisfied();
    }
}
