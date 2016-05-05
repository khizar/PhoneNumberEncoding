package com.khizar.codingtask.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utils {

    public static boolean isNumeric(String str) {
	try {
	    Double.parseDouble(str);
	} catch (NumberFormatException nfe) {
	    return false;
	}
	return true;
    }

    /**
     * Gets the properties from the config file
     * 
     * @param property
     *            Name of the property in the config file
     * @return value of the said property
     */
    public static String getConfigProperty(String property) {
	String propFileName = "config/config.properties";
	
	return getProperty(property, propFileName);
    }
    
    /**
     * Gets the message from the messages file
     * 
     * @param property
     *            Name of the property in the messages file
     * @return value of the said property
     */
    public static String getMessage(String messageName) {
	String propFileName = "messages.properties";
	
	return getProperty(messageName, propFileName);
    }

    /**
     * Get a property from a file
     * @param property
     * 		Name of the property
     * @param propFileName
     * 		File name
     * @return
     */
    private static String getProperty(String property, String propFileName) {
	String result = "";
	InputStream inputStream = null;
	try {
	    Properties prop = new Properties();
	    inputStream = Utils.class.getClassLoader().getResourceAsStream(propFileName);

	    if (inputStream != null) {
		prop.load(inputStream);
	    } else {
		throw new FileNotFoundException("Property file '" + propFileName + "' not found in the classpath");
	    }

	    result = prop.getProperty(property);
	} catch (Exception e) {
	    System.out.println("Exception: " + e);
	    e.printStackTrace();

	} finally {
	    try {
		inputStream.close();
	    } catch (IOException e) {
		System.out.println("Cannot close InputStream for Config file!");
		e.printStackTrace();
	    }
	}
	return result;
    }
}
