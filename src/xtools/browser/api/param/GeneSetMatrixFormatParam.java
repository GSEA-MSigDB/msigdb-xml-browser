/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xtools.browser.api.param;

import edu.mit.broad.msigdb_browser.genome.parsers.DataFormat;

/**
 * @author Aravind Subramanian
 * @version %I%, %G%
 */
public class GeneSetMatrixFormatParam extends DataFormatAbstractParam {

    /**
     * Class constructor
     *
     * @param name
     * @param desc
     */
    public GeneSetMatrixFormatParam() {
        super(DATAFORMAT_GENESETMATRIX, DATAFORMAT_GENESETMATRIX_ENGLISH, DATAFORMAT_GENESETMATRIX_DESC,
                DataFormat.GMT_FORMAT, DataFormat.ALL_GENESETMATRIX_FORMATS, false);
    }

}    // End class GeneSetMatrixFormatParam
