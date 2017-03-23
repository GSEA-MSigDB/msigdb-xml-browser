/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.genome;

import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Class NamingConventions
 * <p/>
 * Rules for object / default etc naming conventions are all captured here.
 *
 * @author Aravind Subramanian
 * @version %I%, %G%
 */
public class NamingConventions {
    private static final int MAX_FILE_LEN_ALLOWED = 100;

    public static boolean isURL(final String path) {
        if (path.toLowerCase().startsWith("http")
                || path.toLowerCase().startsWith("www")
                || path.toLowerCase().startsWith("ftp")
                ) {
            return true;
        } else {
            return false;
        }
    }

    /*
    private static Set kMonths;

    static {
        kMonths = new HashSet();
        kMonths.add("JAN");
        kMonths.add("FEB");
        kMonths.add("MAR");
        kMonths.add("APR");
        kMonths.add("MAY");
        kMonths.add("JUN");
        kMonths.add("JUL");
        kMonths.add("AUG");
        kMonths.add("SEP");
        kMonths.add("OCT");
        kMonths.add("NOV");
        kMonths.add("DEC");
    }
    */

    public static String titleize(String title) {

        if (isNull(title)) {
            return Constants.NULL;
        } else {

            if (title.startsWith("\"")) {
                title = title.substring(1, title.length());
            }

            if (title.endsWith("\"")) {
                title = title.substring(0, title.length() - 1);
            }

            return title;
        }

    }

    public static String symbolize(String symbol) {

        if (symbol == null) {
            return null;
        }

        symbol = symbol.trim();

        if (symbol.equals("---") ||
                // symbol.equalsIgnoreCase(Constants.NA) || // this is a problem as there is a gene symbol called NA !!
                symbol.equalsIgnoreCase(Constants.NULL) ||
                symbol.length() == 0) {

            return null;
        }

        symbol = symbol.toUpperCase(); // @note IMP IMp all sumbols are UC only

        /*
        // Then want to cath errors such as
        // 2-APR and convert them into the correct APR2
        // For the excel bug
        StringTokenizer tok = new StringTokenizer(symbol, "-");
        if (tok.countTokens() == 2) {
            String one = tok.nextToken();
            String two = tok.nextToken();
            try {
                int num = Integer.parseInt(one);
                if (kMonths.contains(two)) {
                    //System.out.println("### morphed: " + symbol + " into: " + two + num);
                    return two + num;
                }

                // do below
            } catch (Throwable t) {
                // keep going below
                //return symbol; // not a
            }
        }
        */

        // Finally if the symbol is toolung, truncate it
        // This happens when affy truncates many eaually likely genes into 1
        // e.g KLRA15 /// KLRA20 ...

        if (symbol.length() > 20) {
            return symbol.substring(0, 20).trim();
        } else {
            return symbol;
        }

    }

    public static String createNiceEnglishDate_for_dirs() {
        //Format formatter = new SimpleDateFormat("MMM_dd_yy");
        //Format formatter = new SimpleDateFormat("MMM_dd");
        // Get today's date
        Date date = new Date();
        Format formatter = new SimpleDateFormat("MMM");
        String s = (formatter.format(date));
        formatter = new SimpleDateFormat("dd");
        s = (s + (formatter.format(date))).toLowerCase();
        //s = s.replace('_', '\s');
        return s;
    }

    public static boolean isNull(String s) {
        if (s == null) {
            return true;
        }

        s = s.trim();

        if (s.length() == 0) {
            return true;
        }

        if (s.equalsIgnoreCase(Constants.NA)) {
            return true;
        }

        if (s.equalsIgnoreCase(Constants.NULL)) {
            return true;
        }


        return false;
    }


    // TODO: possibly use Commons IO FilenameUtils.  We're doing some extra sophisticated stuff here, though, so it may not be valid.
    public static String removeExtension(final String fileName) {

        if (fileName == null) {
            throw new IllegalArgumentException("Param fileName cannot be null");
        }

        if (fileName.indexOf(".") == -1) {
            return fileName;
        }

        if (fileName.indexOf('#') != -1) { // aux names are of the form: foo.cls#classA so removing the ext is deleterious
            return fileName;
        }

        if (fileName.endsWith(".")) { // simply strip off the period
            return fileName.substring(0, fileName.length() - 1);
        }

        // we also dont want to do the following:
        // foo_genes.hgu95av2.grp_mapped_symbols_from_HuGeneFL -> foo_genes.hgu95av2
        // (this would happen if we simply remove the extension)
        final int index = fileName.lastIndexOf('.');

        if (-1 == index) { // no periods
            return fileName;
        } else {
            String just_name = fileName.substring(0, index);
            String ext = fileName.substring(index + 1, fileName.length());
            if (ext.length() <= 4) {
                return just_name; // common heuristic
            }

            StringTokenizer tok = new StringTokenizer(ext, "_");
            tok.nextToken(); // get rid of the "ext"
            StringBuffer extra = new StringBuffer("_");
            while (tok.hasMoreElements()) {
                extra.append(tok.nextToken());
                if (tok.hasMoreElements()) {
                    extra.append("_");
                }
            }

            if (extra.length() != 1) {
                return just_name + extra;
            } else {
                return just_name;
            }

            //return filename.substring(0, index);
        }
    }

