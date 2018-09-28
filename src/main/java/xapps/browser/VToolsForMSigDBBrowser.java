/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xapps.browser;

import edu.mit.broad.msigdb_browser.genome.JarResources;
import edu.mit.broad.msigdb_browser.genome.NamingConventions;
import edu.mit.broad.msigdb_browser.genome.objects.*;
import edu.mit.broad.msigdb_browser.genome.parsers.ParserFactory;
import edu.mit.broad.msigdb_browser.genome.viewers.Viewer;
import edu.mit.broad.msigdb_browser.vdb.VdbRuntimeResources;
import edu.mit.broad.msigdb_browser.vdb.map.Chip2ChipMapper;
import edu.mit.broad.msigdb_browser.vdb.map.MGeneSetMatrix;
import edu.mit.broad.msigdb_browser.vdb.msigdb.DeconAccessions;
import edu.mit.broad.msigdb_browser.vdb.msigdb.GeneSetAnnotation;
import edu.mit.broad.msigdb_browser.vdb.msigdb.MSigDB;
import edu.mit.broad.msigdb_browser.vdb.msigdb.MSigDBImpl;
import edu.mit.broad.msigdb_browser.xbench.core.api.Application;
import xapps.browser.api.vtools.AbstractVToolWithParams;
import xapps.browser.api.vtools.Hooks;
import xapps.browser.gsea.GeneSetAnnotationWithOverlaps;
import xtools.browser.api.param.*;

import javax.swing.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Aravind Subramanian
 */
public class VToolsForMSigDBBrowser {

    public static class ExportGeneSets extends AbstractVToolWithParams {

        private GeneSetMatrixFormatParam fGmFormatParam;

        private ChipChooserMultiParam fChipsTargetParam;

        private ViewerHook hook;

        private SelectedOrAllParam fOnlySelectedFieldsParam;


        /**
         * Class constructor
         *
         * @param msigdb
         * @param selectedGeneSets
         */
        public ExportGeneSets(final ViewerHook hook) {
            super("Export gene sets", new MyBaseHook("export_"));
            this.hook = hook;
        }

        public Viewer execute() throws Exception {

            GeneSetMatrix gm;

            final String[] selNames = hook.getSelectedGeneSetNames();
            if (fOnlySelectedFieldsParam.isOnlySelected() && selNames != null && selNames.length > 0) {
                gm = hook.getMSigDB().createGeneSetMatrix(selNames);
            } else {
                gm = hook.getMSigDB().createGeneSetMatrix(hook.getAllDisplayedGeneSetNames());
            }

            final Chip2ChipMapper mapper = VdbRuntimeResources.getGeneSymbolMapper(fChipsTargetParam.getChipCombo());

            final MGeneSetMatrix mgm = mapper.map(gm);

            String newName = getSaveName() + "." + mapper.getTargetChip().getName();
            final GeneSetMatrix mapped_gm = mgm.getMappedGeneSetMatrix("mapped_").cloneShallow(newName);

            final File gmFile = kReport.savePageInvisibly2Cache(mapped_gm, fGmFormatParam);

            // @note also register as gene sets
            ParserFactory.read(gmFile, false);

            Application.getWindowManager().showMessage("Saved GeneSetMatrix at: " + gmFile.getPath());

            return null;
        }

        public void declareParams() {
            // i dont think we want verbose options here
            // lets just go form gene symbol to whatever (via gene symbols)

            this.fGmFormatParam = new GeneSetMatrixFormatParam();
            this.fChipsTargetParam = new ChipChooserMultiParam("chip_target", "Target chip(s)", 
                    "The destination chip - to which orthology/homology mappings are converted to", true);

            fParamSet.addParam(fChipsTargetParam);
            fParamSet.addParam(fGmFormatParam);

            fOnlySelectedFieldsParam = new SelectedOrAllParam();
            fParamSet.addParam(fOnlySelectedFieldsParam);
        }

        public String getTitle() {
            return "Export selected gene sets";
        }

        public String getHelpURL() {
            return JarResources.getHelpURL("xapps_mbrowser_ExportGeneSets"); // override as default too long for heidi
        }

    } // End class ExportGeneSets


    /**
     * @author Aravind Subramanian
     */
    public static class SearchByGene extends AbstractVToolWithParams {

        private JTabbedPane viewer_window_opt;

        private MSigDB msigdb;

        private StringInputParam fGeneNameParam;

        public SearchByGene(final MSigDB msigdb, final JTabbedPane viewer_window_opt) {
            super("Search by gene", new MyBaseHook("gene_"));
            this.viewer_window_opt = viewer_window_opt;
            this.msigdb = msigdb;
        }

        public Viewer execute() throws Exception {

            String geneName = fGeneNameParam.getString().toUpperCase(); // @note

            if (!VdbRuntimeResources.getChip_Seq_Accession().isProbe(geneName)) {
                Application.getWindowManager().showMessage("This is not a valid gene symbol (or known alias): " + geneName);
                return null;
            } else {
                DeconAccessions.Lookup lp = DeconAccessions.creatLookup(geneName);
                geneName = lp.getSymbol();
            }

            List hits = new ArrayList();
            for (int i = 0; i < msigdb.getNumGeneSets(); i++) {
                GeneSetAnnotation ga = msigdb.getGeneSetAnnotation(i);
                if (ga.getGeneSet(false).isMember(geneName)) { // @todo make this case IN sensitive
                    hits.add(ga);
                }
            }

            klog.debug("# of hits: " + hits.size());

            final MSigDB hits_msigdb = new MSigDBImpl(geneName, msigdb.getVersion(), msigdb.getBuildDate(),
                    (GeneSetAnnotation[]) hits.toArray(new GeneSetAnnotation[hits.size()]));

            final MSigDBViewer widget = new MSigDBViewer(hits_msigdb, false);

            return _openIt("# of hits: " + hits.size(), "Gene search: " + geneName, widget, viewer_window_opt);
        }

