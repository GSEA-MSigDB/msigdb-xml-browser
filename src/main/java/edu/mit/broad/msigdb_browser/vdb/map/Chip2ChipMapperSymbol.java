/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.vdb.map;

import edu.mit.broad.msigdb_browser.genome.objects.AbstractObject;
import edu.mit.broad.msigdb_browser.genome.objects.GeneSetMatrix;
import edu.mit.broad.msigdb_browser.vdb.chip.Chip;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Aravind Subramanian
 */
public class Chip2ChipMapperSymbol extends AbstractObject implements Chip2ChipMapper {

    public static final String createId(final Chip sourceChip, final Chip targetChip, final MappingDbType db) {
        return sourceChip.getName() + '.' + targetChip.getName() + '.' + db.getName();
    }

    private Chip fSourceChip;

    private Chip fTargetChip;

    /**
     * Class constructor
     *
     * @param sourceChip
     * @param targetChip
     */
    public Chip2ChipMapperSymbol(final Chip sourceChip, final Chip targetChip) {

        if (sourceChip == null) {
            throw new IllegalArgumentException("Param sourceChip cannot be null");
        }
        if (targetChip == null) {
            throw new IllegalArgumentException("Param targetChip cannot be null");
        }

        System.out.println("##### source: " + sourceChip.getName() + " target: " + targetChip.getName());


        this.fSourceChip = sourceChip;
        this.fTargetChip = targetChip;

        super.initialize(createId(fSourceChip, fTargetChip, getMappingDbType()));
    }

    public String getQuickInfo() {
        return null;
    }

    public MappingDbType getMappingDbType() {
        return MappingDbTypes.GENE_SYMBOL;
    }

    public Chip getTargetChip() {
        return fTargetChip;
    }

    public Set map(final String sourceProbeName) throws Exception {

        Set targetProbes = new HashSet();
        // first add directly if a direct probe match
        if (fTargetChip.isProbe(sourceProbeName)) {
            targetProbes.add(sourceProbeName); // should i simply return here ?
        }

        // Then do a symbol based lookup
        String symbol = fSourceChip.getSymbol(sourceProbeName, Chip.OMIT_NULLS);
        if (symbol != null) {
            Set set = fTargetChip.getProbeNames(symbol);
            targetProbes.addAll(set);
        }

        //System.out.println(">>> MAPPING " + sourceProbeName + " >" + symbol + " >" + fTargetChip.getProbeNames(symbol) + " source >" + fSourceChip.getName() + "< target>" + fTargetChip.getName());

        return targetProbes;
    }

    public MGeneSetMatrix map(final GeneSetMatrix sourceGm) throws Exception {
        return new MGeneSetMatrixImpl(sourceGm, fSourceChip.getName(), fTargetChip.getName(), MappingDbTypes.GENE_SYMBOL, this);
    }

} // End class SymbolMappedChip
