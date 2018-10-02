/*
 * Copyright (c) 2003-2018 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.xbench.core.api;

import edu.mit.broad.msigdb_browser.genome.TraceUtils;
import xapps.browser.api.frameworks.fiji.StatusBarAppender;

import java.awt.*;

import org.apache.log4j.Logger;

/**
 * Factory method for an application
 * <p/>
 * This should be like a POB because only 1 version of it exists (for junit)
 */
public class Application {

    private static Handler kAppHandler;

    public static Dimension DEFAULT_MODAL_WIDGET_RUNNER_DIALOG = new Dimension(575, 500);

    private Application() {
    }

    public static boolean isHandlerSet() {
        return kAppHandler != null;
    }

    public static void registerHandler(final Handler appHandler) {
        //TraceUtils.showTrace();

        if (appHandler == null) {
            throw new IllegalArgumentException("Param appHandler cannot be null");
        }

        kAppHandler = appHandler;
    }

    private static void _check() {
        if (kAppHandler == null) {
            // Dont because that could trigger a recursive call
            //Application.getWindowManager().showError("No Application handler set");
            TraceUtils.showTrace();
            // @todo return a default handler
            throw new IllegalStateException("No Application handler set yet: " + kAppHandler);
        }
    }

    public static FileManager getFileManager() {
        _check();
        return kAppHandler.getFileManager();
    }

    public static VdbManager getVdbManager() {
        _check();
        return kAppHandler.getVdbManager();
    }

    public static WindowManager getWindowManager() throws HeadlessException {
        _check();
        return kAppHandler.getWindowManager();
    }

    public interface Handler {

        public StatusBarAppender getStatusBarAppender() throws HeadlessException;

        public FileManager getFileManager();

        public VdbManager getVdbManager();

        public WindowManager getWindowManager() throws HeadlessException;

    } // End interface Handler


} // End class Application
