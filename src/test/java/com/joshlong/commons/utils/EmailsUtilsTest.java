package com.joshlong.commons.utils;

import org.jmock.Mockery;
import org.jmock.lib.JavaReflectionImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
public class EmailsUtilsTest {

    @Test
    public void testA() {
    }

    @Before
    public void init() {
    }

    @After
    public void destroy() {
        this.context.assertIsSatisfied();
    }

    private Mockery context = new Mockery() {
        {
            setImposteriser(JavaReflectionImposteriser.INSTANCE);
        }
    };
}
