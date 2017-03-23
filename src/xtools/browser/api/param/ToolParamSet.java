/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xtools.browser.api.param;

import edu.mit.broad.msigdb_browser.genome.XLogger;

import org.apache.log4j.Logger;

import java.util.*;

/**
 * enforces checking to make sure no parameters are reused
 * only 1 param of each type added
 *
 * @author Aravind Subramanian
 * @version %I%, %G%
 */
public class ToolParamSet implements ParamSet {

    private final List fReqdParams;
    private final List fOptParams;
    private final Logger log = XLogger.getLogger(ToolParamSet.class);

    /**
     * Class constructor
     *
     * @param name
     */
    public ToolParamSet() {
        this.fReqdParams = new ArrayList();
        this.fOptParams = new ArrayList();
    }

    public void addParam(final Param param) {

        if (param == null) {
            throw new IllegalArgumentException("Null param not allowed");
        }

        //log.debug("Adding opt param: " + param.key + " " + param.val);
        checkUniqueness(param);

        if (param.isReqd()) {
            fReqdParams.add(param);
        } else {
            fOptParams.add(param);
        }
    }

    /**
     * combined total -> reqd + opt
     *
     * @return
     */
    public int getNumParams() {
        return fReqdParams.size() + fOptParams.size();
    }

    /**
     * in order, reqd first and then opt
     *
     * @param pos
     * @return
     */
    public Param getParam(final int pos) {

        if (pos < fReqdParams.size()) {
            return (Param) fReqdParams.get(pos);
        } else {
            return (Param) fOptParams.get(pos - fReqdParams.size());
        }
    }

    // NO checking
    public Properties toProperties() {

        final Properties props = new Properties();

        for (int i = 0; i < getNumParams(); i++) {
            final Param param = getParam(i);
            //log.debug("name=" + param.getName() + " value=" + param.getValue() + " str=" + param.getValueStringRepresentation());
            String s = param.getValueStringRepresentation(true);
            if (s != null) {
                s = s.trim();
                if (s.length() > 0) {
                    props.setProperty(param.getName(), s);
                }
            }
        }

        return props;
    }

    public boolean isRequiredAllSet() throws RuntimeException {

        for (int i = 0; i < fReqdParams.size(); i++)
        { // @todo make this actually work (i think its gets confused on Object[]{})
            final Param param = (Param) fReqdParams.get(i);
            //log.debug(param.getName() + " is set: " + param.isSpecified() + " def: " + param.getDefault());
            if ((!param.isSpecified()) && (param.getDefault() == null)) {
                //log.debug("FALSE!");
                return false;
            }
        }

        return true;
    }

    /**
     * returns a safe copy
     *
     * @return
     */
    public Param[] getParams() {

        List all = new ArrayList();

        for (int i = 0; i < fReqdParams.size(); i++) {
            all.add(fReqdParams.get(i));
        }

        for (int i = 0; i < fOptParams.size(); i++) {
            all.add(fOptParams.get(i));
        }

        return (Param[]) all.toArray(new Param[all.size()]);
    }

    /**
     * reports label param if available
     * else null
     *
     * @return
     */
    public ReportLabelParam getReportLabelParam() {
        for (int i = 0; i < getNumParams(); i++) {
            Param p = getParam(i);
            if (p instanceof ReportLabelParam) {
                return (ReportLabelParam) p;
            }
        }

        return null;

    }

    /**
     * param whether reqd or not cannot be in duplicate
     *
     * @param param
     */
    private void checkUniqueness(Param param) {

        if (param == null) {
            throw new IllegalArgumentException("param cannot be null");
        }

        //log.debug("fOptParams: " + fOptParams + " size: " + fOptParams.size());
        //log.debug("fReqdParams: " + fReqdParams + " size: " + fReqdParams.size());
        if (fOptParams.contains(param)) {
            throw new RuntimeException("Duplicated param in declarations - already have param: "
                    + param + " # params: " + getNumParams()
                    + " in the opt param list");
        }

        if (fReqdParams.contains(param)) {
            throw new RuntimeException("Duplicated param in declarations - already have param: "
                    + param.getName() + " # params: " + getNumParams()
                    + " in the reqd param list");
        }
    }

}    // End ToolParamSet

/**
 * MOVED TO SEPERATE CLASS - DELETE LATER IF FOUND TO BE OK
 *
 * @todo '-' in file paths/names causes the parsing to barf
 *
 * two kinds of keyvals are allowed
 * 1) -cls foo    <-- parameter name "cls" has value foo
 * 2) -tag        <-- parameter name "tag: is TRUE
 * Trouble is that we need to know if tag is a boolean param or not
 * value less specification is only allowed for booleans
 * Other examples
 * -tag false  <-- param tag is Boolean.FALSE
 * -names foo,bar,zok -> param names -> foo, bar, zok (NOT parsed here)
 *
 * @param args
 */
/*
public void fill(String[] args) {

    // first recreate the arg line -- its easier to parse
    StringBuffer buf = new StringBuffer();

    for (int i = 0; i < args.length; i++) {
        buf.append(args[i]).append(" ");
    }

    String argline = buf.toString().trim();

    StringTokenizer tok = new StringTokenizer(argline, "-"); // IMP spaces are NOT delimiters at this stage

    Set keyval = new HashSet();
    while (tok.hasMoreElements()) {
        String kv = tok.nextToken();
        keyval.add(kv);
        //log.debug("found kv: " + kv);
    }

    // now fill them up
    Iterator it = keyval.iterator();
    while (it.hasNext()) {
        String kv = it.next().toString();
        tok = new StringTokenizer(kv, " ="); // '=' as a favor to human errors
        _fillParam(tok, kv);
    }

}

private void _fillParam(StringTokenizer tok, String origString) {
    List tokens = ParseUtils.getUniqueTokens(tok);

    // now the trouble is that the second param, sometimes a file name, can have spaces
    // note that in general there is nothing to prevent the 'value' from having a space
    // as it doesnt affect the '-' based tokenizing (' ' is NOT a delim that seperates 'key-val' pairs
    // so as a mechanism to allow space in file names (the most common instance where space occurs in  a key-val)

    String param_name = null;
    String param_val = null;

    if (tokens.size() == 1) {
        param_name = tokens.get(0).toString();
    } else if (tokens.size() == 2) {
        param_name = tokens.get(0).toString();
        param_val = tokens.get(1).toString();
    } else {
        log.warn("More than 2 tokens for key-value pair >" + origString + "<" + " " + tokens.size());
        int num = tokens.size();
        param_name = tokens.get(0).toString();

        // heres the space fix
        param_val = "";
        for (int i = 1; i < num; i++) {
            param_val = param_val + tokens.get(i) + " ";
        }
    }

    param_name = param_name.trim();
    if (param_val != null) {
        param_val = param_val.trim();
    }

    //System.out.println("Doing >" + param_name + "< >" + param_val + "<");

    Param p = getParam(param_name);
    //log.debug("Asked for: " + param_name + " got: " + p);
    if (p == null) {
        log.warn("Invalid parameter for xtool >" + param_name + "< THIS PARAMETER WAS IGNORED!!");
        // dont barf (happens on cmd line sometimes, no point penalizing totally)
        //throw new IllegalArgumentException("Invalid parameter for xtool: " + param_name);
    }

    if ((param_val == null) || (param_val.length() == 0)) {
        if (p instanceof BooleanParam) {
            param_val = Boolean.TRUE.toString(); // @note
        }
    } else if (param_val == null) {
        throw new IllegalArgumentException("Invalid empty value for parameter: " + param_name);
    }

    p.setValue(param_val);

}
*/