/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.vdb;

import edu.mit.broad.msigdb_browser.genome.NotImplementedException;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Aravind Subramanian
 * @version %I%, %G%
 */
public class Organism {

    public static final Organism GENERIC = new Organism("Generic");
    public static final Organism XSPECIES = new Organism("Human,Mouse,Rat,Dog");

    public static final Organism CHIMPANZEE = new Organism("Chimpanzee");
    public static final Organism PIG = new Organism("Pig");
    public static final Organism RHESUS = new Organism("Rhesus");

    public static final Organism HUMAN = new Organism("Homo sapiens");
    public static final Organism ZEBRA_FISH = new Organism("Zebra Fish");
    public static final Organism HUMAN_en = new Organism("Human");
    public static final Organism MOUSE = new Organism("Mus musculus");
    public static final Organism MOUSE_en = new Organism("Mouse");
    public static final Organism DROSOPHILLA = new Organism("Drosophilla");
    public static final Organism RAT = new Organism("Rattus norvegicus");
    public static final Organism RAT_EN = new Organism("Rat");
    public static final Organism YEAST = new Organism("Saccharomyces cerevisiae");
    public static final Organism DANIO = new Organism("Danio rerio");
    public static final Organism MAC = new Organism("Macaca mulatta");

    public static final Organism[] ALL_TAGS_en = new Organism[]{GENERIC, ZEBRA_FISH, PIG, CHIMPANZEE, HUMAN, HUMAN_en,
            MOUSE, MOUSE_en, RAT, RAT_EN, YEAST, DROSOPHILLA, XSPECIES, RHESUS, DANIO, MAC};

    private String fValue;

    /**
     * privatized constructor.
     * Use one of the declared objects.
     */
    private Organism(final String name) {
        this.fValue = name;
    }

    public String toString() {
        return fValue;
    }

    public String toFormattedString() {
        return fValue;
    }

    public static Organism parseOrganism(String fromValue) {

        if (fromValue == null) {
            throw new NullPointerException("Param fromvalue cannot be null");
        }

        fromValue = fromValue.trim();

        for (int i = 0; i < ALL_TAGS_en.length; i++) {
            if (fromValue.equalsIgnoreCase(ALL_TAGS_en[i].toFormattedString())) {
                return ALL_TAGS_en[i];
            }
        }

        throw new IllegalArgumentException("Cannot parse Organism from: " + fromValue);
    }
}    // Organism
