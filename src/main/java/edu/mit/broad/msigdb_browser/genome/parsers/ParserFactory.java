/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.genome.parsers;

import edu.mit.broad.msigdb_browser.genome.*;
import edu.mit.broad.msigdb_browser.genome.io.FtpResultInputStream;
import edu.mit.broad.msigdb_browser.genome.io.FtpSingleUrlTransferCommand;
import edu.mit.broad.msigdb_browser.genome.objects.*;
import edu.mit.broad.msigdb_browser.genome.reports.api.Report;
import edu.mit.broad.msigdb_browser.vdb.VdbRuntimeResources;
import edu.mit.broad.msigdb_browser.vdb.chip.Chip;
import edu.mit.broad.msigdb_browser.vdb.msigdb.MSigDB;
import edu.mit.broad.msigdb_browser.xbench.core.api.Application;

import org.apache.log4j.Logger;

import xapps.browser.gsea.GseaWebResources;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * Facade pattern to easily use the parser classes and methods
 * for simple read in / write out stuff that is used 95% of the time.
 * <p/>
 * Also implements a static cache so that Files are parsed only once (by default)
 * for each jvm invocation.
 *
 * @author Aravind Subramanian
 * @author David Eby
 * @version %I%, %G%
 */
public class ParserFactory implements Constants {

    // These probably belong elsewhere if we make a broader file cache than just for special CHIPs
    private static final File fileCacheDir = new File(Application.getVdbManager().getRuntimeHomeDir(), "file_cache");
    private static final File chipCacheDir = new File(fileCacheDir, "chip");
    static {
        // Make sure the cache dirs exist.
        if (!chipCacheDir.exists()) {
            chipCacheDir.mkdirs();
        }
    }
    private static final Logger klog = XLogger.getLogger(ParserFactory.class);

    /**
     * Privatized Class constructor
     * static methods only.
     */
    private ParserFactory() {
    }

    private static final ObjectCache kDefaultObjectCache = new ObjectCache();

    // The default one is the generic NON-application related cache
    //additionally there are application specific classes
    static ObjectCache _getCache() {
        return kDefaultObjectCache;
    }

    /**
     * @return The Parsers object cache
     */
    public static ObjectCache getCache() {
        return _getCache();
    }

    public static GeneSet readGeneSet(File file) throws Exception {
        InputStream is = createInputStream(file);
        return readGeneSet(file.getPath(), is, true, true);
    }

    private static GeneSet readGeneSet(String path, InputStream is, boolean useCache, boolean add2Cache) throws Exception {

        if (path == null) {
            throw new IllegalArgumentException("Param path cannot be null");
        }

        if (is == null) {
            throw new IllegalArgumentException("Param is cannot be null");
        }

        if ((_getCache().isCached(path, GeneSet.class)) && (useCache)) {
            is.close();
            return (GeneSet) _getCache().get(path, GeneSet.class);
        }

        GeneSet gset;

        if (AuxUtils.isAux(path)) {
            String gsetName = AuxUtils.getAuxNameOnlyNoHash(path);
            File f = AuxUtils.getBaseFileFromAuxFile(new File(path));
            GeneSetMatrix gm = readGeneSetMatrix(f.getPath(), createInputStream(f), useCache, true, add2Cache);
            return gm.getGeneSet(gsetName);
        } else {
            Parser parser = new FSetParser();
            gset = (GeneSet) parser.parse(toName(path), is).get(0);
            _getCache().addInvisibly(path, new DefaultGeneSetMatrix(gset.getName(), new GeneSet[]{gset}));
        }

        if (add2Cache) {
            _getCache().add(path, gset, GeneSet.class);
        }
        is.close();
        return gset;
    }


    private static Report readReport(String path, InputStream is, boolean useCache) throws Exception {

        if (path == null) {
            throw new IllegalArgumentException("Param path cannot be null");
        }

        if (is == null) {
            throw new IllegalArgumentException("Param is cannot be null");
        }


        if ((_getCache().isCached(path, Report.class) && (useCache))) {
            is.close();
            return (Report) _getCache().get(path, Report.class);
        }

        //log.debug("Parsing Report from: " + path);
        ReportParser parser = new ReportParser();
        Report rpt = (Report) parser.parse(toName(path), is).get(0);

        if (useCache) { // note special -- dont add
            _getCache().add(path, rpt, Report.class);
        }

        is.close();
        return rpt;
    }


