/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.genome;

/**
 * Commonly used headers in parsing etc
 * to avoid hardcoding all over the place
 */
public interface Headers extends Constants {

    public static final String REAL_SCORE = "REAL_SCORE";


    public static final String MIN = "MIN";
    public static final String MAX = "MAX";
    public static final String MEAN = "MEAN";
    public static final String MEDIAN = "MEDIAN";
    public static final String STDDEV = "STDDEV";

    /**
     * Other commonly used Headers
     */
    public static final String DATASET = "DATASET";
    public static final String RANKED_LIST = "RANKED_LIST";
    public static final String TEMPLATE = "TEMPLATE";
    public static final String ANNOTATION = "ANNOTATION";
    public static final String GENESET = "GENESET";
    public static final String METAPROBESET = "METAPROBESET";
    public static final String GENESETMATRIX = "GENESETMATRIX";
    public static final String METAPROBEMATRIX = "METAPROBEMATRIX";

    public static final String FILEOFFILENAMES = "FILEOFFILENAMES";
    public static final String REPORT = "REPORT";
    /**
     * affy control probe name prefix
     */
    public static final String AFFX_CONTROL_PREFIX = "AFFX";

    public static final String AFFX_NULL = "---";

    public static final String CORR = "CORR";

    public static final String LV_PROC = "LC_PROC";
    public static final String SORT_MODE = "SORT_MODE";
    public static final String ORDER = "ORDER";
    public static final String METRIC = "METRIC";
    public static final String NUM_PERMS = "NUM_PERMS";

    public static final String COL_NAMES = "COL_NAMES";
    public static final String ROW_NAMES = "ROW_NAMES";

    public static final String UP = "UP";
    public static final String DOWN = "DOWN";

    public static final String USE_MEDIAN = "USE_MEDIAN";
    public static final String USE_BIASED = "USE_BIASED";
    public static final String FIX_LOW = "FIX_LOW";

    public static final String DIR = "DIR";

    public static final String EXCLUDE_NAME = "EXCLUDE_NAME";

    public static final String TOP_X = "TOP_X";
    public static final String BOT_X = "BOT_X";

    public static final String META_GENE_ID = "MEG_ID";
    public static final String MEG = "MEG";
    public static final String DMEG = "DMEG";
    public static final String GENE_ALIAS = "GENE_ALIAS";
    public static final String SEQUENCE_ACCESSION = "SEQUENCE_ACCESSION";
    public static final String PREVIOUS_SYMBOL = "PREVIOUS_SYMBOL";
    public static final String REF_SEQ = "REF_SEQ";
    public static final String UNIGENE = "UNIGENE";
    public static final String LOCUS_LINK = "LOCUS_LINK";
    public static final String GENE_SYMBOL = "GENE_SYMBOL";
    public static final String GENE_TITLE = "GENE_TITLE";
    public static final String CHIP = "CHIP";
    public static final String PATHWAYS = "PATHWAYS";
    public static final String GENE_NOTES = "GENE_NOTES";


    /**
     * @notes 1) dont use chiptype as a given probe can be on more than 1 chip
     * @maint IMP IMP these constants must be kept in synch woith the affy taf files and anuy otehr annotations you
     * use
     * <p/>
     * sequence id is NOT the same as "accession" > refers to genbank or refseq usually
     * whikle sequence if is something else??
     * <p/>
     * Probe_name	Chip	Organism	Sequence Type	Sequence Source	Sequence Derived From	Sequence Description
     * Sequence ID	Transcript ID	Group ID	Title	Gene Symbol	Location	Unigene	LocusLink	SwissProt
     * EC Number	OMIM	References	Full Length Ref. Sequences
     * Biological Process (GO)	Cellular Component (GO)	Molecular Function (GO)
     * Biochemical Function (Proteome) Cellular Role (Proteome)	Molecular Localization (Proteome)
     * Organismal Role (Proteome)	Subcellular Localization (Proteome)
     * Pathways GenMAPP
     * Pathways KEGG	InterPro	Pfam	BLOCKS	Prints	SCOP	TMM	Protein Families
     * Protein Similarities	Orthologs/Homologs	Annotation Date
     */
    // @todo clean this up dont like themixed case and spaces - but watch out for netaffx formats

    public static final String PROBE_SET_ID = "Probe Set ID";
    public static final String ORGANISM = "Organism";
    public static final String SEQUENCE_ID = "Sequence ID";

    public static final String GENE_SYNONYMS = "Gene Synonyms";


    public static final String CYTO_BAND = "CYTO_BAND";

    public static final String SUMMARY_FUNCTION = "SUMMARY_FUNCTION";
    public static final String ENZYMATIC_FUNCTION = "ENZYMATIC_FUNCTION";
    public static final String SUBCELLULAR_LOCALIZATION = "SUBCELLULAR_LOCALIZATION";
    public static final String GO_IDS = "GO_IDS";
    public static final String GO_ID = "GO_ID";
    public static final String GO_DESCRIPTION = "GO_DESCRIPTION";

    public static final String SWISSPROT_ID = "SWISSPROT_ID";
    public static final String ENSEMBL_PROTEIN_ID = "ENSEMBL_PROTEIN_ID";
    public static final String REFSEQ_PROTEIN_ID = "REFSEQ_PROTEIN_ID";
    public static final String HUGO = "HUGO";


