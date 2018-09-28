/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.genome.parsers;

import edu.mit.broad.msigdb_browser.genome.Headers;
import edu.mit.broad.msigdb_browser.genome.objects.FSet;
import edu.mit.broad.msigdb_browser.genome.objects.GeneSet;
import edu.mit.broad.msigdb_browser.genome.objects.PersistentObject;
import edu.mit.broad.msigdb_browser.vdb.msigdb.*;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MSigDBParser extends AbstractParser implements Headers.MSigDB {

    // cant have tab (would jave been nice as easier to use in excel as when read _in_
    // the dom library seems to convert tabs into spaces
    // private static final char DELIM = ' ';

    // private boolean fDeepInitAtLoad;

    /**
     * Class Constructor.
     */
    public MSigDBParser() {
        super(MSigDB.class);
    }

    /**
     *
     */
    // i prefer to write it out manually rather than use the dom4j method as want control
    // over the format of the file for easy vieweing in text viewer etc
    public void export(final PersistentObject pob, final File file) throws Exception {
        export((MSigDB) pob, file, true);
    }    // End export

    public void export(final MSigDB msigdb, final File file, final boolean convertToSymbols) throws Exception {

        final PrintWriter pw = startExport(msigdb, file);
        final Document document = DocumentHelper.createDocument();
        final Element root = document.addElement(MSIGDB);
        root.addAttribute("NAME", msigdb.getName());
        root.addAttribute("VERSION", msigdb.getVersion());
        root.addAttribute("BUILD_DATE", msigdb.getBuildDate());

        for (int g = 0; g < msigdb.getNumGeneSets(); g++) {
            final GeneSetAnnotation ann = msigdb.getGeneSetAnnotation(g);
            _addGeneSetElement(root, ann, convertToSymbols);
        }

        final OutputFormat format = OutputFormat.createPrettyPrint();
        //OutputFormat format = OutputFormat.createCompactFormat();
        final XMLWriter writer = new XMLWriter(pw, format);
        writer.write(document);
        writer.close();

        pw.close();

        doneExport();

    }    // End export

    public static void setGeneSetAttributes(final Element el, final GeneSetAnnotation ann, final boolean convertToSymbols) {
        el.addAttribute(STANDARD_NAME, ann.getStandardName());
        el.addAttribute(SYSTEMATIC_NAME, ann.getLSIDName());
        el.addAttribute(ORGANISM, ann.getOrganism().toString());

        GeneSetExternalLinks links = ann.getExternalLinks();
        addAttribute_safe(el, PMID, links.getPMID());
        addAttribute_safe(el, GEOID, links.getGeoID());
        addAttribute_safe(el, GENESET_LISTING_URL, links.getSpecificGeneSetListingURL());
        addAttribute_safe(el, EXTERNAL_DETAILS_URL, links.getExtDetailsURL());

        el.addAttribute(CHIP, ann.getChipOriginal_name());
        el.addAttribute(CATEGORY_CODE, ann.getCategory().getCode());
        el.addAttribute(CONTRIBUTOR, ann.getContributor().getName());
        el.addAttribute(DESCRIPTION_BRIEF, ann.getDescription().getBrief());
        el.addAttribute(DESCRIPTION_FULL, ann.getDescription().getFull());
        el.addAttribute(TAGS, _toString(ann.getTags()));
        el.addAttribute(MEMBERS, toString(ann.getGeneSet(true)));
        if (convertToSymbols) {
            el.addAttribute(MEMBERS_SYMBOLIZED, toString(ann.getGeneSet(false)));
        }
    }

    /*
    public void setDeepInitAtLoad(final boolean setDeepInitAtLoad) {
        this.fDeepInitAtLoad = setDeepInitAtLoad;
    }
    */

    private static Element _addGeneSetElement(final Element root, final GeneSetAnnotation ann, final boolean convertToSymbols) {
        final Element el = root.addElement(GENE_SET);
        setGeneSetAttributes(el, ann, convertToSymbols);
        return el;
    }

    private static void addAttribute_safe(Element el, String attName, String attValue) {
        if (attValue != null) {
            el.addAttribute(attName, attValue);
        }
    }

    private static String toString(final GeneSet gset) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < gset.getNumMembers(); i++) {
            buf.append(gset.getMember(i));
            if (i != gset.getNumMembers() - 1) {
                buf.append(',');
            }
        }

        return buf.toString();
    }

    /**
     * @see above for format
     */
    public List parse(String sourcepath, InputStream is) throws Exception {

        startImport(sourcepath);

        SAXReader reader = new SAXReader();
        Document document = reader.read(is);

        Element root = document.getRootElement();

        // first ensure that the meg exists
        String msigdb_name = root.attribute("NAME").getValue();
        String msigdb_version = root.attribute("VERSION").getValue();
        String msigdb_build_date = root.attribute("BUILD_DATE").getValue();

        // then onto the elements
        List list = new ArrayList();

        int cnt = 0;
        // each element is converted into a GeneSetAnn Object
        for (Iterator i = root.elementIterator(GENE_SET); i.hasNext();) {
            Element el = (Element) i.next();
            list.add(_parseOneGsa(el));

            if (cnt % 500 == 0) {
                System.out.println("read in from gsetann: " + (cnt + 1));
            }

            cnt++;
        }

        final MSigDB db = new MSigDBImpl(msigdb_name, msigdb_version, msigdb_build_date,
                (GeneSetAnnotation[]) list.toArray(new GeneSetAnnotation[list.size()]));
        db.addComment(fComment.toString());

        doneImport();
        return unmodlist(db);
    }

    private static String attribute_safe(final Element el, final String attName) {
        Attribute att = el.attribute(attName);
        if (att != null) {
            return att.getValue();
        } else {
            return null;
        }
    }

    private static String attribute_reqd(final Element el, final String attName) {
        Attribute att = el.attribute(attName);
        if (att != null) {
            return att.getValue();
        } else {
            throw new IllegalArgumentException("Expected attribute: >" + attName + "< was not found");
        }
    }

    private GeneSetAnnotation _parseOneGsa(final Element el) throws Exception {
        final GeneSet gset = new FSet(el.attribute(STANDARD_NAME).getValue(), _fromSet(el, MEMBERS)); // name of set doesnt matter much

        // @note change Nov 2006 -- the xml has the proper symbols already
        final GeneSet gset_symbols = new FSet(el.attribute(STANDARD_NAME).getValue(), _fromSet(el, MEMBERS_SYMBOLIZED)); // name of set doesnt matter much

        GeneSetAnnotation gsann = new GeneSetAnnotationImpl(
                gset,
                gset_symbols,
                attribute_reqd(el, CHIP),
                attribute_reqd(el, CONTRIBUTOR),
                attribute_safe(el, PMID),
                attribute_safe(el, GEOID),
                attribute_safe(el, GENESET_LISTING_URL),
                attribute_safe(el, EXTERNAL_DETAILS_URL),
                attribute_reqd(el, STANDARD_NAME),
                attribute_reqd(el, SYSTEMATIC_NAME),
                attribute_reqd(el, ORGANISM),
                attribute_reqd(el, CATEGORY_CODE),
                attribute_reqd(el, DESCRIPTION_BRIEF),
                attribute_safe(el, DESCRIPTION_FULL),
                _fromSet(el, TAGS)
        );

        /*
        if (fDeepInitAtLoad) {
            gsann.getGeneSet(true);
            gsann.getGeneSet(false);
        }
        */
        return gsann;
    }

    private static Set _fromSet(final Element el, final String attName) {
        Attribute att = el.attribute(attName);
        if (att != null) {
            return ParseUtils.string2stringsSet(att.getValue(), ",", false);
        } else {
            return null;
        }
    }

    private static String _toString(final Set set) {
        if (set == null || set.isEmpty()) {
            return "";
        }

        StringBuffer buf = new StringBuffer();
        for (Iterator iterator = set.iterator(); iterator.hasNext();) {
            Object key = iterator.next();
            buf.append(key);
            if (iterator.hasNext()) {
                buf.append(",");
            }
        }

        return buf.toString();
    }
}    // End of class MSigDbParser
