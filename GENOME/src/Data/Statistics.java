package Data;

import java.io.Serializable;
import java.util.stream.IntStream;

public class Statistics implements Serializable {
    private static final long serialVersionUID = 2L;

    /**
     * Type of this Statistic
     */
    private final Type m_TYPE;
    /**
     * Array to store trinucleotide statistics
     */
    private final Tuple[] m_TRINUCLEOTIDE_TABLE;
    /**
     * Array to store dinucleotide statistics
     */
    private final Tuple[] m_DINUCLEOTIDE_TABLE;
    /**
     * Number total of trinucleotide
     */
    private long m_totalTrinucleotide;
    /**
     * Number total of dinucleotide
     */
    private long m_totalDinucleotide;
    /**
     * The number of CDS sequences
     */
    private long m_CDSNumber;
    /**
     * The number of valid CDS sequences
     */
    private long m_validCDSNumber;

    /**
     * Class constructor
     */
    Statistics(Type _type) {
        m_TYPE = _type;
        m_TRINUCLEOTIDE_TABLE = new Tuple[Trinucleotide.values().length];
        IntStream.range(0, Trinucleotide.values().length).parallel().forEach(i -> m_TRINUCLEOTIDE_TABLE[i] = new Tuple());
        m_DINUCLEOTIDE_TABLE = new Tuple[Dinucleotide.values().length];
        IntStream.range(0, Dinucleotide.values().length).parallel().forEach(i -> m_DINUCLEOTIDE_TABLE[i] = new Tuple());
        m_totalTrinucleotide = 0L;
        m_totalDinucleotide = 0L;
        m_CDSNumber = 0L;
        m_validCDSNumber = 0L;
    }

    /**
     * Get the type of this Replicon
     *
     * @return the type
     */
    public final Type getType() {
        return m_TYPE;
    }

    /**
     * get the total trinucleotide number
     *
     * @return the m_totalTrinucleotide
     */
    public final long getTotalTrinucleotide() {
        return m_totalTrinucleotide;
    }

    /**
     * get the total dinucleotide number
     *
     * @return the m_totalDinucleotide
     */
    public final long getTotalDinucleotide() {
        return m_totalDinucleotide;
    }

    /**
     * @return the m_TRINUCLEOTIDE_TABLE
     */
    public final Tuple[] getTriTable() {
        return m_TRINUCLEOTIDE_TABLE;
    }

    /**
     * @return the m_TRINUCLEOTIDE_TABLE
     */
    public final Tuple[] getDiTable() {
        return m_DINUCLEOTIDE_TABLE;
    }

    /**
     * Get the number of CDS
     *
     * @return the number of CDS
     */
    public final long getCDSNumber() {
        return m_CDSNumber;
    }

    /**
     * Get the number of valid CDS
     *
     * @return the number of valid CDS
     */
    public final long getValidCDSNumber() {
        return m_validCDSNumber;
    }

    /**
     * Update statistics
     *
     * @param _stats, the stats use to update
     */
    final void update(Statistics _stats) {
        IntStream.range(0, Trinucleotide.values().length).parallel().forEach(i -> {
            final Tuple inputRow = _stats.m_TRINUCLEOTIDE_TABLE[i];
            m_TRINUCLEOTIDE_TABLE[i].incr(StatLong.PHASE0, inputRow.get(StatLong.PHASE0));
            m_TRINUCLEOTIDE_TABLE[i].incr(StatLong.PHASE1, inputRow.get(StatLong.PHASE1));
            m_TRINUCLEOTIDE_TABLE[i].incr(StatLong.PHASE2, inputRow.get(StatLong.PHASE2));
            m_TRINUCLEOTIDE_TABLE[i].incr(StatLong.PREF0, inputRow.get(StatLong.PREF0));
            m_TRINUCLEOTIDE_TABLE[i].incr(StatLong.PREF1, inputRow.get(StatLong.PREF1));
            m_TRINUCLEOTIDE_TABLE[i].incr(StatLong.PREF2, inputRow.get(StatLong.PREF2));
        });
        IntStream.range(0, Dinucleotide.values().length).parallel().forEach(i -> {
            final Tuple inputRow = _stats.m_DINUCLEOTIDE_TABLE[i];
            m_DINUCLEOTIDE_TABLE[i].incr(StatLong.PHASE0, inputRow.get(StatLong.PHASE0));
            m_DINUCLEOTIDE_TABLE[i].incr(StatLong.PHASE1, inputRow.get(StatLong.PHASE1));
            m_DINUCLEOTIDE_TABLE[i].incr(StatLong.PREF0, inputRow.get(StatLong.PREF0));
            m_DINUCLEOTIDE_TABLE[i].incr(StatLong.PREF1, inputRow.get(StatLong.PREF1));
        });
        m_totalTrinucleotide += _stats.m_totalTrinucleotide;
        m_totalDinucleotide += _stats.m_totalDinucleotide;
        m_CDSNumber += _stats.m_CDSNumber;
        m_validCDSNumber += _stats.m_validCDSNumber;
    }

