/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.genome.parsers;

import edu.mit.broad.msigdb_browser.genome.*;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * public static utilities to aid in parsing
 * <p/>
 * <bold> Implementation Notes </bold><br>
 * There are a range of possible exceptions that can occur duing parsing.
 * Rather than throw an error-specific exception on encountering an error,
 * a ParseException is thrown that captures the base exception(s).
 * <p/>
 * <confirm-this>
 * In addition,
 * during parsing, often runtime (unchecked) exceptions are show-stoppers. These
 * too are caught and parcelled into ParseException.
 * </confirm-this>
 *
 * @author Michael Angelo
 * @author Aravind Subramanian
 * @version 1.0
 */
public class ParseUtils {

    private static final char DEFAULT_FIELD_SEP_CSV = ',';

    /**
     * NamingConventions delimiters - <pre>" \t\n,;:"</pre>
     */
    public final static String DEFAULT_DELIMS = " \t\n,;:";

    public static String[] string2strings(final String s,
                                          final String delim,
                                          final boolean useNullonMagicNullWord) throws IllegalArgumentException {

        if (null == s) {
            throw new NullPointerException("Cannot parse on null String");
        }

        StringTokenizer tok = new StringTokenizer(s, delim);
        String[] ret = new String[tok.countTokens()];
        int i = 0;

        while (tok.hasMoreTokens()) {
            ret[i] = _magic(tok.nextToken().trim(), useNullonMagicNullWord);
            i++;
        }

        return ret;
    }

    public static String[] string2stringsV2(String s) throws IllegalArgumentException {
        List ret = string2stringsV2_list(s);
        return (String[]) ret.toArray(new String[ret.size()]);
    }

    // double tabs are tolerated and no need for the NULL thing
    public static List string2stringsV2_list(String s) throws IllegalArgumentException {

        if (null == s) {
            throw new NullPointerException("Cannot work on null String");
        }

        s = s.trim(); // no tabs before or after

        StringTokenizer tok = new StringTokenizer(s, "\t", true); // note including the delim in rets
        List ret = new ArrayList();

        String prev = null;
        while (tok.hasMoreTokens()) {
            String curr = tok.nextToken(); // dont trim!
            if (!curr.equals("\t")) {
                ret.add(curr);
            }

            if (curr.equals(prev)) { // 2 consecutive tabs
                ret.add(""); //empty field
            }

            prev = curr;
        }

        return ret;
    }

    public static List string2stringsList(String s, String delim) throws IllegalArgumentException {

        if (null == s) {
            throw new NullPointerException("Cannot work on null String");
        }

        StringTokenizer tok = new StringTokenizer(s, delim);
        List ret = new ArrayList(tok.countTokens());

        while (tok.hasMoreTokens()) {
            ret.add(tok.nextToken().trim());
        }

        return ret;
    }

    public static Set string2stringsSet(String s, String delim, boolean respectNullMagicWord) throws IllegalArgumentException {

        if (null == s) {
            return new HashSet();
        }

        StringTokenizer tok = new StringTokenizer(s, delim);
        Set ret = new HashSet();

        while (tok.hasMoreTokens()) {
            String elem = tok.nextToken().trim();
            elem = _magic(elem, respectNullMagicWord);
            if (elem != null) {
                ret.add(elem);
            }
        }

        return ret;
    }

    private static final String[] MAGIC_NULLS = new String[]{Constants.NULL, Constants.NA, Constants.HYPHEN, Headers.AFFX_NULL};

    private static String _magic(String s, boolean respectNullMagicWord) {
        return _magic(s, MAGIC_NULLS, respectNullMagicWord);
    }

    /**
     * @noinspection ReturnOfNull
     */
    private static String _magic(String s, String[] magicNulls, boolean respectNullMagicWord) {

        if (respectNullMagicWord) {
            for (int i = 0; i < magicNulls.length; i++) {
                if (s.trim().equalsIgnoreCase(magicNulls[i])) {
                    return null;
                }
            }
        }

        return s;
    }

