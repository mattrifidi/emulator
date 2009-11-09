/*
 *  AwidAutonomous.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.awid.commandhandler;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.awid.module.AwidReaderSharedResources;
import org.rifidi.emulator.reader.awid.sharedrc.tagmemory.AwidTagMemory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.sharedrc.radio.generic.GenericRadio;
import org.rifidi.tags.enums.TagGen;
import org.rifidi.tags.impl.RifidiTag;
import org.rifidi.utilities.ByteAndHexConvertingUtility;

/**
 * The autonomous commands. These commands are not called directly by the
 * outside but instead called repeatedly within the simulation itself.
 * 
 * @author Matthew Dean - matt@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class AwidAutonomous {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory.getLog(AwidAutonomous.class);

	/**
	 * Single tag meter for EPC C1 Gen2 tags.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject single_epc_c1_gen2(CommandObject arg,
			AbstractReaderSharedResources asr) {
		logger.debug("calling single_epc_c1_gen2");

		AwidReaderSharedResources awidSR = (AwidReaderSharedResources) asr;

		GenericRadio newRadio = awidSR.getRadio();

		logger.debug("before scan");

		newRadio.scan(null, awidSR.getTagMemory());
		logger.debug("after scan");

		AwidTagMemory memory = (AwidTagMemory) asr.getTagMemory();
		ArrayList<RifidiTag> tagList = (ArrayList<RifidiTag>) memory
				.getTagReport();

		ArrayList<Object> retVal = new ArrayList<Object>();

		if (awidSR.isRf_power()) {
			for (RifidiTag i : tagList) {
				
				if (i.getTagGen().equals(TagGen.GEN2)) {
					String returnValue = "20 00 ";
					returnValue += AwidCommon.formatTagString("30"
							+ "00"
							+ ByteAndHexConvertingUtility.toHexString(
									i.getTag().readId()).replace(" ", "")
							+ getCRC(i));
					this.getPrefix(returnValue);
					retVal.add(this.getPrefix(returnValue));
				}
			}
		}
		arg.setReturnValue(retVal);

		return arg;
	}
	
	/**
	 * Single tag meter for EPC C1 Gen2 tags.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject gen2_portal(CommandObject arg,
			AbstractReaderSharedResources asr) {
		logger.debug("calling single_epc_c1_gen2");

		AwidReaderSharedResources awidSR = (AwidReaderSharedResources) asr;

		GenericRadio newRadio = awidSR.getRadio();
		
		

		logger.debug("before scan");

		newRadio.scan(null, awidSR.getTagMemory());
		logger.debug("after scan");

		AwidTagMemory memory = (AwidTagMemory) asr.getTagMemory();
		ArrayList<RifidiTag> tagList = (ArrayList<RifidiTag>) memory
				.getTagReport();

		ArrayList<Object> retVal = new ArrayList<Object>();

		if (awidSR.isRf_power()) {
			for (RifidiTag i : tagList) {
				
				if (i.getTagGen().equals(TagGen.GEN2)) {
					String returnValue = "20 1E ";
					returnValue += AwidCommon.formatTagString("30"
							+ "00"
							+ ByteAndHexConvertingUtility.toHexString(
									i.getTag().readId()).replace(" ", "")
							+ getCRC(i));
					this.getPrefix(returnValue);
					retVal.add(this.getPrefix(returnValue));
				}
			}
		}
		arg.setReturnValue(retVal);

		return arg;
	}

	/**
	 * Single tag meter for EPC C1 Gen1 tags.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject single_epc_c1_gen1(CommandObject arg,
			AbstractReaderSharedResources asr) {
		logger.debug("Started gen1 meter");

		AwidReaderSharedResources awidSR = (AwidReaderSharedResources) asr;

		GenericRadio newRadio = awidSR.getRadio();

		logger.debug("before scan");
		newRadio.scan(null, awidSR.getTagMemory());

		AwidTagMemory memory = (AwidTagMemory) asr.getTagMemory();
		ArrayList<RifidiTag> tagList = (ArrayList<RifidiTag>) memory
				.getTagReport();

		ArrayList<Object> retVal = new ArrayList<Object>();

		// TODO: Is this right? I don't think it is.
		if (awidSR.isRf_power()) {
			for (RifidiTag i : tagList) {
				if (i.getTagGen().equals(TagGen.GEN1)) {
					String tempString1 = "16 00 "
							+ ByteAndHexConvertingUtility.toHexString(i
									.getTag().readId());
					retVal.add(this.getPrefix(tempString1));
				}
			}
		}

		for (Object i : retVal) {
			logger.debug("RETURNING THIS TAG: " + i.toString());
		}

		arg.setReturnValue(retVal);

		return arg;
	}

	/**
	 * Tag meter for EPC C1 Gen1 tags.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject epc_c1_gen1(CommandObject arg,
			AbstractReaderSharedResources asr) {

		AwidReaderSharedResources awidSR = (AwidReaderSharedResources) asr;

		GenericRadio newRadio = awidSR.getRadio();

		logger.debug("before scan");
		newRadio.scan(null, awidSR.getTagMemory());
		logger.debug("after scan");

		AwidTagMemory memory = (AwidTagMemory) asr.getTagMemory();
		ArrayList<RifidiTag> tagList = (ArrayList<RifidiTag>) memory
				.getTagReport();

		logger.debug("after the getTagList tagList size=" + tagList.size());

		ArrayList<Object> retVal = new ArrayList<Object>();

		if (awidSR.isRf_power()) {
			for (RifidiTag i : tagList) {
				if (i.getTagGen().equals(TagGen.GEN1)) {
					String tempString1 = "16 11 "
							+ ByteAndHexConvertingUtility.toHexString(i
									.getTag().readId()) + " 20";

					retVal.add(this.getPrefix(tempString1));
				}
			}
		}

		logger.debug("after the adding of the strings retVal size = "
				+ retVal.size());

		arg.setReturnValue(retVal);

		return arg;
	}

	/**
	 * Tag meter for EPC C1 Gen2 tags.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject epc_c1_gen2(CommandObject arg,
			AbstractReaderSharedResources asr) {

		AwidReaderSharedResources awidSR = (AwidReaderSharedResources) asr;

		GenericRadio newRadio = awidSR.getRadio();

		logger.debug("before scan");
		newRadio.scan(null, awidSR.getTagMemory());
		logger.debug("after scan");
		AwidTagMemory memory = (AwidTagMemory) asr.getTagMemory();

		ArrayList<RifidiTag> tagList = (ArrayList<RifidiTag>) memory
				.getTagReport();

		logger.debug("after the getTagList tagList size=" + tagList.size());

		ArrayList<Object> retVal = new ArrayList<Object>();

		if (awidSR.isRf_power()) {
			for (RifidiTag i : tagList) {
				if (i.getTagGen().equals(TagGen.GEN2)) {
					String tempString1 = "30 00 "
							+ AwidCommon.formatTagString("30"
									+ "00"
									+ ByteAndHexConvertingUtility.toHexString(
											i.getTag().readId()).replace(" ",
											"") + getCRC(i));
					retVal.add(this.getPrefix(tempString1));
				}
			}
		}

		logger.debug("after the adding of the strings retVal size = "
				+ retVal.size());

		arg.setReturnValue(retVal);

		return arg;
	}

	private String getPrefix(String data) {
		Integer count = ((data.length() + 1) / 3) + 3;
		String retVal = Integer.toHexString(count) + " " + data;
		return retVal;
	}

	public String getCRC(RifidiTag tag) {
		return calculateCRC(calculateProtocolArray("30", "00", tag.getTag()
				.readId()));
	}

	private static byte[] calculateProtocolArray(String header,
			String protocol, byte[] tagData) {

		byte[] retVal = new byte[tagData.length + 2];

		retVal[0] = Byte.parseByte(header, 16);
		retVal[1] = Byte.parseByte(protocol, 16);
		for (int i = 2; i < (tagData.length + 2); i++) {
			retVal[i] = tagData[i - 2];
		}
		return retVal;
	}

	private static String calculateCRC(byte[] b) {
		int crc;
		crc = 0xFFFF;
		for (byte bit : b) {
			crc = ((int) bit << 8) ^ crc;
			for (int i = 0; i < 8; i++) {
				if ((crc & 0x8000) != 0) {
					crc = (crc << 1) ^ 0x1021;
				} else {
					crc <<= 1;
				}
			}
		}

		int retInt = (crc ^ 0xFFFF);

		String firstString = Integer.toHexString(retInt);

		String lastString = firstString.substring(firstString.length() - 4,
				firstString.length());

		return lastString;
	}
}
