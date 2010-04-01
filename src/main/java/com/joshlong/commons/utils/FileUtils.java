package com.joshlong.commons.utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * At the moment, this simply has an idiom encapsulated into a method for all the place where we do file copies.
 *
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */

public class FileUtils {

    private ProcessUtils processUtils;



    public FileUtils(ProcessUtils processUtils) {
        this.processUtils = processUtils;
    }

    public boolean ensureDirectoryExists(File f) {
        if (f.exists()) {
            return true;
        }
        boolean answer = false;
        if (f.isFile()) {
            File fParent = f.getParentFile();
            if (fParent.isDirectory()) {
                answer = fParent.mkdirs();
            }
        }
        else {
            answer = f.mkdirs();
        }
        return answer;
    }

    public void setProcessUtils(ProcessUtils processUtils) {
        this.processUtils = processUtils;
    }

    public boolean deleteFile(File pathFile) throws Throwable {
//       File pathFile = new File(path);
        boolean exists = pathFile.exists();
        if (!exists) {
            return true;
        } // its already gone
        Process delPrc = processUtils.execute(Arrays.asList("rm", "-rf", pathFile.getAbsolutePath()));
        if (delPrc.waitFor() == 0) {  // then we stat the file to confirm
            if (!pathFile.exists()) {
                return true;
            }
        }
        return false;
    }
 

    public boolean copyFile(File i, File fout) throws Throwable {
        List<String> parts = Arrays.asList("cp", "-f", i.getAbsolutePath(),
                                          fout.getAbsolutePath());
        if (!ensureDirectoryExists(fout.getParentFile())) {
            return false;
        }
        Process cpProc = processUtils.execute(parts);
        if (cpProc.waitFor() == 0) {

            if (fout.exists() && fout.length() > 0) {
                return true;
            }
        }
        return false;
    }

    
}