    public static Chip readChip(String sourcePath, boolean useCache) throws Exception {
        return readChip(sourcePath, createInputStream(sourcePath), useCache);
    }

    private static Chip readChip(String path, InputStream is, boolean useCache) throws Exception {

        if (path == null) {
            throw new IllegalArgumentException("Param path cannot be null");
        }

        if (is == null) {
            throw new IllegalArgumentException("Param is cannot be null");
        }

        if ((_getCache().isCached(path, Chip.class) && (useCache))) {
            is.close();
            return (Chip) _getCache().get(path, Chip.class);
        }

        Parser parser = new ChipParser();
        Chip chip = (Chip) parser.parse(path, is).get(0);

        if (useCache) {
            _getCache().add(path, chip, Chip.class);
        }

        is.close();

        return chip;
    }

    private static GeneSetMatrix readGeneSetMatrix(String path,
                                                   final InputStream is,
                                                   final boolean useCache,
                                                   final boolean checkforduplicates,
                                                   final boolean add2Cache) throws Exception {

        if (path == null) {
            throw new IllegalArgumentException("Param file cannot be null");
        }

        if (is == null) {
            throw new IllegalArgumentException("Param is cannot be null");
        }

        // aux mechanism is a bit diff and much simpler from templates' we just strip it off
        // and register the gsetnames with the gmx -- no new objects are created
        if (AuxUtils.isAux(path)) {
            path = AuxUtils.getBasePathFromAuxPath(path);
        }

        String ext = NamingConventions.getExtension(path);
        if (ext.equals(Constants.GMT)) {
            return readGeneSetMatrixT(path, is, useCache, checkforduplicates, add2Cache);
        }

        if (ext.equals(Constants.GRP)) {
            GeneSet gset = readGeneSet(path, is, useCache, add2Cache);
            return new DefaultGeneSetMatrix(toName(path), new GeneSet[]{gset});
        }

        if ((_getCache().isCached(path, GeneSetMatrix.class)) && (useCache)) {
            is.close();
            return (GeneSetMatrix) _getCache().get(path, GeneSetMatrix.class);
        }

        GmxParser parser = new GmxParser();
        parser.setCheckForDuplicates(checkforduplicates);
        GeneSetMatrix gmx = (GeneSetMatrix) parser.parse(toName(path), is).get(0);

        if (add2Cache) {
            _getCache().add(path, gmx, GeneSetMatrix.class);
        }

        // IMP also fetch and add all gsets in the gmx to cache
        File parentFile = new File(path).getParentFile();
        for (int i = 0; i < gmx.getNumGeneSets(); i++) {
            File pseudo = new File(parentFile, gmx.getGeneSet(i).getName());

            if (add2Cache) {
                _getCache().addInvisibly(pseudo, gmx.getGeneSet(i));
            }

        }

        if (add2Cache) {
            _getCache().sortModel(GeneSet.class);
            // @todo fix me
            _getCache().hackAddAuxSets(gmx);
        }

        is.close();
        return gmx;
    }

    private static GeneSetMatrix readGeneSetMatrixT(String path,
                                                    InputStream is,
                                                    boolean useCache,
                                                    boolean checkForDuplicates,
                                                    boolean add2Cache) throws Exception {

        if (path == null) {
            throw new IllegalArgumentException("Param file cannot be null");
        }

        if (is == null) {
            throw new IllegalArgumentException("Param is cannot be null");
        }

        if (AuxUtils.isAux(path)) {
            path = AuxUtils.getBasePathFromAuxPath(path);
        }

        // @note as a help to make gm ext agnostic
        String ext = NamingConventions.getExtension(path);
        if (ext.equals(Constants.GMX)) {
            return readGeneSetMatrix(path, is, useCache, checkForDuplicates, add2Cache);
        }

        if (ext.equals(Constants.GRP)) {
            GeneSet gset = readGeneSet(path, is, useCache, true);
            return new DefaultGeneSetMatrix(toName(path), new GeneSet[]{gset});
        }

        if ((_getCache().isCached(path, GeneSetMatrix.class)) && (useCache)) {
            is.close();
            return (GeneSetMatrix) _getCache().get(path, GeneSetMatrix.class);
        }

        Parser parser = new GmtParser();
        GeneSetMatrix gmx = (GeneSetMatrix) parser.parse(toName(path), is).get(0);

        _getCache().add(path, gmx, GeneSetMatrix.class);

        //disabled after advance chooser
        // IMP also fetch and add all gsets in the gmx to cache
        File parentFile = new File(path).getParentFile();
        for (int i = 0; i < gmx.getNumGeneSets(); i++) {
            File pseudo = new File(parentFile, gmx.getGeneSet(i).getName());

            _getCache().addInvisibly(pseudo, gmx.getGeneSet(i));

        }


        _getCache().hackAddAuxSets(gmx);

        is.close();
        return gmx;
    }

