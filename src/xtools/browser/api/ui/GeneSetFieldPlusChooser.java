/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xtools.browser.api.ui;

import edu.mit.broad.msigdb_browser.genome.swing.GuiHelper;
import edu.mit.broad.msigdb_browser.genome.swing.fields.GFieldPlusChooser;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Aravind Subramanian
 * @version %I%, %G%
 */
public class GeneSetFieldPlusChooser extends JPanel implements GFieldPlusChooser {
    private JTextField tfEntry = new JTextField(40);

    private JButton bEntry = new JButton(GuiHelper.ICON_ELLIPSIS);

    private ChooserWindow fWindow;

    // needed as otherwise a default one is added and then again one another one is added
    // if the setCustomActionListener is called
    /**
     * Class constructor
     */
    public GeneSetFieldPlusChooser(boolean addDefaultActionListener, int selMode) {

        GeneSetTabbedChooserPanel chooser = new GeneSetTabbedChooserPanel(selMode);
        this.fWindow = new ChooserWindow(chooser);

        if (addDefaultActionListener) {
            init();
        } else {
            jbInit();
        }
    }

    public void setCustomActionListener(ActionListener customActionListener) {
        bEntry.addActionListener(customActionListener);
    }

    private void init() {

        jbInit();
        bEntry.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object[] sels = fWindow.show();
                format(sels);
            }
        });
    }

    public Object[] showMeDirectly() {
        Object[] sels = fWindow.show();
        //format(sels);
        return sels;
    }

    private void jbInit() {

        this.setLayout(new BorderLayout());
        tfEntry.setEditable(true);
        this.add(tfEntry, BorderLayout.CENTER);
        this.add(bEntry, BorderLayout.EAST);
    }

    private void format(final Object[] sels) {

        if (sels == null) {
            tfEntry.setText("");
            return;
        }

        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < sels.length; i++) {
            if (sels[i] == null) {
                continue;
            }

            buf.append(sels[i].toString().trim());

            if (i != sels.length - 1) {
                buf.append(',');
            }
        }

        tfEntry.setText(buf.toString());
    }

    public String getText() {
        return tfEntry.getText();
    }

    public void setText(String text) {
        tfEntry.setText(text);
    }

    /**
     * so that the tf can have its events listened to
     *
     * @return
     */
    public JTextField getTextField() {
        return tfEntry;
    }

    public Object getValue() {
        return getText();
    }

    public JComponent getComponent() {
        return this;
    }

    public void setValue(Object obj) {
        if (obj == null) {
            this.setText(null);
        } else {
            this.setText(obj.toString());
        }
    }


}    // End GOptionsFieldPlusChooser

