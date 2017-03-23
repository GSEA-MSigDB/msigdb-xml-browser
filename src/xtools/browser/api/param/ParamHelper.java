/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xtools.browser.api.param;

import edu.mit.broad.msigdb_browser.genome.swing.fields.GComboBoxField;
import edu.mit.broad.msigdb_browser.genome.swing.fields.GFieldUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.event.ActionListener;

/**
 * Bunch of static methods that help concrete Params do their thing.
 *
 * @author Aravind Subramanian
 * @version %I%, %G%
 */
public class ParamHelper {

    /**
     * Privatized Class constructor
     */
    private ParamHelper() {
    }

    /**
     * @param param
     * @return
     */
    /*
    protected static GOptionsFieldPlusChooser createOptionChooser(Param param) {

        GOptionsFieldPlusChooser ocf = new GOptionsFieldPlusChooser(param.getHints(), Application.getWindowManager().getRootFrame());
        Object def = param.getDefault();

        if (param.getValue() != null) {
            StringBuffer buf = new StringBuffer();
            Object val = param.getValue();

            if (val instanceof Object[]) {
                for (int i = 0; i < ((Object[]) val).length; i++) {
                    buf.append(((Object[]) val)[i]);

                    if (i != ((Object[]) val).length - 1) {
                        buf.append(',');
                    }
                }
            } else {
                buf.append(val);
            }

            ocf.setText(buf.toString());
        } else if (def == null) {
            ocf.setText("");
        } else {
            ocf.setText(def.toString());
        }

        ocf.setListSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // dont add!! no point and makes it repeatedly parse!
        //addDocumentListener(param, ocf.getTextField());

        return ocf;
    }
    */
    public static void addDocumentListener(final JTextField tf, final Param param) {

        tf.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                param.setValue(tf.getText());
                if (param.isFileBased()) {
                    tf.setForeground(GFieldUtils.getFileFieldColor(tf.getText()));
                }
            }

            public void removeUpdate(DocumentEvent e) {
                param.setValue(tf.getText());
                if (param.isFileBased()) {
                    tf.setForeground(GFieldUtils.getFileFieldColor(tf.getText()));
                }
            }

            public void changedUpdate(DocumentEvent e) {
            }
        });

    }

    protected static GComboBoxField createActionListenerBoundHintsComboBox(boolean editable, ActionListener al, Param param) {
        DefaultComboBoxModel model = new DefaultComboBoxModel(param.getHints());

        if ((param.getValue() != null) && (model.getIndexOf(param.getValue()) == -1)) {
            model.addElement(param.getValue());
        }

        JComboBox cb = new JComboBox(model);
        cb.setEditable(editable);
        cb.setSelectedItem(param.getValue());
        cb.addActionListener(al);
        return new GComboBoxField(cb);
    }

    protected static void safeSelectFirst(JComboBox cb) {
        // imp -- so that somethign is selected at startup -> the act of selection
        // fires the event that does the param setting of value
        if (cb.getModel().getSize() > 0) {
            cb.setSelectedIndex(0);
        }
    }

    protected static void safeSelectValueDefaultOrNone(JComboBox cb, Param param) {

        Object t = param.getValue();

        if (t == null) {
            t = param.getDefault();
        }

        if (t != null) {
            cb.setSelectedItem(t);
        }

    }

    // need to use this as cant use new object as it isnt equal
    // for instance, see MetricParam
    // the signal2n is def, but its a different object than the one in hints
    protected static void safeSelectValueDefaultByString(JComboBox cb, Param param) {
        Object sel = getIfHasValue(param.getValue(), cb);

        if (sel == null) { // null value or value not found, so use default
            sel = param.getDefault();
        }

        if (sel == null) {
            return; // cannot do anything
        }

        String sels = sel.toString();
        for (int i = 0; i < cb.getModel().getSize(); i++) {
            if (cb.getModel().getElementAt(i).toString().equals(sels)) {
                cb.setSelectedIndex(i);
                return;
            }
        }

        safeSelectFirst(cb);
    }

    private static Object getIfHasValue(Object val, JComboBox cb) {

        if (val == null) {
            return null;
        }

        String vals = val.toString();
        for (int i = 0; i < cb.getModel().getSize(); i++) {
            if (cb.getModel().getElementAt(i).toString().equals(vals)) {
                return val;
            }
        }

        return null;

    }

} // End ParamHelper