    public static MSigDB readMSigDB(final String path, final InputStream is, final boolean useCache, final boolean deepInitAtLoad) throws Exception {

        if (path == null) {
            throw new IllegalArgumentException("Param path cannot be null");
        }

        if ((_getCache().isCached(path, MSigDB.class)) && (useCache)) {
            return (MSigDB) _getCache().get(path, MSigDB.class);
        }
        long startTime = System.currentTimeMillis();
        MSigDBParser parser = new MSigDBParser();
        final MSigDB msigdb = (MSigDB) parser.parse(path, is).get(0);
        klog.info("Done parsing.  Time: " + (System.currentTimeMillis() - startTime));
        _getCache().add(path, msigdb, MSigDB.class);
        return msigdb;
    }

    // IMP have to give back the  aux object if the file is an aux one
    // but have to read data from the base file
    public static PersistentObject read(final File file, final boolean useCache) throws Exception {
        File baseFile = AuxUtils.getBaseFileFromAuxFile(file);
        final String path = file.getPath();
        final InputStream is = createInputStream(baseFile);
        try {
            if (path == null) {
                throw new IllegalArgumentException("Param file cannot be null");
            }
        
            if (is == null) {
                throw new IllegalArgumentException("Param is cannot be null");
            }
        
            final String tmp = stripAt(path);
            final String ext = NamingConventions.getExtensionLiberal(tmp).toLowerCase();
        
            // TODO: further removal of unused file formats.
            // Almost certainly not used: 
            //    (Elsewhere in Factory) Dataframe (DF), PCL, DChip, GenesOfInterest
            // In use with absolute certainty: GRP, GMX, GMT, CHIP
            // Likely in use: RPT
            if (ext.equalsIgnoreCase(GRP)) {
                return readGeneSet(path, is, useCache, true);
            } else if (ext.startsWith(GMX)) {// IMP note -- special for the aux hash
                return readGeneSetMatrix(path, is, useCache, true, true);
            } else if (ext.startsWith(GMT)) {// IMP note -- special for the aux hash
                return readGeneSetMatrixT(path, is, useCache, true, true);
            } else if (ext.equalsIgnoreCase(RPT)) {
                return readReport(path, is, useCache);
            } else if (ext.equalsIgnoreCase(CHIP) || ext.equalsIgnoreCase(CSV)) {
                return readChip(path, is, useCache);
            } else {
                throw new IllegalArgumentException("Unknown file format: " + path + " no known Parser for ext: " + ext);
            }
        
        } catch (java.io.InterruptedIOException e) {
            klog.info("progress exception - possibly cancelled ... ignoring");
            return null;
        } finally {
            is.close();
        }
    }

    private static String stripAt(final String path) {
        StringTokenizer tok = new StringTokenizer(path, "@");
        return tok.nextToken();
    }

    /**
     * @param gset
     * @param toFile
     * @throws Exception
     */
    public static void save(GeneSet gset, File toFile) throws Exception {

        Parser parser = new FSetParser();

        parser.export(gset, toFile);

        _getCache().add(toFile, gset, GeneSet.class);

    }

    /// cant use df due to unit loaded violations

    public static void saveGmt(GeneSetMatrix gmt, File toFile, boolean add2cache) throws Exception {
        Parser parser = new GmtParser();
        parser.export(gmt, toFile);
        if (add2cache) {
            _getCache().add(toFile, gmt, GeneSetMatrix.class);
        }
    }

    public static void save(GeneSetMatrix gmx, File toFile, boolean add2cache) throws Exception {
        Parser parser = new GmxParser();
        parser.export(gmx, toFile);
        if (add2cache) {
            _getCache().add(toFile, gmx, GeneSetMatrix.class);
        }
    }

    // @note convention
    private static String toName(final String path) {
        return new File(path).getName();
    }

