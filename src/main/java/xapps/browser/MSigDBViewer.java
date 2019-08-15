/*
 * Copyright (c) 2003-2019 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xapps.browser;

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.grid.*;
import com.jidesoft.swing.*;

import edu.mit.broad.msigdb_browser.genome.JarResources;
import edu.mit.broad.msigdb_browser.genome.models.MSigDBModel;
import edu.mit.broad.msigdb_browser.genome.swing.GPopupChecker;
import edu.mit.broad.msigdb_browser.genome.swing.GuiHelper;
import edu.mit.broad.msigdb_browser.genome.viewers.AbstractViewer;
import edu.mit.broad.msigdb_browser.vdb.msigdb.GeneSetAnnotation;
import edu.mit.broad.msigdb_browser.vdb.msigdb.MSigDB;
import edu.mit.broad.msigdb_browser.xbench.actions.ext.BrowserAction;
import edu.mit.broad.msigdb_browser.xbench.core.api.Application;
import xapps.browser.gsea.GseaWebResources;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Aravind Subramanian
 */
public class MSigDBViewer extends AbstractViewer {

    public static final String NAME = "MSigDBViewer";

    public static final Icon ICON = JarResources.getIcon("icon_16x16.png");

    private MSigDBViewer fInstance = this;

    private JideTabbedPane fTabbedPane_opt;

    private MSigDB fMSigDB;

    private boolean showDeepSearchOptions;

    private JLabel fStatusLabel;

    private TableModel curr_displayed_table_model; // @note the none after sorting etc (not the original one)

    private QuickTableFilterField quickFilterTable;

    private JTable sortableTable;

    private MSigDBModel msigDbTableModel;

    /**
     * Class constructor
     *
     * @param msigdb
     * @param showDeepSearchOptions
     */
    public MSigDBViewer(final MSigDB msigdb, final boolean showDeepSearchOptions) {
        this(msigdb, showDeepSearchOptions, null);
    }

    /**
     * Class constructor
     *
     * @param msigdb
     * @param showDeepSearchOptions
     * @param tp
     */
    public MSigDBViewer(final MSigDB msigdb, final boolean showDeepSearchOptions, final JideTabbedPane tp) {
        super(NAME, ICON, "Gene sets browser [MSigDB]");
        this.fMSigDB = msigdb;
        this.showDeepSearchOptions = showDeepSearchOptions;
        this.fTabbedPane_opt = tp;
        jbInit();
    }

    public String[] getSelectedGeneSetNames() {
        JTable table = getDisplayedTable();
        final int[] rows = getDisplayedTable().getSelectedRows();
        final String[] names = new String[rows.length];
        for (int i = 0; i < rows.length; i++) {
            //int actual = quickFilterTable.getDisplayTableModel().getActualRowAt(rows[i]);
            //names[i] = sortableTable.getModel().getValueAt(rows[i], 1).toString();
            names[i] = table.getModel().getValueAt(rows[i], 1).toString();
        }

        return names;
    }

    public String[] getDisplayedGeneSetNames() {
        JTable table = getDisplayedTable();
        final String[] names = new String[getDisplayedTable().getRowCount()];
        for (int i = 0; i < getDisplayedTable().getRowCount(); i++) {
            //int actual = quickFilterTable.getDisplayTableModel().getActualRowAt(rows[i]);
            //names[i] = sortableTable.getModel().getValueAt(rows[i], 1).toString();
            names[i] = table.getModel().getValueAt(i, 1).toString();
        }

        return names;
    }

    public int getNumSelectedRows() {
        return getDisplayedTable().getSelectedRows().length;
    }

    public GeneSetAnnotation getSelectedGeneSetAnnotation() {
        final String[] selNames = getSelectedGeneSetNames();
        if (selNames.length > 0) {
            return fMSigDB.getGeneSetAnnotation(selNames[0]);
        } else {
            return null;
        }
    }

    private void jbInit() {

        this.setLayout(new BorderLayout(15, 15));

        this.add(createViewAndSearchComponent(), BorderLayout.CENTER);
        this.add(MGUIUtils.createExportSetsControlPanel(new MyViewerHook(),
                new MyViewerHook()), BorderLayout.SOUTH);

        this.revalidate();
    }

