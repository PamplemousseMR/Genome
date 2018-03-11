package Download;

import Utils.Logs;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class OrganismParser {

    /**
     * Key to get replicons
     */
    private static final String s_REPLICON = "replicons";
    /**
     * Key to get the id
     */
    private static final String s_ID = "id";
    /**
     * Key to get the name
     */
    private static final String s_NAME = "organism";
    /**
     * Key to get the kingdom
     */
    private static final String s_KINGDOM = "kingdom";
    /**
     * Key to get the group
     */
    private static final String s_GROUP = "group";
    /**
     * Key to get the subgroup
     */
    private static final String s_SUBGROUP = "subgroup";
    /**
     * Key to get the modification date
     */
    private static final String s_MODIFICATION_DATE = "modify_date";
    /**
     * Key to get the release date
     */
    private static final String s_RELEASE_DATE = "release_date";
    /**
     * Key to get the version
     */
    private static final String s_VERSION = "_version_";
    /**
     * Regex used to get only replicon which contain NC_
     */
    private static final String s_REGEX = "NC_[^(/|;| |\n|:)]*";
    /**
     * Object to parse
     */
    private final JSONObject m_object;
    /**
     * List of replicon's ID of this organism
     */
    private final ArrayList<String> m_replicons;
    /**
     * ID of this organism
     */
    private long m_id;
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
     * Version of this organism
     */
    private long m_version;
    /**
     * Modification date of this organism
     */
    private Date m_modificationDate;

    /**
     * Class constructor
     *
     * @param _obj, the json object to parse
     * @throws JSONException, if an error occurred
     */
    public OrganismParser(JSONObject _obj) {
        m_object = _obj;
        m_id = -1;
        m_name = null;
        m_kingdom = null;
        m_group = null;
        m_subGroup = null;
        m_version = -1;
        m_modificationDate = new Date();
        m_replicons = new ArrayList<>();
    }

    public void parse() throws JSONException {
        try {
            m_id = m_object.getLong(s_ID);
            m_name = m_object.getString(s_NAME);
            m_kingdom = m_object.getString(s_KINGDOM);
            m_group = m_object.getString(s_GROUP);
            m_subGroup = m_object.getString(s_SUBGROUP);
            m_version = m_object.getLong(s_VERSION);
        } catch (JSONException e) {
            final String message = "Unable to get basic data : " + m_object;
            Logs.warning(message);
            Logs.exception(new JSONException(message, e));
            throw e;
        }

        LocalDateTime dateTime = null;
        try {
            // Replicons formatting
            if (m_object.has(s_REPLICON)) {
                final Pattern pattern = Pattern.compile(s_REGEX, Pattern.CASE_INSENSITIVE);
                final Matcher m = pattern.matcher(m_object.getString(s_REPLICON));
                while (m.find()) {
                    m_replicons.add(m.group(0));
                }
            }

            // Dates formatting
            final DateTimeFormatter format = DateTimeFormatter.ISO_DATE_TIME;
            if (m_object.has(s_MODIFICATION_DATE)) {
                dateTime = LocalDateTime.parse(m_object.getString(s_MODIFICATION_DATE), format);
            } else if (m_object.has(s_RELEASE_DATE)) {
                dateTime = LocalDateTime.parse(m_object.getString(s_RELEASE_DATE), format);
            }
        } catch (JSONException e) {
            final String message = "Unable to get specifics data : " + m_object;
            Logs.warning(message);
            Logs.exception(new JSONException(message, e));
            throw e;
        }

        if (dateTime != null) {
            final Calendar cal = Calendar.getInstance();
            cal.set(dateTime.getYear(), dateTime.getMonthValue() - 1, dateTime.getDayOfMonth(), 0, 0, 0);
            m_modificationDate = cal.getTime();
        }
    }

    /**
     * Get the id
     *
     * @return the id
     */
    public long getId() {
        return m_id;
    }

    /**
     * Get the name
     *
     * @return the name
     */
    public String getName() {
        return m_name;
    }

    /**
     * Get the kingdom name
     *
     * @return the kingdom name
     */
    public String getKingdom() {
        return m_kingdom;
    }

    /**
     * Get the group name
     *
     * @return the group name
     */
    public String getGroup() {
        return m_group;
    }

    /**
     * Get the subGroup name
     *
     * @return the subGroup name
     */
    public String getSubGroup() {
        return m_subGroup;
    }

    /**
     * Get the replicon's id list
     *
     * @return the replicon's id list
     */
    public ArrayList<String> getReplicons() {
        return m_replicons;
    }

    /**
     * Get the modification date
     *
     * @return the modification date
     */
    public Date getModificationDate() {
        return m_modificationDate;
    }

    /**
     * Get the version of the organism
     *
     * @return the version's number
     */
    public long getVersion() {
        return m_version;
    }
}
