/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.vdb.msigdb;

/**
 * @author Aravind Subramanian
 */
public class GeneSetDescriptionImpl implements GeneSetDescription {

    private String fBrief;
    private String fFull;

    /**
     * Class constructor
     *
     * @param brief
     * @param full
     */
    public GeneSetDescriptionImpl(String brief, String full) {
        this.fBrief = brief;
        this.fFull = full;
    }

    public String getFull() {
        return fFull;
    }

    public String getBrief() {
        return fBrief;
    }

} // End class GeneSetDescriptionImpl
