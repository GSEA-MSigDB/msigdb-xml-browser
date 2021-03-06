/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.genome.objects;

/**
 * A reasonable unique id generator.
 *
 * @author Aravind Subramanian
 * @version %I%, %G%
 */
//public class Id implements javax.xml.bind.IdentifiableElement { // @note comm out agu 2006 for the web stuff
public class Id {

    private String fId;

    /**
     * Factory method for creating a new Id.
     * Reasonably guaranteed to be unique
     *
     * @see UUID
     */
    public static Id createId() {
        return new Id("todo");
        // @todo
        // @note id'ing disabled as:
        // 1) not used
        // 2) unable to create ID on macs and if run under LSF
        //return new Id(new UUID().toString());
    }

    /**
     * privatized constructor -> use teh factroy metjod to create a new object instead
     * <p/>
     * NO - need it. Maybe change if pob is changed to id.
     */
    public Id(String id) {

        if (id.indexOf("\n") != -1) {
            throw new IllegalArgumentException("Ids must not have \n");
        }

        id = id.trim();
        this.fId = id;
    }

    // Yes! Needed by db40
    // @load check if needed -> exp for db40 for use with load
    public Id() {
    }

    /**
     * Returns this element object's identifier value. Never null.
     */
    public String id() {
        return fId;
    }

    public int hashCode() {
        return fId.hashCode();
    }

    public boolean equals(Object obj) {

        if (obj instanceof Id) {
            return fId.equals(((Id) obj).fId);
        }

        return false;
    }

    public Id cloneId() {
        return new Id(this.fId);
    }

    /**
     * Ensures that this object does not violate any local structural
     * constraints.
     * implementatioin onte: we could add some checks about uniqueness etc here, but as teh UUId mechanism
     * is used to generate ids, its failry safe and this method doesn nothing.
     */
    public void validateThis() {
    }
}    // End Id
