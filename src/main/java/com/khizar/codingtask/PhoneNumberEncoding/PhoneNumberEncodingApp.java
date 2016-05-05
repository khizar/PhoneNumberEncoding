package com.khizar.codingtask.PhoneNumberEncoding;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.khizar.codingtask.PhoneNumberEncoding.service.PhoneNumberEncodingService;
import com.khizar.codingtask.utils.Utils;


/**
 * 
 * This is the main class for the app. It simply starts the application, uses the 
 * PhoneNumberEncodingService to encode numbers and prints the output. The error messages
 * come from the messaeges.properties file which is located in the resources folder. The 
 * Config file contains names of the dictionary file and input file which are in the root 
 * folder of the app. There is also a Utils class which contains some utility functions needed
 * by the app.
 * 
 * @author Khizar
 *
 */
public class PhoneNumberEncodingApp {

    public static void main(String[] args) {
	PhoneNumberEncodingApp encodingApp = new PhoneNumberEncodingApp();
	PhoneNumberEncodingService encodingService = new PhoneNumberEncodingService();

	encodingService.readAndConvertDictionaryFileToMap(encodingService.DICTIONARY_FILE_NAME_PROPERTY);
	encodingApp.readInputAndEncode(encodingService);
	
    }
    
    /**
     * 
     * This method reads the input file with phone numbers and encodes them. Since the phone numbers
     * can be infinitely large, this method uses java.util.Scanner to scan through each line and not
     * keep a reference to keep it memory efficient.
     * 
     */
    public void readInputAndEncode(PhoneNumberEncodingService encodingService) {
	FileInputStream inputStream = null;
	Scanner fileScanner = null;
	try {
	    inputStream = new FileInputStream(Utils.getConfigProperty(encodingService.INPUT_FILE_NAME_PROPERTY));
	    fileScanner = new Scanner(inputStream);
	    while (fileScanner.hasNextLine()) {
		
	        String phoneNumber = fileScanner.nextLine();
	        
	        List<List<String>> encodings = encodingService.encodeNumber(phoneNumber);
		if (!encodings.isEmpty()) {
		    StringBuilder encodingString = new StringBuilder();
		    for (List<String> list : encodings) {
			encodingString.setLength(0);
			for (String encoding : list) {
			    encodingString.append(encoding).append(" ");
			}
			System.out.println(phoneNumber + ": " + encodingString.toString().trim());
		    }

		}
	    }
	    // because the Scanner suppresses exceptions
	    if (fileScanner.ioException() != null) {
	        throw fileScanner.ioException();
	    }
	} catch (FileNotFoundException e) {
	    System.out.println(Utils.getMessage("input.filenotfound"));
	    e.printStackTrace();
	} catch (IOException e) {
	    System.out.println(Utils.getMessage("input.filenotopened"));
	    e.printStackTrace();
	} finally {
	    if (inputStream != null) {
	        try {
		    inputStream.close();
		} catch (IOException e) {
		    System.out.println(Utils.getMessage("input.streamnotclosed"));
		    e.printStackTrace();
		}
	    }
	    if (fileScanner != null) {
	        fileScanner.close();
	    }
	}
	
    }
}
