/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.vdb.msigdb;

import edu.mit.broad.msigdb_browser.genome.objects.*;
import gnu.trove.THashMap;

import java.util.*;

/**
 * @author Aravind Subramanian
 */
public class MSigDBImpl extends AbstractObject implements MSigDB {

    private GeneSetAnnotation[] fGeneSetAnns;

    private String fVersion;

    private String fBuildDate;

    private Map fGeneSetNameGeneSetAnnotationMap;

    /**
     * Class constructor
     *
     * @param name
     * @param gsanns
     */
    public MSigDBImpl(final String name, final String version, final String build_date, final GeneSetAnnotation[] gsanns) {
        initHere(name, version, build_date, gsanns);
    }

    private void initHere(final String name, final String version, final String build_date, final GeneSetAnnotation[] gsanns) {
        super.initialize(name);
        // chekc to make sure names are unique

        Set duplicates = new HashSet();
        Set all = new HashSet();
        this.fGeneSetNameGeneSetAnnotationMap = new THashMap();
        for (int i = 0; i < gsanns.length; i++) {
            if (all.contains(gsanns[i].getStandardName())) {
                duplicates.add(gsanns[i].getStandardName());
            } else {
                all.add(gsanns[i].getStandardName());
                fGeneSetNameGeneSetAnnotationMap.put(gsanns[i].getStandardName(), gsanns[i]);
            }
        }

        if (!duplicates.isEmpty()) {
            throw new IllegalArgumentException("Duplicate gene set annotations: " + duplicates.size() + "\n" + duplicates);
        }

        this.fGeneSetAnns = gsanns;
        this.fVersion = version;
        this.fBuildDate = build_date;
    }

    public String getQuickInfo() {
        return null;
    }

    public String getVersion() {
        return fVersion;
    }

    public String getBuildDate() {
        return fBuildDate;
    }

    public GeneSetMatrix createGeneSetMatrix(final String[] gsetNames) {
        // always get as symbols
        final GeneSet[] gsets = new GeneSet[gsetNames.length];
        for (int i = 0; i < gsetNames.length; i++) {
            gsets[i] = getGeneSetAnnotation(gsetNames[i]).getGeneSet(false);
        }

        String name = "gsets_" + gsetNames.length + "_" + _toName();
        return new DefaultGeneSetMatrix(name, gsets);
    }

    private String _toName() {
        return getName() + "." + getVersion() + ".symbols";
    }

    public GeneSetAnnotation getGeneSetAnnotation(int g) {
        return fGeneSetAnns[g];
    }

    public GeneSetAnnotation getGeneSetAnnotation(final String gsStdName) {

        if (gsStdName == null) {
            throw new IllegalArgumentException("Param gsStdName cannot be null");
        }

        Object obj = fGeneSetNameGeneSetAnnotationMap.get(gsStdName);
        if (obj == null) {
            // try harder
            obj = fGeneSetNameGeneSetAnnotationMap.get(gsStdName.toUpperCase());
            if (obj == null) {
                throw new IllegalArgumentException("No gset ann with std name: " + gsStdName);
            }
        }

        return (GeneSetAnnotation) obj;
    }

    public int getNumGeneSets() {
        return fGeneSetAnns.length;
    }

} // End class MSigDbImpl

