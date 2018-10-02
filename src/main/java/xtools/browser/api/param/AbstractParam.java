/*
 * Copyright (c) 2003-2018 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xtools.browser.api.param;

import edu.mit.broad.msigdb_browser.genome.JarResources;

import org.apache.log4j.Logger;

import javax.swing.*;

/**
 * Object to capture commandline params / params that a Tool accepts/needs
 */
public abstract class AbstractParam implements Param {

    protected static final Logger klog = Logger.getLogger(AbstractParam.class);

    protected Logger log;
    private String fName;
    private Class[] fClassTypes;
    private String fDesc;
    private String fNameEnglish;
    private StringBuffer fHtmlLabel_v2;
    private StringBuffer fHtmlLabel_v3;
    private boolean fReqd;
    private Object fValue;
    private Object[] fHints;
    private Object fDefault;
    private Param.Type fType;

    // users must call init
    protected AbstractParam() {
    }

    /**
     * Class constructor
     * <p/>
     * The first object in the hints array is used as a default if non-null
     *
     * @param name
     * @param types
     * @param desc
     */
    protected AbstractParam(final String name,
                            final String nameEnglish,
                            final Class[] types,
                            final String desc,
                            final Object[] hintsanddef,
                            final boolean reqd) {

        if (hintsanddef == null) {
            throw new IllegalArgumentException("hint param cannot be null");
        }

        Object def;
        if (hintsanddef.length > 0) {
            def = hintsanddef[0];
        } else {
            def = null;
        }

        init(name, nameEnglish, types, desc, hintsanddef, def, reqd, null);
    }

    /**
     * Class constructor
     *
     * @param name
     * @param type
     * @param desc
     * @param hintsanddef
     * @param reqd
     */
    protected AbstractParam(final String name,
                            final String nameEnglish,
                            final Class type,
                            final String desc,
                            final Object[] hintsanddef,
                            final boolean reqd) {

        this(name, nameEnglish, new Class[]{type}, desc, hintsanddef, reqd);
    }

    /**
     * Explicitly specified default
     *
     * @param name
     * @param types
     * @param desc
     * @param def
     * @param hints
     * @param reqd
     */
    protected AbstractParam(final String name,
                            final String nameEnglish,
                            final Class[] types,
                            final String desc,
                            final Object def,
                            final Object[] hints,
                            final boolean reqd) {
        init(name, nameEnglish, types, desc, hints, def, reqd, null);
    }

    /**
     * Class constructor
     *
     * @param name
     * @param nameEnglish
     * @param type
     * @param desc
     * @param def
     * @param hints
     * @param reqd
     */
    protected AbstractParam(final String name,
                            final String nameEnglish,
                            final Class type,
                            final String desc,
                            final Object def,
                            final Object[] hints,
                            final boolean reqd) {
        this(name, nameEnglish, new Class[]{type}, desc, def, hints, reqd);
    }

    /**
     * Common initialization routine
     *
     * @param name
     * @param type
     * @param desc
     * @param hintsanddef
     * @param reqd
     */
    protected void init(final String name,
                        final String nameEnglish,
                        final Class[] classTypes,
                        final String desc,
                        final Object[] hints,
                        final Object def,
                        final boolean reqd,
                        final Param.Type type) {

        if (name == null) {
            throw new IllegalArgumentException("name param cannot be null");
        }

        if (classTypes == null) {
            throw new IllegalArgumentException("types param cannot be null");
        }

        if (classTypes.length == 0) {
            throw new IllegalArgumentException("types param cannot be empty");
        }

        for (int i = 0; i < classTypes.length; i++) {
            if (classTypes[i] == null) {
                throw new IllegalArgumentException("types param cannot be null at: " + i);
            }
        }

        if (desc == null) {
            throw new IllegalArgumentException("desc param cannot be null");
        }

        if (hints == null) {
            throw new IllegalArgumentException("hint param cannot be null");
        }

        if (name.indexOf(" ") != -1) {
            throw new IllegalArgumentException("Programmatic error: param name cannot contain whitespace");
        }

        if (name.indexOf("'") != -1) {
            throw new IllegalArgumentException("Programmatic error: param name cannot contain quote '");
        }

        if (name.indexOf("\"") != -1) {
            throw new IllegalArgumentException("Programmatic error: param name cannot contain quote \"");
        }

        this.fName = name;
        this.fNameEnglish = nameEnglish;
        this.fClassTypes = classTypes;
        this.fDesc = desc;
        this.fHints = hints;
        this.fReqd = reqd;
        this.fDefault = def;
        this.fType = type;
        this.log = Logger.getLogger(this.getClass());
    }

