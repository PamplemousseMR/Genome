package Download.Tests;

import Data.RawOrganism;
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
            RawOrganism ro = go.getNext();
            for(String s : ro.getReplicons()){
                System.out.println(s);
            }
            count++;
        }

        count+=go.getFailedOrganism();

        assertEquals(count, go.getTotalCount());

    }

}