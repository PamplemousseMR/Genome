package Download;

import Utils.Logs;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RawOrganism {
    private int m_id;
    private String m_organism;
    private String m_kingdom;
    private String m_group;
    private List<String> m_replicons;

    private LocalDateTime m_releaseDate;
    private LocalDateTime m_modificationDate;

    private int m_version;

    public RawOrganism(JSONObject obj) throws JSONException{

        try {
            m_id = obj.getInt("id");
            m_organism = obj.getString("organism");
            m_kingdom = obj.getString("kingdom");
            m_group = obj.getString("group");
            m_version = obj.getInt("_version_");
        }catch (JSONException e){
            Logs.exception(e);
            throw e;
        }

        // Replicons formatting
        if (obj.has("replicons")) {
            m_replicons = Arrays.asList(obj.getString("replicons").split("\\s*,\\s*"));
        } else {
            m_replicons = Collections.emptyList();
        }

        // Dates formatting
        DateTimeFormatter format = DateTimeFormatter.ISO_DATE_TIME;
        if (obj.has("release_date"))
            m_releaseDate = LocalDateTime.parse(obj.getString("release_date"), format);
        if (obj.has("modify_date"))
            m_modificationDate = LocalDateTime.parse(obj.getString("modify_date"), format);
    }

    public int getId() {
        return m_id;
    }

    public String getOrganism() {
        return m_organism;
    }

    public String getKingdom() {
        return m_kingdom;
    }

    public String getGroup() {
        return m_group;
    }

    public List<String> getReplicons() {
        return m_replicons;
    }

    public LocalDateTime getReleaseDate() {
        return m_releaseDate;
    }

    public LocalDateTime getModificationDate() {
        return m_modificationDate;
    }

    public int getVersion() {
        return m_version;
    }
}
