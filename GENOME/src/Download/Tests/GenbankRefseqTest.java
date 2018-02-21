package Download.Tests;

import Download.GenbankRefseq;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class GenbankRefseqTest {
    @Test
    void runTest() throws Exception {

        GenbankRefseq gr = new GenbankRefseq("NC_007788.1");

        System.out.println(gr.getRefseqData());

        assertNotEquals(0, gr.getRefseqDataSize());
    }

}