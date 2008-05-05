/*
 *  _C1G2LockPayload.java
 *
 *  Created:	Oct 10, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 *	Author: 	kyle
 */
package org.rifidi.emulator.reader.llrp.airprotocol.c1g2;

/**
 * @author kyle
 *
 */
public class _C1G2LockPayload {
	
	public static final int PRIVILEGE_READ_WRITE = 0;
	
	public static final int PRIVILEGE_PERMALOCK = 1;
	
	public static final int PRIVILEGE_PERMAUNLOCK = 2;
	
	public static final int PRIVILEGE_UNLOCK = 3;
	
	public static final int DATA_KILL_PASSWORD = 0;
	
	public static final int DATA_ACCESS_PASSWORD = 1;
	
	public static final int DATA_EPC_MEMORY = 2;
	
	public static final int DATA_TID_MEMOR = 3;
	
	public static final int DATA_USER_MEMOR = 4;
	
	private short OpSpecID;
	
	private int privilege;
	
	private int data;
	
	public _C1G2LockPayload(short OpSpecID, int privilege, int data){
		this.OpSpecID = OpSpecID;
		this.privilege = privilege;
		this.data = data;
		
	}
	
	public static boolean isValidPrivilegeAndData(int privilege, int data){
		return (privilege>=0 && privilege <=3) && (data>=0 && data<=4);
	}

	public int getData() {
		return data;
	}

	public int getPrivilege() {
		return privilege;
	}
	
	

}
