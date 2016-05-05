package com.khizar.codingtask.PhoneNumberEncoding.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.khizar.codingtask.utils.Utils;

/**
 * This is the service class and contains the methods necessary to perform
 * the main functions of this application.
 *  
 * @author Khizar
 *
 */
public class PhoneNumberEncodingService {
    
    public final String DICTIONARY_FILE_NAME_PROPERTY = "dictionary.file.name";
    public final String INPUT_FILE_NAME_PROPERTY = "input.file.name";
    
    private Map<String, List<String>> convertedDictionary = new HashMap<>();
    private static final Map<Character, Integer> mainMap = new HashMap<>();
    static {
	// initiallize the map, do in static block
	mainMap.put('E', 0); mainMap.put('e', 0);
	mainMap.put('j', 1); mainMap.put('J', 1);
	mainMap.put('N', 1); mainMap.put('n', 1);
	mainMap.put('Q', 1); mainMap.put('q', 1);
	mainMap.put('R', 2); mainMap.put('r', 2);
	mainMap.put('W', 2); mainMap.put('w', 2);
	mainMap.put('X', 2); mainMap.put('x', 2);
	mainMap.put('D', 3); mainMap.put('d', 3);
	mainMap.put('S', 3); mainMap.put('s', 3);
	mainMap.put('Y', 3); mainMap.put('y', 3);
	mainMap.put('F', 4); mainMap.put('f', 4);
	mainMap.put('T', 4); mainMap.put('t', 4);
	mainMap.put('A', 5); mainMap.put('a', 5);
	mainMap.put('M', 5); mainMap.put('m', 5);
	mainMap.put('C', 6); mainMap.put('c', 6);
	mainMap.put('I', 6); mainMap.put('i', 6);
	mainMap.put('V', 6); mainMap.put('v', 6);
	mainMap.put('B', 7); mainMap.put('b', 7);
	mainMap.put('K', 7); mainMap.put('k', 7);
	mainMap.put('U', 7); mainMap.put('u', 7);
	mainMap.put('L', 8); mainMap.put('l', 8);
	mainMap.put('O', 8); mainMap.put('o', 8);
	mainMap.put('P', 8); mainMap.put('p', 8);
	mainMap.put('G', 9); mainMap.put('g', 9);
	mainMap.put('H', 9); mainMap.put('h', 9);
	mainMap.put('Z', 9); mainMap.put('z', 9);
    }



    
    /**
     * 
     * Reads the dictionary file and initializes the convertedDictionary map, which contains the dictionary
     * read into memory. Keeping it in a map with the number mapping as key makes it easy and efficient to
     * search for words for number combinations that are present in the dictionary.
     * 
     */
    public void readAndConvertDictionaryFileToMap(String fileName) {
	BufferedReader bufferredReader = null;
	try {
	    
	    String sCurrentLine;
	    bufferredReader = new BufferedReader(new FileReader(Utils.getConfigProperty(fileName)));
	    while ((sCurrentLine = bufferredReader.readLine()) != null) {
		String encodedNumberValue = mapDictionaryWordToNumber(sCurrentLine);
		if (convertedDictionary.containsKey(encodedNumberValue)) {
		    convertedDictionary.get(encodedNumberValue).add(sCurrentLine);
		} else {
		    convertedDictionary.put(encodedNumberValue, new LinkedList<String>(Arrays.asList(sCurrentLine)));
		}
	    }
	    
	    //Adding numbers from 0 to 9 to the map as themselves as key and value as at any point
	    //if no word can be used from dictionary, we can encode with the digit.
	    for (int index = 0; index < 10; index++) {
		convertedDictionary.put(String.valueOf(index), Arrays.asList(String.valueOf(index)));
	    }

	} catch (IOException e) {
	    System.out.println(Utils.getMessage("dictionary.filenotopened"));
	    e.printStackTrace();
	} finally {
	    try {
		if (bufferredReader != null) {
		    bufferredReader.close();
		}
	    } catch (IOException ex) {
		System.out.println(Utils.getMessage("dictionary.readernotclosed"));
		ex.printStackTrace();
	    }
	}

    }