    private JPanel createDeepSearchPanel() {

        ButtonPanel bp = new ButtonPanel();

        bp.setBorder(BorderFactory.createTitledBorder("Deep search options"));

        /*
        JButton bKeywordSearch = new JButton("Search by MeSH ...");
        bp.addButton(bKeywordSearch);
        bKeywordSearch.addActionListener(new VToolsForMSigDBBrowser.SearchByMesh(fMSigDB, fTabbedPane_opt));
        */

        /*
        JButton bPhraseSearch = new JButton("Search in abstract ...");
        bp.addButton(bPhraseSearch);
        bPhraseSearch.addActionListener(new VToolsForMSigDBBrowser.SearchByDescription(fMSigDB, fTabbedPane_opt));
        */

        JButton bGeneSetSearch = new JButton("Find sets that overlap with my set ...");
        bp.addButton(bGeneSetSearch);
        bGeneSetSearch.addActionListener(new VToolsForMSigDBBrowser.SearchByGeneSet(fMSigDB, fTabbedPane_opt));

        JButton bGeneSearch = new JButton("Find sets that contain this gene ...");
        bp.addButton(bGeneSearch);
        bGeneSearch.addActionListener(new VToolsForMSigDBBrowser.SearchByGene(fMSigDB, fTabbedPane_opt));

        return bp;
    }

    private JTable getOrigTable() {
        return sortableTable;
    }

    private JTable getDisplayedTable() {
        return quickFilterTable.getTable();
    }

