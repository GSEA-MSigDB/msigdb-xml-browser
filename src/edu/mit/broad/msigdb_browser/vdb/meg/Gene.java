/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.vdb.meg;

import edu.mit.broad.msigdb_browser.genome.Constants;

import java.util.Set;

/**
 * @author Aravind Subramanian
 */
public interface Gene {

    public static final Gene NULL_GENE = new DefaultGene(Constants.NULL, Constants.NULL);

    public String getSymbol();

    public String getTitle();

    public String getTitle_truncated();

    public Set getAliases();

    public String[] getAliasesArray();

    public static class Helper {

        public static String getTitle_truncated(String title) {
            if (title != null && title.length() > 150) {
                return title.substring(150);
            }

            return title;
        }

    } // End class Helper

    // End class Hugo
}