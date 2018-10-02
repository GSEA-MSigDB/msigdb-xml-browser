/*
 * Copyright (c) 2003-2018 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.genome.reports.api;

import edu.mit.broad.msigdb_browser.genome.*;
import edu.mit.broad.msigdb_browser.genome.objects.*;
import edu.mit.broad.msigdb_browser.genome.parsers.DataFormat;
import edu.mit.broad.msigdb_browser.genome.parsers.ParserFactory;
import edu.mit.broad.msigdb_browser.genome.reports.pages.*;
import edu.mit.broad.msigdb_browser.genome.utils.FileUtils;

import org.apache.log4j.Logger;

import xtools.browser.api.Tool;
import xtools.browser.api.param.GeneSetMatrixFormatParam;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Both a Report and a ReportWriter (for a Tool)
 * <p/>
 * Automagically:
 * 1) makes reports dir if not already extant
 * 2) all reports data saved in tha dir
 * 3) makes files on request -- stamped with a common timestamp to avoid name clashes
 * 4) on complete, saves a Report of itself
 * <p/>
 * <p/>
 * A Report is a colection of zero, one or more files (pdfs, txts etc)
 * and charts.
 * <p/>
 * interface reprsenting a Report that is also a writing mechanism
 * Abstracts out the format - html, pdf etc
 * <p/>
 * Idea here is for the reports to maintain an "index file" with links etc to all the added stuff
 * This would be the kick off point for someone to look at the data
 * that the reports produced. If odf, would mostly all be in the file
 * if htnl, would be linked etc
 *
 * @author Aravind Subramanian
 * @version %I%, %G%
 */
// dont extend abstractobject -- easier to impl ourselves here as impl not pob but reports
public class ToolReport implements Report {
    private transient Logger log = Logger.getLogger(this.getClass());
    private transient static Logger klog = Logger.getLogger(ToolReport.class);

    /**
     * @maint ALL OF THE OBJECTS STORED NEED TO BE SERIALIZABLE (as stored in http session)
     * @maint IMP IMP IMP
     * If you add more objects make sure to check if the sizeXXX and allXXX methods
     * need to be updated
     */

    /**
     * The tool for which this reports is being generated
     */

    // Try to NOT store the report as it could be heavy
    // // (see Web stuff for example where each page is a tool and we dont want to cache)
    private Tool fTool_opt; // not always cached

    private Class fProducerClass;

    /**
     * All reports files get saved within here - see gp note below
     */
    private File fReportDir;

    private ToolComments fToolComments;

    private final long fTimestamp = System.currentTimeMillis();

    private int fNumPagesAdded;

    /**
     * Name of this reports - NOT the analysis
     */
    private String fReportName;

    private List fErrors;

    /**
     * Contains Page objects
     */
    private Pages fPages;

    private File fHtmlIndexPageFile;

    /**
     * Class constructor
     * @param useThisRptDir
     * @param makeReportIndexPage
     * @throws IllegalArgumentException
     */
    public ToolReport(final File useThisRptDir) throws IllegalArgumentException {

        if (!useThisRptDir.exists()) {
            throw new IllegalArgumentException("Report dir does not exists!!: " + useThisRptDir.getAbsolutePath());
        }
        
        this.fReportDir = useThisRptDir;
        this.fToolComments = new ToolComments();

        this.fPages = new Pages();
    }

    /**
     * @return The name of this reports
     * @see edu.mit.broad.msigdb_browser.genome.NamingConventions for format
     */
    public String getName() {
        return fReportName;
    }

    public long getTimestamp() {
        return fTimestamp;
    }
    
    public String getNameEnglish() {
        return null;
    }

    public String getQuickInfo() {
        return "" + fNumPagesAdded;
    }

    public File[] getFilesProduced() {
        List files = fPages.getFiles_list();
        if (fHtmlIndexPageFile != null && fHtmlIndexPageFile.exists() && !files.contains(fHtmlIndexPageFile)) {
            files.add(fHtmlIndexPageFile);
        }

        return (File[]) files.toArray(new File[files.size()]);
    }

    public void addComment(String comment) {
        fToolComments.add(comment);
    }

    // this must never fail
    public void addError(final String msg, final Throwable t) {
        if (fErrors == null) {
            fErrors = new ArrayList();
        }

        fErrors.add(t);
        log.error(msg, t);
    }

    /**
     * Report impl
     *
     * @return
     */
    public Properties getParametersUsed() {
        if (fTool_opt != null) {
            return fTool_opt.getParamSet().toProperties();
        } else {
            throw new IllegalStateException("Tool not cached: " + fTool_opt);
        }
    }

