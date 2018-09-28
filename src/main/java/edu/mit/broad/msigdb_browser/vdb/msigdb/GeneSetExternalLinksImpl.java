/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.vdb.msigdb;

/**
 * @author Aravind Subramanian
 */
public class GeneSetExternalLinksImpl implements GeneSetExternalLinks {

    private String fPMID;
    private String fGeoID;
    private String fSpecificGeneSetListingURL;
    private String fExtDetailsURL;

    /**
     * Class constructor
     *
     * @param pmid
     * @param specificGeneSetListiingURL
     * @param extDetailsurl
     */
    public GeneSetExternalLinksImpl(String pmid,
                                    String geoid,
                                    String specificGeneSetListingURL,
                                    String extDetailsURL) {
        this.fPMID = pmid;
        this.fGeoID = geoid;
        this.fSpecificGeneSetListingURL = specificGeneSetListingURL;
        this.fExtDetailsURL = extDetailsURL;
    }

    public String getPMID() {
        return fPMID;
    }

    public String getGeoID() {
        return fGeoID;
    }

    public String getSpecificGeneSetListingURL() {
        return fSpecificGeneSetListingURL;
    }

    public String getExtDetailsURL() {
        return fExtDetailsURL;
    }

} // End class GeneSetExternalLinkImpl