    public String getName() {
        return fName;
    }

    protected Action createHelpAction() {
        return JarResources.createHelpAction(getName());
    }

    public Param.Type getType() {
        if (fType != null) {
            return fType;
        } else if (fReqd) {
            return Param.REQUIRED;
        } else {
            return Param.BASIC;
        }
    }

    public void setType(Param.Type type) {
        this.fType = type;
    }

    public String getHtmlLabel_v2() {
        if (fHtmlLabel_v2 == null) {
            fHtmlLabel_v2 = new StringBuffer("<Html><body><b>").append(getNameEnglish()).append("</b></body></Html>");
        }

        return fHtmlLabel_v2.toString();
    }

    public String getHtmlLabel_v3() {
        if (fHtmlLabel_v3 == null) {
            fHtmlLabel_v3 = new StringBuffer("<Html><body><b>").append(getNameEnglish()).append("</b>");

            if (isReqd() || getType() == Param.PSEUDO_REQUIRED) {
                fHtmlLabel_v3.append("<font color=\"red\">*</font>");
            }

            fHtmlLabel_v3.append("</body></Html>");
        }

        //System.out.println("####\n" + fHtmlLabel.toString() + "/n");

        return fHtmlLabel_v3.toString();
    }

    public String getDesc() {
        return fDesc;
    }

    public String getNameEnglish() {
        if (fNameEnglish == null) {
            return fName;
        } else {
            return fNameEnglish;
        }
    }

    public Class[] getTypes() {
        return fClassTypes;
    }

    public boolean isReqd() {
        return fReqd;
    }

    /**
     * IMP doesnt evaluate equality of value - just the name
     *
     * @param obj
     * @return
     */
    public boolean equals(Object obj) {

        if (obj instanceof AbstractParam) {
            if (fName.equals(((AbstractParam) obj).fName)) {
                return true;
            }
        }

        return false;
    }

    public int hashCode() {
        return fName.hashCode();
    }

    public void setValue(Object value) {
        //if (value != null) log.debug("Setting value: " + value + " for: " + getName() + " class: " + value.getClass());
        //TraceUtils.showTrace();
        //log.debug("#### setting value: " + value);
        this.fValue = value;
    }

    /**
     * if value is not null, returns it
     * else return the default (which can be null too)
     */
    public Object getValue() {

        if (fValue != null) {
            return fValue;
        } else {
            return fDefault;
        }
    }

    /**
     * IMP subclasses must assess whether to override or not
     *
     * @return
     */
    public String getValueStringRepresentation(boolean full) {
        // full has no effect
        Object val = getValue();

        if (val == null) {
            return null;
        } else {
            return val.toString();
        }
    }

    public Object[] getHints() {
        return fHints;
    }

    public Object getDefault() {
        return fDefault;
    }

    /**
     * @return
     */
    public boolean isSpecified() {

        boolean defaultAvailable;
        final Object def = getDefault();

        if (def != null) {
            defaultAvailable = true;
            if (def instanceof Object[] && ((Object[]) def).length == 0) {
                defaultAvailable = false;
            }

        } else {
            defaultAvailable = false;
        }

        //System.out.println(">>> " + getName() + " DEF>" + def + "< defAvailable: " + defaultAvailable + " value: " + fValue);

        if (this.fValue == null && !defaultAvailable) { // @todo check impact
            return false;
        } else if (this.fValue != null && this.fValue instanceof Object[] && ((Object[]) fValue).length != 0) {
            return true;
        } else if (this.fValue != null && this.fValue.toString().length() == 0 && !defaultAvailable) {
            return false;
        } else {
            return true;    // irrespective of the value
        }
    }

}    // End class AbstractParam
