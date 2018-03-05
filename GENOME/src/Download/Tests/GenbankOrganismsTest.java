package Download.Tests;

import Download.GenbankOrganisms;
import Download.OrganismParser;
import Exception.MissException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GenbankOrganismsTest {
    @Test
    void runTest() throws MissException {
        GenbankOrganisms go = GenbankOrganisms.getInstance();
        go.downloadOrganisms();

        int count = 0;
        while (go.hasNext()) {
            OrganismParser ro = go.getNext();
            for (String CDS : ro.getReplicons()) {
                assertTrue(CDS.indexOf("NC_") == 0);
            }
            count++;
        }

        count += go.getFailedOrganism();

        assertEquals(count, go.getTotalCount());
    }

}