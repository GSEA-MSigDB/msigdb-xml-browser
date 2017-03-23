/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xtools.browser.api.param;

import java.io.Serializable;
import java.util.Properties;

/**
 * @author Aravind Subramanian
 */
public interface ParamSet extends Serializable {

    public static final String PARAM_FILE = "param_file";

    public ReportLabelParam getReportLabelParam();

    public Properties toProperties();

} // End interface ParamSet
