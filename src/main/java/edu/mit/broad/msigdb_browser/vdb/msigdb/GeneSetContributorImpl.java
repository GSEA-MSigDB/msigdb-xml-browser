/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.vdb.msigdb;


/**
 * @author Aravind Subramanian
 */
public class GeneSetContributorImpl implements GeneSetContributor {

    public static final GeneSetContributor BROAD = new GeneSetContributorImpl("Broad Institute");
    public static final GeneSetContributor XHX = new GeneSetContributorImpl("Xiaohui Xie");
    public static final GeneSetContributor BIOCARTA = new GeneSetContributorImpl("BioCarta");
    public static final GeneSetContributor SIGMA_ALDRICH = new GeneSetContributorImpl("SigmaAldrich");
    public static final GeneSetContributor GEARRAY = new GeneSetContributorImpl("GEArray");
    public static final GeneSetContributor GENMAPP = new GeneSetContributorImpl("GenMAPP");
    public static final GeneSetContributor GO = new GeneSetContributorImpl("GO");
    public static final GeneSetContributor STKE = new GeneSetContributorImpl("Signalling Transduction KE");
    public static final GeneSetContributor SIGNALLING_ALLIANCE = new GeneSetContributorImpl("Signalling Alliance");
    public static final GeneSetContributor KEVIN_VOGELSANG = new GeneSetContributorImpl("Kevin Vogelsang");

    static GeneSetContributor[] ALL = new GeneSetContributor[]{
            BROAD, BIOCARTA,
            SIGMA_ALDRICH,
            GEARRAY, GENMAPP, GO,
            STKE, SIGNALLING_ALLIANCE
    };

    private String fName;

    public static GeneSetContributor create(String name) {
        for (int i = 0; i < ALL.length; i++) {
            if (ALL[i].getName().equalsIgnoreCase(name)) {
                return ALL[i];
            }
        }

        return new GeneSetContributorImpl(name);
    }

    /**
     * Class constructor
     *
     * @param name
     */
    public GeneSetContributorImpl(String name) {
        this.fName = name;
    }

    public String getName() {
        return fName;
    }


} // End class GeneSetSourceImpl
