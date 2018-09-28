/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xtools.browser.api.param;

import edu.mit.broad.msigdb_browser.genome.objects.FSet;
import edu.mit.broad.msigdb_browser.genome.objects.GeneSet;
import edu.mit.broad.msigdb_browser.genome.objects.GeneSetMatrix;
import edu.mit.broad.msigdb_browser.genome.objects.GenesOfInterest;
import edu.mit.broad.msigdb_browser.genome.objects.PersistentObject;
import edu.mit.broad.msigdb_browser.genome.parsers.AuxUtils;
import edu.mit.broad.msigdb_browser.genome.parsers.DataFormat;
import edu.mit.broad.msigdb_browser.genome.parsers.ParseUtils;
import edu.mit.broad.msigdb_browser.genome.parsers.ParserFactory;
import edu.mit.broad.msigdb_browser.genome.swing.fields.GFieldPlusChooser;
import edu.mit.broad.msigdb_browser.vdb.chip.Chip;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.ListSelectionModel;

import xtools.browser.api.ui.GeneSetFieldPlusChooser;

/**
 * choose 1 or more string
 *
 * @author Aravind Subramanian
 * @version %I%, %G%
 * @note diff from StringsInputParam -> the chpices are presented in a JListWindow and not
 * in a text area
 */
public class GeneSetMultiChooserParam extends AbstractObjectChooserParam {

    private MyPobActionListener fAl;

    private GeneSetFieldPlusChooser fPlusChooser;

    /**
     * Class constructor
     *
     * @param name
     * @param nameEnglish
     * @param desc
     */
    public GeneSetMultiChooserParam(String name, String nameEnglish, String desc) {
        super(name, nameEnglish, GeneSet[].class, desc, new GeneSet[]{}, new GeneSet[]{}, false);
    }

    public GeneSetMultiChooserParam() {
        this(GRP, GRP_ENGLISH, GRP_DESC);
    }

    public GeneSet[] getGeneSets() throws Exception {
        Object[] objs = _getObjects();
        
        //log.debug("num of selections: " + objs.length);
        
        if (isReqd() && objs.length == 0) {
            throw new IllegalArgumentException("Must specify GeneSetMatrix parameter: " + getName());
        }
        
        List gsets = new ArrayList();
        
        // TODO: verify which of these are allowed in the Browser code paths.
        // May not wish to allow loading from Chip esp if only GENE_SYMBOL is loaded. Probably do not want.
        // GenesOfInterest is also prob. bogus for our use.
        for (int i = 0; i < objs.length; i++) {
            if (objs[i] instanceof GeneSetMatrix) {
                gsets.addAll(((GeneSetMatrix) objs[i]).getGeneSetsL());
            } else if (objs[i] instanceof GeneSet) {
                gsets.add(objs[i]);
            } else if (objs[i] instanceof Chip) {
                gsets.add(((Chip) objs[i]).getProbeNamesAsGeneSet());
            } else if (objs[i] instanceof GenesOfInterest) {
                gsets.add(((GenesOfInterest) objs[i]).getAsOneGeneSet());
            } else {
                throw new IllegalArgumentException("Unknown object: " + objs[i]);
            }
        }
        
        return (GeneSet[]) gsets.toArray(new GeneSet[gsets.size()]);
    }

    public GeneSet getGeneSetCombo() throws Exception {
        GeneSet[] gsets = getGeneSets();
        Set all = new HashSet();
        for (int i = 0; i < gsets.length; i++) {
            all.addAll(gsets[i].getMembersS());
        }

        return new FSet("combo", all);
    }

    /**
     * @param value
     */
    public void setValue(Object value) {

        if (value instanceof String[]) {
            this.setValue((String[]) value);
        } else if (value instanceof String) {
            super.setValue((String) value);
        } else {
            super.setValue(value);
        }
    }