    /**
     * Maps a given word to it corresponding number mapping using our mapping. The words
     * are in German, hence they contain umlaut characters. The umlaut characters are encoded
     * with a double qoute which should be ignored as our mapping does not contain the umlaut
     * characters.
     * 
     * @param word
     * 		A string containing alphabets and umlaut characters encoded with a double quote.
     * @return
     * 		String representation of the number that maps to that word using our mapping.
     */
    public String mapDictionaryWordToNumber(String word) {
	StringBuilder mappedToNumber = new StringBuilder();
	for (int i = 0; i < word.length(); i++) {
	    char currentChar = word.charAt(i);
	    if (currentChar != '\"') {
		mappedToNumber.append(mainMap.get(currentChar));
	    }
	}
	return mappedToNumber.toString();
    }


    
    /**
     * This method receives a phone number, which can be a random collection of dashes(-),
     * slashes(/) and digits. It strips the number of the everything except digits because
     * they are not to be encoded and calls the method to get all possible encodings for the
     * given number.
     * 
     * @param number
     * A phone number which can be a random copllection of dashes(-), slashes(/) and digits.
     * @return
     * A List containing encodings. Each encoding in itself is a list of words from the 
     * dictionary that the number can be encoded to.
     */
    public List<List<String>> encodeNumber(String number) {
	number = number.replaceAll("[^\\d]", "");
	return findAllEncodings(number, 0, false);
    }

    /**
     * Finds if this number can have an encoding from the dictionary starting with the first digit.
     * Returns true if it finds any such encoding else returns false.
     * @param number
     * A string of digits
     * @return
     * boolean true if it finds an encoding, false if it cant
     */
    private boolean hasAnotherEncoding(String number) {
	if (number.length() < 1) {
	    return false;
	}
	StringBuilder encoding = new StringBuilder();
	for (int i = 1; i < number.length(); i++) {
	    if (i == 1) {
		encoding.append(number.substring(0, 2));
	    } else {
		encoding.append(number.charAt(i));
	    }

	    if (convertedDictionary.containsKey(encoding.toString())) {
		return true;
	    }
	}
	return false;
    }

    /**
     * This method recursively finds all possible encodings for a given number. If and only if at a particular 
     * point no word at all from the dictionary can be inserted, a single digit from the phone number can be 
     * copied to the encoding instead. Two subsequent digits are never allowed, though.
     * 
     * @param number
     * 		A string on digits
     * @param startAt
     * 		integer stating the starting point from where to find the encodings in the number.
     * @param isLastEncodingNumeric
     * 		boolean which shows if the previous encoding was number or not
     * @return
     */
    public List<List<String>> findAllEncodings(String number, int startAt, boolean isLastEncodingNumeric) {
	LinkedList<List<String>> result = new LinkedList<>();
	if (startAt == number.length()) {
	    result.add(new LinkedList<String>());
	    return result;
	}
	for (int endAt = startAt + 1; endAt <= number.length(); endAt++) {
	    List<String> words = convertedDictionary.get(number.substring(startAt, endAt));
	    if (words != null) {
		List<List<String>> encodings = null;

		if ((endAt - startAt == 1) && (words.size() == 1) && (Utils.isNumeric(words.get(0)))) {
		  //check for if its a consecutive numeric encoding or if this number can be part of another word
		  //in the dictionary when combined with the next few numbers
		    if (isLastEncodingNumeric || hasAnotherEncoding(number.substring(endAt - 1))) {
			continue;
		    }
		    encodings = findAllEncodings(number, endAt, true);
		} else {
		    encodings = findAllEncodings(number, endAt, false);
		}

		for (String word : words) {
		    for (List<String> encoding : encodings) {
			List<String> enc = new LinkedList<>(encoding);
			enc.add(0, word);
			result.add(enc);
		    }
		}
	    }
	}
	return result;
    }

}
