/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.genome.parsers;

import edu.mit.broad.msigdb_browser.genome.Constants;
import edu.mit.broad.msigdb_browser.genome.JarResources;
import edu.mit.broad.msigdb_browser.genome.NamingConventions;
import edu.mit.broad.msigdb_browser.genome.objects.*;
import edu.mit.broad.msigdb_browser.genome.reports.api.Report;
import edu.mit.broad.msigdb_browser.genome.utils.SystemUtils;
import edu.mit.broad.msigdb_browser.vdb.chip.Chip;
import edu.mit.broad.msigdb_browser.vdb.msigdb.MSigDB;

import javax.swing.*;
import javax.swing.filechooser.FileView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Central repository for data formats.
 * A DataFormat is primarily identified by a file extension.
 * Also associates thungs like a desc, icon etc with DataFprmats.
 *
 * @author Aravind Subramanian
 * @version %I%, %G%
 */
public class DataFormat extends DataType implements Constants {


    /**
     * Class constructor
     * Does nothing. Dont use -- static methods only
     */
    public DataFormat() {
    }

    public static final DataFormat XLS_FORMAT = new DataFormat(ExtFormat.class, "Excel",
            "Microsoft Excel", Constants.XLS,
            JarResources.getIcon("Xls.gif"));
    public static final DataFormat TXT_FORMAT = new DataFormat(ExtFormat.class, "Text",
            "Plain Text", "txt",
            JarResources.getIcon("Txt.gif"));
    public static final DataFormat XML_FORMAT = new DataFormat(ExtFormat.class, "XML",
            "Extensible Markup Language", Constants.XML,
            JarResources.getIcon("Xml.gif"));

    public static final DataFormat GRP_FORMAT = new DataFormat(GeneSet.class, "GeneSet",
            "MIT GeneSet Format", GRP,
            JarResources.getIcon("Grp.gif"));

    public static final DataFormat GMX_FORMAT = new DataFormat(GeneSetMatrix.class, "GeneSetMatrix",
            "MIT format for a Matrix of Gene Sets",
            Constants.GMX, JarResources.getIcon("Gmx.png"));

    public static final DataFormat GMT_FORMAT = new DataFormat(GeneSetMatrix.class,
            "GeneSetMatrix_Transposed",
            "MIT format for a Matrix of Gene Sets",
            Constants.GMT, JarResources.getIcon("Gmt.png"));

    public static final DataFormat MSIGDB_FORMAT = new DataFormat(MSigDB.class,
            "MSigDB",
            "MSigDB format for a database of gene sets and their annotations",
            Constants.XML, JarResources.getIcon("Xml.gif"));

    public static final DataFormat RPT_FORMAT = new DataFormat(Report.class, "Report",
            "Report for a program",
            Constants.RPT, JarResources.getIcon("Rpt.gif"));

    public static final DataFormat CHIP_FORMAT = new DataFormat(Chip.class, "Chip",
            "Chip",
            Constants.CHIP, JarResources.getIcon("Chip16.png"));

    /**
     * @maint manually keep in synch with declared formats
     */

    // imp to not expose
    private static final DataFormat[] ALL = new DataFormat[]{
            GRP_FORMAT, GMT_FORMAT, GMX_FORMAT,
            MSIGDB_FORMAT,
            RPT_FORMAT,
            CHIP_FORMAT,
            XLS_FORMAT, TXT_FORMAT, XML_FORMAT
    };

    public static final DataFormat[] ALL_GENESETMATRIX_FORMATS = new DataFormat[]
            {GMX_FORMAT, GMT_FORMAT};

    static class ParsableFileView extends FileView {

        /**
         * Custom icons for file types that have associated actions.
         */
        public Icon getIcon(final File file) {
            return DataFormat.getIconOrNull(file);
        }

        /**
         * Default handling.
         * Let the L&F FileView figure this out.
         */
        public String getTypeDescription(final File file) {
            return null;
        }

        /**
         * Default handling.
         * Let the L&F FileView figure this out.
         */
        public String getDescription(final File file) {
            return DataFormat.getDesc(file);
        }

    }    // End class ParsableFileView


    public static FileView getParsableFileView() {
        return new ParsableFileView();
    }


    /**
     * @param obj Typically a DataFormat or ext.
     *            Ok, to specify the toString value alos (for example: Foo[bar])
     *            This is used typically form a combo box.
     * @return
     */
    public static DataFormat getExtension(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Parameter obj cannot be null");
        }

        String ext;
        if (obj instanceof DataFormat) {
            ext = ((DataFormat) obj).getExtension();
        } else {
            ext = obj.toString();
        }

        for (int i = 0; i < ALL.length; i++) {
            if (ALL[i].getExtension().equalsIgnoreCase(ext)) {
                return ALL[i];
            }

            if (ALL[i].toString().equalsIgnoreCase(ext)) { // ok, to specify the toString value alos (for example: Foo[bar])
                return ALL[i];
            }
        }

