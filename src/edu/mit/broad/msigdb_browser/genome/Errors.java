/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.genome;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aravind Subramanian
 */
public class Errors {

    private List fErrors_as_strings;
    private String fErrorName;

    /**
     * Class constructor
     */
    public Errors() {
        this("ERROR(S)");
    }

    /**
     * Class constructor
     *
     * @param errorName
     */
    public Errors(final String errorName) {
        this.fErrorName = errorName;
        this.fErrors_as_strings = new ArrayList();
    }

    public void add(final String s) {
        if (fErrors_as_strings.contains(s) == false) {
            fErrors_as_strings.add(s);
        }
    }

    public void barfIfNotEmptyRuntime() throws RuntimeException {
        barfIfNotEmptyRuntime(null);
    }

    public void barfIfNotEmptyRuntime(String msg) throws RuntimeException {
        if (fErrors_as_strings.isEmpty() == false) {
            StringBuffer buf = new StringBuffer();
            if (msg != null && msg.length() > 0) {
                buf.append(msg).append('\n');
            }

            buf.append("There were errors: ").append(fErrorName).append(" #:").append(fErrors_as_strings.size()).append("\n");
            for (int i = 0; i < fErrors_as_strings.size(); i++) {
                buf.append(fErrors_as_strings.get(i).toString()).append('\n');
            }
            throw new RuntimeException(buf.toString());
        } else {
            //klog.info("NO error(s) for: " + fErrorName);
        }
    }

} // End Errors
