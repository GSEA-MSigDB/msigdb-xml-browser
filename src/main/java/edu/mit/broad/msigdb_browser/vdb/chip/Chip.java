/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.vdb.chip;

import edu.mit.broad.msigdb_browser.genome.objects.*;
import edu.mit.broad.msigdb_browser.vdb.meg.Gene;

import java.util.Set;

/**
 * Capture a Chip object while enabling lazy loading of chip data
 * <p/>
 * Probe -> unique sequence feature used to measure a gene
 */
public interface Chip extends PersistentObject {

    public static final String COMBO_DUMMY_SOURCE = "combo_dummy_source";
    public static NullSymbolMode OMIT_NULLS = new NullSymbolModes.OmitNullSymbolMode();
    public Chip cloneShallow(final String newName);

    public int getNumProbes() throws Exception;

    // HUGO/GENE SYMBOL BASED -------------------------------------
    // will error out if no such probe
    public Gene getHugo(final String probeName) throws Exception;

    public String getSymbol(final String probeName, final NullSymbolMode nmode);

    public GeneSet symbolize(final GeneSet gset);

    public Set getProbeNames(final String symbol) throws Exception;

    public Set getProbeNames() throws Exception;

    public GeneSet getProbeNamesAsGeneSet() throws Exception;

    public boolean isProbe(final String probeName) throws Exception;

    // PROBE RELATED ------------------------------------------

    public Probe getProbe(final int i) throws Exception;

    public Probe getProbe(final String probeName) throws Exception;

    public Probe[] getProbes() throws Exception;

} // End Chip