    /**
     * Report impl
     *
     * @return
     */
    public Class getProducer() {
        return fProducerClass;
    }

    public File savePageInvisibly2Cache(GeneSetMatrix gm, GeneSetMatrixFormatParam gmf) {

        File file = _createFile(gm.getName(), gmf.getDataFormat().getExtension());

        try {

            if (gmf.getDataFormat() == DataFormat.GMT_FORMAT) {
                ParserFactory.saveGmt(gm, file, false);
            } else if (gmf.getDataFormat() == DataFormat.GMX_FORMAT) {
                ParserFactory.save(gm, file, false);
            } else {
                log.warn("Unkown gm format: " + gmf.getDataFormat());
                ParserFactory.save(gm, file, false);
            }

            _centralAddPage(new FileWrapperPage(file));    // @note

        } catch (Throwable t) {
            addError("Trouble saving pob to reports", t);
        }

        return file;

    }

    private void _centralAddPage(final FileWrapperPage page) {
        _centralAddPage(page, false);
    }

    private void _centralAddPage(final FileWrapperPage page, boolean keepTrack) {
        if (page == null) {
            throw new IllegalArgumentException("Param page cannot be null");
        }

        if (keepTrack) {
            fPages.add(page);
        }

        fNumPagesAdded++;
    }

    /**
     * @param filename
     * @param suffix
     * @return
     * @maint File naming conventionmanaged here:
     * name ->> name.timestamp.suffix
     * <p/>
     * Common timestamp to all
     */
    private File _createFile(final String fname, final String suffix) {
        return _createFile(fname, suffix, fReportDir);
    }

    private static File _createFile(final String fname, final String suffix, final File inDir) {

        File file = null;

        try {
            StringBuffer name;

            if (fname.endsWith(suffix)) {
                name = new StringBuffer(fname);
            } else {
                name = new StringBuffer(fname).append('.').append(suffix);
            }

            file = createSafeReportFile(name.toString(), inDir);

            if (FileUtils.isLocked(file)) {
                // dont do this as it can make the file name way too long
                // instead just give it a flavor (10 chars) of what the real name is
                //name = new StringBuffer(fname).append(".WARNING_renamed_on_detecting_lock").append(System.currentTimeMillis()).append('.').append(suffix);
                int max = fname.length();
                if (max > 10) {
                    max = 10;
                }
                name = new StringBuffer(fname.substring(0, max)).append(".WARNING_renamed_on_detecting_lock").append(System.currentTimeMillis()).append('.').append(suffix);
                String name_str = NamingConventions.createSafeFileName(name.toString()); // make doubly sure
                file = new File(inDir, name_str);
            }

            if (!file.exists()) {
                boolean made = file.createNewFile();

                if (!made) {
                    throw new IOException("Could not make file: " + file.getAbsolutePath());
                }
            }

        } catch (Throwable t) {
            if (file == null) {
                file = createSafeReportFile("tmp_report_error_file." + System.currentTimeMillis() + suffix, inDir);
            }
            StringBuffer err = new StringBuffer("Fatal error making file to save a component of the reports in");
            err.append("\nInstead using a result file: ").append(file.getPath());
            klog.fatal(err, t);
        }

        return file;
    }

    // replaces chars that make file paths barf such as @ and #
    private static File createSafeReportFile(final String name, final File inDir) {
        return NamingConventions.createSafeFile(inDir, name);
    }

    /**
     * Inner class for class objectiviy when adding pages
     * <p/>
     * Also manages some of the business rules when pages are added - event firing etc
     */
    static class Pages implements java.io.Serializable {

        private List plist;
        private List flist;

        /**
         * Utility fields for the event firing mechanism
         * <p/>
         * Maybe make a better event type later
         */
        //private PropertyChangeSupport pageAddedSupport;

        Pages() {
            this.plist = new ArrayList();
            this.flist = new ArrayList();
            //this.pageAddedSupport = new PropertyChangeSupport(this);
        }

        private void writeObject(java.io.ObjectOutputStream out) {
            klog.debug("Ignoring: " + out);
        }

        private void readObject(java.io.ObjectInputStream in) {
            klog.debug("Ignoring: " + in);
        }

        void add(final FileWrapperPage page) {
            plist.add(page);
            flist.add(page.getFile());

            // always fire an event to signifiy page added
            //pageAddedSupport.firePropertyChange(new PropertyChangeEvent(this, page.getFile().getName(), null, page));

        }

        List getFiles_list() {
            return flist;
        }

    } // End inner class Pages

}    // End ToolReport

