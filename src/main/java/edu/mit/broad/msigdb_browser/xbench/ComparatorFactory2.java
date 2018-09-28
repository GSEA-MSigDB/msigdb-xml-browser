/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.xbench;

import java.util.Comparator;
/**
 * A collection of commonly useful comparators.
 *
 * @author Aravind Subramanian
 * @version %I%, %G%
 */
public class ComparatorFactory2 {

    /**
     * Privatized class constructor.
     * Only static methods.
     */
    private ComparatorFactory2() {
    }


    public static class IntegerComparator implements Comparator {

        public int compare(Object pn1, Object pn2) {

            Integer s1 = new Integer(Integer.MIN_VALUE);

            try {
                if (pn1 != null) {
                    s1 = new Integer(pn1.toString());
                }
            } catch (Throwable t) {
            }

            Integer s2 = new Integer(Integer.MIN_VALUE);

            try {
                if (pn2 != null) {
                    s2 = new Integer(pn2.toString());
                }
            } catch (Throwable t) {
            }

            return s1.compareTo(s2);
        }

        public boolean equals(Object o2) {
            return false;
        }
    }    // End IntegerComparator

    public static class FloatComparator implements Comparator {

        public int compare(Object pn1, Object pn2) {

            Float s1 = new Float(Float.MIN_VALUE);

            try {
                if (pn1 != null) {
                    s1 = new Float(pn1.toString());
                }
            } catch (Throwable t) {
            }

            Float s2 = new Float(Float.MIN_VALUE);

            try {
                if (pn2 != null) {
                    s2 = new Float(pn2.toString());
                }
            } catch (Throwable t) {
            }

            return s1.compareTo(s2);
        }

        public boolean equals(Object o2) {
            return false;
        }
    }    // End FloatComparator

}    // End ComparatorFactory2
