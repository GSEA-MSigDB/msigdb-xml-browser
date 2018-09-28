/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xapps.browser.api.vtools;

import edu.mit.broad.msigdb_browser.genome.JarResources;
import edu.mit.broad.msigdb_browser.genome.XLogger;
import edu.mit.broad.msigdb_browser.genome.reports.api.ToolReport;
import edu.mit.broad.msigdb_browser.xbench.core.api.Application;
import edu.mit.broad.msigdb_browser.xbench.core.api.DialogType;
import edu.mit.broad.msigdb_browser.xbench.core.api.VTool;

import org.apache.log4j.Logger;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.io.File;

/**
 * @author Aravind Subramanian
 */
public abstract class AbstractVTool extends AbstractAction implements VTool {

    private DialogType fDialogType;

    private static final transient Logger klog = XLogger.getLogger(AbstractVTool.class);

    // @todo assess if static is ok?
    protected static ToolReport kReport;

    private String fName;

    /**
     * Class constructor
     *
     * @param name
     * @param icon
     * @param dt
     */
    public AbstractVTool(final String name, final Icon icon, final DialogType dt) {
        super(name, icon);
        this.fDialogType = dt;
        this.fName = name;

        if (kReport == null) {
            klog.debug("Making vtoolreport");
            final File dir = Application.getVdbManager().getDefaultOutputDir();
            kReport = new ToolReport(dir);
        }

    }

    public String getName() {
        return fName;
    }

    public void actionPerformed(final ActionEvent evt) {
        Application.getWindowManager().runModalTool(this, fDialogType);
    }

    public String getHelpURL() {
        //return JarResources.getHelpURL(getName());
        return JarResources.getHelpURL(this.getClass().getName());
    }

} // End class AbstractModalAction

/*
static class MyTool implements Tool {

    public void execute() throws Exception {
    }

    public ToolCategory getCategory() {
        throw new NotImplementedException();
    }

    public ToolReport getReport() {
        throw new NotImplementedException();
    }

    public void declareParams() {
    }

    public String getHelpURL() {
        throw new NotImplementedException();
    }

    public ParamSet getParamSet() {
        throw new NotImplementedException();
    }

    public String getName() {
        throw new NotImplementedException();
    }

    public String getTitle() {
        throw new NotImplementedException();
    }

    public String getDesc() {
        throw new NotImplementedException();
    }

    public long getExecutionTime() {
        throw new NotImplementedException();
    }

    public long getStartTime() {
        throw new NotImplementedException();
    }

    public long getStopTime() {
        throw new NotImplementedException();
    }

    public void printfUsage() {
    }

    public void requestKill() {
    }

    public void setOutputStream(final PrintStream sout) {
    }

}
*/

/*
public File save(final PersistentObject pob, final boolean showSavedItDialog) throws Exception {
    File file = createFile(pob.getName(), DataFormat.getExtension(pob));
    ParserFactory.save(pob, file);
    if (showSavedItDialog) {
        Application.getWindowManager().showMessage("Saved result to : " + file.getPath());
    }
    return file;
}

public File[] save(final PersistentObject[] pobs, final boolean showSavedItDialog) throws Exception {

    File[] files = new File[pobs.length];
    for (int i = 0; i < pobs.length; i++) {
        files[i] = createFile(pobs[i].getName(), DataFormat.getExtension(pobs[i]));
        ParserFactory.save(pobs[i], files[i]);
    }

    if (files.length > 0 && showSavedItDialog) {
        Application.getWindowManager().showMessage("Saved " + pobs.length + " results to : " + files[0].getParent());
    }

    return files;
}

public File save(final String name, final BufferedImage buf) throws Exception {
    File file = createFile(name, "png");
    ImageUtils.savePng(buf, file);
    Application.getWindowManager().showMessage("Saved image to : " + file.getPath());
    return file;
}
*/