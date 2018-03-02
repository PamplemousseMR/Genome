package Download.Tests;

import Download.GenbankCDS;
import org.junit.jupiter.api.Test;
import Exception.HTTPException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class GenbankCDSTest {
    @Test
    void runTest() throws HTTPException, IOException {
        GenbankCDS gr = new GenbankCDS("NC_007788.1");
        gr.getRefseqData();
        assertNotEquals(0, gr.getRefseqDataSize());
    }
}