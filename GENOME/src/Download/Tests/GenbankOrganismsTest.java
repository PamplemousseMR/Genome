package Download.Tests;

import Download.GenbankOrganisms;
import Exception.MissException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GenbankOrganismsTest {
    @Test
    void runTest() throws MissException {
        GenbankOrganisms go = GenbankOrganisms.getInstance();
        go.downloadOrganisms();

        int count = 0;
        while (go.hasNext()) {
            go.getNext();
            count++;
        }

        count += go.getFailedOrganism();

        assertEquals(count, go.getTotalCount());
    }

}