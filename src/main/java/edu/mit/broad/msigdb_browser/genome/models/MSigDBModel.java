/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.genome.models;

import edu.mit.broad.msigdb_browser.vdb.msigdb.GeneSetAnnotation;
import edu.mit.broad.msigdb_browser.vdb.msigdb.MSigDB;

import javax.swing.table.AbstractTableModel;

/**
 * An implementation of AbstractTableModel for Datasets. <br>
 * <p/>
 * Does not replicate any of Dataset's data strcutures.
 * <p/>
 * Why not just make Dataset an AbstractTableModel? Just trying to avoid
 * placing any GUI related code in the core datastructures.
 * <p/>
 * Why extending AbstractTableModel rather than DefaultTableModel?
 * Because dont want several of the "mutability" properties that
 * DefaultTableModel has. i.e setRiwAt etc - this is a Fixture - once
 * constructed its #rows and # columns is set. (the contents can change though)
 *
 * @author Aravind Subramanian
 * @version %I%, %G%
 */
public class MSigDBModel extends AbstractTableModel {

    private MSigDB fMSigDB;

    /**
     * Class Constructor.
     * Cretaes a default row name based annotation for use.
     */
    public MSigDBModel(final MSigDB msigdb) {

        if (msigdb == null) {
            throw new IllegalArgumentException("Param msigdb cannot be null");
        }

        this.fMSigDB = msigdb;
    }

    public MSigDB getMSigDB() {
        return fMSigDB;
    }

    public int getColumnCount() {
        return COL_NAMES.length;
    }

    public int getRowCount() {
        return fMSigDB.getNumGeneSets();
    }

    public String getColumnName(int col) {
        return COL_NAMES[col];
    }

    public static final int COL_PUBMEDID = 10;

    private static String[] COL_NAMES = new String[]{
            "",      // 0
            "NAME",  // 1
            "# GENES",          // 2
            "DESCRIPTION",      // 3
            "COLLECTION",        // 4
            "ORGANISM",       // 5
            "CHIP",           // 6
            "CONTRIBUTER",    // 7
            "PUBMED ID",     //  8
            "EXTERNAL URL"  // 9
    };

    public Object getValueAt(int row, int col) {

        GeneSetAnnotation ann = fMSigDB.getGeneSetAnnotation(row);

        if (col == 0) {
            return "" + (row + 1);
        } else if (col == 1) {
            return ann.getStandardName();
        } else if (col == 2) {
            return "" + ann.getGeneSet(true).getNumMembers();
        } else if (col == 3) {
            return ann.getDescription().getBrief();
        } else if (col == 4) {
            return ann.getCategory().getName();
        } else if (col == 5) {
            return ann.getOrganism().toString();
        } else if (col == 6) {
            return ann.getChipOriginal_name();
        } else if (col == 7) {
            return ann.getContributor().getName();
        } else if (col == 8) {
            return ann.getExternalLinks().getPMID();
        } else if (col == 9) {
            return ann.getExternalLinks().getExtDetailsURL();
        } else {
            return "huh";
        }

    }

    public Class getColumnClass(int col) {
        Class cl;
        if (col == 0 || col == 3) {
            cl = Integer.class;
        } else {
            cl = String.class;
        }

        //System.out.println("clkass: " + cl + " col: " + col + " name: " + getColumnName(col));
        return cl;
    }

    public boolean isEditable() {
        return false;
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }
}    // End MSigDBModel
