/*
 * @(#)IPCreator.java
 *
 * Created:     August 18, 2006, 11:08 AM
 * Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 * Copyright:	Pramari LLC and the Rifidi Project
 * License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.utilities.device;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * This class provides the possibility to create virtual IP addresses.
 * 
 * @author Mike Graupner - mike@pramari.com
 */
public class IPCreator {

	/** Holdes the instance of the Runtime class */
	private Runtime runtimeenv;

	/** Initial version of the configuration */
	private Vector<String> initialConfig = new Vector<String>();

	/** Current version of the configuration */
	// private Vector<String> currentConfig = new Vector<String>();
	/** The tool used to create the IP's */
	private static String IPTOOL = "netsh";

	/*
	 * The following command options have to be splitted into three parts,
	 * because the exec command wants it that way, otherwise it is not working
	 */

	/** Command to get the interfaces and IP addresses, first part */
	private static String commandGetAddress1 = "interface";

	/** Command to get the interfaces and IP addresses, second part */
	private static String commandGetAddress2 = "ip";

	/** Command to get the interfaces and IP addresses, third part */
	private static String commandGetAddress3 = "dump";

	/** The name of the used config file for the ip creation */
	private static String CONFIGFILENAME = "config.txt";

	/**
	 * The singleton instance of the IPCreator.
	 */
	private static final IPCreator SINGLETON_INSTANCE = new IPCreator();

	/**
	 * Gets the singleton instance of IPCreator.
	 * 
	 * @return The singleton instance of IPCreator.
	 */
	public static IPCreator getInstance() {
		return SINGLETON_INSTANCE;
	}

	/** Creates a new instance of IPCreator */
	private IPCreator() {
		/* get instance */
		this.runtimeenv = Runtime.getRuntime();

		this.initialConfig = this.createSnapshot();
		// this.currentConfig = this.initialConfig;
	}

	/**
	 * This method takes an IP as a Parameter and creates this as a virtual IP.
	 * Works just under windows and is a very insecure operation which should
	 * only be called by experienced users of RIFIDI which have a good knowledge
	 * of network operations.
	 * 
	 * @param nic
	 *            The network card the IP will be created for
	 * @param ipaddr
	 *            The virtual IP address
	 * @param netmask
	 *            The netmask of the IP
	 * @return Error code of the method
	 */
	public int createIP(String nic, String ipaddr, String netmask) {
		/* Temp variable which holds the current line */
		String line;
		/* Name of the interface on which the virtual IP should be created on */
		String interfaceName = null;
		/* String that holds the information for the new IP address */
		String ipcreationCMD = null;
		/* The current configuration */
		Vector<String> currentConfig = this.createSnapshot();
		/* Iterator for the connfiguration list */
		Iterator<String> iter = currentConfig.iterator();
		/* IP addresses and NetMask for the check */
		InetAddress virtualIP = null;
		InetAddress virtualMask = null;
		/* Error Code */
		int errorCode = -1;
		/* Position where the new line should be inserted */
		int posNewAddress = 0;
		/* Found the Interface in the List? */
		boolean foundDescription = false;

		/* Get the corresponding logical name for the hardware nic */
		Map<String, String> names = this.getLogicalIterfaceNames();
		Set<String> nameSet = names.keySet();
		Iterator<String> nameIter = nameSet.iterator();

		while (nameIter.hasNext()) {
			String hardwareName = nameIter.next();
			if (nic.contains(hardwareName)) {
				interfaceName = names.get(hardwareName);
			}
		}

		/* Check if IP is in the right format */
		try {
			virtualIP = InetAddress.getByName(ipaddr);
		} catch (UnknownHostException ex) {
			ex.printStackTrace();
			// logger.log("Virtual IP address is in the wrong format");
		}
		/* Check if the Netmask is in the right format */
		try {
			virtualMask = InetAddress.getByName(netmask);
		} catch (UnknownHostException ex) {
			ex.printStackTrace();
			// logger.log("Virtual Net mask is in the wrong format");
		}

		/*
		 * Iterate through the list of the Interface/IP description and if found
		 * create the line which will be added to the config file
		 */
		while (iter.hasNext()) {
			line = iter.next();
			if (line.contains("address") & line.contains(nic)
					& line.contains("source=dhcp")) {
			} else if (line.contains("address") & line.contains(interfaceName)
					& line.contains("source=static")) {
				posNewAddress = currentConfig.indexOf(line) + 1;
				ipcreationCMD = "add address name=\"" + interfaceName
						+ "\" addr=" + virtualIP.getHostAddress() + " mask="
						+ virtualMask.getHostAddress();
				foundDescription = true;
			} else {
			}
		}

		/* If foundDescription is set a new address can be added. */
		if (foundDescription) {
			/* Insert the new line to the Vector which holds the configuration */
			currentConfig.insertElementAt(ipcreationCMD, posNewAddress);
			/* Call the creation method with the configuration as parameter */
			errorCode = this.writeConfigAndExecute(currentConfig);
		}

		return errorCode;
	}

