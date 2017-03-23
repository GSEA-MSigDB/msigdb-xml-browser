/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.genome.utils;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.*;

/**
 * Collection of utility methods related to Files.
 * <p/>
 * @author Aravind Subramanian
 * @version %I%, %G%
 */
public class FileUtils {

    /**
     * One element per line
     *
     * @param list
     * @param file
     * @throws IOException
     */
    public static void write(List<?> list, File file) throws IOException {
        PrintWriter out = new PrintWriter(new FileOutputStream(file));
        try{
            IOUtils.writeLines(list, IOUtils.LINE_SEPARATOR, out);
        }
        finally {
            out.close();
        }
    }

    /**
     * Need -> to detect of some other external process (like ms excel) has a file open and thus
     * locked. If java tries to write to such a file, a IOException is thrown.
     * This method will attempt to detect if the specified file is locked by some such external process
     * Java doesnt have this built -- this method is a hck copied from a javalang.help posting.
     * Not sure if it will work in all cases, but seems reasonable.
     * <p/>
     * If file doesnt exts, returns false always
     *
     * @param file
     * @return
     */
    // TODO: Clean this up...
    // Actually, this is probably not robust anyway - there is going to be a race condition using this
    // method no matter how it is implemented, so it's probably better to just write to the file, detect
    // any issues, and recover from there.
    // There's only one use so I think we can get there.
    public static boolean isLocked(File file) {

        if (!file.exists()) {
            return false;    // cant be locked if it doesnt exist!
        }

        File origFile = new File(file.getAbsolutePath());

// cant use this as it actually makes a result file by itslef, and then cant rename into it
//File newFile = File.createTempFile(file.getName(), ".lock_check");
        StringBuffer btmp = new StringBuffer(file.getName()).append(System.currentTimeMillis()).append(".lock_check");
        File newFile = new File(SystemUtils.getTmpDir(), btmp.toString());

//log.debug("Created lock result file: " + newFile.getPath() + " file: " + file.getAbsolutePath());
        boolean able2rename = file.renameTo(newFile);

//log.debug("able2rename: " + able2rename);
        if (!able2rename) {
            return true;    // couldnt rename, so we take this to mean that it must be locked
        } else {

// move it back!
            boolean movedback = newFile.renameTo(origFile);

            if (!movedback) {
                throw new IllegalStateException("Bad error - couldnt move file back after lock check");
            }

            return false;
        }
    }
}        // End FileUtils
