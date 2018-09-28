/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.vdb.map;

import edu.mit.broad.msigdb_browser.genome.Constants;

/**
 * @author Aravind Subramanian
 */
public class MappingDbTypes {

    public static MappingDbType GENE_SYMBOL = new MappingDbTypeImpl(Constants.GENE_SYMBOL);

    static class MappingDbTypeImpl implements MappingDbType {

        String type;

        MappingDbTypeImpl(String type) {
            this.type = type;
        }

        public boolean equals(Object obj) {
            if (obj instanceof MappingDbType) {
                return ((MappingDbType) obj).getName().equals(this.type);
            }

            return false;
        }

        public String getName() {
            return type;
        }

    }

} // End class MappingDbTypes
