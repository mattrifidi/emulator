package org.rifidi.emulator.reader.llrp.admin;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.llrp.module.LLRPReaderModule;

/**
 * This is the LLRP Reader Administration Interface. 
 * 
 * @author Andreas Huebner - andreas@pramari.com
 */
public class LLRPReaderModuleAdministration {

	private LLRPReaderModule reader = null;
	
	private LLRPReaderModuleAdministrationConsole console;
	private Thread consoleThread = null;

	//	 default port on which AdminConsole is listening
	private int defaultPort = 22000;
	private String defaultAddress = "127.0.0.1";
	
	private boolean preEnabledServermode = false;
	private LLRPReaderModuleConnectionType connectionType = null;
	
	private static Log logger = LogFactory.getLog(LLRPReaderModule.class);

	
	
	public LLRPReaderModuleAdministration(LLRPReaderModule module) 
	{
		// Keep a reference to the calling LLRP Reader
		this.reader = module;
	}

	public LLRPReaderModuleAdministration(LLRPReaderModule module, int port) 
	{
		// Keep a reference to the calling LLRP Reader
		this.reader = module;
		this.defaultPort = port;
	}
	
	public LLRPReaderModuleAdministration(LLRPReaderModule module, String localAddress, int port) 
	{
		// Keep a reference to the calling LLRP Reader
		this.reader = module;
		this.defaultAddress = localAddress;
		this.defaultPort = port;
	}

	
	public void start()
	{
		if(preEnabledServermode){
			reader.setConnection(connectionType);
		}
		InetAddress addr = null;
		try {
			addr = Inet4Address.getByName(defaultAddress);
		} catch (UnknownHostException e) {
			logger.info("Couldn't start Administration Interface because of wrong hostname");
			return;
		}
			
		if(defaultPort < 1 || defaultPort > 65535)
		{
			logger.info("Coudn't start Administration Interface because of wrong port");
			return;
		}
		console = new LLRPReaderModuleAdministrationConsole(reader,addr,defaultPort);
		consoleThread = new Thread(console,"AdministrationInterface");
		consoleThread.start();
	}
	
	public void stop()
	{
			if(console.curServerSocket == null)
			{
				logger.debug("Stopping Admin Console: curServerSocket == null");
			}else
			{
				//if(! console.curServerSocket.isClosed())
					//console.curServerSocket.close();
				console.closeServerSocket();
			}
			if(console.currentConnection == null)
			{
				logger.debug("Stopping Admin Console: CurrentConnection == null");
			}else
			{
				//if(! console.currentConnection.isClosed())
					//console.currentConnection.close();
				console.closeCurConnection();
			}
			consoleThread.stop();
	}
	
	public void preEnabledServerMode(LLRPReaderModuleConnectionType connectionType)
	{
		preEnabledServermode = true;
		this.connectionType = connectionType;
	}
}
