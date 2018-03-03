package Download.Tests;

import Download.GenbankCDS;
import Exception.HTTPException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GenbankCDSTest {
    @Test
    void runTest() throws HTTPException, IOException {
        for (int i = 0; i < 10; ++i) {
            GenbankCDS gr = new GenbankCDS("NC_00778" + i);
            gr.download();
            assertTrue(gr.getRefseqData().indexOf("ORIGIN") != -1);
        }
    }
}