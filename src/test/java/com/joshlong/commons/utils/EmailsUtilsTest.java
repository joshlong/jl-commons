package com.joshlong.commons.utils;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
public class EmailsUtilsTest {
    private JavaMailSender javaMailSender;
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

        String x = this.emailUtils.mergeTemplate("Hello ${a}", map);
        Assert.assertTrue(x.equals("Hello josh"));
    }

    @Test
    public void testMergeTemplateWithEmptyTemplate() throws Throwable {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("a", "josh");
        String x = this.emailUtils.mergeTemplate("", map);
        Assert.assertTrue(x.equals(""));
    }

    @Test
    public void testSendEmailMessage() throws Throwable {
        final String key = "k", value = "v";
        final Map<String, Object> map = this.context.mock(Map.class);
        final Set<String> keys = new HashSet<String>(Arrays.asList(key));
        //  final JavaMailSender jms = this.context.mock(JavaMailSender.class);
        final MimeMessage msg = this.context.mock(MimeMessage.class);
        final InternetAddress internetAddress = this.context.mock(InternetAddress.class);

        final String subject = "subject";
        this.context.checking(new Expectations() {
            {

                atLeast(1).of(map).keySet();
                will(returnValue(keys));
                atLeast(1).of(map).get(key);
                will(returnValue(value));
                one(javaMailSender).createMimeMessage();
                will(returnValue(msg));
                one(msg).setFrom(with(any(InternetAddress.class)));
                one(msg).setSubject(subject);
                one(msg).setRecipients(with(any(Message.RecipientType.class)), with(any(Address[].class)));
                one(msg).setContent(with(any(Multipart.class)));
                one(msg).saveChanges();

                one(javaMailSender).send(msg);
            }
        });

        this.emailUtils.sendEmailMessage(
                "from", new String[]{"a", "b"}, "subject", "txt", "html", map);

    }

    @Test
    public void testMergeTemplateWithNonNullMapMacros() throws Throwable {
        final Map<String, Object> map = this.context.mock(Map.class);
        final String key = "key", value = "val";
        final Set<String> keys = new HashSet<String>(Arrays.asList(key));
        this.context.checking(new Expectations() {
            {
                one(map).keySet();
                will(returnValue(keys));
                one(map).get(key);
                will(returnValue(value));
            }
        });
        this.emailUtils.mergeTemplate("x", map);

    }

    @Test
    public void testMergeTemplateWithEmptyTemplateMap()
            throws Throwable {

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
        this.emailUtils = new EmailUtils(this.javaMailSender);
    }

    @After
    public void destroy() {
        this.context.assertIsSatisfied();
    }
}
