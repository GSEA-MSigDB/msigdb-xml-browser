/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.genome;

/**
 * An object to represent version strings.  The original intention
 * of this was likely to allow version-specific behavior by 
 * comparing e.g. incoming data files with a parser implementation
 * (all of its uses are within parsers).  None of that was implemented,
 * however, and so this class has been simplified to remove unused
 * features and have a clean implementation.
 * 
 * The only reason it is being kept at all is to have a typed encapsulation
 * of version info.  For the way that GSEA uses it at present a simple
 * String would suffice (or even nothing at all).
 * 
 * @author David Eby
 */
public class Version {
    private String version;

    /**
     * Wrap the given String as a Version
     */
    public Version(String version) {
        if (version == null) {
            throw new IllegalArgumentException("Version cannot have null version information");
        }
        this.version = version;
    }

    /**
     * Wrap the given integer as a Version
     */
    public Version(int majorVersion) {
        this(Integer.toString(majorVersion));
    }

    public boolean equals(Object obj) {
        return this.version.equals(obj);
    }
    
    public String toString() {
        return version;
    }

    public Object clone() {
        Version ver = null;

        try {
            ver = (Version) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString());
        }

        ver.version = version;

        return ver;
    }
}
