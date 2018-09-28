/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.vdb.map;

import edu.mit.broad.msigdb_browser.genome.objects.FSet;
import edu.mit.broad.msigdb_browser.genome.objects.GeneSet;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Internal class
 */
class MGeneSetImpl implements MGeneSet {

    private GeneSet mappedGeneSet; // always stored

    /**
     * Class constructor
     *
     * @param sourceGeneSet
     * @param maintainEtiology
     */
    protected MGeneSetImpl(final GeneSet sourceGeneSet,
                           final String sourceChipName,
                           final String targetChipName,
                           final MappingDbType dbType,
                           final Mapper mapper) throws Exception {

        if (sourceGeneSet == null) {
            throw new IllegalArgumentException("Param sourceGeneSet cannot be null");
        }

        if (sourceChipName == null) {
            throw new IllegalArgumentException("Param sourceChipName cannot be null");
        }

        if (targetChipName == null) {
            throw new IllegalArgumentException("Param targetChipName cannot be null");
        }

        if (dbType == null) {
            throw new IllegalArgumentException("Param dbType cannot be null");
        }
        if (mapper == null) {
            throw new IllegalArgumentException("Param mapper cannot be null");
        }

        Set targets = new HashSet();

        for (int i = 0; i < sourceGeneSet.getNumMembers(); i++) {
            String sourceProbeName = sourceGeneSet.getMember(i);
            Object target = mapper.map(sourceProbeName);

            if (target == null) {

            } else if (target instanceof String) {
                targets.add(target);
            } else if (target instanceof Set) {
                targets.addAll((Set) target);
            } else {
                throw new IllegalStateException("Unnown mapped object: " + target + " " + target.getClass());
            }
        }

        this.mappedGeneSet = new FSet(sourceGeneSet.getName(), Collections.unmodifiableSet(targets));
    }

    public GeneSet getMappedGeneSet() {
        return mappedGeneSet;
    }

} // End class MGeneSetImpl
