/*
 * Copyright (c) 2003-2018 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.genome.swing;

import edu.mit.broad.msigdb_browser.genome.Constants;
import edu.mit.broad.msigdb_browser.genome.JarResources;
import gnu.trove.TIntArrayList;

import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;

/**
 * A collection of GUI related utilities and methods to aid UI related classes.
 * <p/>
 * <p/>
 * - central repository of UI related default constants and empty arrays etc.
 *
 * @author Aravind Subramanian
 * @version %I%, %G%
 * @see Ideoms#Helper
 */
public class GuiHelper implements Constants {

    /**
     * STANDARD SET OF ICONS USED
     */
    public static final Icon ICON_ERROR16 = JarResources.getIcon("Error.gif");
    public static final Icon ICON_START16 = JarResources.getIcon("Run16.png");
    public static final Icon ICON_HELP16 = JarResources.getIcon("Help16_v2.gif");

    public static final Icon ICON_ELLIPSIS = JarResources.getIcon("Ellipsis.png");

    // commonly used wondow size
    public static final Dimension DIMENSION_STANDARD_WINDOW = new Dimension(500, 500);
    public static final Font FONT_DEFAULT_BOLD = new Font("Helvetica",
            Font.BOLD, 12);
    public static final Font FONT_DEFAULT = new Font("Helvetica",
            Font.PLAIN, 12);

    public static final Color COLOR_DARK_BROWN = new Color(128, 64, 64);

    // -- private statics --
    private static final Logger klog = Logger.getLogger(GuiHelper.class);
    private static final Dimension kPlaceholderSize = new Dimension(200, 50);

    /**
     * Privatized class constructor.
     */
    private GuiHelper() {
    }

    /**
     * call after setting frames size
     *
     * @param comp
     */
    public static void centerComponent(Component comp) {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size = comp.getSize();

        comp.setLocation((screenSize.width - size.width) / 2,
                (screenSize.height - size.height) / 2);
    }

    public static JPanel createPlaceholderPanel(final Dimension prefsize, final TextIconPair pair) {
        return _createPanel(prefsize, pair.text, pair.icon);
    }

    public static JPanel createWaitingPlaceholder() {
        return createPlaceholderPanel(kPlaceholderSize, TextIconPair.WAITING_FOR_TASK);
    }

    private static JPanel _createPanel(final Dimension prefsize, final String text,
                                       final Icon icon) {

        JPanel panel = new JPanel();

        panel.setPreferredSize(prefsize);

        JLabel label = new JLabel(text);

        label.setSize(prefsize);
        label.setIcon(icon);

        //label.setIconTextGap(label.getIconTextGap() + 20);
        //label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(label);

        //this.setLayout(new BorderLayout());
        //this.add(label, BorderLayout.CENTER);
        panel.setBackground(Color.white);

        return panel;
    }

    /**
     * Table related helper methods
     */
    public static class Table {

        public static void setColumnSize(int size, int col, JTable table, boolean alsoMax) {
            // the order of set calls is apparently important
            TableColumn column = table.getColumnModel().getColumn(col);

            column.setMinWidth(0);

            if (alsoMax) {
                column.setMaxWidth(size);
            }
            column.setPreferredWidth(size);
        }
    }    // End GuiHelper.Table


    public static class List2 {

        public static void setSelected(final Object[] selected_vals, final JList jlist, final DefaultListModel listModel) {

            //Print.outl(listModel);
            if (selected_vals == null) {
                klog.error("Null arg for selected selected_vals");
                return;
            }

            TIntArrayList indices = new TIntArrayList();

            for (int i = 0; i < selected_vals.length; i++) {

                int index = listModel.indexOf(selected_vals[i]);
                if (index != -1) {
                    indices.add(index);
                }
                //log.debug("Checking for: " + selected_vals[i] + " class: " + selected_vals[i].getClass() + " found index: " + index);
            }

            jlist.setSelectedIndices(indices.toNativeArray());
            //log.debug(" >>> " + jlist.getSelectionMode() + " " + ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            //log.debug(">>> # selected: " + jlist.getSelectedIndices().length);
        }


    } // End class List
}        // End GuiHelper
