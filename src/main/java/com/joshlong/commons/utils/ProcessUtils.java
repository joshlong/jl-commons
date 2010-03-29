package com.joshlong.commons.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
public class ProcessUtils {

    public String getUniqueHostId() {
        String uid = null;
        try {
            uid = InetAddress.getLocalHost().getCanonicalHostName();
        }
        catch (Throwable t) {
            try {
                uid = InetAddress.getLocalHost().getHostAddress();
            }
            catch (Throwable te) {
                // don't care
            }
        }
        return uid;
    }

    public ProcessBuilder prepare(List<String> cmd) throws Throwable {
        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        processBuilder.redirectErrorStream(true);
        return processBuilder;
    }

    public ProcessBuilder prepare(String cmd) throws Throwable {
        return prepare(toAtoms(cmd));
    }

    private List<String> toAtoms(String cmd) {
        List<String> atoms = new ArrayList<String>();
        cmd = StringUtils.defaultString(cmd) + " ";
        String cmdPS[] = cmd.split(" ");
        for (String cP : cmdPS) {
            if (!StringUtils.isEmpty(cP)) {
                atoms.add(StringUtils.defaultString(cP).trim());
            }
        }
        return atoms;
    }

    public Process execute(String cmd) throws Throwable {
        return execute(toAtoms(cmd));
    }

    public Process execute(List<String> cmd) throws Throwable {
        return prepare(cmd).start();
    }
    // hacky!

    public String adjustForOsSpecificPath(String commmand) {
        if (SystemUtils.IS_OS_WINDOWS) {
            return new File("/cygwin/bin/", commmand).getAbsolutePath();
        }
        return commmand;
    }

}
