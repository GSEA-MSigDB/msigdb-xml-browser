/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xapps.browser.api.vtools;

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.ButtonResources;
import com.jidesoft.dialog.StandardDialog;

import edu.mit.broad.msigdb_browser.genome.swing.GuiHelper;
import edu.mit.broad.msigdb_browser.xbench.actions.ext.BrowserAction;
import edu.mit.broad.msigdb_browser.xbench.core.api.Application;
import edu.mit.broad.msigdb_browser.xbench.core.api.DialogType;
import edu.mit.broad.msigdb_browser.xbench.core.api.VTool;
import edu.mit.broad.msigdb_browser.xbench.project.ProjectLoggers;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Locale;

/**
 * A class that runs an action in the background after poping up a jdialog thing.
 * The jdialog can:
 * <p/>
 * 1) present some info to the user
 * 2) ask the user for some inout (e.g file name)
 * 3) do it, cancel and help buttons
 * 4) runs the action (while blocking) and shows the cool Romain animation when running
 * 5) Once done it tells the user success or error and then goes away.
 * <p/>
 *
 * @author Aravind Subramanian
 */
public class VToolRunner {

    private ProjectLoggers.MyLoggerLight logger = new ProjectLoggers.MyLoggerLight();

    /**
     * Class constructor
     */
    public VToolRunner() {
    }


    public void showRunner(final VTool tool, final DialogType dt) {

        final StandardDialogJide dialog = new StandardDialogJide(Application.getWindowManager().getRootFrame(), tool, dt);

        dialog.createBannerPanel();
        dialog.createContentPanel();
        dialog.createButtonPanel();

        dialog.bRun.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                SwingWorker<Object, Void> worker = new SwingWorker<Object, Void>() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        try {
                            dialog.setEnabled(false);
                            logger.startSpinner(tool.getTitle(), dialog.getRootPane());
                            tool.getRunnable().run();

                            logger.stopSpinner(dialog.getRootPane());

                        } catch (final Throwable t) {
                            logger.write("Error: " + t.getMessage());
                            Application.getWindowManager().showError(t);
                        } finally {
                            if (logger != null) {
                                logger.stopSpinner(dialog.getRootPane());
                            }
                            dialog.setDialogResult(StandardDialog.RESULT_AFFIRMED);
                            dialog.setVisible(false);
                            dialog.dispose();
                        }
                        return null;
                    }
                };
                worker.execute();
            }
        });

        dialog.bRun.setEnabled(tool.isRequiredAllSet());

        dialog.bCancel.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent evt) {
                if (dialog != null) {
                    dialog.setDialogResult(StandardDialogJide.RESULT_CANCELLED);
                    dialog.setVisible(false);
                    dialog.dispose();
                }
            }
        });

        dialog.bHelp.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                String url = tool.getHelpURL();
                BrowserAction ba = new BrowserAction("Help", "Online documentation for this tool", GuiHelper.ICON_HELP16, url);
                ba.actionPerformed(evt);
            }
        });

        tool.getComponent().addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                dialog.checkAllSet();
            }
        });

        // also need to add it to the components made here

        dialog.buttonPanel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                dialog.checkAllSet();
            }
        });

        tool.getInitFocusedComponent().addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                dialog.checkAllSet();
            }
        });

        dialog.setDefaultCancelAction(new AbstractAction() {
            public void actionPerformed(ActionEvent evt) {
                dialog.setDialogResult(StandardDialogJide.RESULT_CANCELLED);
                dialog.setVisible(false);
                dialog.dispose();
            }
        });

        dialog.setDefaultAction(dialog.bRun.getAction());
        dialog.getRootPane().setDefaultButton(dialog.bRun);

        dialog.pack();
        dialog.setSize(Application.DEFAULT_MODAL_WIDGET_RUNNER_DIALOG);
        _centerFrame(dialog);
        dialog.show();

    }

    // replicate method here to avoid loading GuiHelper right away - reduces launch time when developing
    private static void _centerFrame(java.awt.Window w) {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size = w.getSize();

        w.setLocation((screenSize.width - size.width) / 2,
                (screenSize.height - size.height) / 2);
    }


    private static class StandardDialogJide extends StandardDialog {

        private JButton bRun;
        private JButton bCancel;
        private JButton bHelp;
        private VTool tool;
        private DialogType dt;

        private ButtonPanel buttonPanel;

        StandardDialogJide(final JFrame parent, final VTool tool, final DialogType dt) throws HeadlessException {
            super(parent, tool.getTitle());
            this.tool = tool;
            this.dt = dt;
        }


        void checkAllSet() {
            if (tool.isRequiredAllSet()) {
                bRun.setEnabled(true);
            } else {
                bRun.setEnabled(false);
            }
        }

        public JComponent createBannerPanel() {
            return dt.createBannerPanel(tool.getTitle());
        }

        public JComponent createContentPanel() {
            JComponent comp = tool.getComponent();
            setInitFocusedComponent(tool.getInitFocusedComponent());
            return comp;
        }

        public ButtonPanel createButtonPanel() {
            if (buttonPanel == null) {
                buttonPanel = new ButtonPanel();
                bRun = new JButton(GuiHelper.ICON_START16);
                bCancel = new JButton(GuiHelper.ICON_ERROR16);
                bHelp = new JButton(GuiHelper.ICON_HELP16);
                bHelp.setMnemonic(ButtonResources.getResourceBundle(Locale.US).getString("Button.help.mnemonic").charAt(0));

                buttonPanel.addButton(bRun, ButtonPanel.AFFIRMATIVE_BUTTON);
                buttonPanel.addButton(bCancel, ButtonPanel.CANCEL_BUTTON);
                buttonPanel.addButton(bHelp, ButtonPanel.HELP_BUTTON);

                buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            }
            return buttonPanel;
        }
    }
} // End class ApplicationModalActionRunner

