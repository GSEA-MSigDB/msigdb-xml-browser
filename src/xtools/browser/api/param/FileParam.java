/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xtools.browser.api.param;

import edu.mit.broad.msigdb_browser.genome.NamingConventions;
import edu.mit.broad.msigdb_browser.genome.swing.choosers.GFileFieldPlusChooser;
import edu.mit.broad.msigdb_browser.genome.swing.fields.GFieldPlusChooser;

import java.io.File;
import java.net.URL;

/**
 * Object to capture commandline params
 */
public class FileParam extends AbstractParam {

    private GFileFieldPlusChooser fChooser;

    public FileParam(final String name, final String nameEnglish, final boolean reqd) {
        super(name, nameEnglish, File.class, FILE_DESC, new File[]{}, reqd);
    }

    public void setValue(final Object value) {

        if (value == null) {
            super.setValue(value);
        } else if (value instanceof File) {
            super.setValue(value);
        } else if (value instanceof String) {
            this.setValue(value.toString());
        } else if (value instanceof URL) {
            this.setValue(value.toString());
        } else {
            throw new IllegalArgumentException("Invalid type, only File and String accepted. Specified: " + value + " class: "
                    + value.getClass());
        }
    }

    public void setValue(final File file) {
        super.setValue(file);
    }

    public void setValue(final String path) {
        super.setValue(path); // changed for the url thing Feb 2006
        //super.setValue(new File(filepath));
    }

    public void setValue_url(final String filepath) {
        super.setValue(filepath);
    }

    public boolean isFileBased() {
        return true;
    }

    public String getValueStringRepresentation(boolean full) {

        Object val = getValue();

        if (val == null) {
            return null;
        } else {

            if (val instanceof URL) {
                return val.toString();
            } else if (val instanceof File) {
                final File file = (File) getValue();
                if (full) {
                    return file.getAbsolutePath();
                } else {
                    return file.getName();
                }
            } else if (NamingConventions.isURL(val.toString())) {
                return val.toString();
            } else {
                return val.toString();
            }
        }
    }

    public GFieldPlusChooser getSelectionComponent() {

        if (fChooser == null) {
            fChooser = new GFileFieldPlusChooser();
            if (getValue() != null) {
                fChooser.setValue(getValue());
            } else {
                fChooser.setValue(getDefault());
            }

            ParamHelper.addDocumentListener(fChooser.getTextField(), this);

        }

        return fChooser;
    }


}    // End class FileParam
