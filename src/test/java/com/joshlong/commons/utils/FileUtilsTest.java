package com.joshlong.commons.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SystemUtils;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
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
                one(f).exists();
            }
        });
        this.fileUtils.deleteFile(f);
    }

    @Test
    public void testDeleteFilesDelProcFails() throws Throwable {
        final File f = this.context.mock(File.class);
        final File tmpExistingFile = File.createTempFile("aaa", "txt");
        Writer w = new FileWriter(tmpExistingFile.getAbsolutePath());
        IOUtils.write("hello", w);
        IOUtils.closeQuietly(w);

        final Process proc = this.context.mock(Process.class);
        this.context.checking(new Expectations() {
            {
                one(f).getAbsolutePath();
                one(f).exists();
                will(returnValue(true));

                one(processUtils).execute(with(any(List.class)));
                will(returnValue(proc));
                one(proc).waitFor();
                will(returnValue(1));

            }
        });
        this.fileUtils.deleteFile(f);
    }

    @Test
    public void testDeleteFiles() throws Throwable {
        final File f = this.context.mock(File.class);
        final File tmpExistingFile = File.createTempFile("aaa", "txt");
        Writer w = new FileWriter(tmpExistingFile.getAbsolutePath());
        IOUtils.write("hello", w);
        IOUtils.closeQuietly(w);

        final Process proc = this.context.mock(Process.class);
        this.context.checking(new Expectations() {
            {
                one(f).getAbsolutePath();
                one(f).exists();
                will(returnValue(true));

                one(processUtils).execute(with(any(List.class)));
                will(returnValue(proc));
                one(proc).waitFor();
                will(returnValue(0));

                one(f).exists();
                will(returnValue(false));

            }
        });
        this.fileUtils.deleteFile(f);
    }

    @Test
    public void testDeleteFileString() throws Throwable {
        this.context.checking(new Expectations() {
            {
                one(processUtils).execute(with(any(List.class)));
            }
        });

        this.fileUtils.deleteFile(File.createTempFile("aaaaaaaaaaaaa", "bbbbbbbbbbbbbbb"));
    }

    @Test
    public void testDeleteFiles1() throws Throwable {
        final File f = this.context.mock(File.class);
        final File tmpExistingFile = File.createTempFile("aaa", "txt");
        Writer w = new FileWriter(tmpExistingFile.getAbsolutePath());
        IOUtils.write("hello", w);
        IOUtils.closeQuietly(w);
        final Process proc = this.context.mock(Process.class);
        this.context.checking(new Expectations() {
            {
                one(f).exists();
            }
        });
        this.fileUtils.deleteFile(f);
    }

    @Test
    public void testDeleteFilesThatDoesntExist() throws Throwable {
        final File f = this.context.mock(File.class);
        final File tmpExistingFile = new File(SystemUtils.getUserHome(), File.createTempFile("bbbvbbbb",
                                                                                             "txt").getName() + ".txt");

        if (tmpExistingFile.exists()) {
            tmpExistingFile.delete();
        }

        Writer w = new FileWriter(tmpExistingFile.getAbsolutePath());
        IOUtils.write("hello", w);
        IOUtils.closeQuietly(w);
        this.context.checking(new Expectations() {
            {
                one(f).exists();
            }
        });
        this.fileUtils.deleteFile(f);
    }

    @Test
    public void testDeleteFileStringPath() throws Throwable {
        File x = new File(SystemUtils.getUserHome(), "/Desktop/foo.txt1");
        Assert.assertTrue(this.fileUtils.deleteFile(x));
    }

    @Test
    public void testCopyFileThatStillFails() throws Throwable {
        final File a = this.context.mock(File.class, "a");
        final File b = this.context.mock(File.class, "b");
        final Process proc = this.context.mock(Process.class);
        this.context.checking(new Expectations() {

            {
                one(a).getAbsolutePath();
                will(returnValue("/tmp/a.txt"));
                one(b).getAbsolutePath();
                will(returnValue("/tmp/b.txt"));

                one(processUtils).execute(with(any(List.class)));
                will(returnValue(proc));
                one(proc).waitFor();
                will(returnValue(1));
            }
        });
        this.fileUtils.copyFile(a, b);
    }

    void write(File f, String o) {

        try {
            Writer w = new FileWriter(f);
            IOUtils.write(o, w);
            IOUtils.closeQuietly(w);
        }
        catch (IOException e) {
            //
        }
    }
     @Test
    public void testCopyFileThatDoesntUltimatelyExist() throws Throwable {

        File tmp = SystemUtils.getJavaIoTmpDir();
        File a = new File(tmp, "/a/a.txt");
        File b = new File(tmp, "/b/b.txt");

        // clean up
        if (a.exists()) a.delete();
        if (b.exists()) b.delete();


        // setup
        b.mkdirs();
        write(b, "aaaaaaaaaaa");


        this.context.checking(new Expectations() {
            {
                one(processUtils).execute(with(any(List.class)));

            }

        });

        this.fileUtils.copyFile(a, b);
    }
    @Test
    public void testCopyFile() throws Throwable {

        File tmp = SystemUtils.getJavaIoTmpDir();
        File a = new File(tmp, "/a/a.txt");
        File b = new File(tmp, "/b/b.txt");

        // clean up
        if (a.exists()) a.delete();
        if (b.exists()) b.delete();


        // setup 
        b.mkdirs();
        write(b, "aaaaaaaaaaa");


        this.context.checking(new Expectations() {
            {
                one(processUtils).execute(with(any(List.class)));

            }

        });

        this.fileUtils.copyFile(a, b);
    }

    @Test
    public void testEnsureDirectoryExistsWithParentDirectory()
            throws Throwable {
        final File f = this.context.mock(File.class, "child");
        final File parent = this.context.mock(File.class, "parent");
        this.context.checking(new Expectations() {
            {
                one(f).exists();
                will(returnValue(false));
                one(f).isFile();
                will(returnValue(true));
                one(f).getParentFile();
                will(returnValue(parent));
                one(parent).isDirectory();
                will(returnValue(true));
                one(parent).mkdirs();
            }
        });

        this.fileUtils.ensureDirectoryExists(f);
    }

    @Test
    public void testEnsureDirectoryExistsWithOutParentDirectory()
            throws Throwable {
        final File f = this.context.mock(File.class, "child");
        final File parent = this.context.mock(File.class, "parent");
        this.context.checking(new Expectations() {
            {
                one(f).exists();
                will(returnValue(false));
                one(f).isFile();
                will(returnValue(true));
                one(f).getParentFile();
                will(returnValue(parent));
                one(parent).isDirectory();
                will(returnValue(false));
            }
        });
        this.fileUtils.ensureDirectoryExists(f);
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
