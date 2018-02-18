package Download.Tests;

import Download.GenbankOrganisms;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GenbankOrganismsTest {
    @Test
    void runTest() throws Exception {

        GenbankOrganisms go = GenbankOrganisms.getInstance();
        go.downloadOrganisms();

        int count = 0;
        while (go.hasNext()) {
            go.getNext().getOrganism();
            count++;
        }

        while(go.hasFailedChunk()){
            go.getNextFailedChunk();
        }

        assertEquals(count, go.getTotalCount());

    }

}