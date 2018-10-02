/*
 * Copyright (c) 2003-2018 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.xbench.project;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import edu.mit.broad.msigdb_browser.genome.JarResources;

import javax.swing.*;

import org.apache.log4j.Logger;

/**
 * @author Aravind Subramanian
 */
public class ProjectLoggers {

    private static final Logger klog = Logger.getLogger(ProjectLoggers.class);

    public static class MyLoggerLight {

        private JLabel spinnerImageLabel;
        
        /**
         * Class constructor
         */
        public MyLoggerLight() {
        }
    
        public void startSpinner(final String name, final JRootPane rootPane) {
            spinnerImageLabel = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    Rectangle clip = g.getClipBounds();
                    AlphaComposite alphaComposite = AlphaComposite.SrcOver.derive(0.65f);
                    g2d.getComposite();
                    g2d.setComposite(alphaComposite);
                    g2d.setColor(getBackground());
                    g2d.fillRect(clip.x, clip.y, clip.width, clip.height);
                }
            };
            Image spinnerImage = JarResources.getImage("wait_spinner.gif").getScaledInstance(128, 128, Image.SCALE_DEFAULT);
            ImageIcon spinnerIcon = new ImageIcon(spinnerImage);
            spinnerImageLabel.setIcon(spinnerIcon);
            spinnerImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            spinnerImageLabel.setVerticalAlignment(SwingConstants.CENTER);

            rootPane.setGlassPane(spinnerImageLabel);
            rootPane.getGlassPane().setVisible(true);
            rootPane.revalidate();
        }

        public void stopSpinner(final JRootPane rootPane) {
            rootPane.getGlassPane().setVisible(false);
            spinnerImageLabel = null;
        }
    
        public void write(final String msg) {
            klog.debug(msg);
        }
    }
}
