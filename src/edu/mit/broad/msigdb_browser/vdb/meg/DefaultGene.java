/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.vdb.meg;

import edu.mit.broad.msigdb_browser.genome.NamingConventions;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author aravind subramanian
 */
public class DefaultGene implements Gene {

    private String fSymbol;

    private String fTitle;

    protected String fCyto_opt = null;

    private Set fAliases = null;

    protected Set fSeqAccessions = null;

    private static final Set EMPTY_SET = Collections.unmodifiableSet(new HashSet());

    // db4o constructor
    public DefaultGene() {
    }

    public Gene cloneDeep() {
        DefaultGene gene = new DefaultGene();
        gene.fSymbol = this.fSymbol;
        gene.fTitle = this.fTitle;
        gene.fAliases = new HashSet(fAliases);
        gene.fSeqAccessions = new HashSet(fSeqAccessions);
        return gene;
    }

    /**
     * Class constructor
     *
     * @param symbol
     * @param title
     * @param seqAccessions
     * @param aliases
     */
    public DefaultGene(final String symbol, String title) {

        if (symbol == null) {
            throw new IllegalArgumentException("Parameter symbol cannot be null");
        }

        this.fTitle = NamingConventions.titleize(title);
        this.fSymbol = symbol.toUpperCase(); // @note
    }

    public String getSymbol() {
        return fSymbol;
    }

    public String getTitle() {
        return fTitle;
    }

    public String getTitle_truncated() {
        return Helper.getTitle_truncated(fTitle);
    }

    public Set getAliases() {
        if (fAliases == null) {
            return EMPTY_SET;
        } else {
            return Collections.unmodifiableSet(fAliases);
        }
    }

    public String[] getAliasesArray() {
        Set set = getAliases();
        return (String[]) set.toArray(new String[set.size()]);
    }

    public boolean equals(Object obj) { // @note equals on just the symbol
        return obj instanceof Gene && ((Gene) obj).getSymbol().equalsIgnoreCase(getSymbol());
    }

    public String toString() {
        return getSymbol();
    }

    // @note equals on justthe symbol
    public int hashCode() {
        return getSymbol().hashCode();
    }


} // End class DefaultNugo
