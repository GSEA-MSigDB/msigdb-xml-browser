/*
 * Copyright (c) 2003-2021 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.genome.viewers;

import com.jidesoft.grid.SortableTable;

import edu.mit.broad.msigdb_browser.genome.models.NumberedProxyModel;
import edu.mit.broad.msigdb_browser.genome.swing.GuiHelper;

import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.TableModel;

/**
 * Base class for several Viewer.
 * Contains commonly useful initialization and methods.
 *
 * @author Aravind Subramanian
 */
public abstract class AbstractViewer extends JPanel implements Viewer {

    protected Logger log;

    protected static final Logger klog = Logger.getLogger(AbstractViewer.class);

    private Icon fIcon;

    private String fName;

    private String fTitle;

    // Users of this method must call init
    protected AbstractViewer() {
        this.log = Logger.getLogger(this.getClass());
    }

    /**
     * Class constructor
     *
     * @param name
     * @param icon
     * @param title
     */
    public AbstractViewer(final String name, final Icon icon, final String title) {
        init(name, icon, title);
    }

    protected void init(final String name, final Icon icon, final String title) {

        if (name == null) {
            throw new IllegalArgumentException("Param name cannot be null");
        }

        if (title == null) {
            throw new IllegalArgumentException("Param title cannot be null");
        }

        this.fIcon = icon;
        this.fName = name;
        this.fTitle = title;
        if (log == null) {
            this.log = Logger.getLogger(this.getClass());
        }
    }

    public JComponent getWrappedComponent() {
        _checkInit();
        return this;
    }

    public Icon getAssociatedIcon() {
        _checkInit();
        return fIcon;
    }

    public String getName() {
        _checkInit();
        return fName;
    }

    private void _checkInit() {
        if (fName == null) {
            throw new IllegalStateException("Viewer likely not init'ed name: " + fName);
        }

        if (fTitle == null) {
            throw new IllegalStateException("Viewer likely not init'ed title: " + fTitle);
        }
    }

    public String getAssociatedTitle() {
        return fTitle;
    }

    public JMenuBar getJMenuBar() {
        return EMPTY_MENU_BAR;
    }

    protected static void setColumnSize(int size, int col, JTable table, boolean alsoMax) {
        GuiHelper.Table.setColumnSize(size, col, table, alsoMax);
    }

    protected static SortableTable createTable(final TableModel model,
            final boolean addRowNumCol, final boolean boldHeaders) {
        TableModel amodel = model;

        if (addRowNumCol) {
            amodel = new NumberedProxyModel(model);
        }

        SortableTable table = new SortableTable(amodel);

        if (addRowNumCol) { // has to be done after setting model
            setColumnSize(35, 0, table, true);
        }

        table.setCellSelectionEnabled(true);
        
        return table;
    }
}