    public static final String SEQUENCE_DESCRIPTION = "Sequence Description"; // this is the full-fledged  like desc

    public static final String SEQUENCE_TYPE = "Sequence Type"; // this is the full-fledged  like desc

    public static final String OMIM = "OMIM"; // always longs

    public static final String ENSEMBL_ID = "Ensembl ID";

    public static final String XOLOG = "Orthologs/Homologs";

    // these are my own (not from affy taf) <- for the dchip genome info files
    public static final String CHROMOSOME = "Chromosome";
    public static final String CHROMOSOME_START = "Chromsome Start";
    public static final String CHROMOSOME_STOP = "Chromsome Stop";

    public static final String GENOME_ALIGNMENT = "Genome Alignment";


    public static final String SEQUENCE_SOURCE = "Sequence Source"; // typically RefSeq or GenBank
    public static final String SEQUENCE_DERIVED_FROM = "Sequence Derived From"; // needs to be further parsed in refseq or gb etc


    /**
     * PATHWAYS RELATED HEADERS
     */
    public static final String PATHWAYS_GENMAPP = "Pathways GenMAPP";


    /**
     * ONTOLOGY RELATED HEADERS
     */
    public static final String GO_BIOLOGICAL_PROCESS = "Biological Process (GO)";
    public static final String GO_CELLULAR_COMPONENT = "Cellular Component (GO)";
    public static final String GO_MOLECULAR_FUNCTION = "Molecular Function (GO)";


    public static final String PROTEOME_BIOCHEMICAL_FUNCTION = "Biochemical Function (Proteome)";
    public static final String PROTEOME_CELLULAR_ROLE = "Cellular Role (Proteome)";
    public static final String PROTEOME_MOLECULAR_LOCALIZATION = "Molecular Localization (Proteome)";
    public static final String PROTEOME_ORGANISMAL_ROLE = "Organismal Role (Proteome)";
    public static final String PROTEOME_SUBCELLULAR_LOCALIZATION = "Subcellular Localization (Proteome)";

    public static final String[] ONTOLOGY_GO_HEADERS = new String[]{
            Headers.GO_BIOLOGICAL_PROCESS,
            Headers.GO_CELLULAR_COMPONENT,
            Headers.GO_MOLECULAR_FUNCTION
    };

    public static final String[] ONTOLOGY_PROTEOME_HEADERS = new String[]{

            Headers.PROTEOME_BIOCHEMICAL_FUNCTION,
            Headers.PROTEOME_CELLULAR_ROLE,
            Headers.PROTEOME_MOLECULAR_LOCALIZATION,
            Headers.PROTEOME_ORGANISMAL_ROLE,
            Headers.PROTEOME_SUBCELLULAR_LOCALIZATION
    };

    public static final String[] ONTOLOGY_ALL_HEADERS = new String[]{

            Headers.PROTEOME_BIOCHEMICAL_FUNCTION,
            Headers.PROTEOME_CELLULAR_ROLE,
            Headers.PROTEOME_MOLECULAR_LOCALIZATION,
            Headers.PROTEOME_ORGANISMAL_ROLE,
            Headers.PROTEOME_SUBCELLULAR_LOCALIZATION,
            Headers.GO_BIOLOGICAL_PROCESS,
            Headers.GO_CELLULAR_COMPONENT,
            Headers.GO_MOLECULAR_FUNCTION
    };

    /**
     * Protein related headers
     */
    public static final String INTERPRO = "InterPro";
    public static final String PFAM = "Pfam";
    public static final String BLOCKS = "BLOCKS";
    public static final String SCOP = "SCOP";
    public static final String[] PROTEIN_HEADERS = new String[]{Headers.INTERPRO,
            Headers.PFAM,
            Headers.BLOCKS,
            Headers.SCOP
    };


    public static interface MSigDB {

        // cant have tab (would jave been nice as easier to use in excel as when read _in_
        // the dom library seems to convert tabs into spaces
        // private static final char DELIM = ' ';

        public static final String MSIGDB = "MSIGDB";

        public static final String GENE_SET = "GENESET";

        // Rest are Attribute fields
        public static final String STANDARD_NAME = "STANDARD_NAME";
        public static final String SYSTEMATIC_NAME = "SYSTEMATIC_NAME";
        public static final String ORGANISM = "ORGANISM";
        public static final String CHIP = "CHIP";

        public static final String GENESET_LISTING_URL = "GENESET_LISTING_URL";
        public static final String EXTERNAL_DETAILS_URL = "EXTERNAL_DETAILS_URL";

        public static final String CATEGORY_CODE = "CATEGORY_CODE";
        public static final String CONTRIBUTOR = "CONTRIBUTOR";
        public static final String DESCRIPTION_BRIEF = "DESCRIPTION_BRIEF";
        public static final String DESCRIPTION_FULL = "DESCRIPTION_FULL";
        public static final String MEMBERS = "MEMBERS";
        public static final String MEMBERS_SYMBOLIZED = "MEMBERS_SYMBOLIZED";

        public static final String PMID = "PMID";

        public static final String GEOID = "GEOID";

        public static final String TAGS = "TAGS";

    } // End class MSigDB


} // End Headers
