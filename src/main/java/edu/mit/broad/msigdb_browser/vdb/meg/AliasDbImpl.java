/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.vdb.meg;

import edu.mit.broad.msigdb_browser.genome.*;
import edu.mit.broad.msigdb_browser.genome.objects.AbstractObject;
import edu.mit.broad.msigdb_browser.vdb.VdbRuntimeResources;
import edu.mit.broad.msigdb_browser.vdb.chip.Chip;
import edu.mit.broad.msigdb_browser.vdb.chip.Probe;
import edu.mit.broad.msigdb_browser.vdb.chip.SimpleProbe2;

import java.util.*;

/**
 * @author Aravind Subramanian
 */
public class AliasDbImpl extends AbstractObject implements AliasDb {

    // chip must be specified
    private Chip fChip_opt;

    /**
     * Class constructor
     * <p/>
     * Either file or chip must be specified
     *
     * @param file
     */
    public AliasDbImpl(final Chip chip_opt) {
        super.initialize(_name(chip_opt));
        this.fChip_opt = chip_opt;
    }

    public String getQuickInfo() {
        return null;
    }

    public boolean isAlias(final String alias) throws Exception {
        String symbol = getSymbol(alias);
        if (symbol == null) {
            return false;
        } else {
            return true;
        }
    }

    // may return null
    public String getSymbol(final String alias) throws Exception {
        if (alias == null) {
            throw new IllegalArgumentException("Param alias cannot be null");
        }

        _initAliasSymbolMap();

        Object obj = fAliasSymbolMap.get(alias);
        if (obj != null) {
            return obj.toString();
        }

        obj = fAliasSymbolMap.get(alias.toUpperCase());
        if (obj != null) {
            return obj.toString();
        }

        return null;
    }

    public Gene getHugo(final String alias) throws Exception {
        if (fChip_opt == null) {
            throw new NotImplementedException("Only works for in mem gene symbol chip");
        }

        String symbol = getSymbol(alias);
        return fChip_opt.getHugo(symbol);
    }

    public Probe[] getAliasesAsProbes() throws Exception {
        _initAliasSymbolMap();
        final Probe[] probes = new Probe[fAliasSymbolMap.size()];
        int cnt = 0;
        for (Iterator iterator = fAliasSymbolMap.keySet().iterator(); iterator.hasNext();) {
            String alias = iterator.next().toString();
            String symbol = fAliasSymbolMap.get(alias).toString();
            probes[cnt++] = new SimpleProbe2(alias, symbol, VdbRuntimeResources.getChip_Gene_Symbol());
        }

        return probes;
    }

    private static String _name(final Chip chip_opt) {
        if (chip_opt != null) {
            return chip_opt.getName();
        } else {
            throw new IllegalArgumentException("Both file and chip cannot be null");
        }
    }

    private Map fAliasSymbolMap;

    private void _initAliasSymbolMap() throws Exception {
        if (fAliasSymbolMap != null) {
            return;
        }

        final Map map = new HashMap();
        final Set duplicates = new HashSet();

        final Probe[] probes = fChip_opt.getProbes();
        for (int i = 0; i < probes.length; i++) {
            Gene gene = probes[i].getGene();
            if (gene != null && gene != Gene.NULL_GENE && gene.getAliases() != null && !gene.getAliases().isEmpty()) {
                final String symbol = gene.getSymbol();
                final String[] aliases = gene.getAliasesArray();
                for (int s = 0; s < aliases.length; s++) {
                    String alias = aliases[s].toUpperCase(); // @note aliases always stored in UC
                    if (map.containsKey(alias)) {
                        duplicates.add(alias);
                    } else { // ignore duplicates -- dont add at all
                        map.put(alias, symbol);
                    }
                }
            }
        }

        if (!duplicates.isEmpty()) {
            log.warn("There are duplicate entry for accessions: " + duplicates.size() + " ignoring them ..." + " total #: " + map.size());
            //throw new IllegalStateException("Duplicate entry for accessions: " + duplicates.size() + "\n" + duplicates);
        }

        log.info("# of aliases: " + map.size());

        this.fAliasSymbolMap = map;
    }

} // End class AliasDbImpl

