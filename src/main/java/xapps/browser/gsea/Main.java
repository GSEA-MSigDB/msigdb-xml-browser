/*
 * Copyright (c) 2003-2018 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xapps.browser.gsea;

import com.jidesoft.comparator.ObjectComparatorManager;

import edu.mit.broad.msigdb_browser.xbench.ComparatorFactory2;

import org.apache.log4j.Logger;

import xapps.browser.api.MostMain;

/**
 * Main class of GSEA application
 * Use this in jar files, command line etc to launch the application.
 *
 * @author Aravind Subramanian
 * @version %I%, %G%
 */
public class Main extends MostMain {

    private static final Logger klog = Logger.getLogger(Main.class);

    /**
     * Class Constructor.
     */
    public Main() {

        super();

        super.setLnF();

        // start up the application
        final GseaFijiTabsApplicationFrame frame = new GseaFijiTabsApplicationFrame();

	try {
	    frame.backgroundInit();
	} catch (Throwable t) {
	    System.out.println("Error while initializing .., things may not work");
	    t.printStackTrace();
	}

        frame.makeVisible(true);

        // A global object that can register comparator with a type and a ComparatorContext.
        ObjectComparatorManager.registerComparator(Integer.class, new ComparatorFactory2.IntegerComparator());
        ObjectComparatorManager.registerComparator(Float.class, new ComparatorFactory2.FloatComparator());

    }

    /**
     * Main method to launch the Tools Desktop application
     *
     * @param args Ignored
     */
    public static void main(final String[] args) {

        try {
            new Main();
        } catch (Throwable e) {
            e.printStackTrace();
            klog.fatal("Could not create application", e);
        }
    }
}    // End Main