        throw new IllegalArgumentException("No DataFormat found for: " + ext);
    }


    /**
     * key -> Class (one of the pobs), value -> string ext
     */
    private static final Map kClassExtMap;

    /**
     * key -> ext, value -> DataFormat that represents that ext
     */
    private static final Map kExtDfMap;

    /**
     * key -> classname, value -> icon to represent the class
     */
    private static final Map kClassIconMap;

    /**
     * key -> class, value->corresp dataformats name
     */
    private static final Map kClassNameMap;

    static {

        /**
         * Hash the internal data formats as client code deals with them
         * in object form and nees to lookup things like, extension, icon etc.
         */
        kClassExtMap = new HashMap();
        kExtDfMap = new HashMap();
        kClassIconMap = new HashMap();
        kClassNameMap = new HashMap();

        for (int i = 0; i < ALL.length; i++) {
            kExtDfMap.put(ALL[i].getExtension(), ALL[i]);

            Class repClass = ALL[i].getRepresentationClass();

            if (ExtFormat.class.isAssignableFrom(repClass)) {
                // dont bother hashing
            } else {

                // use the first one as the default
                if (!kClassNameMap.containsKey(repClass)) {
                    kClassNameMap.put(ALL[i].getRepresentationClass(), ALL[i].getName());
                }
                if (!kClassExtMap.containsKey(repClass)) {
                    kClassExtMap.put(ALL[i].getRepresentationClass(), ALL[i].getExtension());
                }

                if (!kClassIconMap.containsKey(repClass)) {
                    kClassIconMap.put(ALL[i].getRepresentationClass(), ALL[i].getIcon());
                }
            }
        }
    }

    /**
     * Class variables
     */
    private String fName;
    private String fDesc;
    private Icon fIcon;
    private String fExt;

    /**
     * Class to represent a (and all) external data formats.
     * Such as ms excel, adobe pdf etc - things that have no pob's to
     * represent them.
     *
     * @author Aravind Subramanian
     * @version %I%, %G%
     */
    private class ExtFormat {
    }

    /**
     * Class Constructor.
     * <p/>
     * ext should not include the ".". For example cls files
     * have the ext as "cls" and not ".cls"
     * @param ext The extension.
     * @param dummy TODO
     *
     * @throws java.lang.IllegalArgumentException
     *          on invalid ext and bad input params
     */

    public DataFormat(Class repClass, String name, String desc, String ext, Icon icon)
            throws IllegalArgumentException {

        super(repClass, ext);

        if (name == null) {
            throw new IllegalArgumentException("Param name cannot be null");
        }

        if (desc == null) {
            throw new IllegalArgumentException("Param desc cannot be null");
        }

        if (!SystemUtils.isHeadless() && icon == null) {
            throw new IllegalArgumentException("Param icon cannot be null");
        }

        if (ext == null) {
            throw new IllegalArgumentException("Null extension is not allowed: " + ext);
        }

        if (ext.startsWith(".")) {
            throw new IllegalArgumentException("DataFormat extension cannot begin with a period '.' " + ext);
        }

        if (!(ext.length() > 0)) {
            throw new IllegalArgumentException("DataFormat extension cannot have zero length "
                    + ext);
        }

        // parser can be null

        this.fExt = ext;
        this.fIcon = icon;
        this.fName = name;
        this.fDesc = desc;
    }

    /**
     * @return Name of this DataFormat
     *         In "English"
     */
    public String getName() {
        return fName;
    }

    /**
     * @return A short description of this DataFormat
     */
    public String getDesc() {
        return fDesc;
    }

    /**
     * @return Extension for this DataFormat
     * @see "IMP to NOT use. As this makes a
     *      String(foo) equal to an Extension("foo")"
     */

    /**
     * @return Extension for this DataFormat
     */
    public String getExtension() {
        return fExt;
    }

    /**
     * @return Icon standard for this DataFormat
     */
    public Icon getIcon() {
        return fIcon;
    }

    /**
     * @return
     * @note IMP IMP: overriding the base (java mime-type impl) to get a more user friendly output
     * -- used in combo boxes etc.
     * (dont leave spaces though as needs to be used in cmd line also)
     */
    public String toString() {
        return new StringBuffer(getName()).append("[").append(getExtension()).append("]").toString();
    }

    /**
     * @param obj
     * @return
     * @note IMP: Again, override base impl so that lookups based on the EXT value work
     */
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (obj instanceof DataFormat) {
            if (((DataFormat) obj).fExt.equals(this.fExt)) {
                return true;
            }
        } else {
            return fExt.equalsIgnoreCase(obj.toString());
        }

        return false;
    }

    //------------------------------------------------------------------------------//
    // --------------------------- STATIC LOOKUP METHODS ---------------------------//
    //------------------------------------------------------------------------------//

    /**
     * @param ext
     * @return
     */
    public static DataFormat getDataFormat(String ext) {

        Object obj = kExtDfMap.get(ext);

        if (obj != null) {
            return (DataFormat) obj;
        } else {
            return null;
        }
    }

    /**
     * @param ext
     * @return standard Icon for specified ext, if known.
     *         Else JarResources.ICON_NOT_FOUND.
     */
    public static Icon getIcon(final String ext) {

        //log.info("Getting getIcon for ext: " + ext);
        DataFormat df = getDataFormat(ext);

        if (df != null) {
            return df.getIcon();
        } else {
            return JarResources.ICON_UNKNOWN_DATA_FORMAT;
        }
    }

    public static Icon getIconOrNull(final String ext) {

        //log.info("Getting getIcon for ext: " + ext);
        DataFormat df = getDataFormat(ext);

        if (df != null) {
            return df.getIcon();
        } else {
            return null;
        }
    }

    public static String getDesc(File file) {

        //log.info("Getting getIcon for ext: " + ext);
        String ext = NamingConventions.getExtension(file).toLowerCase();
        DataFormat df = getDataFormat(ext);

        if (df != null) {
            return df.getDesc();
        } else {
            return null;
        }
    }

    /**
     * @param obj
     * @return
     */
    public static Icon getIcon(final Object obj) {
        if (obj == null) {
            return JarResources.ICON_NOT_FOUND;
        } else if (obj instanceof File) {
            return getIcon(NamingConventions.getExtension((File) obj));
        } else if (obj instanceof String) {
            return getIcon(NamingConventions.getExtension(obj.toString()));
        } else {
            Object ic = kClassIconMap.get(obj.getClass());
            if (ic == null && obj instanceof PersistentObject) {
                Class repcl = getRepresentationClass((PersistentObject) obj);
                ic = kClassIconMap.get(repcl);
            }

            if (ic == null) {
                return JarResources.ICON_NOT_FOUND;
            } else {
                return (Icon) ic;
            }
        }
    }

    /**
     * The class that represent specified object
     * Central location where objects are linked with registered classes.
     * <p/>
     * why? other generic ways are tough to implement when dealing with interfaces, subclasses etc
     *
     * @param pob
     * @return
     * @maint add new objects and this will need updating
     */
    public static Class getRepresentationClass(final PersistentObject pob) {

        // TODO: further removal of unused file formats.
        // Almost certainly not used: 
        // - GenesOfInterest (GIN)
        // In use with absolute certainty: 
        // - GeneSetMatrix (GRP, GMX, GMT)
        // - Chip (CHIP)
        // Likely in use: Report (RPT), MSigDB
        if (pob instanceof GenesOfInterest) {
            return GenesOfInterest.class;
        } else if (pob instanceof GeneSet) {
            return GeneSet.class;
        } else if (pob instanceof GeneSetMatrix) {
            return GeneSetMatrix.class;
        } else if (pob instanceof Report) {
            return Report.class;
        } else if (pob instanceof Chip) {
            return Chip.class;
        } else if (pob instanceof MSigDB) {
            return MSigDB.class;
        } else {
            throw new IllegalArgumentException("Unknown object: " + pob);
        }
    }

    public static Icon getIconOrNull(final File file) {
        String ext = NamingConventions.getExtension(file).toLowerCase();
        return getIconOrNull(ext);
    }

    // @todo this is buggy (see saving reports for error)
    public static boolean isCompatibleRepresentationClass(Object obj, Class cl) {

        if (obj == null) {
            throw new IllegalArgumentException("Parameter obj cannot be null");
        }

        if (cl == null) {
            throw new IllegalArgumentException("Parameter cl cannot be null");
        }

        // first check directly
        if (obj.getClass().getName().equals(cl.getName())) {
            return true;
        }

        // then check interfaces in obj
        Class[] interfacesA = obj.getClass().getInterfaces();
        for (int i = 0; i < interfacesA.length; i++) {
            if (cl.getName().equals(interfacesA[i].getName())) {
                return true;
            }
        }

        // then check interfaces in cl
        Class[] interfacesB = cl.getInterfaces();
        for (int i = 0; i < interfacesB.length; i++) {
            if (obj.getClass().getName().equals(interfacesB[i].getName())) {
                return true;
            }
        }

        // then check interfaces vs interfaces
        for (int a = 0; a < interfacesA.length; a++) {
            for (int b = 0; b < interfacesB.length; b++) {
                if (interfacesA[a].getName().equals(interfacesB[b].getName())) {
                    return true;
                }
            }
        }

        // @todo fix me see GeneSetMatrixReqdParam uses for why
        if (obj instanceof GeneSetMatrix) {
            return true; // disable the check
        }

        // ok, thats its
        return false;
    }

}    // End DataFormat
