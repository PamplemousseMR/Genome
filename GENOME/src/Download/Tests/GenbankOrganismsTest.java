package Download.Tests;

import Download.GenbankOrganisms;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GenbankOrganismsTest {
    @Test
    void runTest() {

        GenbankOrganisms go = GenbankOrganisms.getInstance();
        go.downloadOrganisms();

        new Thread(() -> {
            int count = 0;
            while (go.hasNext()) {
                try {
                    System.out.println(go.getNext().getOrganism());
                    count++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            assertEquals(count, go.getTotalCount());
        }).start();

        System.out.println(go.getTotalCount());
    }

}