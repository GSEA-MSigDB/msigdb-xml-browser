/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xtools.browser.api;

import edu.mit.broad.msigdb_browser.genome.JarResources;
import xtools.browser.api.param.ParamSet;

import javax.swing.*;

import java.io.Serializable;

/**
 * @author Aravind Subramanian
 * @version %I%, %G%
 */
public interface Tool extends Serializable {

    public static final Icon ICON = JarResources.getIcon("Tool16.gif");

    /**
     * @return The name of this Tool
     */
    public String getName();

    /**
     * @return ParamSet that this tools accepts/needs to execute
     */
    public ParamSet getParamSet();

}    // End Tool
