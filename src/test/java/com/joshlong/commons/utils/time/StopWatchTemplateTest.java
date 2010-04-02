package com.joshlong.commons.utils.time;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;

/**
 * StopWatchTemplate Tester.
 *
 * @author <a href="mailto:josh@Joshlong.com">Josh Long</a>
 */
public class StopWatchTemplateTest {
    private Mockery context = new Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    private StopWatchTemplate stopWatchTemplate ;

    @Before
    public void setUp() throws Exception {

        this.stopWatchTemplate=new StopWatchTemplate();

    }

    /**
     * Method: execute(StopWatchCallback stopWatchCallback)
     */     @Test
    public void testExecuteWithNullStopWatchCallback() throws Throwable {
          final   StopWatchCallback stopWatchCallback = this.context.mock( StopWatchCallback.class) ;

        this.context.checking( new Expectations(){
            {
               // one(stopWatchCallback).execute();;
            }
        });
        this.stopWatchTemplate.execute(  null  );
    }


    /**
     * Method: execute(StopWatchCallback stopWatchCallback)
     */     @Test
    public void testExecute() throws Throwable {
          final   StopWatchCallback stopWatchCallback = this.context.mock( StopWatchCallback.class) ;

        this.context.checking( new Expectations(){
            {
                one(stopWatchCallback).execute();;
            }
        });
        this.stopWatchTemplate.execute( stopWatchCallback );
    }

}
