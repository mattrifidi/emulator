/*
 *  LogInputScraper.java
 *
 *  Created:	Dec 13, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 *  Author:    Kyle Neumeier - kyle@pramari.com
 */
package org.rifidi.emulator.reader.llrp.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import org.rifidi.utilities.formatting.ByteAndHexConvertingUtility;

import edu.uark.csce.llrp.Message;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 *
 */
public class LogInputScraper {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LogInputScraper scraper = new LogInputScraper();
		scraper.readFileLoop(args[0]);

	}
	
	private void readFileLoop(String fileName){
		try {
	        BufferedReader in = new BufferedReader(new FileReader(fileName));
	        String str;
	        while ((str = in.readLine()) != null) {
	            process(str);
	        }
	        in.close();
	    } catch (IOException e) {
	    }
	}
	
	private void process(String line){
		if(line.contains("INPUT")){
			String bytes = line.substring((line.lastIndexOf('>') + 1), line.length() - 1);
			bytes = bytes.trim();
			printLLRPMessage(bytes);
		}
	}
	
	private void printLLRPMessage(String byteString){
		byte[] bytes = ByteAndHexConvertingUtility.fromHexString(byteString);
		ByteArrayInputStream is = new ByteArrayInputStream(bytes);
		try {
			Message m = Message.receive(is);
			System.out.println(m.toXMLString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
