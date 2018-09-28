/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xapps.browser.gsea;

import edu.mit.broad.msigdb_browser.vdb.VdbRuntimeResources;
import edu.mit.broad.msigdb_browser.vdb.chip.Chip;
import edu.mit.broad.msigdb_browser.vdb.msigdb.GeneSetAnnotation;

import javax.swing.table.AbstractTableModel;

// @todo imprive by adding the mapping details

public class GeneSetAnnotation_gene_set_TableModel extends AbstractTableModel {

    private final GeneSetAnnotation fGsann;

    private Chip symbol_chip;

    private static final String[] COL_NAMES = new String[]{"Gene Symbol", "Gene Title"};

    /**
     * Class Constructor.
     * Initializes model to specified Template.
     */
    public GeneSetAnnotation_gene_set_TableModel(final GeneSetAnnotation gsa) {
        this.fGsann = gsa;
        try {
            this.symbol_chip = VdbRuntimeResources.getChip_Gene_Symbol();
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    public int getColumnCount() {
        return COL_NAMES.length;
    }

    public int getRowCount() {
        return fGsann.getGeneSet(false).getNumMembers();
    }

    public String getColumnName(int col) {
        return COL_NAMES[col];
    }


    public Object getValueAt(int row, int col) {
        String symbol = fGsann.getGeneSet(false).getMember(row);

        if (col == 0) {
            return symbol;
        } else if (symbol_chip != null) {
            try {
                return symbol_chip.getHugo(symbol).getTitle_truncated();
            } catch (Throwable t) {

            }

            return null;
        }

        return null;
    }

    public Class getColumnClass(int col) {
        return String.class;
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }
}    // End GeneSetModel