    private Component createViewAndSearchComponent() {

        this.msigDbTableModel = new MSigDBModel(fMSigDB);

        // @note IMP these indices must synch with the tablemodels
        final QuickFilterPane quickFilterPane = new QuickFilterPane(new SortableTableModel(msigDbTableModel), new int[]{4, 5, 6, 7});


        this.quickFilterTable = new QuickTableFilterField(quickFilterPane.getDisplayTableModel(),
                new int[]{1, 4});  // name and desc


        JPanel quickSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        quickSearchPanel.add(quickFilterTable);
        quickSearchPanel.setBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH),
                "", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP));

        JideSplitPane pane = new JideSplitPane(JideSplitPane.VERTICAL_SPLIT);

        quickFilterPane.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "QuickFilterPane", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(6, 0, 0, 0)));
        pane.addPane(quickFilterPane);

        JPanel tablePanel = new JPanel(new BorderLayout(2, 2));
        tablePanel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "Filtered result List (right click for options)", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        fStatusLabel = new JLabel(quickFilterTable.getDisplayTableModel().getRowCount() + " out of " + msigDbTableModel.getRowCount() + " gene sets");
        fStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        fStatusLabel.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        fStatusLabel.setForeground(GuiHelper.COLOR_DARK_BROWN);

        this.sortableTable = new SortableTable(quickFilterTable.getDisplayTableModel());
        this.sortableTable.getTableHeader().setPreferredSize(new Dimension(100, 20));

        DefaultTableCellRenderer tcrColumn = new DefaultTableCellRenderer();
        tcrColumn.setHorizontalAlignment(JTextField.LEFT);
        sortableTable.getColumnModel().getColumn(3).setCellRenderer(tcrColumn);


        final MyPopup popup = new MyPopup();
        sortableTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                popup.maybeShowPopup(e);
            }

            public void mousePressed(MouseEvent e) {
                popup.maybeShowPopup(e);
            }

            public void mouseReleased(MouseEvent e) {
                popup.maybeShowPopup(e);
            }

        });

        ListSelectionModel lsm = sortableTable.getSelectionModel();
        lsm.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); // @note allow many ranked lists to be selected

        lsm.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                //Ignore extra messages.
                if (e.getValueIsAdjusting()) {
                }

                // updateSelectedLists();
            }
        });

        //sortableTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        quickFilterTable.setTable(sortableTable);
        JScrollPane scrollPane = new JScrollPane(sortableTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        curr_displayed_table_model = msigDbTableModel;
        quickFilterTable.getDisplayTableModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getSource() instanceof FilterableTableModel) {
                    curr_displayed_table_model = ((TableModel) e.getSource());
                    int count = curr_displayed_table_model.getRowCount();
                    fStatusLabel.setText(count + " out of " + msigDbTableModel.getRowCount() + " gene sets ");
                }
            }
        });

        sortableTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int sel_count = sortableTable.getSelectedRowCount();
                int count = curr_displayed_table_model.getRowCount();
                fStatusLabel.setText(count + " out of " + msigDbTableModel.getRowCount() + " gene sets " + " [ " + sel_count + " selected ]");
            }
        });

        tablePanel.add(fStatusLabel, BorderLayout.BEFORE_FIRST_LINE);
        tablePanel.add(scrollPane);

        if (showDeepSearchOptions) {
            pane.add(createDeepSearchPanel());
            //quickSearchPanel.add(Box.createHorizontalStrut(100));
            //quickSearchPanel.add(createDeepSearchPanel()); // @note
        }

        pane.addPane(tablePanel);

        JPanel panel = new JPanel(new BorderLayout(3, 3));
        panel.add(quickSearchPanel, BorderLayout.BEFORE_FIRST_LINE);
        panel.add(pane);

        return panel;
    }

    public JMenuBar getJMenuBar() {
        return EMPTY_MENU_BAR;
    }

    // -------------------------------------------------------------------------------------------- //

    class MyPopup extends GPopupChecker {

        private JPopupMenu fPopup;
        private OpenPubMedIdAction pubmedidAction;

        private QuickViewAction quickViewAction;

        private CopyGeneSetMembersIntoClipboardAction copyGeneSetAction;

        private OpenGeneSetPageAction openGeneSetPageAction;

        MyPopup() {

            this.fPopup = new JPopupMenu("For the selected set ...");

            fPopup.setInvoker(getOrigTable());

            this.pubmedidAction = new OpenPubMedIdAction();
            this.copyGeneSetAction = new CopyGeneSetMembersIntoClipboardAction();
            this.quickViewAction = new QuickViewAction();
            this.openGeneSetPageAction = new OpenGeneSetPageAction();

            fPopup.add(quickViewAction);
            fPopup.addSeparator();
            fPopup.add(openGeneSetPageAction);
            fPopup.add(pubmedidAction);
            fPopup.addSeparator();
            fPopup.add(copyGeneSetAction);
            fPopup.add(new CopyGeneSetNamesAction());
        }

        void show(final MouseEvent e) {

            if (getDisplayedTable().getSelectedRowCount() == 1) {

                GeneSetAnnotation gsa = getSelectedGeneSetAnnotation();

                if (gsa == null) {
                    return;
                }

                if (gsa.getExternalLinks() == null || gsa.getExternalLinks().getPMID() == null || gsa.getExternalLinks().getPMID().length() == 0) {
                    pubmedidAction.setEnabled(false);
                } else {
                    pubmedidAction.setEnabled(true);
                }
            } else {

            }

            if (getDisplayedTable().getSelectedRows().length > 1) {
                quickViewAction.setEnabled(false);
                openGeneSetPageAction.setEnabled(false);
                pubmedidAction.setEnabled(false);
                copyGeneSetAction.setEnabled(false);
            } else {
                quickViewAction.setEnabled(true);
                openGeneSetPageAction.setEnabled(true);
                copyGeneSetAction.setEnabled(true);
            }

            fPopup.show(e.getComponent(), e.getX(), e.getY());
        }

        protected void maybeShowPopup(MouseEvent e) {

            if (e.isPopupTrigger() || e.getClickCount() == 2) {
                show(e);
            }

        }


    } // End internal class MyPopup


    class QuickViewAction extends AbstractAction {

        GeneSetPagePanel panel;

        QuickViewAction() {
            super("Annotation QuickView", JarResources.getIcon("QuickView.gif"));
            panel = new GeneSetPagePanel();
        }

        public void actionPerformed(ActionEvent e) {
            GeneSetAnnotation gsa = getSelectedGeneSetAnnotation();

            if (gsa == null) {
                return;
            }

            log.debug("setting: " + gsa.getStandardName());

            panel.setData(gsa);

            JButton but = new JButton(new OpenGeneSetPageAction());
            but.setText("View full GeneSetPage");

            Application.getWindowManager().showMessage("QuickView: " + gsa.getStandardName() + " [" +
                    gsa.getGeneSet(false).getNumMembers() + " genes]", panel, new JButton[]{but}, true);
        }
    }

    class CopyGeneSetMembersIntoClipboardAction extends AbstractAction {

        CopyGeneSetMembersIntoClipboardAction() {
            super("Copy gene set members into clipboard", JarResources.getIcon("QuickView.gif"));
        }

        public void actionPerformed(ActionEvent e) {
            GeneSetAnnotation gsa = getSelectedGeneSetAnnotation();

            if (gsa == null) {
                return;
            }

            log.debug("setting: " + gsa.getStandardName());

            String[] names = gsa.getGeneSet(false).getMembersArray();

            final StringBuffer buf = new StringBuffer();
            for (int i = 0; i < names.length; i++) {
                buf.append(names[i]).append('\n');
            }

            final StringSelection stsel = new StringSelection(buf.toString());
            final Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
            clip.setContents(stsel, stsel);
        }
    }

    class OpenGeneSetPageAction extends AbstractAction {

        OpenGeneSetPageAction() {
            super("Full GeneSetPages at Broad website", JarResources.getIcon("ViewGeneSetPages.gif"));
        }

        public void actionPerformed(ActionEvent e) {
            GeneSetAnnotation gsa = getSelectedGeneSetAnnotation();
            new BrowserAction(GseaWebResources.getGeneSetURL(gsa.getStandardName())).actionPerformed(e);
        }

    }

    class CopyGeneSetNamesAction extends AbstractAction {

        CopyGeneSetNamesAction() {
            super("Copy gene set names into the clipboard", JarResources.getIcon("Copy16_b.gif"));
        }

        public void actionPerformed(final ActionEvent e) {

            final String[] gsetNames = getSelectedGeneSetNames();
            final StringBuffer buf = new StringBuffer();
            for (int i = 0; i < gsetNames.length; i++) {
                buf.append(gsetNames[i]).append('\n');
            }

            final StringSelection stsel = new StringSelection(buf.toString());
            final Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();

            clip.setContents(stsel, stsel);
        }

    }

    class OpenPubMedIdAction extends AbstractAction {

        OpenPubMedIdAction() {
            super("Open Entrez page for this PubMed ID", JarResources.getIcon("ViewPubMedPage.gif"));
        }

        public void actionPerformed(ActionEvent e) {
            GeneSetAnnotation gsa = getSelectedGeneSetAnnotation();
            new BrowserAction(GseaWebResources.getPubMedURL(gsa.getExternalLinks().getPMID())).actionPerformed(e);
        }

    }


    class MyViewerHook implements VToolsForMSigDBBrowser.ViewerHook {
        public MyViewerHook() {
        }

        public String[] getSelectedGeneSetNames() {
            return fInstance.getSelectedGeneSetNames();
        }

        public int getNumSelectedRows() {
            return fInstance.getNumSelectedRows();
        }

        public MSigDB getMSigDB() {
            return fMSigDB;
        }

        public String[] getAllDisplayedGeneSetNames() {
            return fInstance.getDisplayedGeneSetNames();
        }

    } // End class MyViewerHook


} // End class MSigDBBrowserWidget

