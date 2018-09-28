/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.vdb.map;

import edu.mit.broad.msigdb_browser.genome.objects.DefaultGeneSetMatrix;
import edu.mit.broad.msigdb_browser.genome.objects.GeneSet;
import edu.mit.broad.msigdb_browser.genome.objects.GeneSetMatrix;

/**
 * @author Aravind Subramanian
 */
class MGeneSetMatrixImpl implements MGeneSetMatrix {

    private MGeneSet[] mappedGeneSets; // always stored

    private String fOrigGmName;

    /**
     * Class constructor
     *
     * @param sourceGeneSetMatrix
     * @param maintainEtiology
     */

    MGeneSetMatrixImpl(final GeneSetMatrix sourceGeneSetMatrix,
                       final String sourceChipName,
                       final String targetChipName,
                       final MappingDbType dbType,
                       final Mapper mapper) throws Exception {

        if (sourceGeneSetMatrix == null) {
            throw new IllegalArgumentException("Param sourceGeneSetMatrix cannot be null");
        }

        if (sourceChipName == null) {
            throw new IllegalArgumentException("Param sourceChipName cannot be null");
        }

        if (targetChipName == null) {
            throw new IllegalArgumentException("Param targetChipName cannot be null");
        }

        this.mappedGeneSets = new MGeneSet[sourceGeneSetMatrix.getNumGeneSets()];
        for (int i = 0; i < sourceGeneSetMatrix.getNumGeneSets(); i++) {
            this.mappedGeneSets[i] = new MGeneSetImpl(sourceGeneSetMatrix.getGeneSet(i),
                    sourceChipName, targetChipName, dbType, mapper);
        }

        this.fOrigGmName = sourceGeneSetMatrix.getName();

    }

    public GeneSetMatrix getMappedGeneSetMatrix(final String prefix) {
        GeneSet[] gsets = new GeneSet[mappedGeneSets.length];
        for (int i = 0; i < gsets.length; i++) {
            gsets[i] = mappedGeneSets[i].getMappedGeneSet();
        }

        String name;
        if (prefix == null || prefix.length() == 0) {
            name = fOrigGmName;
        } else {
            name = prefix + "_" + fOrigGmName;
        }

        return new DefaultGeneSetMatrix(name, gsets);
    }

} // End interface MGeneSetMatrixImpl
