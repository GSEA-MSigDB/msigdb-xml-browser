/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xapps.browser;

import com.jidesoft.grid.SortableTable;
import com.jidesoft.swing.JideSplitPane;

import edu.mit.broad.msigdb_browser.vdb.msigdb.GeneSetAnnotation;
import xapps.browser.gsea.GeneSetAnnotation_gene_set_TableModel;

import javax.swing.*;

import java.awt.*;

/**
 * @author Aravind Subramanian
 */
// Only show the aspects that are not in the table already
public class GeneSetPagePanel extends JPanel {

    private JTextArea taDesc;
    //private JTextArea taKeywords;
    private JTable gsetTable;

    public GeneSetPagePanel() {
        jbInit();
    }

    public void setData(final GeneSetAnnotation gsa) {
        //tfName.setText(gsa.getStandardName());
        taDesc.setText(gsa.getDescription().getFull());
        //taKeywords.setText(gsa.getMeshTerms().toString());
        gsetTable.setColumnSelectionAllowed(true);
        gsetTable.setModel(new GeneSetAnnotation_gene_set_TableModel(gsa));
        gsetTable.revalidate();
    }

    private void jbInit() {

        this.setLayout(new BorderLayout());

        JideSplitPane split = new JideSplitPane(JideSplitPane.HORIZONTAL_SPLIT);
        split.setInitiallyEven(true);

        //JPanel pan = new JPanel(new SCLayout(1));
        taDesc = new JTextArea(10, 60);
        taDesc.setLineWrap(true);
        taDesc.setEditable(false);
        JScrollPane sp1 = new JScrollPane(taDesc);
        sp1.setBorder(BorderFactory.createTitledBorder("Description"));
        //pan.add(sp1);

        //taKeywords = new JTextArea(10, 60);
        //taKeywords.setLineWrap(true);
        //taKeywords.setEditable(false);
        //JScrollPane sp2 = new JScrollPane(taKeywords);
        //sp2.setBorder(BorderFactory.createTitledBorder("Keywords / MeSH"));
        //pan.add(sp2);

        split.add(sp1);

        gsetTable = new SortableTable();
        gsetTable.setColumnSelectionAllowed(true);
        gsetTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        JScrollPane sp = new JScrollPane(gsetTable);
        sp.setBorder(BorderFactory.createTitledBorder("Genes in the set"));
        split.add(sp);

        this.add(split, BorderLayout.CENTER);
    }

} // End class GeneSetPagePanel
