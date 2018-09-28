/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.vdb.msigdb;

import edu.mit.broad.msigdb_browser.genome.objects.GeneSet;
import edu.mit.broad.msigdb_browser.vdb.Organism;

import java.util.Set;

/**
 * @author Aravind Subramanian
 */
public interface GeneSetAnnotation {

    public GeneSet getGeneSet(final boolean origIds);

    public GeneSetContributor getContributor();

    public GeneSetExternalLinks getExternalLinks();

    public String getStandardName();

    public String getLSIDName();

    public String getChipOriginal_name();

    public Organism getOrganism();

    public GeneSetCategory getCategory();

    public GeneSetDescription getDescription();

    public Set getTags();

    public String[] getTagsArray();

} // End interface GeneSetAnnotation
