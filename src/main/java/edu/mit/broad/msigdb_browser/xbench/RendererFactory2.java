/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.xbench;

import edu.mit.broad.msigdb_browser.genome.JarResources;
import edu.mit.broad.msigdb_browser.genome.charts.XChart;
import edu.mit.broad.msigdb_browser.genome.objects.PersistentObject;
import edu.mit.broad.msigdb_browser.genome.parsers.DataFormat;
import edu.mit.broad.msigdb_browser.genome.parsers.ParserFactory;

import org.genepattern.browser.uiutil.FTPFile;

import javax.swing.*;

import java.awt.*;
import java.io.File;

/**
 * Collection of renderers for components bearing (all or some) objects
 *
 * @author Aravind Subramanian
 * @version %I%, %G%
 */
public class RendererFactory2 {

    public static final Icon FTP_FILE_ICON = JarResources.getIcon("FTPFile.gif");

    /**
     * For rendering the lists/combo boxes containing known Files and
     * objects.
     * <p/>
     * No events
     *
     * @see CommonLookAndDoubleClickListRenderer
     */
    public static class CommonLookListRenderer extends DefaultListCellRenderer {
        private boolean ifFileOnlyShowName;

        public CommonLookListRenderer(final boolean ifFileOnlyShowName) {
            this.ifFileOnlyShowName = ifFileOnlyShowName;
        }

        public CommonLookListRenderer() {
            this(false); // default is to show the full path
        }

        public Component getListCellRendererComponent(JList list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {

            // doesnt work properly unless called
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            /*
            klog.debug(">>>>>>>>>>>>>>>>>value " + value);
            if (value != null) {
                klog.debug(" class: " + value.getClass());
            }
            */

            // order is important
            if (value instanceof PersistentObject) {
                PersistentObject pob = (PersistentObject) value;

                if (pob.getQuickInfo() != null) {
                    StringBuffer buf = new StringBuffer("<html><body>").append(pob.getName());
                    buf.append("<font color=#666666> [").append(pob.getQuickInfo()).append(']').append("</font></html></body>");
                    this.setText(buf.toString());
                } else {
                    this.setText(pob.getName());
                }

                File f = null;

                if (ParserFactory.getCache().isCached(pob)) {
                    f = ParserFactory.getCache().getSourceFile(pob);
                }

                if (f != null) {
                    this.setToolTipText(f.getAbsolutePath());
                } else {
                    this.setToolTipText("Unknown origins of file: " + f);
                }
            } else if (value instanceof File) {
                if (ifFileOnlyShowName) {
                    this.setText(((File) value).getName());
                } else {
                    this.setText(((File) value).getAbsolutePath());
                }
                this.setIcon(DataFormat.getIcon(value));
                this.setToolTipText(((File) value).getAbsolutePath());
            } else if (value instanceof XChart) {
                this.setText(((XChart) value).getName());
                this.setIcon(XChart.ICON);
            } else if (value instanceof FTPFile) {

                String s = ((FTPFile) value).getPath();
                String slc = s.toLowerCase();
                if (slc.indexOf("c1.") != -1) {
                    s = s + " [Positional]";
                } else if (slc.indexOf("c2.") != -1) {
                    s = s + " [Curated]";
                } else if (slc.indexOf("c3.") != -1) {
                    s = s + " [Motif]";
                } else if (slc.indexOf("c4.") != -1) {
                    s = s + " [Computational]";
                }

                this.setText(s);
                this.setIcon(FTP_FILE_ICON);
            }

            return this;
        }
    }    // End CommonLookListRenderer

}        // End RendererFactory2