    /**
     * @param paths
     */
    public void setValue(String[] paths) {
        try {
            PersistentObject[] pobs = new PersistentObject[paths.length];
            for (int i = 0; i < paths.length; i++) {
                File file = new File(paths[i]);
                PersistentObject pob = ParserFactory.read(file, true);
                if (DataFormat.isCompatibleRepresentationClass(pob, GeneSet.class)) { // @note not checking getType as that returns an array
                    pobs[i] = pob;
                } else {
                    throw new RuntimeException("Unexpected parsed value: " + pob + " class: " + pob.getClass() + " from file: " + file + " expecting: " + getTypes());
                }
            }

            super.setValue(pobs);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private Object[] _getObjects() throws Exception {

        Object val = getValue();

        Object[] objs;

        if (val instanceof String) {
            String[] paths = _parse(val.toString());
            objs = new Object[paths.length];
            for (int p = 0; p < paths.length; p++) {
                if (AuxUtils.isAux(paths[p])) {
                    objs[p] = ParserFactory.readGeneSet(new File(paths[p]));
                } else {
                    objs[p] = ParserFactory.read(new File(paths[p]), true);
                }
            }
        } else if (val instanceof Object[]) {
            objs = (Object[]) val;
        } else {
            objs = new Object[]{val};
        }

        return objs;
    }

    private static String[] _parse(String s) {

        if (s == null) {
            throw new IllegalArgumentException("Parameter s cannot be null");
        }

        Set vals = ParseUtils.string2stringsSet(s, ",", false); // only commas!!

        System.out.println("to parse>" + s + "< got: " + vals);

        Set use = new HashSet();
        for (Iterator it = vals.iterator(); it.hasNext();) {
            String key = it.next().toString();
            if (key.length() > 0) {
                use.add(key);
            }
        }

        return (String[]) use.toArray(new String[use.size()]);
    }

    // override base class method to do for both pobs and strings
    protected static String format(Object[] vals) {
        if (vals == null) {
            return "";
        }

        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < vals.length; i++) {

            if (vals[i] == null) {
                // do nothing
            } else {
                //log.debug(vals[i].getClass());
                if (vals[i] instanceof PersistentObject) {
                    String p = ParserFactory.getCache().getSourcePath(vals[i]);
                    buf.append(p);
                } else {
                    buf.append(vals[i].toString().trim());
                }

                if (i != vals.length - 1) {
                    buf.append(',');
                }
            }
        }

        return buf.toString();
    }

    // override base impl

    protected ActionListener getActionListener() {

        if (fAl == null) {
            this.fAl = new MyPobActionListener(true);
            fAl.setChooser(fPlusChooser);
        }

        return fAl;

    }

    public boolean isFileBased() {
        return true;
    }


    // redo from the abstract super class here as we dont want to swap out the model
    // (model is the datasets etc)
    // REIMPLEMENT HERE as we want to (possibly) use both genesets and genesetmatrixes
    private static class MyPobActionListener implements ActionListener {

        private boolean fMultipleAllowed;

        private GeneSetFieldPlusChooser fFieldPlusChooser;

        public MyPobActionListener(boolean multipleAllowed) {
            this.fMultipleAllowed = multipleAllowed;
        }

        // cant have this in the class constructor as the action list needs to
        // be instantiated before the chooser object is made
        public void setChooser(GeneSetFieldPlusChooser plusChooser) {
            this.fFieldPlusChooser = plusChooser;
        }

        public void actionPerformed(ActionEvent e) {

            if (fFieldPlusChooser == null) {
                //klog.debug("Chooser not yet inited: " + fChooser);
                return;
            }

            int selmode;
            if (fMultipleAllowed == false) {
                selmode = ListSelectionModel.SINGLE_SELECTION;
            } else {
                selmode = ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
            }

            //Object[] sels = fFieldPlusChooser.showMe(model, selmode, rend);
            Object[] sels = fFieldPlusChooser.showMeDirectly();

            if ((sels == null) || (sels.length == 0)) { // <-- @note
            } else {
                String[] paths = new String[sels.length];
                for (int i = 0; i < sels.length; i++) {
                    paths[i] = ParserFactory.getCache().getSourcePath(sels[i]);
                }

                String str = ChooserHelper.formatPob(sels);
                fFieldPlusChooser.setText(str);
            }
        }
    }


    // have to make the strs into paths
    public String getValueStringRepresentation(boolean full) {

        Object val = getValue();

        if (val == null) {
            return null;
        }

        if (val instanceof String) {
            return (String) val;
        } else if (val instanceof Object[]) {
            Object[] objs = (Object[]) val;
            return format(objs);
        } else {
            return format(new Object[]{val});
        }

    }

    public GFieldPlusChooser getSelectionComponent() {

        if (fPlusChooser == null) {
            fPlusChooser = new GeneSetFieldPlusChooser(false, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            fPlusChooser.setCustomActionListener(getActionListener());
            String text = this.getValueStringRepresentation(false);
            if (text == null) {
                text = format((Object[]) getDefault());
            }

            if (isFileBased()) { // as otherwise lots of exceptions thrown if user edits a bad file
                // @todo but problem is that no way to cancel and "null out" a choice once made
                //fChooser.getTextField().setEditable(false);
            }

            fPlusChooser.setText(text);
            ParamHelper.addDocumentListener(fPlusChooser.getTextField(), this);
        }

        return fPlusChooser;
    }

}    // End class GeneSetMultiParam


