/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.genome;

/**
 * @author Aravind Subramanian
 */
public class StandardException extends RuntimeException {

    private int fErrorCode;

    /**
     * Class constructor
     *
     * @param badParams
     */
    public StandardException(final String title, int errorCode) {
        super(title);

        this.fErrorCode = errorCode;
    }

    public int getErrorCode() {
        return fErrorCode;
    }

} // End class StandardException