    /**
     * Compute the frequencies and the preferences of each trinucleotide for each phases
     */
    final void compute() {
        if (m_totalTrinucleotide != 0) {
            for (Tuple row : m_TRINUCLEOTIDE_TABLE) {
                row.set(StatFloat.FREQ0, row.get(StatLong.PHASE0) / (float) m_totalTrinucleotide);
                row.set(StatFloat.FREQ1, row.get(StatLong.PHASE1) / (float) m_totalTrinucleotide);
                row.set(StatFloat.FREQ2, row.get(StatLong.PHASE2) / (float) m_totalTrinucleotide);
            }
        }
        if (m_totalDinucleotide != 0) {
            for (Tuple row : m_DINUCLEOTIDE_TABLE) {
                row.set(StatFloat.FREQ0, row.get(StatLong.PHASE0) / (float) m_totalDinucleotide);
                row.set(StatFloat.FREQ1, row.get(StatLong.PHASE1) / (float) m_totalDinucleotide);
            }
        }
    }

    /**
     * Increment by 1 the value of a trinucleotide for a stat
     *
     * @param _tri,  the Trinucleotide to set
     * @param _stat, the statistic to set
     */
    final void incrementStat(Trinucleotide _tri, StatLong _stat) {
        m_TRINUCLEOTIDE_TABLE[_tri.ordinal()].incr(_stat, 1L);
    }

    /**
     * Increment by 1 the value of a dinucleotide for a stat
     *
     * @param _di,   the Dinucleotide to set
     * @param _stat, the statistic to set
     */
    final void incrementStat(Dinucleotide _di, StatLong _stat) {
        m_DINUCLEOTIDE_TABLE[_di.ordinal()].incr(_stat, 1L);
    }

    /**
     * Increment the value of total trinucleotide by the parameter
     *
     * @param _inc, the value to increment
     */
    final void incrementTriTotal(long _inc) {
        m_totalTrinucleotide += _inc;
    }

    /**
     * Increment the value of total trinucleotide by the parameter
     *
     * @param _inc, the value to increment
     */
    final void incrementDiTotal(long _inc) {
        m_totalDinucleotide += _inc;
    }

    /**
     * Increment the number of CDS sequence
     *
     * @param _long, the value to increment
     */
    final void incrementCDS(long _long) {
        m_CDSNumber += _long;
    }

    /**
     * Increment the number of valid CDS sequence
     *
     * @param _long, the value to increment
     */
    final void incrementValidCDS(long _long) {
        m_validCDSNumber += _long;
    }

