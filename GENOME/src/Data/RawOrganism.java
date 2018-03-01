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

public class RawOrganism {

    private static final String s_REPLICON = "replicons";
    private static final String s_MODIFICATION_DATE = "modify_date";
    private static final String s_RELEASE_DATE = "release_date";
    private static final String s_VERSION = "_version_";
    private static final String s_REGEX = "NC_[^(\\/|;| |\\n)]*";

    private int m_id;
    private String m_name;
    private String m_kingdom;
    private String m_group;
    private String m_subGroup;
    private ArrayList<String> m_replicons;
    private Date m_modificationDate;
    private int m_version;

    /**
     * Class constructor
     * @param obj, the json object to parse
     * @throws JSONException, if an error occurred
     */
    public RawOrganism(JSONObject obj) throws JSONException{

        try {
            m_id = obj.getInt("id");
            m_name = obj.getString("organism");
            m_kingdom = obj.getString("kingdom");
            m_group = obj.getString("group");
            m_subGroup = obj.getString("subgroup");
            m_version = obj.getInt("_version_");
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
