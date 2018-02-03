package Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.Set;

public final class Options {

	/**
	 * File's name
	 */
	private static final String s_OPTIONS_FILE_NAME = "options.ini";

	/**
	 * Option's properties
	 */
	private static Properties m_properties = null;

	private static String s_BASE_LINK = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/";

	/**
	 * Open the options's file in order to read options and fill the static fields with it
	 */
	public static void initializeOptions() {
		File file = new File(s_OPTIONS_FILE_NAME);

		// Create file if it's not exist 
		try {
			if(!file.createNewFile()){
				throw new IOException("Can't create new file");
			}
		}catch(IOException e) {
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
			if(m_properties.getProperty(key) != null) {
				try {
					Options.class.getDeclaredField(key).set(Options.class,m_properties.getProperty(key));
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
		if(m_properties != null) {
			File file = new File(s_OPTIONS_FILE_NAME);

			// Create file if it's not exist 
			try {
				if(!file.createNewFile()){
					throw new IOException("Can't create new file");
				}
			}catch(IOException e) {
				Logs.exception(e);
				return;
			}
			
			// Set all unmodified properties
			for(Field field : Options.class.getDeclaredFields()) {
				if(field.getName().compareTo("s_OPTIONS_FILE_NAME")!=0 && field.getName().compareTo("m_properties")!=0 && m_properties.getProperty(field.getName()) == null) {
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

	public static String getBaseLink() {
		return s_BASE_LINK;
	}

	public static void setBaseLink(String _overviewLink) {
		m_properties.put("s_OVERVIEW_LINK", _overviewLink);
	}

}