    // if file doesnt exsits or is a dir then error out with an intuitive message
    // useful before using a new FileInputStream(f) as the error message from that is not
    // very explanatory
    private static InputStream createInputStream(File file) throws IOException {

        // @note as a convebicne auto detect of the file is really a URL
        if (NamingConventions.isURL(file.getPath())) {
            return createInputStream(new URL(file.getPath()));
        }

        if (AuxUtils.isAuxFile(file)) {
            file = AuxUtils.getBaseFileFromAuxFile(file);
        }

        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
        }

        if (file.isDirectory()) {
            throw new IOException("Specified path is a Directory (expecting a File): " + file.getAbsolutePath());
        }

        if (!file.canRead()) {
            throw new IOException("Check file permissions - cannot read data from file: " + file.getAbsolutePath());
        }

        return new BufferedInputStream(new FileInputStream(file));
    }

    private static InputStream createInputStream(URL url) throws IOException {
        String filePath = url.getPath();

        // Special work-around for GENE_SYMBOL & SEQ_ACCESSION FTP dependencies.
        File localFile = null;
        final boolean isGeneSymbolChip = VdbRuntimeResources.isPathGeneSymbolChip(filePath);
        final boolean isSeqAccessionChip = VdbRuntimeResources.isChipSeqAccession(filePath);
        if (isGeneSymbolChip || isSeqAccessionChip) {
            // Short-term hack fix for Special CHIP files.  There are hard-coded URL references to these in the code base
            // which won't always work (private FTP site).  We modify these URLs here to use the public FTP site instead.
            // We plan to transition to a different transfer method in the near future, so we'll rework things more completely
            // at that point.
            String localFileName = (isGeneSymbolChip) ? Constants.GENE_SYMBOL_CHIP : Constants.SEQ_ACCESSION_CHIP;
            url = new URL("ftp://ftp.broadinstitute.org/pub/gsea/annotations/" + localFileName);
            
            // Are we retrieving GENE_SYMBOL or SEQ_ACCESSION?  Check whether we can pick it up 
            // from the local cache instead.
            localFile = new File(chipCacheDir, localFileName);
            if (localFile.exists() && localFile.isFile()) {
                klog.info("File found in local file cache; will process " + localFile.getAbsolutePath()
                        + " in place of " + url.toString() + ".  Ignore any following message(s) about this URL.");
                return new BufferedInputStream(new FileInputStream(localFile));
            }
        }

        klog.debug("Parsing URL: " + url.getPath() + " >> " + url.toString());
        if (url.getProtocol().equalsIgnoreCase("ftp") && url.getHost().equalsIgnoreCase(GseaWebResources.getGseaFTPServer())) {
            try {
                FtpSingleUrlTransferCommand ftpCommand = new FtpSingleUrlTransferCommand(url);
                FtpResultInputStream ftpInputStream = ftpCommand.retrieveAsInputStream();
                
                // If it's a file for the local cache, fetch it by FTP, save it, and return a stream from the File.
                // NOTE: for now this is just one of the "special CHIPs" but it could be other things in the future.
                if (localFile != null && !localFile.exists()) {
                    org.apache.commons.io.FileUtils.copyInputStreamToFile(ftpInputStream, localFile);
                    return new BufferedInputStream(new FileInputStream(localFile));
                }
                
                return ftpInputStream;
            }
            catch (Exception e) {
                throw new IOException(e);
            }
        } else {
            return new BufferedInputStream(url.openStream());
        }
    }

    public static InputStream createInputStream(final Object source) throws IOException {

        if (source == null) {
            throw new IllegalArgumentException("Parameter source cannot be null");
        }

        if (source instanceof File) {
            return createInputStream((File) source);
        } else if (source instanceof URL) {
            return createInputStream((URL) source);
        }

        String path = source.toString();


        if (NamingConventions.isURL(path)) {
            // common error is ftp.broad... while it should be ftp://ftp.broad...

            if (path.startsWith("ftp.")) {
                path = "ftp://" + path;
            } else if (path.startsWith("gseaftp.")) {
                path = "ftp://" + path;
            }

            return createInputStream(new URL(path));
        }

        // Ok, it might be a file

        File file = new File(path);

        if (file.exists()) {
            return createInputStream(file);
        }

        throw new IOException("Bad data source -- neither file nor url exists for: " + source);
    }
}    // End ParserFactory