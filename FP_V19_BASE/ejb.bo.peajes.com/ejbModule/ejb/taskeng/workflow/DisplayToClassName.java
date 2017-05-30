package ejb.taskeng.workflow;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

public class DisplayToClassName {
	private Properties properties = new Properties();

	public DisplayToClassName() {
		try {
			FileInputStream inStream = new FileInputStream(new File(
					"tasks" + File.separator + "step.properties"));
			properties.load(inStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String get(String displayName) {
		Set<Entry<Object, Object>> entrySet = properties.entrySet();
		for (Entry<Object, Object> entry : entrySet) {
			Object key = entry.getKey();
			String value = (String)entry.getValue();
			if(value.contains(displayName)){
				return (String)key;
			}
		}
		return null;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public Properties getProperties() {
		return properties;
	}
}
