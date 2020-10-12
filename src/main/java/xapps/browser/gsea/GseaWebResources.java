/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xapps.browser.gsea;

import edu.mit.broad.msigdb_browser.genome.parsers.AuxUtils;

/**
 * @author Aravind Subramanian
 */
public class GseaWebResources {

    private final static String GSEA_FTP_SERVER = "ftp.broadinstitute.org";
    private final static String GSEA_FTP_SERVER_USERNAME = "anonymous";
    private final static String GSEA_FTP_SERVER_PASSWORD = "gsea@broadinstitute.org";
    private final static String GSEA_FTP_SERVER_BASE_DIR = "/pub/gsea";
    private final static String GSEA_FTP_SERVER_CHIPFILES_SUB_DIR = "annotations_versioned";
    private final static String GSEA_FTP_SERVER_MSIGDB_FILE_CURRENT = "msigdb_v7.2.xml";
    private final static String GSEA_FTP_SERVER_XML_SUB_DIR = "/xml";

    public static String getGseaFTPServer() {
        return GSEA_FTP_SERVER;
    }

    public static String getGseaFTPServerUserName() {
        return GSEA_FTP_SERVER_USERNAME;
    }

    public static String getGseaFTPServerPassword() {
        return GSEA_FTP_SERVER_PASSWORD;
    }

    public static String getFTPBase() {
        return "ftp://" + GSEA_FTP_SERVER + GSEA_FTP_SERVER_BASE_DIR;
    }

    public static String getGseaFTPServerChipDir() {
        return GSEA_FTP_SERVER_BASE_DIR + "/" + GSEA_FTP_SERVER_CHIPFILES_SUB_DIR;
    }

    public static String getGseaFTPServerXMLDir() {
        return getFTPBase() + GSEA_FTP_SERVER_XML_SUB_DIR;
    }

    public static String getMSigDB_current_xml_fileName() {
        return GSEA_FTP_SERVER_MSIGDB_FILE_CURRENT;
    }

    public static String getGseaBaseURL() {
        return "http://www.gsea-msigdb.org/gsea";
    }

    public static String getGseaURLDisplayName() {
        return "www.gsea-msigdb.org/gsea";
    }

    public static String getGseaHelpURL() {
        return getGseaBaseURL() + "/wiki";
    }

    public static String getGseaContactURL() {
        return getGseaBaseURL() + "/contact.jsp";
    }

    public static String getGseaDataFormatsHelpURL() {
        return getGseaBaseURL() + "/wiki/index.php/Data_formats";
    }

    public static String getGeneSetURL(String gsetName) {
        gsetName = AuxUtils.getAuxNameOnlyNoHash(gsetName);
        return getGseaBaseURL() + "/msigdb/cards/" + gsetName + ".html";
    }

    public static String getPubMedURL(final String pmid) {
        return "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=PubMed&dopt=Citation&list_uids=" + pmid;
    }

} // End classes AppWebResources

