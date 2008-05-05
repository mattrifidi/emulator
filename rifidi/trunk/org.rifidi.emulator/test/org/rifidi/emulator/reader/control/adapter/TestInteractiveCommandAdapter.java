/*
 *  @(#)TestInteractiveCommandAdapter.java
 *
 *  Created:	Oct 27, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.control.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.rifidi.emulator.common.ControlSignal;

/**
 * A test interactive command adapter. It takes the old command parsing way of
 * Rifidi AlienReader 0.5 and adapts it to the CommandAdapter interface. Uses
 * simple helper methods which return constant values.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class TestInteractiveCommandAdapter implements CommandAdapter {

	/**
	 * A constant for a carriage return then a newline.
	 */
	private static String crnl = "\r\n";

	/**
	 * A constant string which the reader sends out when indication end of
	 * reply.
	 */
	private static String endofreply = "\r\n\0";

	/**
	 * The signal for connections.
	 */
	private ControlSignal<Boolean> connectionControlSignal;

	/**
	 * HashMap for the properties
	 */
	private Map<String,String> properties;

	/**
	 * The signal for reader power.
	 */
	private ControlSignal<Boolean> readerPowerControlSignal;

	/**
	 * Constructs a test sample server.
	 * 
	 * @param readerPowerControlSignal
	 *            The reader power signal to alert.
	 * @param connectionControlSignal
	 *            The connection signal to alert.
	 * 
	 */
	public TestInteractiveCommandAdapter(
			ControlSignal<Boolean> readerPowerControlSignal,
			ControlSignal<Boolean> connectionControlSignal) {
		/* Initialize properties */
		//this.properties = JFig.getInstance().getSection("AlienReader");

		/* If the JFIG didn't get set up correctly, make a blank one. */
		if (this.properties == null) {
			this.properties = new HashMap<String,String>();

		}

		/* Assign instance variables */
		this.connectionControlSignal = connectionControlSignal;
		this.readerPowerControlSignal = readerPowerControlSignal;

	}

	/**
	 * Executes an alien command.
	 * 
	 * @see org.rifidi.emulator.reader.control.adapter.CommandAdapter#executeCommand(byte[])
	 */
	public ArrayList<Object> executeCommand(byte[] command) {
		String commandString = new String(command);
		String retString = "";

		/* Should a prompt be shown after this commandString is parsed? */
		boolean showPrompt = true;

		// If the commandString contains a \1 at the beginning dont send back
		// the commandString prompt
		if (commandString.contains("\1")) {
			showPrompt = false;
		}

		/* Trim the leading / lagging whitespace */
		String trimmedCommand = commandString.trim();

		/* Echo the trimmed commandString */
		retString += (trimmedCommand + TestInteractiveCommandAdapter.crnl);

		/* Grab the first commandString */
		int firstSpace = trimmedCommand.indexOf(" ");
		String firstCommand = null;

		if (firstSpace != -1) {
			firstCommand = trimmedCommand.substring(0, firstSpace);
		} else {
			firstCommand = trimmedCommand;
		}

		/* Now, determine what to do based on the first commandString */
		if (firstCommand.equalsIgnoreCase("set")) {
			/* Grab the location of the first equal sign */
			int firstEqualsign = trimmedCommand.indexOf("=");

			/* Check that the message is well-formed */
			if (firstEqualsign != -1) {
				/* Grab the property and value for this set */
				String property = trimmedCommand.substring(firstSpace,
						firstEqualsign).trim();
				String value = "";
				/*
				 * Hack to avoid an weird behavior of the Alien Reader tool
				 * which sends commandStrings in small letters, will be avoided
				 * in future of another implementation of the commandString set
				 * so it is case insensitive.
				 */
				if (property.length() > 0) {
					String sub1 = property.substring(0, 1);
					String sub2 = property.substring(1, property.length());
					property = sub1.toUpperCase() + sub2;
				}

				/* Make sure we don't go out of bounds */
				if ((firstEqualsign + 1) != trimmedCommand.length()) {
					value = trimmedCommand.substring(firstEqualsign + 1).trim();
				}

				/* Check that value is not empty */
				if (!value.equals("")) {
					/* Now, try to set */
					if (this.properties.containsKey(property)) {
						this.properties.put(property, value);
						retString += (property + " = " + value + TestInteractiveCommandAdapter.endofreply);
					} else {
						/* Property wasn't in the property map, give error */
						retString += (this.properties.get("error") + TestInteractiveCommandAdapter.endofreply);
					}

				} else {
					retString += ("Error: Malformed message." + TestInteractiveCommandAdapter.endofreply);

				}

			} else {
				/* Malformed message */
				retString += ("Error: Malformed message." + TestInteractiveCommandAdapter.endofreply);

			}

		} else if (firstCommand.equalsIgnoreCase("get")) {
			/* Make sure there is a property to get */
			String property = "";
			//String arguments = "";
			if (firstSpace != -1) {
				// Since trailing whitespace was trimmed, no bounds check is
				// needed.

				/* See if there are additional arguments */
				int secondSpace = trimmedCommand.indexOf(" ", firstSpace + 1);

				/* If there is a second space, grab the arguments */
				if (secondSpace != -1) {
					property = trimmedCommand
							.substring(firstSpace, secondSpace).trim();
					//arguments = trimmedCommand.substring(secondSpace).trim();

				} else {
					/* Grab the argument to the get */
					property = trimmedCommand.substring(firstSpace).trim();

				}

			}

			/*
			 * Hack to avoid an weird behavior of the Alien Reader tool which
			 * sends commandStrings in small letters, will be avoided in future
			 * of another implementation of the commandString set so it is case
			 * insensitive.
			 */
			if (property.length() > 0) {
				String sub1 = property.substring(0, 1);
				String sub2 = property.substring(1, property.length());
				property = sub1.toUpperCase() + sub2;
			}

			if (property.equalsIgnoreCase("TagList")) {
				retString += (this.getTagList());

			} else if (property.equalsIgnoreCase("Timer")) {
				retString += (this.getTimer().toString() + TestInteractiveCommandAdapter.endofreply);

			} else if (this.properties.containsKey(property)) {
				retString += (property + " = " + this.properties.get(property) + TestInteractiveCommandAdapter.endofreply);

			} else {
				retString += (this.properties.get("error") + TestInteractiveCommandAdapter.endofreply);
			}

		} else {
			/*
			 * Hack to avoid an weird behavior of the Alien Reader tool which
			 * sends commandStrings in small letters, will be avoided in future
			 * of another implementation of the commandString set so it is case
			 * insensitive.
			 */
			if (trimmedCommand.length() > 0) {
				String sub1 = trimmedCommand.substring(0, 1);
				String sub2 = trimmedCommand.substring(1, trimmedCommand
						.length());
				trimmedCommand = sub1.toUpperCase() + sub2;
			}

			/* Check for single things like info, etc. */
			if (this.properties.containsKey(trimmedCommand)) {
				retString += (this.properties.get(trimmedCommand) + TestInteractiveCommandAdapter.endofreply);

			} else if (trimmedCommand.equalsIgnoreCase("quit")
					|| trimmedCommand.equalsIgnoreCase("q")
					|| trimmedCommand.equalsIgnoreCase("exit")) {
				retString += "Goodbye!\r\n";
				/* Force a disconnect */
				this.connectionControlSignal.setControlVariableValue(false);

			} else if (trimmedCommand.equalsIgnoreCase("shutdown")) {
				/* Force a shutdown */
				this.connectionControlSignal.setControlVariableValue(false);
				this.readerPowerControlSignal.setControlVariableValue(false);

			} else if (trimmedCommand.equals("")) {
				retString += (TestInteractiveCommandAdapter.endofreply);

			} else if (trimmedCommand.equalsIgnoreCase("t")) {
				retString += (this.getTagList());

			} else {
				retString += (this.properties.get("error") + TestInteractiveCommandAdapter.endofreply);
			}

		}

		/* Show the prompt unless explicitly given not to. */
		if (showPrompt) {
			retString += (TestInteractiveCommandAdapter.crnl + this.properties
					.get("prompt"));
		}

		//return retString.getBytes();
		return null;
	}

	/**
	 * Returns a constant taglist.
	 * 
	 * @return A gettaglist result.
	 * 
	 */
	private String getTagList() {
		return "Tag:A5A5 0000 0000 6101 5414 6406, "
				+ "Disc:2006/10/27 15:39:10, Last:2006/10/27 "
				+ "15:39:10, Count:1, Ant:3, Proto:1"
				+ TestInteractiveCommandAdapter.endofreply;

	}

	/**
	 * Returns a constant timer.
	 * 
	 * @return A constant timer result.
	 */
	private String getTimer() {
		return "#Timing Results\r\n" + "Total Time (ms) = 1214\r\n"
				+ "Total Commands=1   Commands/Sec=0\r\n"
				+ "Total Unique Tags=20   Unique Tags/Sec=16\r\n"
				+ "Total Tag Reads=21   Reads/Sec=17\r\n";

	}

}
