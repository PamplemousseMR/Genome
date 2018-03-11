package Download.Tests;

import Download.OrganismParser;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.Format;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrganismParserTest {

    private final static String s_ID = "27587";
    private final static String s_NAME = "Lachnospiraceae bacterium UBA4380";
    private final static String s_KINGDOM = "Bacteria";
    private final static String s_GROUP = "Terrabacteria group";
    private final static String s_SUBGROUP = "Firmicutes";
    private final static String s_RELEASE = "2017-09-28T00:00:00Z";
    private final static String s_MODIFICATION = "2017-09-28T00:00:00Z";
    private final static String s_VERSION = "1593911655818854400";
    private final static String s_REPLICONS = "NC_011961.1";

    private final static String s_ID_JS = "\"id\":" + s_ID;
    private final static String s_NAME_JS = "\"organism\":\"" + s_NAME + "\"";
    private final static String s_KINGDOM_JS = "\"kingdom\":\"" + s_KINGDOM + "\"";
    private final static String s_GROUP_JS = "\"group\":\"" + s_GROUP + "\"";
    private final static String s_SUBGROUP_JS = "\"subgroup\":\"" + s_SUBGROUP + "\"";
    private final static String s_RELEASE_JS = "\"release_date\":\"" + s_RELEASE + "\"";
    private final static String s_MODIFICATION_JS = "\"modify_date\":\"" + s_MODIFICATION + "\"";
    private final static String s_VERSION_JS = "\"_version_\":" + s_VERSION;
    private final static String s_REPLICONS_JS = "\"replicons\":\"unnamed:" + s_REPLICONS + "\"";

    static private OrganismParser m_OrganismParser;

    @BeforeAll
    static void setUp() {
        String json = "{" + s_SUBGROUP_JS + "," + s_NAME_JS + "," + s_RELEASE_JS + "," + s_VERSION_JS + "," + s_ID_JS + "," + s_KINGDOM_JS + "," + s_MODIFICATION_JS + "," + s_REPLICONS_JS + "," + s_GROUP_JS + "}";
        m_OrganismParser = new OrganismParser(new JSONObject(json));
        m_OrganismParser.parse();
    }

    @Test
    void error() {
        final String jsonFail_0 = "{" + s_NAME_JS + "," + s_RELEASE_JS + "," + s_VERSION_JS + "," + s_ID_JS + "," + s_KINGDOM_JS + "," + s_MODIFICATION_JS + "," + s_REPLICONS_JS + "," + s_GROUP_JS + "}";
        assertThrows(JSONException.class, () -> new OrganismParser(new JSONObject(jsonFail_0)).parse());

        final String jsonFail_1 = "{" + s_SUBGROUP_JS + "," + s_RELEASE_JS + "," + s_VERSION_JS + "," + s_ID_JS + "," + s_KINGDOM_JS + "," + s_MODIFICATION_JS + "," + s_REPLICONS_JS + "," + s_GROUP_JS + "}";
        assertThrows(JSONException.class, () -> new OrganismParser(new JSONObject(jsonFail_1)).parse());

        final String jsonFail_2 = "{" + s_SUBGROUP_JS + "," + s_NAME_JS + "," + s_VERSION_JS + "," + s_ID_JS + "," + s_KINGDOM_JS + "," + s_MODIFICATION_JS + "," + s_REPLICONS_JS + "," + s_GROUP_JS + "}";
        OrganismParser fail2 = new OrganismParser(new JSONObject(jsonFail_2));
        fail2.parse();

        final String jsonFail_3 = "{" + s_SUBGROUP_JS + "," + s_NAME_JS + "," + s_RELEASE_JS + "," + s_ID_JS + "," + s_KINGDOM_JS + "," + s_MODIFICATION_JS + "," + s_REPLICONS_JS + "," + s_GROUP_JS + "}";
        assertThrows(JSONException.class, () -> new OrganismParser(new JSONObject(jsonFail_3)).parse());

        final String jsonFail_4 = "{" + s_SUBGROUP_JS + "," + s_NAME_JS + "," + s_RELEASE_JS + "," + s_VERSION_JS + "," + s_KINGDOM_JS + "," + s_MODIFICATION_JS + "," + s_REPLICONS_JS + "," + s_GROUP_JS + "}";
        assertThrows(JSONException.class, () -> new OrganismParser(new JSONObject(jsonFail_4)).parse());

        final String jsonFail_5 = "{" + s_SUBGROUP_JS + "," + s_NAME_JS + "," + s_RELEASE_JS + "," + s_VERSION_JS + "," + s_ID_JS + "," + s_MODIFICATION_JS + "," + s_REPLICONS_JS + "," + s_GROUP_JS + "}";
        assertThrows(JSONException.class, () -> new OrganismParser(new JSONObject(jsonFail_5)).parse());

        final String jsonFail_6 = "{" + s_SUBGROUP_JS + "," + s_NAME_JS + "," + s_RELEASE_JS + "," + s_VERSION_JS + "," + s_ID_JS + "," + s_KINGDOM_JS + "," + s_REPLICONS_JS + "," + s_GROUP_JS + "}";
        OrganismParser fail6 = new OrganismParser(new JSONObject(jsonFail_6));
        fail6.parse();

        final String jsonFail_7 = "{" + s_SUBGROUP_JS + "," + s_NAME_JS + "," + s_RELEASE_JS + "," + s_VERSION_JS + "," + s_ID_JS + "," + s_KINGDOM_JS + "," + s_MODIFICATION_JS + "," + s_GROUP_JS + "}";
        OrganismParser fail7 = new OrganismParser(new JSONObject(jsonFail_7));
        fail7.parse();

        final String jsonFail_8 = "{" + s_SUBGROUP_JS + "," + s_NAME_JS + "," + s_RELEASE_JS + "," + s_VERSION_JS + "," + s_ID_JS + "," + s_KINGDOM_JS + "," + s_MODIFICATION_JS + "," + s_REPLICONS_JS + "}";
        assertThrows(JSONException.class, () -> new OrganismParser(new JSONObject(jsonFail_8)).parse());
    }

    @Test
    void getId() {
        assertTrue(s_ID.compareTo(String.valueOf(m_OrganismParser.getId())) == 0);
    }

    @Test
    void getName() {
        assertTrue(s_NAME.compareTo(m_OrganismParser.getName()) == 0);
    }

    @Test
    void getKingdom() {
        assertTrue(s_KINGDOM.compareTo(m_OrganismParser.getKingdom()) == 0);
    }

    @Test
    void getGroup() {
        assertTrue(s_GROUP.compareTo(m_OrganismParser.getGroup()) == 0);
    }

    @Test
    void getSubGroup() {
        assertTrue(s_SUBGROUP.compareTo(m_OrganismParser.getSubGroup()) == 0);
    }

    @Test
    void getReplicons() {
        assertTrue(s_REPLICONS.compareTo(m_OrganismParser.getReplicons().get(0)) == 0);
    }

    @Test
    void getModificationDate() {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder date = new StringBuilder(formatter.format(m_OrganismParser.getModificationDate()));
        date.replace(10, 11, "T");
        date.replace(19, 20, "Z");
        assertTrue(s_MODIFICATION.compareTo(date.toString()) == 0);
    }

    @Test
    void getVersion() {
        assertTrue(s_VERSION.compareTo(String.valueOf(m_OrganismParser.getVersion())) == 0);
    }
}