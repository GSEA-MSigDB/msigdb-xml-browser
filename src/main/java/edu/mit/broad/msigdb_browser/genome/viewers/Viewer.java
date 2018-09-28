/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.genome.viewers;

import edu.mit.broad.msigdb_browser.xbench.core.Widget;

/**
 * Changed -> saving stuff moved to an explicit dat exporter thing. Viewers just provide views of data
 * <p/>
 * Viewer -> a saveable Widget. Generally implies that visualizing something.
 * <p/>
 * OLD
 * <p/>
 * By convention make sure to declare 2 public static finalvars: NAME  and ICON
 * <p/>
 * Whats the diff b/w a  Viewer and a Chart?
 * <p/>
 * 1) One viewer can bring together one or more charts i.e its a container
 * for charts
 * 2) Viewers can choose to have dnd facilities
 * <p/>
 * <p/>
 * if its not likely that a viz you are building would need to be combined with other
 * viz'score, then need not bother making a viewer for it - simpley use a chart.
 * <p/>
 * <p/>
 * <p/>
 * <p> </p>
 * <p> </p>
 *
 * @author Aravind Subramanian
 * @version %I%, %G%
 */
//TODO: Examine if we can collapse this hierarchy and drop this interface.  Should be safe.
public interface Viewer extends Widget {

}    // End Viewer
