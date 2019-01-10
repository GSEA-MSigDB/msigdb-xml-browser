/*******************************************************************************
 * Copyright (c) 2003-2019 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 *******************************************************************************/
module org.gsea_msigdb.xmlbrowser {
    exports edu.mit.broad.msigdb_browser.genome;
    exports edu.mit.broad.msigdb_browser.genome.alg;
    exports edu.mit.broad.msigdb_browser.genome.charts;
    exports edu.mit.broad.msigdb_browser.genome.io;
    exports edu.mit.broad.msigdb_browser.genome.models;
    exports edu.mit.broad.msigdb_browser.genome.objects;
    exports edu.mit.broad.msigdb_browser.genome.parsers;
    exports edu.mit.broad.msigdb_browser.genome.reports.api;
    exports edu.mit.broad.msigdb_browser.genome.reports.pages;
    exports edu.mit.broad.msigdb_browser.genome.swing;
    exports edu.mit.broad.msigdb_browser.genome.swing.choosers;
    exports edu.mit.broad.msigdb_browser.genome.swing.fields;
    exports edu.mit.broad.msigdb_browser.genome.swing.windows;
    exports edu.mit.broad.msigdb_browser.genome.utils;
    exports edu.mit.broad.msigdb_browser.genome.viewers;
    exports edu.mit.broad.msigdb_browser.xbench.actions;
    exports edu.mit.broad.msigdb_browser.xbench.actions.ext;
    exports edu.mit.broad.msigdb_browser.xbench.explorer.filemgr;
    exports edu.mit.broad.msigdb_browser.xbench.project;
    exports edu.mit.broad.msigdb_browser.vdb;
    exports edu.mit.broad.msigdb_browser.vdb.chip;
    exports edu.mit.broad.msigdb_browser.vdb.map;
    exports edu.mit.broad.msigdb_browser.vdb.meg;
    exports edu.mit.broad.msigdb_browser.vdb.msigdb;
    exports edu.mit.broad.msigdb_browser.xbench;
    exports edu.mit.broad.msigdb_browser.xbench.core;
    exports edu.mit.broad.msigdb_browser.xbench.core.api;
    exports edu.mit.broad.msigdb_browser.xbench.prefs;
    exports org.genepattern.browser.uiutil;
    exports xapps.browser;
    exports xapps.browser.api;
    exports xapps.browser.api.frameworks.fiji;
    exports xapps.browser.api.vtools;
    exports xapps.browser.gsea;
    exports xtools.browser.api;
    exports xtools.browser.api.param;
    exports xtools.browser.api.ui;

    requires commons.compress;
    requires commons.io;
    requires commons.lang3;
    requires dom4j.full;
    requires edtftpj;
    requires forms;
    requires java.datatransfer;
    requires java.desktop;
    requires java.prefs;
    requires java.xml;
    requires jide.common;
    requires log4j;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires looks;
    requires trove;
}