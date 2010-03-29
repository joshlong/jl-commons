package com.joshlong.commons.utils;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
public class FileUtilsTest {
    private FileUtils fileUtils;
    private ProcessUtils processUtils;
    private Mockery context = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };

    @After
    public void destroy() throws Throwable {
        this.context.assertIsSatisfied();
    }

    @Before
    public void init() throws Throwable {
        this.processUtils = this.context.mock(ProcessUtils.class);
        this.fileUtils = new FileUtils(this.processUtils);
        this.fileUtils.setProcessUtils(this.processUtils);
    }

    @Test
    public void testDeletingAFile() throws Throwable {
        final File f = this.context.mock(File.class);
        this.context.checking(new Expectations() {

                {
                    one(f).getAbsolutePath();
                }
            });
        this.fileUtils.deleteFile(f);
    }

    @Test
    public void testCopyFile() throws Throwable {
        final File a = this.context.mock(File.class, "a");
        final File b = this.context.mock(File.class, "b");
        this.context.checking(new Expectations() {
                {
                    one(a).getAbsolutePath();
                    will(returnValue("/tmp/a.txt"));
                    one(b).getAbsolutePath();
                    will(returnValue("/tmp/b.txt"));

                    one(processUtils).execute(with(any(List.class)))   ;
                }
            });
        this.fileUtils.copyFile(a, b);
    }

    @Test
    public void testEnsureDirectoryExists() throws Throwable {
        final File f = this.context.mock(File.class);
        this.context.checking(new Expectations() {

                {
                    one(f).exists();
                    will(returnValue(true));
                }
            });

        this.fileUtils.ensureDirectoryExists(f);
    }

    @Test
    public void testEnsureDirectoryExistsInNegativeCase()
        throws Throwable {
        final File f = this.context.mock(File.class);

        final File fParent = this.context.mock(File.class, "parent");

        this.context.checking(new Expectations() {

                {
                    one(f).exists();
                    will(returnValue(false));
                    one(f).isFile();
                    will(returnValue(true));
                    one(f).getParentFile();
                    will(returnValue(fParent));
                    one(fParent).isDirectory();
                    will(returnValue(true));
                    one(fParent).mkdirs();
                    will(returnValue(true));
                }
            });

        this.fileUtils.ensureDirectoryExists(f);
    }

    @Test
    public void testEnsureDirectoryExistsInNegativeCaseAndFileIsFile()
        throws Throwable {
        final File f = this.context.mock(File.class);
        final File fParent = this.context.mock(File.class, "parent");
        this.context.checking(new Expectations() {

                {
                    one(f).exists();
                    will(returnValue(false));
                    one(f).isFile();
                    will(returnValue(false));
                    one(f).mkdirs();
                }
            });

        this.fileUtils.ensureDirectoryExists(f);
    }
}
