package org.rifidi.emulator.reader.llrp.admin;

import java.net.InetAddress;

import org.rifidi.emulator.extra.TCPExtraInformation;

public class LLRPReaderModuleConnectionType extends TCPExtraInformation {
	public boolean isServer = true;

	public LLRPReaderModuleConnectionType(InetAddress address, int port) {
		super(address, port);
	}
	
	public LLRPReaderModuleConnectionType(boolean isServer, InetAddress address, int port) {
		super(address, port);
		this.isServer = isServer;
	}
	
	
}