    /**
     * Convenience method to read in a ffn into an ArrayList Each vector
     * element corresponds to a single line (in order) from the ffn. (ffn->
     * file of file names) Blank lines in file are ignored Leading and trailing
     * whitespace is trimmed
     *
     * @param aFile Description of the Parameter
     * @return Description of the Return Value
     * @throws IOException Description of the Exception
     */
    public static List readFfn(File aFile) throws IOException {

        BufferedReader buf = new BufferedReader(new FileReader(aFile));
        String line;
        ArrayList lines = new ArrayList();

        line = nextLine(buf);

        while (line != null) {
            if (lines.contains(line) == false) {
                lines.add(line);
            }
            line = nextLine(buf);
        }

        buf.close();

        return lines;
    }

    public static String[] slurpIntoArray(URL url, boolean ignoreanycomments) throws IOException {
        Set set = slurpIntoSet(_buf(url), ignoreanycomments);
        return (String[]) set.toArray(new String[set.size()]);
    }

    public static Set slurpIntoSet(BufferedReader buf, boolean ignoreanycomments) throws IOException {
        Set set = new HashSet();
        String line;

        while ((line = buf.readLine()) != null) {
            line = line.trim();

            if (line.length() == 0) {
                continue;
            }

            if ((ignoreanycomments) && (line.startsWith(Constants.COMMENT_CHAR))) {
                continue;
            }

            set.add(line);
        }

        buf.close();

        return set;
    }

    /**
     * adds any comment lines found to the fComnent class var
     *
     * @param buf
     * @return
     * @throws IOException
     */
    public static String nextLine(BufferedReader buf) throws IOException {

        String currLine = buf.readLine();

        if (currLine == null) {
            return null;
        }

        currLine = currLine.trim();

        while ((currLine != null)
                && ((currLine.length() == 0) || (currLine.startsWith(Constants.COMMENT_CHAR)))) {
            currLine = buf.readLine();

            if (currLine != null) {
                currLine = currLine.trim();
            }
        }

        return currLine;
    }

    private static BufferedReader _buf(URL url) throws IOException {
        if (url == null) {
            throw new IllegalArgumentException("Parameter url cannot be null");
        }
        InputStreamReader isr = new InputStreamReader(url.openStream());
        return new BufferedReader(isr);
    }

    /**
     * parse: break the input String into fields
     *
     * @return java.util.Iterator containing each field
     *         from the original as a String, in order.
     */
    public static String[] string2strings_csv(final String csvLine) {
        final StringBuffer sb = new StringBuffer();

        final List list = new ArrayList();
        int i = 0;

        do {
            sb.setLength(0);
            if (i < csvLine.length() && csvLine.charAt(i) == '"') {
                i = advQuoted_for_csv(csvLine, sb, ++i);    // skip quote
            } else {
                i = advPlain_for_csv(csvLine, sb, i);
            }
            list.add(sb.toString());
            i++;
        } while (i < csvLine.length());

        return (String[]) list.toArray(new String[list.size()]);
    }

    public static List string2stringsList_csv(final String csvLine) {
        final String[] ss = string2strings_csv(csvLine);
        final List list = new ArrayList();
        for (int i = 0; i < ss.length; i++) {
            list.add(ss[i]);
        }

        return list;
    }

    /**
     * advQuoted: quoted field; return index of next separator
     */
    private static int advQuoted_for_csv(final String s, final StringBuffer sb, final int i) {
        int j;
        int len = s.length();
        for (j = i; j < len; j++) {
            if (s.charAt(j) == '"' && j + 1 < len) {
                if (s.charAt(j + 1) == '"') {
                    j++; // skip escape char

                } else if (s.charAt(j + 1) == DEFAULT_FIELD_SEP_CSV) { //next delimeter

                    j++; // skip end quotes

                    break;
                }
            } else if (s.charAt(j) == '"' && j + 1 == len) { // end quotes at end of line

                break; //done

            }
            sb.append(s.charAt(j));    // regular character.

        }
        return j;
    }

    /**
     * advPlain: unquoted field; return index of next separator
     */
    private static int advPlain_for_csv(final String s, final StringBuffer sb, final int i) {
        int j;

        j = s.indexOf(DEFAULT_FIELD_SEP_CSV, i); // look for separator

        if (j == -1) {                   // none found

            sb.append(s.substring(i));
            return s.length();
        } else {
            sb.append(s.substring(i, j));
            return j;
        }
    }


}    // End ParseUtils
