/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xapps.browser.api.vtools;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import edu.mit.broad.msigdb_browser.genome.swing.fields.GDirFieldPlusChooser;
import edu.mit.broad.msigdb_browser.genome.swing.fields.GFieldPlusChooser;
import xtools.browser.api.param.Param;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Displays a few params in a jgoodies form
 * No fluff - meant to be displayed in a dilaog window
 * NO basic - advanced concept -- all are shown.
 */
public class ParamSetFormForAFew {

    public static final Color LIGHT_GREEN = Color.decode("#EAFFEA");

    public static final int DEFAULT_INITIAL_DELAY = ToolTipManager.sharedInstance().getInitialDelay();

    public static JPanel createParamsPanel(final Param[] params) {

        StringBuffer colStr = _createColStr();
        StringBuffer rowStr = _createRowStr(params);

        PanelBuilder builder = createPanelBuilder(colStr, rowStr);
        CellConstraints cc = new CellConstraints();

        int rowcnt = 3;
        for (int i = 0; i < params.length; i++) {
            GFieldPlusChooser chooser = params[i].getSelectionComponent();

            if (params[i].isFileBased()) {
                if (chooser instanceof GDirFieldPlusChooser) {
                    (((GDirFieldPlusChooser) chooser)).getTextField().setBackground(LIGHT_GREEN);
                }
            }

            JLabel label = new JLabel(params[i].getHtmlLabel_v3());

            enableToolTips(label, params[i]);
            builder.add(label, cc.xy(1, rowcnt));
            builder.add(chooser.getComponent(), cc.xy(3, rowcnt));
            // builder.add(new ParamDescButton(i, this, fParamSet).getButton(), cc.xy(5, rowcnt));
            //builder.add(new JButton("?"), cc.xy(5, rowcnt));
            rowcnt += 2; // because the spaces also count as a row
        }

        return builder.getPanel();
    }

    public static void enableToolTips(final JLabel label, final Param param) {

        label.setToolTipText(param.getDesc());
        label.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                //System.out.println("entered");
                //Application.setCursor(kAppHandCursor);
                // Show tool tips immediately
                ToolTipManager.sharedInstance().setInitialDelay(0);
            }

            public void mouseExited(MouseEvent e) {
                //System.out.println("exited");
                //Application.setCursor(kAppReadyCursor);
                ToolTipManager.sharedInstance().setInitialDelay(DEFAULT_INITIAL_DELAY);
            }

            //table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // Show tool tips immediately
                ToolTipManager.sharedInstance().setInitialDelay(0);
            }

        });


    }

    private static StringBuffer _createColStr() {
        /* with a desc button at the end
              StringBuffer colStr = new StringBuffer("pref,        4dlu,           pref,        25dlu,       min"); // columns
              //                                     // 1(label)    2 (spacer)     3(field)     4(spacer)    5(? button)

              */

        return new StringBuffer("120dlu,      4dlu,        150dlu,   4dlu,  40dlu"); // columns
        //                                     // 1(label)    2 (spacer)   3(field)
    }

    private static StringBuffer _createRowStr(final Param[] params) {
        StringBuffer rowStr = new StringBuffer();
        rowStr.append("pref, 5dlu,"); // for the spacer
        for (int i = 0; i < params.length; i++) {
            rowStr.append("pref, 3dlu");
            if (params.length != i - 1) {
                rowStr.append(",");
            }
        }
        return rowStr;
    }

    public static PanelBuilder createPanelBuilder(StringBuffer colStr, StringBuffer rowStr) {
        FormLayout layout = new FormLayout(colStr.toString(), rowStr.toString());
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        return builder;
    }

} // End class ParamSetForm

