package Download.Tests;

import Download.RawOrganism;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.Format;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RawOrganismTest {

    private final static String s_ID = "27587";
    private final static String s_NAME = "Lachnospiraceae bacterium UBA4380";
    private final static String s_KINGDOM = "Bacteria";
    private final static String s_GROUP = "Terrabacteria group";
    private final static String s_SUBGROUP = "Firmicutes";
    private final static String s_RELEASE = "2017-09-28T00:00:00Z";
    private final static String s_MODIFICATION = "2017-09-28T00:00:00Z";
    private final static String s_VERSION = "1593911655818854400";
    private final static String s_REPLICONS = "NC_011961.1";

    private RawOrganism m_rawOrganism;

    @BeforeEach
    void setUp() {
        String json = "{\"subgroup\":\""+s_SUBGROUP+"\"," +
                "\"organism\":\""+s_NAME+"\"," +
                "\"release_date\":\""+s_RELEASE+"\"," +
                "\"_version_\":"+s_VERSION+"," +
                "\"id\":"+s_ID+"," +
                "\"kingdom\":\""+s_KINGDOM+"\"," +
                "\"modify_date\":\""+s_MODIFICATION+"\"," +
                "\"replicons\":\"unnamed:"+s_REPLICONS+"/CP001276.1\"," +
                "\"group\":\""+s_GROUP+"\"}";
        m_rawOrganism = new RawOrganism(new JSONObject(json));
    }

    @Test
    void getId() {
        assertTrue(s_ID.compareTo(String.valueOf(m_rawOrganism.getId())) == 0);
    }

    @Test
    void getName() {
        assertTrue(s_NAME.compareTo(m_rawOrganism.getName()) == 0);
    }

    @Test
    void getKingdom() {
        assertTrue(s_KINGDOM.compareTo(m_rawOrganism.getKingdom()) == 0);
    }

    @Test
    void getGroup() {
        assertTrue(s_GROUP.compareTo(m_rawOrganism.getGroup()) == 0);
    }

    @Test
    void getSubGroup() {
        assertTrue(s_SUBGROUP.compareTo(m_rawOrganism.getSubGroup()) == 0);
    }

    @Test
    void getReplicons() {
        assertTrue(s_REPLICONS.compareTo(m_rawOrganism.getReplicons().get(0)) == 0);
    }

    @Test
    void getModificationDate() {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuffer date = new StringBuffer(formatter.format(m_rawOrganism.getModificationDate()));
        date.replace(10,11,"T");
        date.replace(19,20,"Z");
        assertTrue(s_MODIFICATION.compareTo(date.toString()) == 0);
    }

    @Test
    void getVersion() {
        assertTrue(s_VERSION.compareTo(String.valueOf(m_rawOrganism.getVersion())) == 0);
    }
}