/*
JButton bHtml = new JButton("Create HTML reports");
bHtml.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        try {
            Worker.post(new Task() {
                public Object run() throws Exception {
                    final String[] results = getSelectedResultNames();
                    final MakeGseaHTMLReports tool = new MakeGseaHTMLReports();
                    tool.fGseaResultDirParam.setValue(curr_gseaResultDir);
                    tool.fGeneSetNamesParam.setValue(results);
                    TaskManager.getInstance().run(tool, tool.getParamSet(), Thread.MIN_PRIORITY);
                    return null;
                }
            });
        } catch (Throwable t) {
            Application.getWindowManager().showError("Trouble launching make reports", t);
        }
    }
});

bp.add(bHtml);
*/

/*
    private JPanel createControlPanel() {

        //ButtonPanel bp = new ButtonPanel();
        JPanel bp = new JPanel(new BorderLayout());
        bp.setBorder(BorderFactory.createEmptyBorder(5, 10, 20, 10));
        //bp.setBorder(BorderFactory.createTitledBorder("For the selected gene sets: "));

        JButton bHelp = JarResources.createHelpButton("msigdb_browser");
        bHelp.setPreferredSize(new Dimension(60, 30));
        bp.add(bHelp, BorderLayout.WEST);
        //bp.addButton(bHelp, ButtonNames.HELP);

        //bp.add(new JLabel("For selected gene sets: "));

        //JPanel centerPanel = new JPanel(new SRLayout(2, SRLayout.CENTER, SRLayout.CENTER, 1));
        JPanel centerPanel = new JPanel();


        ButtonPanel selectedPanel = new ButtonPanel();
        selectedPanel.setBorder(BorderFactory.createTitledBorder("For selected gene sets"));
        JButton bfindOverlaps = new JButton("Display overlaps");
        selectedPanel.addButton(bfindOverlaps);
        bfindOverlaps.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


            }
        });

        JButton bExport = new JButton("Export as GeneSetMatrix");
        selectedPanel.addButton(bExport);
        centerPanel.add(selectedPanel);


        centerPanel.add(deepSearchPanel);

        bp.add(centerPanel, BorderLayout.CENTER);

        return bp;
    }
    */

