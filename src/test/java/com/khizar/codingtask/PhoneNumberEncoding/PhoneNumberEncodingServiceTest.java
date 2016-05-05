package com.khizar.codingtask.PhoneNumberEncoding;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.khizar.codingtask.PhoneNumberEncoding.service.PhoneNumberEncodingService;


/**
 * Unit test for PhoneNumberEncodingService.
 */
public class PhoneNumberEncodingServiceTest {
    private PhoneNumberEncodingService numberEncodingService;

    @Before
    public void setUp() {
        numberEncodingService = new PhoneNumberEncodingService();
        numberEncodingService.readAndConvertDictionaryFileToMap("dictionary.test.file.name");
    }
    
    /**
     * Test for number of encodings
     */
    @Test
    public void encodingsNumberTest() {
	List<List<String>> encodings = numberEncodingService.findAllEncodings("107835", 0, false);
	assertTrue(encodings.size() == 3);
    }
    
    /**
     * Test for numbers with no encodings
     */
    @Test
    public void noEncodingTest() {
	List<List<String>> encodings = numberEncodingService.findAllEncodings("112", 0, false);
	assertTrue(encodings.size() == 0);
    }
    
    /**
     * Testing the exact match for an encoding
     */
    @Test
    public void encodingMatchTest() {
	List<List<String>> encodings = numberEncodingService.findAllEncodings("381482", 0, false);
	String spaceSeperatedencoding = "";
	assertTrue(encodings.size() == 1);
	
	for (String string : encodings.get(0)) {
	    spaceSeperatedencoding+=string;
	    spaceSeperatedencoding+=" ";
	}
	assertTrue("so 1 Tor".equals(spaceSeperatedencoding.trim()));
    }
    
    /**
     * Testing mapping of words with an umlaut.
     */
    @Test
    public void mapDictionaryWordToNumberUmlautTest() {
	String mappedString = numberEncodingService.mapDictionaryWordToNumber("bo\"s");
	assertTrue(mappedString.equals("783"));
    }
    
}
