/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.vdb.chip;

import edu.mit.broad.msigdb_browser.genome.objects.*;
import edu.mit.broad.msigdb_browser.vdb.meg.Gene;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aravind Subramanian
 */
public abstract class AbstractChip extends AbstractObject implements Chip {

    protected String fSourcePath;

    public Chip cloneShallow(final String newName) {
        super.setName(newName);
        return this;
    }

    /**
     * Class constructor
     */
    protected AbstractChip() {
    }

    /**
     * Class constructor
     *
     * @param chipName
     * @param sourcePath
     */
    public AbstractChip(final String chipName,
                        final String sourcePath) {

        // @note dont do common init routine yet -> we're in skeletonmode
        super.initialize(chipName);

        if (sourcePath == null) {
            throw new IllegalArgumentException("Parameter sourcePath cannot be null");
        }

        this.fSourcePath = sourcePath;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Chip) {
            if (((Chip) obj).getName().equalsIgnoreCase(getName())) {
                return true;
            }
        }

        return false;
    }

    public int hashCode() {
        return getName().hashCode();
    }

    public String toString() {
        return getName();
    }

    public GeneSet getProbeNamesAsGeneSet() throws Exception {
        return new FSet(getName(), getProbeNames());
    }

    // will error out if no such probe
    public Gene getHugo(final String probeName) throws Exception {
        return getProbe(probeName).getGene();
    }

    // catch the exception
    public String getSymbol(final String probeName, final NullSymbolMode nmode) {
        try {
            if (isProbe(probeName)) {
                return nmode.getSymbol(probeName, getHugo(probeName));
            } else {
                return nmode.getSymbol(probeName, null);
            }
        } catch (Throwable t) {
            log.error(t);
            return "";
            //return Constants.NA;
        }

    }

    public GeneSet symbolize(final GeneSet gset) {
        final List names = new ArrayList();
        for (int r = 0; r < gset.getNumMembers(); r++) {
            final String n = getSymbol(gset.getMember(r), OMIT_NULLS);
            if (n != null && !names.contains(n)) {
                names.add(n);
            }
        }

        return new FSet(gset.getName(), names, false); // already checked
    }

} // End class AbstractChip
