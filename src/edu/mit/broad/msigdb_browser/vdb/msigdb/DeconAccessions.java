/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.vdb.msigdb;

import edu.mit.broad.msigdb_browser.vdb.VdbRuntimeResources;
import edu.mit.broad.msigdb_browser.vdb.meg.Gene;

/**
 * @author Aravind Subramanian
 */
public class DeconAccessions {

    /**
     * Class constructor
     *
     * @param identifiers
     * @throws Exception
     */
    private DeconAccessions() throws Exception {
    }

    public static Lookup creatLookup(final String originalId) throws Exception {
        Gene gene = null;

        if (VdbRuntimeResources.getChip_Gene_Symbol().isProbe(originalId)) {
            gene = VdbRuntimeResources.getChip_Gene_Symbol().getProbe(originalId).getGene();
        } else {

            if (VdbRuntimeResources.getAliasDb().isAlias(originalId)) {
                gene = VdbRuntimeResources.getAliasDb().getHugo(originalId);
            } else {
                // seq accessions is a special chip: accessions + symbols + aliases
                if (VdbRuntimeResources.getChip_Seq_Accession().isProbe(originalId)) {
                    gene = VdbRuntimeResources.getChip_Seq_Accession().getHugo(originalId);
                }
            }
        }

        return new Lookup(originalId, gene);
    }

    /**
     * Inner class representing one lookup
     */
    public static class Lookup {

        private String fOriginalId;

        private Gene fMatch;

        Lookup(final String original, final Gene match) {
            this.fOriginalId = original;
            this.fMatch = match;
        }

        public String getSymbol() {
            if (fMatch != null) {
                return fMatch.getSymbol();
            } else {
                return fOriginalId;
            }
        }

    } // End class Lookup

} // End class DeconAccessions
