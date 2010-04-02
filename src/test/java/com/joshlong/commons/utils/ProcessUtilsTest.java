package com.joshlong.commons.utils;

import org.apache.commons.lang.StringUtils;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.util.List;

/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
public class ProcessUtilsTest {
    private ProcessUtils processUtils;
    private Mockery context = new Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    @Before
    public void setUp() throws Exception {
        this.processUtils = new ProcessUtils(InetAddress.getLocalHost ()); 
        this.processUtils = new ProcessUtils();
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Method: getUniqueHostId()
     */
    @Test
    public void testGetUniqueHostId() throws Exception {

        final InetAddress inetAddress = this.context.mock(InetAddress.class);

        this.processUtils.setHost(inetAddress);

        this.context.checking(new Expectations() {
            {

                one(inetAddress).getCanonicalHostName();
                will(returnValue("a1"));

            }
        });
        Assert.assertTrue(!StringUtils.isEmpty(processUtils.getUniqueHostId()));
    }

    /**
     * Method: getUniqueHostId()
     */
    @Test
    public void testGetUniqueHostIdFailFirstAttempt() throws Exception {

        final InetAddress inetAddress = this.context.mock(InetAddress.class);

        this.processUtils.setHost(inetAddress);

        this.context.checking(new Expectations() {
            {

                one(inetAddress).getCanonicalHostName();
                will(throwException(new NullPointerException()));
                one(inetAddress).getHostAddress();
                will(returnValue("a2"));

            }
        });
        Assert.assertTrue(!StringUtils.isEmpty(processUtils.getUniqueHostId()));
    }

    /**
     * Method: getUniqueHostId()
     */
    @Test
    public void testGetUniqueHostIdFailFirstAttemptAndSecondAttempt() throws Exception {

        final InetAddress inetAddress = this.context.mock(InetAddress.class);

        this.processUtils.setHost(inetAddress);

        this.context.checking(new Expectations() {
            {

                one(inetAddress).getCanonicalHostName();
                will(throwException(new NullPointerException()));
                one(inetAddress).getHostAddress();
                will( throwException(  new NullPointerException()));

            }
        });
        Assert.assertTrue( StringUtils.isEmpty(processUtils.getUniqueHostId()));
    }

    /**
     * Method: prepare(List<String> cmd)
     *
     * @throws Throwable
     */
    @Test
    public void testPrepareCmd() throws Throwable {
        ProcessBuilder processBuilder = this.processUtils.prepare("ls -la"); //TODO: Test goes here...
        Assert.assertNotNull(processBuilder);
    }

    /**
     * Method: execute(String cmd)
     *
     * @throws Throwable
     */
    @Test
    public void testExecuteCmd() throws Throwable {
        Process proc = this.processUtils.execute("ls -la");
        Assert.assertNotNull(proc);
    }

    /**
     * Method: toAtoms(String cmd)
     *
     * @throws Exception
     */
    @Test
    public void testToAtomsWithNoCommand() throws Exception {
        List<String> parts = processUtils.toAtoms("   ");
        Assert.assertNotNull(parts);
    }


    /**
     * Method: toAtoms(String cmd)
     *
     * @throws Exception
     */
    @Test
    public void testToAtoms() throws Exception {
        List<String> parts = processUtils.toAtoms("ls -la");
        Assert.assertNotNull(parts);
        Assert.assertTrue(parts.size() > 0);
    }
}
