package Download.Tests;

import Download.GenbankOrganisms;
import Utils.Options;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GenbankOrganismsTest {
    @Test
    void runTest() throws Exception {

        GenbankOrganisms go = GenbankOrganisms.getInstance();
        go.downloadOrganisms();

        int count = 0;
        while (go.hasNext()) {
            go.getNext();
            count++;
        }

        if(go.hasFailedChunk()>0){
            count+=go.hasFailedChunk()* Options.getDownloadStep();
        }

        assertEquals(count, go.getTotalCount());

    }

}