	/**
	 * This method deletes a virtual IP address from a given network interface.
	 * TDO: Add Code
	 * 
	 * @param nic
	 *            The network card
	 * @param ipaddr
	 *            The IP address
	 * @return Returns an error code either the operation was successful or not
	 */
	public int removeIP(String nic, String ipaddr) {
		return -1;
	}

	/**
	 * Resets the network configuration to the state it was before adding any
	 * virtual IP's.
	 * 
	 * @return Returns an error code either the operation was successful or not
	 */
	public int resetConfiguration() {
		int errorCode;
		errorCode = this.writeConfigAndExecute(this.initialConfig);
		return errorCode;
	}

	/**
	 * This method takes a coniguration description, writes it to a file and
	 * then calles the netsh utility
	 * 
	 * @param configuration
	 *            The configuration which should be set up.
	 * @return Error code
	 */
	private int writeConfigAndExecute(Vector<String> configuration) {
		/* Error code */
		int errorCode = -1;
		/* File object */
		File configFile = new File(CONFIGFILENAME);
		FileWriter fileHandle = null;

		try {
			fileHandle = new FileWriter(configFile);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		Iterator<String> writeIter = configuration.iterator();
		/*
		 * Write the Vector with the configuration to disk and call the the
		 * netsh command
		 */
		if (fileHandle != null) {
			while (writeIter.hasNext()) {
				try {
					fileHandle.write(writeIter.next()
							+ System.getProperty("line.separator"));
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

			try {
				fileHandle.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}

			try {
				String[] cmdString = { "netsh", "-f",
						configFile.getAbsolutePath() };
				Process proc = this.runtimeenv.exec(cmdString);
				try {
					proc.waitFor();
					String line;
					InputStream stdin = proc.getInputStream();
					InputStreamReader stdinreader = new InputStreamReader(stdin);
					BufferedReader br = new BufferedReader(stdinreader);

					while ((line = br.readLine()) != null) {
						line.charAt(0);
					}
					
					br.close();

				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}

				/*
				 * If this operation does not fail the operation should be
				 * successful
				 */
				errorCode = 0;
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return errorCode;
	}

	/**
	 * This method creates a HashMap with all the available network interfaces
	 * and the IP's which are associated to them. In order to get this
	 * information it calls the createSnapshot() method and parses the
	 * information.
	 * 
	 * @return HashMap which holds interface name and IP's
	 */
	@SuppressWarnings("unused")
	private Map<String, String> getInterfaceList() {
		/* The Hashmap which contains the interfaces and the ip address */
		Map<String, String> interfaceMap = new HashMap<String, String>();
		/* Get the current config */
		Vector<String> config = this.createSnapshot();
		/* Create Iterator */
		Iterator<String> iter = config.iterator();
		/* Temp strings */
		String line;
		String[] splitLine;
		/* String for Interface name and address */
		String interfaceName = null;
		String interfaceAddr = null;

		while (iter.hasNext()) {
			line = iter.next();
			/*
			 * See if the is aline which says address name and source OR add
			 * address name to get Interface Name and OP
			 */
			if ((line.contains("address name") & line.contains("source"))
					| (line.contains("add address name"))) {
				/* Split the string after " to get the interface name */
				splitLine = line.split("\"");
				interfaceName = splitLine[1];
				/* Split the string after "space" to get the address */
				splitLine = line.split(" ");
				for (int i = 0; i < splitLine.length; i++) {
					if (splitLine[i].contains("dhcp")) {
						interfaceAddr = "dhcp";
					} else if (splitLine[i].contains("addr")
							& !splitLine[i].contains("address")) {
						String[] splitAddr = splitLine[i].split("=");
						interfaceAddr = splitAddr[1];
					}
				}
				/*
				 * If the network interface already exists, add another IP to
				 * the list
				 */
				if (interfaceMap.containsKey(interfaceName)) {
					interfaceAddr = interfaceMap.get(interfaceName) + ";"
							+ interfaceAddr;
				}
				interfaceMap.put(interfaceName, interfaceAddr);
			}
		}
		return interfaceMap;
	}

	/**
	 * This method finds out what is the corresponding logical name of a
	 * hardware ethernet card. This is needed because the Java method
	 * NetworkInterface. getDisplayName() just delivers the hardware name, but
	 * for the creation the logical Name is needed. To get the information it
	 * calls ipconfig /all and parses the screen output. This is a very insecure
	 * operation and probably fails on other windows systems with other
	 * languages, but for now it should do the trick. The result is delivered in
	 * a HashMap which holds the Hardware name as a key and the logical name as
	 * the entry.
	 * 
	 * @return HashMap with the information about the logical and hardware name
	 */
	private Map<String, String> getLogicalIterfaceNames() {
		/* Command which is called */
		String cmd = "ipconfig /all";
		/* Vector where the current output of the command is saved */
		Vector<String> config = new Vector<String>();
		/* Temp variable */
		String line;
		/* Logical name of the Ethernet card */
		String logicName = null;
		/* Hardware Name of the Ethernet card */
		String hardwareName = null;
		/*
		 * HashMap which contains the logical name and the hardware name of the
		 * network card
		 */
		Map<String, String> logicHardwareMap = new HashMap<String, String>();

		/* call the exec() method and return the result into a vector */
		try {
			Process proc = this.runtimeenv.exec(cmd);
			InputStream stdin = proc.getInputStream();
			InputStreamReader stdinreader = new InputStreamReader(stdin);
			BufferedReader br = new BufferedReader(stdinreader);

			while ((line = br.readLine()) != null) {
				config.add(line);
			}
			
			br.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		/* Iterator for the vector with the information about the names */
		Iterator<String> iter = config.iterator();

		/* Parse process which delivers the needed information */
		while (iter.hasNext()) {
			boolean finishParse = false;
			line = iter.next();

			if (line.contains("Ethernet adapter")) {
				line = line.replace("Ethernet adapter", "").trim();
				line = line.replace(":", "");
				logicName = line;

				while (!finishParse) {
					line = iter.next();
					if (line.contains("Description")) {
						int index = line.lastIndexOf(":");
						hardwareName = line.substring(index + 1).trim();
						finishParse = true;
					}
				}
				/* Put the found hardware paires into the HashMap */
				logicHardwareMap.put(hardwareName, logicName);
			}
		}
		return logicHardwareMap;
	}

	/**
	 * This method execute's the command netsh and the parameters the command
	 * should be called with. It returns an error code how the called command
	 * finished.
	 * 
	 * @param cmdarray
	 *            The command which will be executed
	 * @deprecated
	 */
	@SuppressWarnings("unused")
	private int runCommand(String[] cmdarray) {
		return 0;
	}

	/**
	 * This function takes a Snapshot of the current IP configuration and stores
	 * it in the vector .
	 * 
	 * @return Vector which contains all the information a "netsh interface ip
	 *         dump" gives you
	 */
	private Vector<String> createSnapshot() {
		Vector<String> configSnap = new Vector<String>();

		/* This is the command which is called, stored in an array */
		String[] cmdArray = { IPTOOL, commandGetAddress1, commandGetAddress2,
				commandGetAddress3 };

		/* Readout method for the configuration */
		try {
			String line;
			Process proc = this.runtimeenv.exec(cmdArray);
			InputStream stdin = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(stdin);
			BufferedReader br = new BufferedReader(isr);

			while ((line = br.readLine()) != null) {
				/*
				 * Leave out lines which contain PRIMARY, because they cause
				 * trouble
				 */
				if (!line.contains("PRIMARY")) {
					configSnap.add(line);
				}
			}
			
			br.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return configSnap;
	}
}
