/*
 * Copyright (c) 2003-2018 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xapps.browser.api.vtools;

import edu.mit.broad.msigdb_browser.genome.viewers.Viewer;
import edu.mit.broad.msigdb_browser.xbench.core.api.Application;
import edu.mit.broad.msigdb_browser.xbench.core.api.DialogType;
import edu.mit.broad.msigdb_browser.xbench.core.api.VTool;

import org.apache.log4j.Logger;

import xtools.browser.api.param.ToolParamSet;

import javax.swing.*;

import java.awt.*;

/**
 * @author Aravind Subramanian
 */
public abstract class AbstractVToolWithParams extends AbstractVTool {

    // IMP IMP IMP: see below about declareParams behaviour

    public JTextArea taMessage;

    protected static final transient Logger klog = Logger.getLogger(AbstractVToolWithParams.class);

    protected ToolParamSet fParamSet;

    private JPanel mainPanel;

    private JPanel paramPanel;

    private Hooks.BaseHook fBaseHook;

    /**
     * Class constructor
     */
    public AbstractVToolWithParams(final String name, final Icon icon, final Hooks.BaseHook hook) {
        super(name, icon, DialogType.ParamChooserPanel);
        this.fBaseHook = hook;
        this.fParamSet = new ToolParamSet();
    }

    public AbstractVToolWithParams(final String name, final Hooks.BaseHook hook) {
        this(name, null, hook);
    }

    // subclasses can override
    public void declareParams() {
    }

    private JTextField tfName_opt;

    // subclasses can override
    public JComponent getInitFocusedComponent() {
        if (fBaseHook == null) {
            return tfName_opt;
        } else {
            return fBaseHook.getTextField();
        }
    }

    public abstract Viewer execute() throws Exception;

    public VTool.Runnable getRunnable() {
        return new VTool.Runnable() {
            public void run() throws Exception {
                Viewer viewer = execute();
                if (viewer != null) {
                    Application.getWindowManager().openWindow(viewer);
                } // sometimes no viewer is made
            }
        };
    }

    public boolean isRequiredAllSet() {
        return fParamSet.isRequiredAllSet();
    }

    public String getSaveName() {
        if (fBaseHook == null) {
            return tfName_opt.getText();
        } else {
            return fBaseHook.getSaveName();
        }
    }

    public String getSaveNameHint() {
        if (fBaseHook == null) {
            return null;
        } else {
            return fBaseHook.getSaveNameHint();
        }
    }

    // subclasses can override
    public String getComponentMessage() {
        if (fBaseHook == null) {
            return null;
        } else {
            return fBaseHook.getComponentMessage();
        }
    }

    public JComponent getComponent() {

        if (mainPanel == null) {
            //TraceUtils.showTrace();

            mainPanel = new JPanel();
            // paramPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
            mainPanel.setLayout(new BorderLayout());

            declareParams();  // @note VV imp that this is called lazilly. Helps make the VTool faster to load

            paramPanel = ParamSetFormForAFew.createParamsPanel(fParamSet.getParams());

            taMessage = new JTextArea();
            taMessage.setEditable(false);
            taMessage.setBackground(Color.LIGHT_GRAY);
        }

        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(new JScrollPane(paramPanel), BorderLayout.CENTER);

        JTextField tfName;

        if (fBaseHook == null) {
            if (tfName_opt == null) {
                tfName_opt = new JTextField(60);
                tfName_opt.setBorder(BorderFactory.createTitledBorder("Enter a short name for the result"));
            }
            tfName = tfName_opt;
        } else {
            tfName = fBaseHook.getTextField();
        }

        String sn = getSaveNameHint();
        if (sn != null && sn.length() > 0) {
            tfName.setText(sn);
        }

        String cm = getComponentMessage();
        if (cm != null && cm.length() > 0) {
            JPanel msgPanel = new JPanel();
            msgPanel.setLayout(new BoxLayout(msgPanel, BoxLayout.PAGE_AXIS));
            taMessage.setText(cm);
            msgPanel.add(new JScrollPane(taMessage));
            msgPanel.add(tfName);
            mainPanel.add(msgPanel, BorderLayout.SOUTH);
        } else {
            mainPanel.add(tfName, BorderLayout.SOUTH);
        }

        return mainPanel;
    }

} // End class AbstractModalParamsAction
