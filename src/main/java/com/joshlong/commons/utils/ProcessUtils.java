package com.joshlong.commons.utils;

import org.apache.commons.lang.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
public class ProcessUtils {
    private InetAddress host;

    public ProcessUtils(InetAddress host) {
        this.setHost(host);
    }

    public ProcessUtils() {
        try {
            this.setHost(InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            // do nothing .. 
        }
    }

    public void setHost(InetAddress addy) {
        this.host = addy;
    }

    public String getUniqueHostId() {
        String uid = null;

        try {
            uid = host.getCanonicalHostName();
        } catch (Throwable t) {
            try {
                uid = host.getHostAddress();
            } catch (Throwable te) {
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

    List<String> toAtoms(String cmd) {
        List<String> atoms = new ArrayList<String>();
        cmd = StringUtils.defaultString(cmd) + " ";

        String[] cmdPS = cmd.split(" ");

        for (String cP : cmdPS) {
           // if (!StringUtils.isEmpty(cP)) {
                atoms.add(StringUtils.defaultString(cP).trim());
         //   }
        }

        return atoms;
    }

    public Process execute(String cmd) throws Throwable {
        return execute(toAtoms(cmd));
    }

    public Process execute(List<String> cmd) throws Throwable {
        return prepare(cmd).start();
    }

   
}