    /**
     * Unload data
     *
     * @param _stats the data to unload
     */
    void unload(Statistics _stats) {
        m_totalTrinucleotide -= _stats.m_totalTrinucleotide;
        m_totalDinucleotide -= _stats.m_totalDinucleotide;
        m_CDSNumber -= _stats.m_CDSNumber;
        m_validCDSNumber -= _stats.m_validCDSNumber;
        IntStream.range(0, Trinucleotide.values().length).parallel().forEach(i -> {
            final Tuple inputRow = _stats.m_TRINUCLEOTIDE_TABLE[i];
            m_TRINUCLEOTIDE_TABLE[i].decr(StatLong.PHASE0, inputRow.get(StatLong.PHASE0));
            m_TRINUCLEOTIDE_TABLE[i].decr(StatLong.PHASE1, inputRow.get(StatLong.PHASE1));
            m_TRINUCLEOTIDE_TABLE[i].decr(StatLong.PHASE2, inputRow.get(StatLong.PHASE2));
            m_TRINUCLEOTIDE_TABLE[i].decr(StatLong.PREF0, inputRow.get(StatLong.PREF0));
            m_TRINUCLEOTIDE_TABLE[i].decr(StatLong.PREF1, inputRow.get(StatLong.PREF1));
            m_TRINUCLEOTIDE_TABLE[i].decr(StatLong.PREF2, inputRow.get(StatLong.PREF2));
        });
        IntStream.range(0, Dinucleotide.values().length).parallel().forEach(i -> {
            final Tuple inputRow = _stats.m_DINUCLEOTIDE_TABLE[i];
            m_DINUCLEOTIDE_TABLE[i].decr(StatLong.PHASE0, inputRow.get(StatLong.PHASE0));
            m_DINUCLEOTIDE_TABLE[i].decr(StatLong.PHASE1, inputRow.get(StatLong.PHASE1));
            m_DINUCLEOTIDE_TABLE[i].decr(StatLong.PREF0, inputRow.get(StatLong.PREF0));
            m_DINUCLEOTIDE_TABLE[i].decr(StatLong.PREF1, inputRow.get(StatLong.PREF1));
        });

    }

    /**
     * List of the 64 trinucleotide
     */
    public enum Trinucleotide {
        AAA,
        AAC,
        AAG,
        AAT,
        ACA,
        ACC,
        ACG,
        ACT,
        AGA,
        AGC,
        AGG,
        AGT,
        ATA,
        ATC,
        ATG,
        ATT,
        CAA,
        CAC,
        CAG,
        CAT,
        CCA,
        CCC,
        CCG,
        CCT,
        CGA,
        CGC,
        CGG,
        CGT,
        CTA,
        CTC,
        CTG,
        CTT,
        GAA,
        GAC,
        GAG,
        GAT,
        GCA,
        GCC,
        GCG,
        GCT,
        GGA,
        GGC,
        GGG,
        GGT,
        GTA,
        GTC,
        GTG,
        GTT,
        TAA,
        TAC,
        TAG,
        TAT,
        TCA,
        TCC,
        TCG,
        TCT,
        TGA,
        TGC,
        TGG,
        TGT,
        TTA,
        TTC,
        TTG,
        TTT
    }

    /**
     * List of the 16 dinucleotide
     */
    public enum Dinucleotide {
        AA,
        AC,
        AG,
        AT,
        CA,
        CC,
        CG,
        CT,
        GA,
        GC,
        GG,
        GT,
        TA,
        TC,
        TG,
        TT
    }

    /**
     * List of statistics (float)
     */
    public enum StatFloat {
        FREQ0,
        FREQ1,
        FREQ2
    }

    /**
     * List of statistics (long)
     */
    public enum StatLong {
        PHASE0,
        PHASE1,
        PHASE2,
        PREF0,
        PREF1,
        PREF2
    }

