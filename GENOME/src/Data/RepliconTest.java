package Data;

import org.junit.jupiter.api.Test;
import Data.*;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

class RepliconTest {

    public static void printTriTable(Statistics _stat){
        System.out.print("\nTRI\tPhase0\tFreq0\tPhase1\tFreq1\tPhase2\tFreq2\t");
        for(Statistics.Trinucleotide tri : Statistics.Trinucleotide.values()){
            System.out.print("\n"+tri+"\t");
            System.out.print((int)(float)_stat.getTable().get(tri).get(Statistics.Stat.PHASE0)+"\t");
            System.out.print(String.format("%.2f\t",_stat.getTable().get(tri).get(Statistics.Stat.FREQ0)));
            System.out.print((int)(float)_stat.getTable().get(tri).get(Statistics.Stat.PHASE1)+"\t");
            System.out.print(String.format("%.2f\t",_stat.getTable().get(tri).get(Statistics.Stat.FREQ1)));
            System.out.print((int)(float)_stat.getTable().get(tri).get(Statistics.Stat.PHASE2)+"\t");
            System.out.print(String.format("%.2f\t",_stat.getTable().get(tri).get(Statistics.Stat.FREQ2)));
            }
        System.out.println("\nTOTAL\t"+_stat.getTotalTrinucleotidePhase0()+"\t\t"
                +_stat.getTotalTrinucleotidePhase1()+"\t\t"
                +_stat.getTotalTrinucleotidePhase2()+"\n");
    }

    @Test
    void RepliconTest() throws Exception{
        SubGroup sg = new SubGroup("Mammal");
        sg.start();
        Organism org = new Organism("Human");
        sg.addOrganism(org);
        Replicon rep1,rep2,rep3,rep4,rep5,rep6;
        rep1 = new Replicon(Statistics.Type.CHROMOSOME,"CR1");
        rep1.addSequence(new StringBuffer("AATAAATACTTACAGGTCAGTCACGATCGAACGATCGACGTACGTATCGATCGTCT"));
        rep1.addSequence(new StringBuffer("ACTGCATGTCGACTGACTGACTGCGTACGTACGTCGTACATCGTAGCATTGCATAGCATGATCGCATCAGT"));
        rep1.addSequence(new StringBuffer("AACAATAAGAATAAGAACAAGAACAGGAT"));
        rep1.addSequence(new StringBuffer("AGCGCTGCTAGCGGCGCTAACGATCATCGCATCCGATCTCT"));
        rep1.addSequence(new StringBuffer("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));

        rep2 = new Replicon(Statistics.Type.CHROMOSOME,"CR2");
        rep2.addSequence(new StringBuffer("AATAAATACTTACAGGTCAGTCACGATCGAACGATCGACGTACGTATCGATCGTCT"));
        rep2.addSequence(new StringBuffer("ACTGCATGTCGACTGACTGACTGCGTACGTACGTCGTACATCGTAGCATTGCATAGCATGATCGCATCAGT"));
        rep2.addSequence(new StringBuffer("AACAATAAGAATAAGAACAAGAACAGGAT"));
        rep2.addSequence(new StringBuffer("AGCGCTGCTAGCGGCGCTAACGATCATCGCATCCGATCTCT"));
        rep2.addSequence(new StringBuffer("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));

        rep3 = new Replicon(Statistics.Type.CHROMOSOME,"CR3");

        rep4 = new Replicon(Statistics.Type.CHROMOSOME,"CR4");

        rep5 = new Replicon(Statistics.Type.CHROMOSOME,"CR5");

        rep6 = new Replicon(Statistics.Type.MITOCHONDRION,"MIT6");

        org.addReplicon(rep1);
        org.addReplicon(rep2);

        org.finish();

        printTriTable(rep1);
        printTriTable(rep2);
        printTriTable(org.getStatistics(Statistics.Type.CHROMOSOME));
        //printTriTable(org.getStatistics(Statistics.Type.MITOCHONDRION));
    }

}