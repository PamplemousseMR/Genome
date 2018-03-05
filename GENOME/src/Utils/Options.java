package Utils;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.Set;

public final class Options {

    /**
     * File's name
     */
    private static final String s_OPTIONS_FILE_NAME = "options.ini";
    /**
     * Connection timeout in ms
     */
    private static final String s_DOWNLOAD_CONNECTION_TIMEOUT = "10000";
    /**
     * Number of organisms to download by request
     */
    private static final String s_DOWNLOAD_STEP_ORGANISM = "100000";
    /**
     * Base URL of genbank REST API for organism
     */
    private static final String s_ORGANISM_BASE_URL = "https://www.ncbi.nlm.nih.gov/Structure/ngram";
    /**
     * Base URL of genbank REST API for CDS
     */
    private static final String s_CDS_BASE_URL = "https://www.ncbi.nlm.nih.gov/sviewer/viewer.fcgi";
    /**
     * Option's properties
     */
    private static Properties m_properties = null;

    /**
     * Open the options's file in order to read options and fill the static fields with it
     */
    public static void initializeOptions() {
        File file = new File(s_OPTIONS_FILE_NAME);

        // Create file if it's not exist
        try {
            file.createNewFile();
            new FileOutputStream(file, false);
        } catch (IOException e) {
            Logs.warning("Unable to initialize options");
            Logs.exception(e);
            return;
        }

        // Load properties
        m_properties = new Properties();
        FileReader fr = null;
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            Logs.exception(e);
        }

        try {
            if (fr != null) {
                m_properties.load(fr);
                fr.close();
            }
        } catch (IOException e) {
            Logs.exception(e);
            return;
        }

        // Set properties to static fields
        Set<String> keys = m_properties.stringPropertyNames();
        for (String key : keys) {
            if (m_properties.getProperty(key) != null) {
                try {
                    Options.class.getDeclaredField(key).set(Options.class, m_properties.getProperty(key));
                } catch (NoSuchFieldException e) {
                    Logs.exception(e);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Write the option's file
     */
    public static void finalizeOptions() {
        if (m_properties != null) {
            File file = new File(s_OPTIONS_FILE_NAME);

            // Create file if it's not exist
            try {
                file.createNewFile();
            } catch (IOException e) {
                Logs.exception(e);
                return;
            }

            // Set all unmodified properties
            for (Field field : Options.class.getDeclaredFields()) {
                if (field.getName().compareTo("s_OPTIONS_FILE_NAME") != 0 && field.getName().compareTo("m_properties") != 0 && m_properties.getProperty(field.getName()) == null) {
                    try {
                        m_properties.put(field.getName(), field.get(""));
                    } catch (IllegalArgumentException e) {
                        Logs.exception(e);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Write file
            FileOutputStream out;
            try {
                out = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                Logs.exception(e);
                return;
            }

            try {
                m_properties.store(out, "");
            } catch (IOException e) {
                Logs.exception(e);
                return;
            }

            // Close stream
            try {
                out.close();
            } catch (IOException e) {
                Logs.exception(e);
            }
        }
    }

    public static int getDownloadStep() {
        return Integer.parseInt(s_DOWNLOAD_STEP_ORGANISM);
    }

    public static int getConnectionTimeout() {
        return Integer.parseInt(s_DOWNLOAD_CONNECTION_TIMEOUT);
    }

    public static String getOrganismBaseUrl() {
        return s_ORGANISM_BASE_URL;
    }

    public static String getCDSBaseUrl() {
        return s_CDS_BASE_URL;
    }

}