    /**
     * This enumeration represent the type of a Statistic
     */
    public enum Type {
        CHROMOSOME,
        MITOCHONDRION,
        PLASMID,
        DNA,
        CHLOROPLAST,
        SEGMENT,
        LINKAGE,
        PLASTID,
        CIRCLE,
        RNA,
        PLTD,
        UNKNOWN,
        UNNAMED,
        _10,
        _11,
        _12,
        _13,
        _14,
        _15,
        _16,
        _19,
        _29,
        _2S,
        _64,
        _72,
        _94,
        A1,
        A2,
        A3,
        AB,
        AC,
        AP,
        AT,
        AU,
        AZ,
        B1,
        B2,
        B3,
        B4,
        B5,
        B7,
        B8,
        B9,
        BA,
        BG,
        BL,
        BO,
        BP,
        BT,
        BY,
        C1,
        C2,
        C3,
        C4,
        C5,
        C6,
        C7,
        C8,
        C9,
        CA,
        CD,
        CL,
        CM,
        CO,
        CP,
        CS,
        CY,
        D1,
        D2,
        D3,
        D4,
        D5,
        D6,
        D7,
        DD,
        DE,
        DF,
        DG,
        EA,
        EB,
        EC,
        ED,
        EF,
        FA,
        G2,
        GL,
        GR,
        HA,
        HP,
        HR,
        I2,
        IC,
        II,
        IN,
        IP,
        IV,
        IX,
        JD,
        KO,
        L1,
        L2,
        L3,
        LA,
        LB,
        LI,
        LK,
        LP,
        M1,
        M2,
        M3,
        M4,
        M5,
        M6,
        MA,
        MC,
        ME,
        MF,
        ML,
        MP,
        N4,
        NA,
        NC,
        ND,
        NE,
        NR,
        NT,
        O1,
        OR,
        P0,
        P1,
        P2,
        P3,
        P4,
        P5,
        P6,
        P7,
        P9,
        PA,
        PB,
        PC,
        PD,
        PE,
        PF,
        PG,
        PH,
        PI,
        PJ,
        PK,
        PL,
        PM,
        PN,
        PO,
        PP,
        PQ,
        PR,
        PS,
        PT,
        PU,
        PV,
        PW,
        PX,
        PY,
        PZ,
        QP,
        R1,
        R2,
        R4,
        R6,
        R7,
        R8,
        RA,
        RM,
        RP,
        RS,
        RT,
        S1,
        S2,
        S3,
        S4,
        S7,
        S8,
        S9,
        SA,
        SC,
        SE,
        SL,
        SM,
        SP,
        SV,
        TC,
        TI,
        TY,
        VA,
        VD,
        VI,
        VR,
        VS,
        WI,
        WK,
        WS,
        XN,
        ZS,
        ZW,
        _1,
        _2,
        _3,
        _4,
        _5,
        _6,
        _7,
        _8,
        _9,
        A,
        B,
        C,
        D,
        E,
        F,
        G,
        H,
        I,
        J,
        K,
        L,
        M,
        N,
        O,
        P,
        Q,
        S,
        T,
        U,
        V,
        X,
        Z;

