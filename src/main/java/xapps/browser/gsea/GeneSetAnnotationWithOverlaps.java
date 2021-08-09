/*
 * Copyright (c) 2003-2021 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xapps.browser.gsea;

import com.jidesoft.grid.SortableTable;

import edu.mit.broad.msigdb_browser.genome.objects.GeneSet;
import edu.mit.broad.msigdb_browser.genome.viewers.AbstractViewer;
import edu.mit.broad.msigdb_browser.vdb.msigdb.GeneSetAnnotation;
import edu.mit.broad.msigdb_browser.vdb.msigdb.MSigDB;
import xapps.browser.MGUIUtils;
import xapps.browser.VToolsForMSigDBBrowser;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * @author Aravind Subramanian
 */
public class GeneSetAnnotationWithOverlaps {

    private GeneSet query;

    private One[] fOnes;

    private String[] fAllGeneSetNames;

    private GeneSetAnnotationWithOverlaps fInstance = this;

    /**
     * Class constructor
     *
     * @param query
     * @param ones
     */
    public GeneSetAnnotationWithOverlaps(final GeneSet query, final One[] ones) {
        this.query = query;
        this.fOnes = ones;
    }

    public TableModel createModel() {
        return new Model();
    }

    public Viewer createViewer(MSigDB msigdb) {
        return new Viewer(query.getName(true), msigdb);
    }


    public String[] getAllDisplayedGeneSetNames() {

        if (fAllGeneSetNames == null) {
            fAllGeneSetNames = new String[fOnes.length];
            for (int i = 0; i < fOnes.length; i++) {
                fAllGeneSetNames[i] = fOnes[i].getAnn().getGeneSet(true).getName(true);
            }
        }

        return fAllGeneSetNames;
    }


    public static class One {

        private GeneSetAnnotation gsa;

        private int inter;

        private double jacq;

        public One(final GeneSetAnnotation gsa, final int inter, final int querySize) {
            this.gsa = gsa;
            this.inter = inter;
            int N = querySize + gsa.getGeneSet(false).getNumMembers();
            int K = gsa.getGeneSet(false).getNumMembers();
            int m = querySize;

            this.jacq = (float) inter / (float) (N - inter);
        }

        public int getOverlap() {
            return inter;
        }

        public double getJaccard() {
            return jacq;
        }

        public GeneSetAnnotation getAnn() {
            return gsa;
        }

    } // End inner class One


    private static String[] COL_NAMES = new String[]{
            "NAME",  // 0
            "OVERLAP", // 1
            "JACCARD", // 2
            "# GENES",          // 3
            "DESCRIPTION",      // 4
            "COLLECTION"        // 5
    };


    /**
     * Inner class hat models as a table
     */
    class Model extends AbstractTableModel {


        /**
         * Class Constructor.
         * Cretaes a default row name based annotation for use.
         */
        public Model() {
        }

        public int getColumnCount() {
            return COL_NAMES.length;
        }

        public int getRowCount() {
            return fOnes.length;
        }

        public String getColumnName(int col) {
            return COL_NAMES[col];
        }

        public Object getValueAt(int row, int col) {

            GeneSetAnnotation ann = fOnes[row].getAnn();

            if (col == 0) {
                return ann.getStandardName();
            } else if (col == 1) {
                return fOnes[row].getOverlap();
            } else if (col == 2) {
                return new Double(fOnes[row].getJaccard());
            } else if (col == 3) {
                return new Integer(ann.getGeneSet(false).getNumMembers());
            } else if (col == 4) {
                return ann.getDescription().getBrief();
            } else if (col == 5) {
                return ann.getCategory().getName();
            } else {
                return "huh";
            }

        }

        public Class getColumnClass(int col) {
            if (col == 1 || col == 3) { return Integer.class; } 
            //if (col == 2) { return Double.class; } 
            return String.class;
        }

        public boolean isCellEditable(int row, int col) {
            return false;
        }
    }    // End GeneSetOverlapModel


    class Viewer extends AbstractViewer {

        /**
         * Class constructor
         *
         * @param ds
         */
        public Viewer(final String queryName, final MSigDB msigdb) {
            super("Overlaps", null, "Overlaps query: " + queryName);
            jbInit(msigdb);
        }

        private void jbInit(final MSigDB msigdb) {
            this.setLayout(new BorderLayout());
            TableModel dmodel = createModel();
            SortableTable table = createTable(dmodel, true, true);
            table.getColumnModel().getColumn(2).setCellRenderer(new JaccardTableCellRenderer());
            
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.setLayout(new BorderLayout());
            JScrollPane sp = new JScrollPane(table);
            this.add(sp, BorderLayout.CENTER);

            this.add(MGUIUtils.createExportSetsControlPanel(new MyViewerHook(msigdb, table),
                    new MyViewerHook(msigdb, table)), BorderLayout.SOUTH);

            this.revalidate();
        }

    }    // End Viewer

    public static class JaccardTableCellRenderer extends DefaultTableCellRenderer {
        private static final DecimalFormat formatter = new DecimalFormat( "#.00000" );
        
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Double) { value = formatter.format((Double)value); }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column );
        }
    }

    class MyViewerHook implements VToolsForMSigDBBrowser.ViewerHook {
        private MSigDB msigdb;

        private SortableTable table;

        MyViewerHook(MSigDB msigdb, final SortableTable table) {
            this.msigdb = msigdb;
            this.table = table;
        }


        public String[] getSelectedGeneSetNames() {
            final int[] rows = table.getSelectedRows();
            final String[] names = new String[rows.length];
            for (int i = 0; i < rows.length; i++) {
                names[i] = table.getModel().getValueAt(rows[i], 1).toString();
            }

            return names;
        }

        public int getNumSelectedRows() {
            return table.getSelectedRowCount();
        }

        public MSigDB getMSigDB() {
            return msigdb;
        }

        public String[] getAllDisplayedGeneSetNames() {
            return fInstance.getAllDisplayedGeneSetNames();
        }

    } // End class MyViewerHook
}
