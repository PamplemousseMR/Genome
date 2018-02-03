package Data.Tests;

import org.junit.jupiter.api.Test;
import Data.*;

class StatisticsTest {

    public static void printTriTable(Statistics _stat){
        System.out.print("TRI\tPhase0\tFreq0\tPhase1\tFreq1\tPhase2\tFreq2\t");
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
        DataBase db = DataBase.getInstance();
        Kingdom k = new Kingdom("Eukaryota");
        Group g = new Group("Animals");
        SubGroup sg = new SubGroup("Mammals");
        Organism org = new Organism("Humans");

        Replicon rep1,rep2;
        rep1 = new Replicon(Statistics.Type.CHROMOSOME,"CR1");
            rep1.addSequence(new StringBuffer("AATAAATACTTACAGGTCAGTCACGATCGAACGATCGACGTACGTATCGATCGTCT"));
            rep1.addSequence(new StringBuffer("ACTGCATGTCGACTGACTGACTGCGTACGTACGTCGTACATCGTAGCATTGCATAGCATGATCGCATCAGT"));
            rep1.addSequence(new StringBuffer("AACAATAAGAATAAGAACAAGAACAGGAT"));
            rep1.addSequence(new StringBuffer("AGCGCTGCTAGCGGCGCTAACGATCATCGCATCCGATCTCT"));
            rep1.addSequence(new StringBuffer("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));

        rep2 = new Replicon(Statistics.Type.MITOCHONDRION,"CR2");
            rep2.addSequence(new StringBuffer("AATAAATACTTACAGGTCAGTCACGATCGAACGATCGACGTACGTATCGATCGTCT"));
            rep2.addSequence(new StringBuffer("ACTGCATGTCGACTGACTGACTGCGTACGTACGTCGTACATCGTAGCATTGCATAGCATGATCGCATCAGT"));
            rep2.addSequence(new StringBuffer("AACAATAAGAATAAGAACAAGAACAGGAT"));
            rep2.addSequence(new StringBuffer("AGCGCTGCTAGCGGCGCTAACGATCATCGCATCCGATCTCT"));
            rep2.addSequence(new StringBuffer("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));

        db.start();
        k.start();
        g.start();
        sg.start();

        db.addKingdom(k);
        k.addGroup(g);
        g.addSubGroup(sg);
        sg.addOrganism(org);
        org.addReplicon(rep1);
        org.addReplicon(rep2);

        db.stop();
        k.stop();
        g.stop();
        sg.stop();

        org.finish();

        for(Statistics stat : db.getStatistics().values()){
            System.out.println("Table sum_"+stat.getType()+" nbElements "+db.getGenomeNumber().get(stat.getType()));
            printTriTable(stat);
        }
    }

}