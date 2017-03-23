/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.genome.alg;

import java.util.Comparator;

import org.apache.commons.io.FilenameUtils;

import edu.mit.broad.msigdb_browser.vdb.VdbRuntimeResources;

/**
 * Collection of usefule comparators
 *
 * @author Aravind Subramanian
 * @version %I%, %G%
 */
public class ComparatorFactory {

    /**
     * Privatized Class constructor
     */
    private ComparatorFactory() {
    }

    public static class FileExtComparator implements Comparator {

        /**
         * Return -1 if o1 is less than o2, 0 if they're equal, +1 if o1 is greater than o2.
         */
        public int compare(Object pn1, Object pn2) {

            String ext1 = FilenameUtils.getExtension(pn1.toString());
            String ext2 = FilenameUtils.getExtension(pn2.toString());

            return ext1.compareTo(ext2);
        }

        /**
         * Return true if this equals o2.
         */
        public boolean equals(Object o2) {
            return false;
        }
    }    // End FileExtComparator

    public static class ChipNameComparator implements Comparator {

        /**
         * Return -1 if o1 is less than o2, 0 if they're equal, +1 if o1 is greater than o2.
         */
        public int compare(Object pn1, Object pn2) {

            String s1 = pn1.toString();
            String s2 = pn2.toString();

            // always want GENE_SYMBOL.chip first
            if (VdbRuntimeResources.isChipGeneSymbol(s1) && VdbRuntimeResources.isChipGeneSymbol(s2)) {
                return 0;
            } else if (VdbRuntimeResources.isChipGeneSymbol(s1)) {
                return -1;
            } else if (VdbRuntimeResources.isChipGeneSymbol(s2)) {
                return 1;
            }

            // always want SEQ_ACCESSION.chip after GENE_SYMBOL.chip but ahead of all the rest
            if (VdbRuntimeResources.isChipSeqAccession(s1) && VdbRuntimeResources.isChipGeneSymbol(s2)) {
                return 0;
            } else if (VdbRuntimeResources.isChipSeqAccession(s1)) {
                return -1;
            } else if (VdbRuntimeResources.isChipSeqAccession(s2)) {
                return 1;
            }

            // next are chip files that begin with "HG"
            if (s1.toUpperCase().startsWith("HG") && s2.toUpperCase().startsWith("HG")) {
                return s1.compareTo(s2);
            } else if (s1.toUpperCase().startsWith("HG")) {
                return -1;
            } else if (s2.toUpperCase().startsWith("HG")) {
                return 1;
            }

            // next are chip files that begin with "HU"
            if (s1.toUpperCase().startsWith("HU") && s2.toUpperCase().startsWith("HU")) {
                return s1.compareTo(s2);
            } else if (s1.toUpperCase().startsWith("HU")) {
                return -1;
            } else if (s2.toUpperCase().startsWith("HU")) {
                return 1;
            }

            // now just string comparison
            return s1.compareTo(s2);

        }

        /**
         * Return true if this equals o2.
         */
        public boolean equals(Object o2) {
            return false;
        }
    }    // End ChipNameComparator

}
