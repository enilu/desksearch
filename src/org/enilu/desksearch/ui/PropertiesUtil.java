package org.enilu.desksearch.ui;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

/**
 * properties配置文件读写
 * 
 * @author enilu
 * 
 */
public class PropertiesUtil {
	private static String filename = System.getProperty("user.dir")
			+ "/conf.properties";

	public static String getValue(String key) throws IOException {
		Properties p = new Properties();
		System.out.println(IOUtils.toString(new FileInputStream(filename)));
		p.load(new FileInputStream(filename));
		String v = p.getProperty(key);
		if (v != null) {
			return String.valueOf(p.get(key));
		} else {
			return "";
		}
	}

	public static void setValue(String key, String value) throws IOException {
		Properties p = new Properties();
		p.load(new FileInputStream(filename));
		p.setProperty(key, value);

		FileOutputStream out = new FileOutputStream(filename);
		p.store(out, "update");
		out.close();
	}
}
