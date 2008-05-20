/*
 *  TagManager.java
 *
 *  Created:	Mar 14, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.io.comm.manager;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

/**
 * 
 * A class that manages incoming XmlRcp-requests by starting a Webserver
 * and binding the required methods to XmlRcp.
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class TagManager {
	WebServer server;
	public TagManager(InetSocketAddress address){
		//start a webserver
		server=new WebServer(address.getPort(),address.getAddress());
		XmlRpcServer xmlRpcServer=server.getXmlRpcServer();
		PropertyHandlerMapping phm = new PropertyHandlerMapping();
		try{
			//register the handlerclass
			phm.addHandler("Taggy",Taggy.class);
			xmlRpcServer.setHandlerMapping(phm);
			XmlRpcServerConfigImpl serverConfig =
				(XmlRpcServerConfigImpl) xmlRpcServer.getConfig();	
			serverConfig.setEnabledForExtensions(true);
			serverConfig.setContentLengthOptional(false);
			server.start();
		}
		catch(XmlRpcException xre){
			//TODO Find something to do here
			xre.printStackTrace();
		}
		catch(IOException ioe){
			//TODO find something to do here	
			ioe.printStackTrace();
		}
	}
	
	public void stop(){
		server.shutdown();
	}
}