    // replaces chars that make file paths barf such as @ and #
    public static File createSafeFile(final File inDir, final String name) {
        return new File(inDir, createSafeFileName(name));
    }

    public static String createSafeFileName(String name) {
        name = name.trim();
        String safename = name.replace('@', '_'); // this does all occurrences
        safename = safename.replace('#', '_');
        safename = safename.replace(' ', '_'); // get rid of whitespace
        safename = safename.replace('%', '_');
        safename = safename.replace('$', '_');
        safename = safename.replace(':', '_'); // IE often doesnt like colons in linked to files
        safename = safename.replace('*', '_');
        safename = safename.replace('\\', '_');
        safename = safename.replace('/', '_');
        safename = safename.replace(Constants.INTRA_FIELD_DELIM, '_');

        if (safename.length() >= MAX_FILE_LEN_ALLOWED) {
            String ext = getExtension(safename);
            safename = safename.substring(0, MAX_FILE_LEN_ALLOWED) + "." + ext; // too many chars make excel (for instance) not recognize the file
        }

        return safename;
    }

    /**
     * Gets the extension of a specified file name. The extension is
     * everything after the last dot.
     * <p/>
     * <pre>
     * foo.txt    --> "txt"
     * a\b\c.jpg  --> "jpg"
     * foo        --> ""
     * </pre>
     *
     * @param f String representing the file name
     * @return extension of the file (or <code>""</code> if it had none)
     */
    public static String getExtension(final String f) {

        String ext;
        int pos;

        pos = f.lastIndexOf(".");

        if (pos == -1) {
            //log.warn("No extension found - returning \"\" file=" + f);
            ext = "";
        } else {
            ext = f.substring(pos + 1);
        }

        //og.debug("got: " + f + " foind: " + ext);

        if (f.indexOf(".meta.") != -1) {
            return "meta." + ext;
        } else {
            return ext;
        }
    }

    /**
     * Gets the extension of a specified file. The extension is
     * everything after the last dot.
     * <p/>
     * <pre>
     * foo.txt    --> "txt"
     * a\b\c.jpg  --> "jpg"
     * foo        --> ""
     * </pre>
     *
     * @param f the File
     * @return extension of the file (or <code>""</code> if it had none)
     * @see String getExtension(String f)
     */
    public static String getExtension(File f) {
        return getExtension(f.getName());
    }


    /**
     * Gets the extension of a specified file name. The extension is
     * everything after the last dot.  Files created with excel may
     * have had a .txt extension appended to the end of the intended file
     * name.  This routine checks txt files to see whether there is a
     * recognized extension preceding it.  Recognized extensions are those
     * associated with file formats that likely to be created/edited using
     * excel: GCT, RES, PCL, GMX, GMT, RNK.  We have introduced this "liberal"
     * parsing of file extension in order to cut back on the number of RT tickets.
     * </p>
     * <pre>
     * foo.txt    --> "txt"
     * a\b\c.jpg  --> "jpg"
     * foo        --> ""
     * foo.gct.txt --> "gct"
     * foo.gmx.txt --> "gmx"
     * </pre>
     *
     * @param f String representing the file name
     * @return extension of the file (or <code>""</code> if it had none)
     */
    public static String getExtensionLiberal(final String f) {

        String ext, ext2;
        int pos, pos2;

        pos = f.lastIndexOf(".");

        if (pos == -1) {
            //log.warn("No extension found - returning \"\" file=" + f);
            ext = "";
        } else {

            if (pos > 0) {
                pos2 = f.lastIndexOf(".", pos - 1 );
                if (pos2 != -1) {
                    ext2 = f.substring(pos2+1, pos);
                    if (ext2.equalsIgnoreCase(Constants.GCT))
                        return Constants.GCT;
                    else if (ext2.equalsIgnoreCase(Constants.RES))
                        return Constants.RES;
                    else if (ext2.equalsIgnoreCase(Constants.PCL))
                        return Constants.PCL;
                    else if (ext2.equalsIgnoreCase(Constants.GMX))
                        return Constants.GMX;
                    else if (ext2.equalsIgnoreCase(Constants.GMT))
                        return Constants.GMT;
                    else if (ext2.equalsIgnoreCase(Constants.RNK))
                        return Constants.RNK;
                    else if (ext2.equalsIgnoreCase(Constants.CLS))
                        return Constants.CLS;
                    }
            }
            ext = f.substring(pos + 1);
        }

        return ext;

    }

}    // End NamingConventions