        /**
         * Get the Type from a String
         *
         * @param _s the String to check
         * @return the corresponding type
         */
        public static Type isTypeOf(String _s) {
            if (_s.contains("CHLOROPLAST")) {
                return CHLOROPLAST;
            } else if (_s.contains("CHROMOSOME")) {
                return CHROMOSOME;
            } else if (_s.contains("PLASMID")) {
                return PLASMID;
            } else if (_s.contains("SEGMENT") || _s.contains("SEGMEN")) {
                return SEGMENT;
            } else if (_s.contains("LINKAGE")) {
                return LINKAGE;
            } else if (_s.contains("PLASTID")) {
                return PLASTID;
            } else if (_s.contains("CIRCLE")) {
                return CIRCLE;
            } else if (_s.contains("PLTD")) {
                return PLTD;
            } else if (_s.contains("UNKNOWN")) {
                return UNKNOWN;
            } else if (_s.contains("UNNAMED")) {
                return UNNAMED;
            } else if (_s.contains("MITOCHONDRION") || _s.contains("MT")) {
                return MITOCHONDRION;
            } else if (_s.contains("DNA") || _s.contains("DN")) {
                return DNA;
            } else if (_s.contains("RNA") || _s.contains("RN")) {
                return RNA;
            } else if(_s.startsWith("10")) {
                return _10;
            } else if(_s.startsWith("11")) {
                return _11;
            } else if(_s.startsWith("12")) {
                return _12;
            } else if(_s.startsWith("13")) {
                return _13;
            } else if(_s.startsWith("14")) {
                return _14;
            } else if(_s.startsWith("15")) {
                return _15;
            } else if(_s.startsWith("16")) {
                return _16;
            } else if(_s.startsWith("19")) {
                return _19;
            } else if(_s.startsWith("29")) {
                return _29;
            } else if(_s.startsWith("2S")) {
                return _2S;
            } else if(_s.startsWith("64")) {
                return _64;
            } else if(_s.startsWith("72")) {
                return _72;
            } else if(_s.startsWith("94")) {
                return _94;
            } else if(_s.startsWith("A1")) {
                return A1;
            } else if(_s.startsWith("A2")) {
                return A2;
            } else if(_s.startsWith("A3")) {
                return A3;
            } else if(_s.startsWith("AB")) {
                return AB;
            } else if(_s.startsWith("AC")) {
                return AC;
            } else if(_s.startsWith("AP")) {
                return AP;
            } else if(_s.startsWith("AT")) {
                return AT;
            } else if(_s.startsWith("AU")) {
                return AU;
            } else if(_s.startsWith("AZ")) {
                return AZ;
            } else if(_s.startsWith("B1")) {
                return B1;
            } else if(_s.startsWith("B2")) {
                return B2;
            } else if(_s.startsWith("B3")) {
                return B3;
            } else if(_s.startsWith("B4")) {
                return B4;
            } else if(_s.startsWith("B5")) {
                return B5;
            } else if(_s.startsWith("B7")) {
                return B7;
            } else if(_s.startsWith("B8")) {
                return B8;
            } else if(_s.startsWith("B9")) {
                return B9;
            } else if(_s.startsWith("BA")) {
                return BA;
            } else if(_s.startsWith("BG")) {
                return BG;
            } else if(_s.startsWith("BL")) {
                return BL;
            } else if(_s.startsWith("BO")) {
                return BO;
            } else if(_s.startsWith("BP")) {
                return BP;
            } else if(_s.startsWith("BT")) {
                return BT;
            } else if(_s.startsWith("BY")) {
                return BY;
            } else if(_s.startsWith("C1")) {
                return C1;
            } else if(_s.startsWith("C2")) {
                return C2;
            } else if(_s.startsWith("C3")) {
                return C3;
            } else if(_s.startsWith("C4")) {
                return C4;
            } else if(_s.startsWith("C5")) {
                return C5;
            } else if(_s.startsWith("C6")) {
                return C6;
            } else if(_s.startsWith("C7")) {
                return C7;
            } else if(_s.startsWith("C8")) {
                return C8;
            } else if(_s.startsWith("C9")) {
                return C9;
            } else if(_s.startsWith("CA")) {
                return CA;
            } else if(_s.startsWith("CD")) {
                return CD;
            } else if(_s.startsWith("CL")) {
                return CL;
            } else if(_s.startsWith("CM")) {
                return CM;
            } else if(_s.startsWith("CO")) {
                return CO;
            } else if(_s.startsWith("CP")) {
                return CP;
            } else if(_s.startsWith("CS")) {
                return CS;
            } else if(_s.startsWith("CY")) {
                return CY;
            } else if(_s.startsWith("D1")) {
                return D1;
            } else if(_s.startsWith("D2")) {
                return D2;
            } else if(_s.startsWith("D3")) {
                return D3;
            } else if(_s.startsWith("D4")) {
                return D4;
            } else if(_s.startsWith("D5")) {
                return D5;
            } else if(_s.startsWith("D6")) {
                return D6;
            } else if(_s.startsWith("D7")) {
                return D7;
            } else if(_s.startsWith("DD")) {
                return DD;
            } else if(_s.startsWith("DE")) {
                return DE;
            } else if(_s.startsWith("DF")) {
                return DF;
            } else if(_s.startsWith("DG")) {
                return DG;
            } else if(_s.startsWith("EA")) {
                return EA;
            } else if(_s.startsWith("EB")) {
                return EB;
            } else if(_s.startsWith("EC")) {
                return EC;
            } else if(_s.startsWith("ED")) {
                return ED;
            } else if(_s.startsWith("EF")) {
                return EF;
            } else if(_s.startsWith("FA")) {
                return FA;
            } else if(_s.startsWith("G2")) {
                return G2;
            } else if(_s.startsWith("GL")) {
                return GL;
            } else if(_s.startsWith("GR")) {
                return GR;
            } else if(_s.startsWith("HA")) {
                return HA;
            } else if(_s.startsWith("HP")) {
                return HP;
            } else if(_s.startsWith("HR")) {
                return HR;
            } else if(_s.startsWith("I2")) {
                return I2;
            } else if(_s.startsWith("IC")) {
                return IC;
            } else if(_s.startsWith("II")) {
                return II;
            } else if(_s.startsWith("IN")) {
                return IN;
            } else if(_s.startsWith("IP")) {
                return IP;
            } else if(_s.startsWith("IV")) {
                return IV;
            } else if(_s.startsWith("IX")) {
                return IX;
            } else if(_s.startsWith("JD")) {
                return JD;
            } else if(_s.startsWith("KO")) {
                return KO;
            } else if(_s.startsWith("L1")) {
                return L1;
            } else if(_s.startsWith("L2")) {
                return L2;
            } else if(_s.startsWith("L3")) {
                return L3;
            } else if(_s.startsWith("LA")) {
                return LA;
            } else if(_s.startsWith("LB")) {
                return LB;
            } else if(_s.startsWith("LI")) {
                return LI;
            } else if(_s.startsWith("LK")) {
                return LK;
            } else if(_s.startsWith("LP")) {
                return LP;
            } else if(_s.startsWith("M1")) {
                return M1;
            } else if(_s.startsWith("M2")) {
                return M2;
            } else if(_s.startsWith("M3")) {
                return M3;
            } else if(_s.startsWith("M4")) {
                return M4;
            } else if(_s.startsWith("M5")) {
                return M5;
            } else if(_s.startsWith("M6")) {
                return M6;
            } else if(_s.startsWith("MA")) {
                return MA;
            } else if(_s.startsWith("MC")) {
                return MC;
            } else if(_s.startsWith("ME")) {
                return ME;
            } else if(_s.startsWith("MF")) {
                return MF;
            } else if(_s.startsWith("ML")) {
                return ML;
            } else if(_s.startsWith("MP")) {
                return MP;
            } else if(_s.startsWith("N4")) {
                return N4;
            } else if(_s.startsWith("NA")) {
                return NA;
            } else if(_s.startsWith("NC")) {
                return NC;
            } else if(_s.startsWith("ND")) {
                return ND;
            } else if(_s.startsWith("NE")) {
                return NE;
            } else if(_s.startsWith("NR")) {
                return NR;
            } else if(_s.startsWith("NT")) {
                return NT;
            } else if(_s.startsWith("O1")) {
                return O1;
            } else if(_s.startsWith("OR")) {
                return OR;
            } else if(_s.startsWith("P0")) {
                return P0;
            } else if(_s.startsWith("P1")) {
                return P1;
            } else if(_s.startsWith("P2")) {
                return P2;
            } else if(_s.startsWith("P3")) {
                return P3;
            } else if(_s.startsWith("P4")) {
                return P4;
            } else if(_s.startsWith("P5")) {
                return P5;
            } else if(_s.startsWith("P6")) {
                return P6;
            } else if(_s.startsWith("P7")) {
                return P7;
            } else if(_s.startsWith("P9")) {
                return P9;
            } else if(_s.startsWith("PA")) {
                return PA;
            } else if(_s.startsWith("PB")) {
                return PB;
            } else if(_s.startsWith("PC")) {
                return PC;
            } else if(_s.startsWith("PD")) {
                return PD;
            } else if(_s.startsWith("PE")) {
                return PE;
            } else if(_s.startsWith("PF")) {
                return PF;
            } else if(_s.startsWith("PG")) {
                return PG;
            } else if(_s.startsWith("PH")) {
                return PH;
            } else if(_s.startsWith("PI")) {
                return PI;
            } else if(_s.startsWith("PJ")) {
                return PJ;
            } else if(_s.startsWith("PK")) {
                return PK;
            } else if(_s.startsWith("PL")) {
                return PL;
            } else if(_s.startsWith("PM")) {
                return PM;
            } else if(_s.startsWith("PN")) {
                return PN;
            } else if(_s.startsWith("PO")) {
                return PO;
            } else if(_s.startsWith("PP")) {
                return PP;
            } else if(_s.startsWith("PQ")) {
                return PQ;
            } else if(_s.startsWith("PR")) {
                return PR;
            } else if(_s.startsWith("PS")) {
                return PS;
            } else if(_s.startsWith("PT")) {
                return PT;
            } else if(_s.startsWith("PU")) {
                return PU;
            } else if(_s.startsWith("PV")) {
                return PV;
            } else if(_s.startsWith("PW")) {
                return PW;
            } else if(_s.startsWith("PX")) {
                return PX;
            } else if(_s.startsWith("PY")) {
                return PY;
            } else if(_s.startsWith("PZ")) {
                return PZ;
            } else if(_s.startsWith("QP")) {
                return QP;
            } else if(_s.startsWith("R1")) {
                return R1;
            } else if(_s.startsWith("R2")) {
                return R2;
            } else if(_s.startsWith("R4")) {
                return R4;
            } else if(_s.startsWith("R6")) {
                return R6;
            } else if(_s.startsWith("R7")) {
                return R7;
            } else if(_s.startsWith("R8")) {
                return R8;
            } else if(_s.startsWith("RA")) {
                return RA;
            } else if(_s.startsWith("RM")) {
                return RM;
            } else if(_s.startsWith("RP")) {
                return RP;
            } else if(_s.startsWith("RS")) {
                return RS;
            } else if(_s.startsWith("RT")) {
                return RT;
            } else if(_s.startsWith("S1")) {
                return S1;
            } else if(_s.startsWith("S2")) {
                return S2;
            } else if(_s.startsWith("S3")) {
                return S3;
            } else if(_s.startsWith("S4")) {
                return S4;
            } else if(_s.startsWith("S7")) {
                return S7;
            } else if(_s.startsWith("S8")) {
                return S8;
            } else if(_s.startsWith("S9")) {
                return S9;
            } else if(_s.startsWith("SA")) {
                return SA;
            } else if(_s.startsWith("SC")) {
                return SC;
            } else if(_s.startsWith("SE")) {
                return SE;
            } else if(_s.startsWith("SL")) {
                return SL;
            } else if(_s.startsWith("SM")) {
                return SM;
            } else if(_s.startsWith("SP")) {
                return SP;
            } else if(_s.startsWith("SV")) {
                return SV;
            } else if(_s.startsWith("TC")) {
                return TC;
            } else if(_s.startsWith("TI")) {
                return TI;
            } else if(_s.startsWith("TY")) {
                return TY;
            } else if(_s.startsWith("VA")) {
                return VA;
            } else if(_s.startsWith("VD")) {
                return VD;
            } else if(_s.startsWith("VI")) {
                return VI;
            } else if(_s.startsWith("VR")) {
                return VR;
            } else if(_s.startsWith("VS")) {
                return VS;
            } else if(_s.startsWith("WI")) {
                return WI;
            } else if(_s.startsWith("WK")) {
                return WK;
            } else if(_s.startsWith("WS")) {
                return WS;
            } else if(_s.startsWith("XN")) {
                return XN;
            } else if(_s.startsWith("ZS")) {
                return ZS;
            } else if(_s.startsWith("ZW")) {
                return ZW;
            } else if(_s.startsWith("1")) {
                return _1;
            } else if(_s.startsWith("2")) {
                return _2;
            } else if(_s.startsWith("3")) {
                return _3;
            } else if(_s.startsWith("4")) {
                return _4;
            } else if(_s.startsWith("5")) {
                return _5;
            } else if(_s.startsWith("6")) {
                return _6;
            } else if(_s.startsWith("7")) {
                return _7;
            } else if(_s.startsWith("8")) {
                return _8;
            } else if(_s.startsWith("9")) {
                return _9;
            } else if(_s.startsWith("A")) {
                return A;
            } else if(_s.startsWith("B")) {
                return B;
            } else if(_s.startsWith("C")) {
                return C;
            } else if(_s.startsWith("D")) {
                return D;
            } else if(_s.startsWith("E")) {
                return E;
            } else if(_s.startsWith("F")) {
                return F;
            } else if(_s.startsWith("G")) {
                return G;
            } else if(_s.startsWith("H")) {
                return H;
            } else if(_s.startsWith("I")) {
                return I;
            } else if(_s.startsWith("J")) {
                return J;
            } else if(_s.startsWith("K")) {
                return K;
            } else if(_s.startsWith("L")) {
                return L;
            } else if(_s.startsWith("M")) {
                return M;
            } else if(_s.startsWith("N")) {
                return N;
            } else if(_s.startsWith("O")) {
                return O;
            } else if(_s.startsWith("P")) {
                return P;
            } else if(_s.startsWith("Q")) {
                return Q;
            } else if(_s.startsWith("S")) {
                return S;
            } else if(_s.startsWith("T")) {
                return T;
            } else if(_s.startsWith("U")) {
                return U;
            } else if(_s.startsWith("V")) {
                return V;
            } else if(_s.startsWith("X")) {
                return X;
            } else if(_s.startsWith("Z")) {
                return Z;
            }
            return null;
        }
    }
}
