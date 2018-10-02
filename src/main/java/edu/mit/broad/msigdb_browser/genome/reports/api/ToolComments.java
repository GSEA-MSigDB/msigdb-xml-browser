/*
 * Copyright (c) 2003-2018 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.genome.reports.api;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aravind Subramanian
 */
// A simple container
public class ToolComments {

    private List fComments;

    private Logger log = Logger.getLogger(ToolComments.class);

    public ToolComments() {
        this.fComments = new ArrayList();
    }

    public void add(String comment) {
        if (comment != null && comment.length() > 0) {
            comment = comment.trim();
            if (comment.endsWith("\n")) {
                comment = comment.substring(0, comment.length() - 1);
            }
            log.info(comment);
            fComments.add(comment);
        }
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < fComments.size(); i++) {
            buf.append(fComments).append('\n');
        }

        return buf.toString();
    }

} // End class ToolComments
