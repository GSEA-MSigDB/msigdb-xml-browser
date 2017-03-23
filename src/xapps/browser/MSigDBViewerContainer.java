/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xapps.browser;

import com.jidesoft.swing.JideTabbedPane;

import edu.mit.broad.msigdb_browser.genome.JarResources;
import edu.mit.broad.msigdb_browser.genome.NamingConventions;
import edu.mit.broad.msigdb_browser.genome.io.FtpResultInputStream;
import edu.mit.broad.msigdb_browser.genome.parsers.ParserFactory;
import edu.mit.broad.msigdb_browser.genome.swing.GuiHelper;
import edu.mit.broad.msigdb_browser.genome.viewers.AbstractViewer;
import edu.mit.broad.msigdb_browser.vdb.msigdb.MSigDB;
import edu.mit.broad.msigdb_browser.xbench.actions.ext.BrowserAction;
import edu.mit.broad.msigdb_browser.xbench.core.api.Application;
import xapps.browser.gsea.GseaWebResources;
import xtools.browser.api.param.FileParam;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.StringTokenizer;

/**
 * @author Aravind Subramanian
 */
public class MSigDBViewerContainer extends AbstractViewer {

    public static final String NAME = "MSigDBBrowser";

    public static final Icon ICON = JarResources.getIcon("msigdb16.png");

    private JideTabbedPane fTabbedPane;

    private FileParam fFileOrURLParam;

    private MSigDB curr_msigdb;

    private JComponent fFiller;

    /**
     * Class constructor
     */
    public MSigDBViewerContainer() {
        super(NAME, ICON, "MSigDB gene sets browser");

        jbInit();
    }

    private void jbInit() {

        this.setLayout(new BorderLayout(15, 15));

        this.fFileOrURLParam = new FileParam("path", "File path or URL to the MSigDB XML database", true);

        this.fFileOrURLParam.setValue_url(GseaWebResources.getMSigDB_current_xml_fileName());


        JButton bLoad = new JButton("Load database", JarResources.getIcon("Run16.png"));
        bLoad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (!fFileOrURLParam.isSpecified()) {
                    Application.getWindowManager().showMessage("No file path or URL specified. First enter one and then try loading");
                } else {
                    boolean firstTime = (curr_msigdb == null);
                    String fileName = fFileOrURLParam.getValue().toString();
                    if (fileName.startsWith("msigdb_v")) {
                        fileName = GseaWebResources.getGseaFTPServerXMLDir() + "/" + fileName;
                    }
                    RetrieveAndParseWorker retrieveAndParseWorker = new RetrieveAndParseWorker(fileName, firstTime);
                    retrieveAndParseWorker.execute();
                }
            }
        });

        JButton bLicense = new JButton("MSigDB License", GuiHelper.ICON_HELP16);
        bLicense.addActionListener(new BrowserAction(GseaWebResources.getGseaBaseURL() + "/" + "license_terms_list.jsp"));

        JPanel buttonPanel = new JPanel(new BorderLayout(10, 5));
        buttonPanel.add(bLicense, BorderLayout.CENTER);
        buttonPanel.add(bLoad, BorderLayout.WEST);
        
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(Color.DARK_GRAY);
        topPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        JLabel label = new JLabel(fFileOrURLParam.getHtmlLabel_v2());
        label.setForeground(Color.WHITE);
        topPanel.add(label, BorderLayout.WEST);

        topPanel.add(fFileOrURLParam.getSelectionComponent().getComponent(), BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        this.setLayout(new BorderLayout(10, 10));
        this.add(topPanel, BorderLayout.NORTH);

        this.fTabbedPane = new JideTabbedPane(JideTabbedPane.TOP);
        this.fTabbedPane.setTabEditingAllowed(true);
        this.fTabbedPane.setShowCloseButton(true);
        this.fTabbedPane.setShowCloseButtonOnTab(true);
        this.fTabbedPane.setShowGripper(true); // just a decorator no action
        this.fTabbedPane.setShowIconsOnTab(true);
        this.fTabbedPane.setShowTabButtons(true);

        this.fFiller = GuiHelper.createWaitingPlaceholder();
        fTabbedPane.addTab("", fFiller);

        this.add(fTabbedPane, BorderLayout.CENTER);

        this.revalidate();
    }

    private static String pathorurl2name(final String path) {
        String name;
        if (NamingConventions.isURL(path)) {
            name = path;
            StringTokenizer tok = new StringTokenizer(name, "/");
            while (tok.hasMoreTokens()) {
                name = tok.nextToken();
            }
        } else {
            File file = new File(path);
            name = file.getName();
        }

        return NamingConventions.removeExtension(name);
    }

    class RetrieveAndParseWorker extends SwingWorker<InputStream, Void> {
        private final String fileName;
        private final boolean firstTime;

        public RetrieveAndParseWorker(String fileName, boolean firstTime) {
            this.fileName = fileName;
            this.firstTime = firstTime;
        }

        protected InputStream doInBackground() throws Exception {
            return ParserFactory.createInputStream(fileName);
        }
        

        @Override
        protected void done() {
            super.done();
            try {
                InputStream msigdbInputStream = get();
                MSigDBParserWorker parserWorker = new MSigDBParserWorker(
                        msigdbInputStream, fileName, firstTime);
                parserWorker.execute();
            }
            catch (Exception e) {
                Application.getWindowManager().showError("Trouble launching Browse MSigDB", e);
            }
        }
    }

    class MSigDBParserWorker extends SwingWorker<MSigDB, Void> {
        private final InputStream msigDbInputStream;
        private final String fileName;
        private final boolean firstTime;
        
        public MSigDBParserWorker(InputStream msigDbInputStream, String fileName, boolean firstTime) {
            this.msigDbInputStream = msigDbInputStream;
            this.fileName = fileName;
            this.firstTime = firstTime;
        }

        protected MSigDB doInBackground() throws Exception {
            ProgressMonitorInputStream progMonIS = new ProgressMonitorInputStream(Application
                    .getWindowManager().getRootFrame(), "Parsing file "
                    + fileName, msigDbInputStream);
            
            return ParserFactory.readMSigDB(fileName, progMonIS, true, true);
        }

        @Override
        protected void done() {
            super.done();
            try {
                curr_msigdb = get();
                MSigDBViewer widget = new MSigDBViewer(curr_msigdb, true, fTabbedPane);
                klog.info("Parsing complete.");
               if (firstTime) {
                    fTabbedPane.remove(fFiller);
                }
            
                fTabbedPane.addTab(pathorurl2name(fileName) + " [version=" + curr_msigdb.getVersion() + " build=" + curr_msigdb.getBuildDate() + "]", widget);
                fTabbedPane.setSelectedComponent(widget);
            }
            catch (Exception e) {
                Application.getWindowManager().showError("Trouble launching Browse MSigDB", e);
           }
            finally {
                // If the inputStream came from an FTP download, clean up the underlying 
                // temp file on disk after processing is complete.
                if (msigDbInputStream instanceof FtpResultInputStream) {
                    File tempFile = ((FtpResultInputStream)msigDbInputStream).getFile();
                    // Try to delete the file.  If that fails, set it to be deleted when the JVM exits.
                    if (!tempFile.delete()) tempFile.deleteOnExit();
                }
            }
        }
    }
}