/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.genome.charts;

import edu.mit.broad.msigdb_browser.genome.JarResources;

import javax.swing.*;

import java.io.File;
import java.io.IOException;

/**
 * simple wrapper Interface
 *
 * @author Aravind Subramanian
 * @version %I%, %G%
 */
public interface XChart {

    public static final Icon ICON = JarResources.getIcon("Chart.gif");

    // Name is NOT the same as Title - name is simple and file name safe. Title is short but can be 'English'.
    public String getName();

    public void saveAsPNG(File inFile, int width, int height) throws IOException;
} // End XChart

