/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.vdb.msigdb;

/**
 * @author Aravind Subramanian
 */
public class GeneSetCategoryImpl implements GeneSetCategory {

    public static GeneSetCategory POSITIONAL = new GeneSetCategoryImpl("c1", "Positional", "Position on the Genome");

    public static GeneSetCategory CURATED = new GeneSetCategoryImpl("c2", "Curated", "Curated gene sets");

    public static GeneSetCategory MOTIF = new GeneSetCategoryImpl("c3", "Motif", "Identified by sequence motif analysis");

    public static GeneSetCategory COMPUTED = new GeneSetCategoryImpl("c4", "Computational", "Identified by computational analysis");

    public static GeneSetCategory GO = new GeneSetCategoryImpl("c5", "Gene Ontology", "Gene Ontology sets");

    public static GeneSetCategory PATHWAY = new GeneSetCategoryImpl("c6", "Oncogenic Signatures", "Oncogenic pathway activation modules");

    public static GeneSetCategory IMMUNE = new GeneSetCategoryImpl("c7", "Immunologic Signatures", "Curated immune system gene sets");

    public static GeneSetCategory HALLMARK = new GeneSetCategoryImpl("H", "Hallmark", "Hallmark gene sets");

    public static GeneSetCategory ARCHIVED = new GeneSetCategoryImpl("ARCHIVED", "Archived", "Retired gene sets referenced by the Hallmarks");

    public static GeneSetCategory[] ALL = new GeneSetCategory[]{
            POSITIONAL,
            CURATED,
            MOTIF,
            COMPUTED,
            GO,
            PATHWAY,
            IMMUNE,
            HALLMARK,
            ARCHIVED
    };

    public static GeneSetCategory lookup(String codeName) {
        for (int i = 0; i < ALL.length; i++) {
            if (ALL[i].getCode().equalsIgnoreCase(codeName)) {
                return ALL[i];
            }
        }

        throw new IllegalArgumentException("No such gene set category for code: " + codeName);
    }

    private String fCode;
    private String fName;
    private String fDesc;

    private GeneSetCategoryImpl(String code, String name, String desc) {
        this.fCode = code;
        this.fName = name;
        this.fDesc = desc;
    }

    public String getCode() {
        return fCode;
    }

    public String getName() {
        return fName;
    }

    public String getDesc() {
        return fDesc;
    }

    public boolean equals(Object obj) {
        if (obj instanceof GeneSetCategory) {
            GeneSetCategory categ = (GeneSetCategory) obj;
            if (categ.getCode().equals(fName)) {
                return true;
            }
        }

        return false;
    }

} // End class GeneSetCategoryImpl
