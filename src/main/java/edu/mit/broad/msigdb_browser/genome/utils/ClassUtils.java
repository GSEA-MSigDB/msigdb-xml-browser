/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.genome.utils;

import java.util.StringTokenizer;

/**
 * Class instantiation, class type checking and java.lang.reflec related utiltities.
 *
 * @author Aravind Subramanian
 * @version %I%, %G%
 */
public class ClassUtils {

    /**
     * @return The short name of clazz by stripping off the package name.
     *         For example: the short name of javax.swing.JTable.class
     *         is "JTable"
     */
    public static String shorten(Class clazz) {
        return shorten(clazz.getName());
    }

    public static String shorten(String name) {

        StringTokenizer tok = new StringTokenizer(name, ".");
        int cnt = tok.countTokens();

        for (int i = 0; i < cnt - 1; i++) {
            tok.nextToken();
        }

        return tok.nextToken();
    }

    /**
     * A Class.toString() yields, Class edu.mit.....Foo
     * But to do a Class.forName, the method needs just the class name -
     * For example the method truncates a string from:
     *  "class edu/mit/broad/msigdb_browser/genome/ JarResources"
     *  to
     *  JarResources
     *
     * If class prefix not found, no truncation done, no errors thrown either.
     */
    /*
    public static String trim(String name) {

        StringTokenizer tok = new StringTokenizer(name, " ");

        if(tok.countTokens() == 1) {
            return name;
        } else {
            tok.nextToken();

            return tok.nextToken();
        }

        //int indx = name.lastIndexOf("class");
        //if (indx == -1) return name;
        //else return name.substring(indx, name.length());
    }
    */
}    // End ClassUtils
