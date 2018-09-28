/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.vdb.chip;

import edu.mit.broad.msigdb_browser.genome.NamingConventions;
import edu.mit.broad.msigdb_browser.vdb.meg.Gene;

/**
 * @author Aravind Subramanian
 */
public class NullSymbolModes {

    public static class OmitNullSymbolMode implements NullSymbolMode {

        public OmitNullSymbolMode() {
        }

        public String getSymbol(String probeId, Gene gene) {
            if (_isNull(gene)) {
                return null;
            } else {
                return gene.getSymbol();
            }
        }

    }

    private static boolean _isNull(Gene gene) {
        if (gene == null || NamingConventions.isNull(gene.getSymbol())) {
            return true;
        } else {
            return false;
        }
    }

} // End class NullSymbolModes

