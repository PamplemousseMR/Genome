package Download.Tests;

import Download.GenbankOrganisms;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GenbankOrganismsTest {
    @Test
    void runTest() {

        GenbankOrganisms go = GenbankOrganisms.getInstance();
        go.downloadOrganisms();

        int count = 0;
        while (go.hasNext()) {
            go.getNext().getOrganism();
            count++;
        }

        assertEquals(count, go.getTotalCount());

    }

}