/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xtools.browser.api.ui;

import edu.mit.broad.msigdb_browser.genome.XLogger;
import edu.mit.broad.msigdb_browser.genome.objects.FSet;
import edu.mit.broad.msigdb_browser.genome.objects.GeneSet;
import edu.mit.broad.msigdb_browser.genome.parsers.ParseUtils;
import edu.mit.broad.msigdb_browser.genome.parsers.ParserFactory;
import edu.mit.broad.msigdb_browser.xbench.RendererFactory2;
import edu.mit.broad.msigdb_browser.xbench.core.ObjectBindery;

import org.apache.log4j.Logger;

import javax.swing.*;

import java.io.File;
import java.util.ArrayList;

/**
 * @author Aravind Subramanian
 * @version %I%, %G%
 */
public class GeneSetTabbedChooserPanel extends JTabbedPane implements ChooserPanel {

    private static final Logger klog = XLogger.getLogger(GeneSetTabbedChooserPanel.class);

    private JList jlGeneSet;

    private JTextArea taGenes;

    private NamedModel fModel;

    private int fSelectionMode = ListSelectionModel.SINGLE_SELECTION;

    /**
     * Class Constructor.
     */
    public GeneSetTabbedChooserPanel(int selMode) {

        this.fModel = new NamedModel("GeneSets(grp)", ObjectBindery.getModel(GeneSet.class));

        // carefull with rebuild / reset the model here -> that ruins the selection policy
        this.jlGeneSet = new JList();

        DefaultListCellRenderer rend = new RendererFactory2.CommonLookListRenderer();
        jlGeneSet.setCellRenderer(rend);

        this.fSelectionMode = selMode;
        jlGeneSet.setModel(fModel.model);
        jlGeneSet.setSelectionMode(selMode);

        taGenes = new JTextArea();
        taGenes.setText(""); // @note

        this.addTab("Text Entry", new JScrollPane(taGenes));
        this.addTab(fModel.name, new JScrollPane(jlGeneSet));

    }

    public JComponent getChooser() {
        return this;
    }

    public String getTitle() {

        String text = "Select a gene set";

        if (fSelectionMode == ListSelectionModel.MULTIPLE_INTERVAL_SELECTION) {
            text = "Select one or more gene set(s)";
        }

        return text;
    }

    public Object[] getChoosenObjects() {

        java.util.List allValues = new ArrayList();

        Object[] sels = jlGeneSet.getSelectedValues();
        if (sels != null) {
            for (int i = 0; i < sels.length; i++) {
                if (sels[i] != null) {
                    allValues.add(sels[i]);
                }
            }
        }

        // add text are stuff as a gene set
        String s = taGenes.getText();

        if (s != null) {
            String[] strs = ParseUtils.string2strings(s, "\t\n", false); // we want things synched
            if (strs.length != 0) {
                GeneSet gset = new FSet("from_text_area_", strs);
                try {
                    ParserFactory.save(gset, File.createTempFile(gset.getName(), ".grp"));
                } catch (Throwable t) {
                    klog.error(t);
                }
                allValues.add(gset);
            }
        }
        return allValues.toArray(new Object[allValues.size()]);

    }

    public JList[] getJListsForDoubleClick() {
        return new JList[]{jlGeneSet};
    }

}        // End GeneSetChooserWindow
