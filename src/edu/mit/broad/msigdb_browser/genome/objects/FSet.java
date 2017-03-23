/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.genome.objects;

import edu.mit.broad.msigdb_browser.genome.parsers.AuxUtils;
import edu.mit.broad.msigdb_browser.vdb.chip.Probe;
import edu.mit.broad.msigdb_browser.vdb.meg.Gene;

import java.util.*;

/**
 * Simply defines a set of things(by name) that belong to the same FSet
 * A list of names - genes/probes etc. Only 1 class in a given FSet (unlike Templates)
 * A vertical template in some ways
 * <p/>
 * (synonyms: Tags, Tag group, GeneSet, BioSet)
 * For visualization purposes, a FSet can also be associated with an icon and color.
 * <p/>
 * There should be no duplicate members
 *
 * @author Aravind Subramanian
 * @version %I%, %G%
 */
public class FSet extends AbstractObject implements GeneSet {

    // -- Class Vars -- //

    /**
     * Each member is a String (not using a Set as we want to be able to do an
     * indexOf
     * As/Also i.e order sometimes matters
     */
    private List fMembers;

    /**
     * Class Constructor.
     */
    private FSet() {
    }

    /**
     * Class Constructor.
     * No member can be null
     * No duplicates allowed
     */
    public FSet(final String name, final String nameEnglish, final String[] members) {
        init(name, nameEnglish, members, true);
    }

    /**
     * Class Constructor.
     * No member can be null
     * No duplicates allowed
     * Objects in specified List are to Stringed
     * Data is NOT shared
     */
    public FSet(final String name, final String nameEnglish, final List members, final boolean checkForDuplicates) {
        init(name, nameEnglish, members, checkForDuplicates);
    }

    public FSet(final String name, final List members, final boolean checkForDuplicates) {
        init(name, null, members, checkForDuplicates);
    }

    public FSet(final String name, final String[] members) {
        init(name, null, members, true);
    }

    /**
     * Data is NOT shared
     *
     * @param name
     * @param members
     */
    public FSet(final String name, final Set members) {
        init(name, null, members, false);
    }

    // @maint IMP see duplicated init method below
    private void init(final String name, final String nameEnglish, final Collection members, final boolean checkForDuplicates) {
        super.initialize(name, nameEnglish);

        if (members == null) {
            throw new NullPointerException("Param members cant be null");
        }

        this.fMembers = new ArrayList(); // make safe copy

        int cnt = 0;
        Iterator it = members.iterator();
        while (it.hasNext()) {
            Object member = it.next();
            cnt++;

            if (member == null) {
                throw new NullPointerException("Member is null at: " + cnt);
            }

            String mn;

            if (member instanceof Gene) {
                Gene gene = ((Gene) member);
                mn = gene.getSymbol();
            } else if (member instanceof Probe) {
                mn = ((Probe) member).getName();
            } else {
                mn = member.toString();
            }

            if (checkForDuplicates) {
                // IMP to add, as in some cases it might be legit
                // for example when creating a combined dataset from bpog (when markers are shared)
                if (fMembers.contains(mn)) {
                    //TraceUtils.showTrace();
                    log.warn("Duplicate GeneSet member: " + mn);// dont barf, just warn (possible imp for randomizations)
                } else {
                    fMembers.add(mn);
                }
            } else { // blindly believe and add
                fMembers.add(mn);
            }
        }

    }

    // @maint IMP see duplicated init method above
    private void init(final String name, final String nameEnglish, final String[] members, final boolean checkForDuplicates) {
        super.initialize(name, nameEnglish);

        if (members == null) {
            throw new NullPointerException("Members param cant be null");
        }

        this.fMembers = new ArrayList(); // make safe copy

        for (int i = 0; i < members.length; i++) {
            if (members[i] == null) {
                throw new NullPointerException("Member is null at: " + i);
            }

            if (checkForDuplicates) {
                if (fMembers.contains(members[i])) {
                    log.warn("Duplicate GeneSet member: " + members[i]);// dont barf, just warn (possible imp for randomizations)
                    //TraceUtils.showTrace();
                } else {
                    fMembers.add(members[i]);
                }
            } else { // blindly believe
                fMembers.add(members[i]);
            }
        }

    }

    public String getQuickInfo() {
        StringBuffer buf = new StringBuffer().append(getNumMembers()).append(" members");
        return buf.toString();
    }

    public String getName(boolean stripAux) {
        if (stripAux) {
            return AuxUtils.getAuxNameOnlyNoHash(getName());
        } else {
            return getName();
        }
    }

    /**
     * @param pos
     * @return Name of member at position pos in the group
     */
    public String getMember(int pos) {
        return (String) fMembers.get(pos);
    }

    /**
     * Checks if specified name belongs to this FSet.
     *
     * @param name
     * @return
     */
    public boolean isMember(final String name) {
        //log.debug(">" + name + "<" + getNumMembers());
        return fMembers.contains(name);
    }

    /**
     * @return Number of members of this FSet
     */
    public int getNumMembers() {
        return fMembers.size();
    }

    /**
     * @return Unmodifiable list of members of this FSet
     */
    public List getMembers() {
        return Collections.unmodifiableList(fMembers);
    }

    public List getMembers_quick() {
        return fMembers;
    }

    public Set getMembersS() {
        return Collections.unmodifiableSet(new HashSet(fMembers));
    }

    /**
     * @return
     */
    public String[] getMembersArray() {
        // safe copy
        return (String[]) fMembers.toArray(new String[fMembers.size()]);
    }

}    // End FSet
