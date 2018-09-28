/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xapps.browser.gsea;

/**
 * Configuration and utils common to all xapplications.
 */
public class GseaAppConf {

    // @maint known extensions
    private static String[] KNOWN_FILE_EXTS = new String[]{"grp", "gmx", "gmt", "chip"}; // @maint

    public static javax.swing.filechooser.FileFilter[] createAllFileFilters() {

        StringBuffer buf = new StringBuffer("GSEA supported file types [");
        for (int i = 0; i < KNOWN_FILE_EXTS.length; i++) {
            buf.append(KNOWN_FILE_EXTS[i]);
            if (i != KNOWN_FILE_EXTS.length - 1) {
                buf.append(",");
            }
        }

        buf.append(']');

        return new GseaFileFilter[]{
                new GseaFileFilter(KNOWN_FILE_EXTS, buf.toString()),
                new GseaFileFilter(new String[]{"grp", "gmt", "gmx"}, "Gene set database files [grp, gmx, gmt]"),
        };
    }

    public static javax.swing.filechooser.FileFilter createGseaFileFilter() {

        StringBuffer buf = new StringBuffer("GSEA supported file types [");
        for (int i = 0; i < KNOWN_FILE_EXTS.length; i++) {
            buf.append(KNOWN_FILE_EXTS[i]);
            if (i != KNOWN_FILE_EXTS.length - 1) {
                buf.append(",");
            }
        }

        buf.append(']');

        return new GseaFileFilter(KNOWN_FILE_EXTS, buf.toString());
    }

} // End class Conf