        public void declareParams() {
            this.fGeneNameParam = new StringInputParam("gene", "Gene (only symbols, case INsensitive)", "Gene name", null, true);
            fParamSet.addParam(fGeneNameParam);
        }

        public String getTitle() {
            return "Search for gene sets that contain the specified gene";
        }

        public String getHelpURL() {
            return JarResources.getHelpURL("xapps_mbrowser_SearchByGene"); // override as default too long for heidi
        }

    } // End class SearchByGene


    /**
     * @author Aravind Subramanian
     */
    public static class SearchByGeneSet extends AbstractVToolWithParams {

        private JTabbedPane viewer_window_opt;

        private MSigDB msigdb;

        private GeneSetMultiChooserParam fGeneSetParam;


        public SearchByGeneSet(final MSigDB msigdb,
                               final JTabbedPane viewer_window_opt) {
            super("Search by gene set", new MyBaseHook("geneset_"));
            this.viewer_window_opt = viewer_window_opt;
            this.msigdb = msigdb;

        }

        public int findInt(Collection set, GeneSet gset) {
            Iterator e = set.iterator();
            int cnt = 0;
            while (e.hasNext()) {
                if (gset.isMember(e.next().toString())) {
                    cnt++;
                }
            }
            return cnt;
        }

        public Viewer execute() throws Exception {

            final GeneSet gset = fGeneSetParam.getGeneSetCombo();
            final List hits = new ArrayList();

            for (int i = 0; i < msigdb.getNumGeneSets(); i++) {
                final GeneSetAnnotation gsa = msigdb.getGeneSetAnnotation(i);
                final FSet query = (FSet) gsa.getGeneSet(false);
                int inter = findInt(query.getMembers_quick(), gset);
                if (inter > 0) {
                    hits.add(new GeneSetAnnotationWithOverlaps.One(gsa, inter, query.getNumMembers()));
                }

                if (i % 250 == 0) {
                    System.out.println("Done searching " + (i + 1) + " / " + msigdb.getNumGeneSets());
                }
            }

            klog.debug("# of hits: " + hits.size());

            GeneSetAnnotationWithOverlaps.One[] inters = (GeneSetAnnotationWithOverlaps.One[]) hits.toArray(new GeneSetAnnotationWithOverlaps.One[hits.size()]);

            GeneSetAnnotationWithOverlaps gso = new GeneSetAnnotationWithOverlaps(gset, inters);

            return _openIt("# of hits: " + hits.size(),
                    "Gene set search: " + NamingConventions.removeExtension(gset.getName(true)) + "( " + gset.getNumMembers() + ")",
                    gso.createViewer(msigdb), viewer_window_opt);
        }

        public void declareParams() {
            this.fGeneSetParam = new GeneSetMultiChooserParam("gset", "Gene set (ids must be symbols)", "Gene set");
            fParamSet.addParam(fGeneSetParam);
        }

        public String getTitle() {
            return "Search for overlapping gene sets";
        }

        public String getHelpURL() {
            return JarResources.getHelpURL("xapps_mbrowser_SearchByGeneSet"); // override as default too long for heidi
        }
    } // End class SearchByGeneSet

    public static interface ViewerHook {

        public String[] getSelectedGeneSetNames();

        public String[] getAllDisplayedGeneSetNames();

        public int getNumSelectedRows();

        public MSigDB getMSigDB();

    } // End inner class ViewerHook


    static class MyBaseHook implements Hooks.BaseHook {

        private JTextField tfName;

        private String fSaveNameHint;

        MyBaseHook(final String save_name_hint_prefix) {
            this.fSaveNameHint = save_name_hint_prefix;
        }

        // @note need to change this to save name
        public String getSaveNameHint() {
            return fSaveNameHint;
        }

        public String getComponentMessage() {
            return null;
        }

        public JTextField getTextField() {

            if (tfName == null) {
                tfName = new JTextField(60);

                tfName.setBorder(BorderFactory.createTitledBorder("Enter a short name for the result"));
            }

            String sn = getSaveNameHint();
            if (sn != null && sn.length() > 0) {
                tfName.setText(sn);
            }

            return tfName;
        }

        public String getSaveName() { // dont make safe - give back whatever user asks for
            if (tfName != null) {
                return tfName.getText();
            } else {
                return "na";
            }
        }

    } // End inner class MyBaseHook

    private static Viewer _openIt(final String msg,
                                  final String tabName,
                                  final Viewer v,
                                  final JTabbedPane viewer_window_opt) {

        Application.getWindowManager().showMessage(msg + ". Results are displayed in a new tab");

        if (viewer_window_opt != null) {
            viewer_window_opt.addTab(tabName, (JComponent) v);
            viewer_window_opt.setSelectedComponent((JComponent) v);
            return null;
        } else {
            return v;
        }

    }

} // End class VToolsForMSigDBBrowser
