/*
 * Copyright (c) 2003-2018 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.xbench.prefs;

import org.apache.log4j.Logger;

/**
 * @author Aravind Subramanian
 */
abstract class AbstractPreference implements Preference {

    private String fName;
    private String fDesc;
    private Object fDefault;

    protected static final Logger klog = Logger.getLogger(AbstractPreference.class);


    /**
     * Must manually call init() if this form of the protected constructor is used by
     * a subclass
     */
    protected AbstractPreference() {
    }

    /**
     * Class constructor
     *
     * @param name
     * @param desc
     * @param def
     */
    protected AbstractPreference(final String name, final String desc, final Object def) {
        init(name, desc, def);
    }

    /**
     * Class constructor
     *
     * @param name
     * @param desc
     * @param def
     */
    protected void init(final String name, final String desc, final Object def) {
        if (name == null) {
            throw new IllegalArgumentException("Parameter name cannot be null");
        }

        if (desc == null) {
            throw new IllegalArgumentException("Parameter desc cannot be null");
        }

        if (def == null) {
            throw new IllegalArgumentException("Parameter def cannot be null");
        }

        this.fName = name;
        this.fDesc = desc;
        this.fDefault = def;
    }

    public String getName() {
        return fName;
    }

    public String getDesc() {
        return fDesc;
    }

    public Object getDefault() {
        return fDefault;
    }

} // End class AbstractPreference
