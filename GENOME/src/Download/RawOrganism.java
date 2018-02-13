package Download;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RawOrganism {
    private int id;
    private String organism;
    private String kingdom;
    private String group;
    private List<String> replicons;

    private LocalDateTime releaseDate;
    private LocalDateTime modificationDate;

    private int version;

    public RawOrganism(JSONObject obj)
    {
        id = obj.getInt("id");
        organism = obj.getString("organism");
        kingdom = obj.getString("kingdom");
        group = obj.getString("group");
        version = obj.getInt("_version_");

        // Replicons formatting
        if (obj.has("replicons")) {
            // Parse replicons
            replicons = Arrays.asList(
                    obj.getString("replicons").split("\\s*,\\s*"));
        } else {
            replicons = Collections.emptyList();
        }

        // Dates formatting
        DateTimeFormatter format = DateTimeFormatter.ISO_DATE_TIME;
        releaseDate = LocalDateTime.parse(obj.getString("release_date"), format);
        modificationDate = LocalDateTime.parse(obj.getString("modify_date"), format);
    }

    public int getId()
    {
        return id;
    }

    public String getOrganism()
    {
        return organism;
    }

    public String getKingdom()
    {
        return kingdom;
    }

    public String getGroup()
    {
        return group;
    }

    public List<String> getReplicons()
    {
        return replicons;
    }

    public LocalDateTime getReleaseDate()
    {
        return releaseDate;
    }

    public LocalDateTime getModificationDate()
    {
        return modificationDate;
    }

    public int getVersion()
    {
        return version;
    }
}
