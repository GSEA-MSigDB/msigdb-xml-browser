/*
 * Copyright (c) 2003-2018 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.vdb.msigdb;

import edu.mit.broad.msigdb_browser.genome.objects.GeneSet;
import edu.mit.broad.msigdb_browser.vdb.Organism;
import edu.mit.broad.msigdb_browser.vdb.VdbRuntimeResources;
import edu.mit.broad.msigdb_browser.vdb.chip.Chip;

import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Set;

/**
 * @author Aravind Subramanian
 */
public class GeneSetAnnotationImpl implements GeneSetAnnotation {

    private Logger log = Logger.getLogger(GeneSetAnnotationImpl.class);

    private GeneSet fGeneSet_orig;

    private GeneSet fGeneSet_symbolized;

    private Chip fChipxx;

    private String fChip_name;

    private GeneSetContributor fContributor;

    private String fStandardName;

    private String fSystematicName;

    private GeneSetExternalLinks fExtLinks;

    private Organism fOrganism;

    private GeneSetCategory fCategory;

    private GeneSetDescription fDesc;

    private Set fTags;

    private void init(final GeneSet gset_orig,
                      final GeneSet gset_symbolized_opt, // @bote sometimes already converted
                      final Chip chip_orig_opt,
                      final String chip_orig_reqd,
                      final GeneSetContributor contributor,
                      final String pmid,
                      final String geoid,
                      final String specificGeneSetListingURL,
                      final String extDetailsURL,
                      final String standardName,
                      final String systematicName,
                      final Organism org,
                      final GeneSetCategory categ,
                      final GeneSetDescription desc,
                      final Set tags
    ) {

        if (gset_orig == null) {
            throw new IllegalArgumentException("Param gset_orig cannot be null");
        }

        if (chip_orig_reqd == null) {
            throw new IllegalArgumentException("Param chip_orig_reqd cannot be null");
        }

        this.fGeneSet_orig = gset_orig;
        this.fGeneSet_symbolized = gset_symbolized_opt;
        this.fChip_name = chip_orig_reqd;
        this.fChipxx = chip_orig_opt;
        this.fContributor = contributor;
        this.fExtLinks = new GeneSetExternalLinksImpl(pmid, geoid, specificGeneSetListingURL, extDetailsURL);
        //log.debug("Made ext link: " + fExtLinks.getPublicationURL() + " " + fExtLinks.getSpecificGeneSetListingURL() + " " + fExtLinks.getExtDetailsURL());
        this.fStandardName = standardName;
        this.fSystematicName = systematicName;
        this.fOrganism = org;
        this.fCategory = categ;
        this.fDesc = desc;

        if (tags != null) {
            this.fTags = Collections.unmodifiableSet(tags);
        } else {
            this.fTags = Collections.EMPTY_SET;
        }

    }

    /**
     * Class constructor
     *
     * @param gset_orig
     * @param chip_orig
     * @param contributor
     * @param extURL
     * @param standardName
     * @param systematicName
     * @param sourceName
     * @param orgName
     * @param categCode
     * @param desc_brief
     * @param desc_full
     */
    public GeneSetAnnotationImpl(final GeneSet gset_orig,
                                 final GeneSet gset_symbolized_opt, // @bote sometimes already converted
                                 final String chip_name,
                                 final String contributor,
                                 final String pmid,
                                 final String geoid,
                                 final String specificGeneSetListingURL,
                                 final String extDetailsURL,
                                 final String standardName,
                                 final String systematicName,
                                 final String orgName,
                                 final String categCode,
                                 final String desc_brief,
                                 final String desc_full,
                                 final Set tags
    ) {

        init(gset_orig,
                gset_symbolized_opt,
                null,
                chip_name,
                GeneSetContributorImpl.create(contributor),
                pmid, geoid, specificGeneSetListingURL, extDetailsURL,
                standardName, systematicName,
                Organism.parseOrganism(orgName),
                GeneSetCategoryImpl.lookup(categCode),
                new GeneSetDescriptionImpl(desc_brief, desc_full),
                tags);
    }


    // orig might be gene symbol even if the orig chip is not gene symbol
    // (might have been curated as symbol)
    // need to do some hackery here as for many of the early curated sets only
    // have gene symbols even though they are
    // from an affy chip
    public GeneSet getGeneSet(final boolean orig) {
        if (orig) {
            return fGeneSet_orig;
        } else {

            // Pre-converted
            if (fGeneSet_symbolized != null) {
                return fGeneSet_symbolized;
            }

            if (isGeneSymbol(fGeneSet_orig)) {
                return fGeneSet_orig;
            } else {

                if (fGeneSet_symbolized == null) {
                    this.fGeneSet_symbolized = _getChipOriginal().symbolize(fGeneSet_orig);

                    if (fGeneSet_symbolized.getNumMembers() == 0) {
                        //log.warn("Bad conversion for: " + fGeneSet_orig.getName() + " zero members");
                        throw new IllegalStateException("Bad conversion for: " + fGeneSet_orig.getName() + " " + getChipOriginal_name());
                    } else if (fGeneSet_symbolized.getNumMembers() < (fGeneSet_orig.getNumMembers() / 2)) {
                        log.warn("POOR conversion for: " +
                                fGeneSet_orig.getName() + " orig: " + fGeneSet_orig.getNumMembers() +
                                " symbols: " + fGeneSet_symbolized.getNumMembers() + " chip: " + getChipOriginal_name());
                    }
                }

                return fGeneSet_symbolized;
            }
        }
    }

    private static boolean isGeneSymbol(final GeneSet gset) {
        boolean isGeneSymbol = false;

        try {
            for (int i = 0; i < gset.getNumMembers(); i++) {
                if (i > 2 || isGeneSymbol) {
                    break;
                }
                if (VdbRuntimeResources.getChip_Gene_Symbol().isProbe(gset.getMember(i))) {
                    isGeneSymbol = true;
                }
            }

        } catch (Throwable t) {
            throw new RuntimeException(t); // sigh
        }

        return isGeneSymbol;
    }

    public GeneSetContributor getContributor() {
        return fContributor;
    }

    public GeneSetExternalLinks getExternalLinks() {
        return fExtLinks;
    }

    public String getStandardName() {
        return fStandardName;
    }

    public String getLSIDName() {
        return fSystematicName;
    }

    private Chip _getChipOriginal() {

        if (fChipxx == null) {
            this.fChipxx = VdbRuntimeResources.getChip(getChipOriginal_name());
        }

        return fChipxx;
    }

    public String getChipOriginal_name() {
        return fChip_name;
    }

    public Organism getOrganism() {
        return fOrganism;
    }

    public GeneSetCategory getCategory() {
        return fCategory;
    }

    public GeneSetDescription getDescription() {
        return fDesc;
    }

    public Set getTags() {
        return fTags;
    }

    public String[] getTagsArray() {
        return (String[]) getTags().toArray(new String[getTags().size()]);
    }

} // End class GeneSetAnnotationImpl
