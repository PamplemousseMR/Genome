package Data;

import Utils.Logs;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RawOrganism {

    private static final String s_REPLICON = "replicons";
    private static final String s_ID = "id";
    private static final String s_NAME = "organism";
    private static final String s_KINGDOM = "kingdom";
    private static final String s_GROUP = "group";
    private static final String s_SUBGROUP = "subgroup";
    private static final String s_MODIFICATION_DATE = "modify_date";
    private static final String s_RELEASE_DATE = "release_date";
    private static final String s_VERSION = "_version_";
    private static final String s_REGEX = "NC_[^(\\/|;| |\\n)]*";

    /**
     * ID of this organism
     */
    private int m_id;
    /**
     * Name of this organism
     */
    private String m_name;
    /**
     * Kingdom of this organism
     */
    private String m_kingdom;
    /**
     * Group of this organism
     */
    private String m_group;
    /**
     * Subgroup of this organism
     */
    private String m_subGroup;
    /**
     * Modification date of this organism
     */
    private Date m_modificationDate;
    /**
     * Version of this organism
     */
    private int m_version;
    /**
     * List of replicon's ID of this organism
     */
    private ArrayList<String> m_replicons;

    /**
     * Class constructor
     * @param obj, the json object to parse
     * @throws JSONException, if an error occurred
     */
    public RawOrganism(JSONObject obj) throws JSONException{

        try {
            m_id = obj.getInt(s_ID);
            m_name = obj.getString(s_NAME);
            m_kingdom = obj.getString(s_KINGDOM);
            m_group = obj.getString(s_GROUP);
            m_subGroup = obj.getString(s_SUBGROUP);
            m_version = obj.getInt(s_VERSION);
        }catch (JSONException e){
            Logs.exception(e);
            throw e;
        }

        // Replicons formatting
        if (obj.has(s_REPLICON)) {
            m_replicons = new ArrayList<>();
            Pattern pattern = Pattern.compile(s_REGEX);
            Matcher m = pattern.matcher(obj.getString(s_REPLICON));
            while (m.find()) {
                m_replicons.add(m.group(0));
            }
        } else {
            m_replicons = new ArrayList<>();
        }

        // Dates formatting
        DateTimeFormatter format = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime dateTime = null;
        if (obj.has(s_MODIFICATION_DATE)) {
            dateTime = LocalDateTime.parse(obj.getString(s_MODIFICATION_DATE), format);
        } else if (obj.has(s_RELEASE_DATE)) {
            dateTime = LocalDateTime.parse(obj.getString(s_RELEASE_DATE), format);
        }

        if(dateTime != null) {
            Calendar cal = Calendar.getInstance();
            cal.set(dateTime.getYear(),dateTime.getMonthValue() - 1, dateTime.getDayOfMonth(), 0, 0);
            m_modificationDate = cal.getTime();
        }
    }

    /**
     * Get the id
     * @return the id
     */
    public int getId() {
        return m_id;
    }

    /**
     * Get the name
     * @return the name
     */
    public String getName() {
        return m_name;
    }

    /**
     * Get the kingdom name
     * @return the kingdom name
     */
    public String getKingdom() {
        return m_kingdom;
    }

    /**
     * Get the group name
     * @return the group name
     */
    public String getGroup() {
        return m_group;
    }

    /**
     * Get the subGroup name
     * @return the subGroup name
     */
    public String getSubGroup() {
        return m_subGroup;
    }

    /**
     * Get the replicon's id list
     * @return the replicon's id list
     */
    public ArrayList<String> getReplicons() {
        return m_replicons;
    }

    /**
     * Get the modification date
     * @return the modification date
     */
    public Date getModificationDate() {
        return m_modificationDate;
    }

    /**
     * Get the version of the organism
     * @return the version's number
     */
    public int getVersion() {
        return m_version;
    }
}
