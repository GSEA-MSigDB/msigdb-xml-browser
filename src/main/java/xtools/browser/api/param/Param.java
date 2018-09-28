/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xtools.browser.api.param;

import edu.mit.broad.msigdb_browser.genome.Constants;
import edu.mit.broad.msigdb_browser.genome.swing.fields.GFieldPlusChooser;

/**
 * 4 kinds of parameters:
 * <p/>
 * 1) required and NO default provided. Example res file 2) required and a
 * default provided. Example metric 3) optional and no default provided 4)
 * optional and a default provided
 *
 * @author Aravind Subramanian
 * @version %I%, %G%
 */
public interface Param {

    public static final Param.Type REQUIRED = new Param.Type("required");
    public static final Param.Type PSEUDO_REQUIRED = new Param.Type("pseudo_required");
    public static final Param.Type BASIC = new Param.Type("basic");
    public static final Param.Type ADVANCED = new Param.Type("advanced");

    public String getName();

    public String getHtmlLabel_v3();

    public String getDesc();

    public String getNameEnglish();

    public Object[] getHints();

    public boolean isReqd();

    /**
     * note diff from isTrue This doesnt check the value, just for the existence
     * of a non-null value
     *
     * @return
     */
    public boolean isSpecified();

    /**
     * default value if any. Typically one of the Hint values
     */
    public Object getDefault();

    public Object getValue();

    public void setValue(Object val);

    /**
     * str rep of value appropriate for use in properties objects Example -< if
     * dataset, then file path If cnpair then cnpair name
     * <p/>
     * must be null if value is null
     */
    public String getValueStringRepresentation(boolean full);

    /**
     * is the data for this param coming from a file
     */
    public boolean isFileBased();

    /**
     * component which represents the choices allowed for this Param should be
     * selectable
     *
     * @return
     */
    public GFieldPlusChooser getSelectionComponent();

    /**
     * CONSTANTS FOR PARAM NAMES AND DESCS
     */

    public static final String CMETRIC = "cmetric";

    public static final String CLS = Constants.CLS;

    public static final String RES = Constants.RES;

    public static final String OUT = "out";
    public static final String OUT_ENGLISH = "Save results in this folder";
    public static final String OUT_DESC = "Path of the directory in which to place output from the analysis (any existing files will NOT be overwritten)";

    public static final String GRP = Constants.GRP;
    public static final String GRP_ENGLISH = "Gene set";
    public static final String GRP_DESC = "GeneSet (grp file; only 1 allowed)";

    public static final String GMX = Constants.GMX;

    public static final String GUI = "gui";
    public static final String GUI_DESC = "Display any reports that the tool produces in a Graphical User Interface";

    public static final String METRIC = "metric";

    public static final String ORDER = "order";

    public static final String RPT = "rpt_label";
    public static final String RPT_ENGLISH = "Analysis name";
    public static final String RPT_DESC = "Label for the analysis - any short phrase meaningful to you";

    public static final String SORT = "sort";

    public static final String DATAFORMAT_DATASET = "dataset_format";
    public static final String DATAFORMAT_DATASET_DESC = "Format of the Dataset created/exported";

    public static final String DATAFORMAT_GENESETMATRIX = "genesetmatrix_format";
    public static final String DATAFORMAT_GENESETMATRIX_ENGLISH = "Gene set matrix output format";
    public static final String DATAFORMAT_GENESETMATRIX_DESC = "Format of the Gene set matrix created/exported";

    // ---- META ---- //
    public static final String FILE_DESC = "Path to data file";

    public static class Type {

        private String type;

        private Type(String type) {
            this.type = type;
        }

        public String toString() {
            return type;
        }

        public boolean equals(Type type) {
            if (type == this) {
                return true;
            }

            return type != null && type.toString().equals(this.toString());

        }

    } // End class Type

} // End class Param
