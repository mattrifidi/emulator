package org.rifidi.emulator.reader.llrp.admin;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Set;

import org.rifidi.emulator.reader.llrp.module.LLRPReaderModule;

/**
 * @author Andreas Huebner - andreas@pramari.com
 *
 */
public class LLRPReaderModuleAdministrationCommands {
	
	// LLRP ReaderModule for executing commands issued at the Console
	private LLRPReaderModule  reader = null;
	private LLRPReaderModuleAdministrationConsole console = null;
	private LLRPReaderModuleConnectionType connectionMode = null;
	
	//For OS independencies 
	String NEW_LINE = System.getProperty("line.separator");
	
	public LLRPReaderModuleAdministrationCommands(LLRPReaderModule reader, LLRPReaderModuleAdministrationConsole console)
	{
		this.reader = reader;
		this.console = console;
	}
	
	private InetAddress convertToAddr(String hostname)
	{
		InetAddress addr = null;
		try {
			addr = Inet4Address.getByName(hostname);
		} catch (UnknownHostException e) {
			// TODO Think about making a better way how we could tell there is a failure
			//e.printStackTrace();
			//logger.debug("Couldn't convert to a IP");
		}
		return addr;
	}
	
	private int convertToPort(String port)
	{
		int intPort = -1;
		if(! port.matches("\\d++"))
			return -1;
		intPort = Integer.valueOf(port);
		if ( intPort < 1 || intPort > 65535)
			return -1;
		return intPort;
	}
	
	public String cmd_mode(String mode, String port){
		if(mode.equalsIgnoreCase("server"))
		{
			
			int _port = convertToPort(port);
			InetAddress _addr = convertToAddr("127.0.0.1");
			if(_port == -1)
			{
				return "Wrong port. Please check this.";
			}
			if(_addr == null)
			{
				return "Wrong hostname. Please check this.";
			}
			
			connectionMode = new LLRPReaderModuleConnectionType(true,_addr,_port);
			reader.setConnection(connectionMode);
			return "Server started on port: " + port + ".";
		}
		return "Error while parsing mode command.";
	}
	
	public String cmd_mode(String mode, String hostname, String port){
		if(mode.equalsIgnoreCase("client"))
		{
			int _port = convertToPort(port);
			InetAddress _addr = convertToAddr(hostname);
			
			if(_port == -1)
			{
				return "Wrong port. Please check this.";
			}
			
			if(_addr == null)
			{
				return "Wrong hostname. Please check this.";
			}
							
			connectionMode = new LLRPReaderModuleConnectionType(false,_addr,_port);
			boolean connectionEstablished = reader.setConnection(connectionMode);
			if(connectionEstablished)
				return "Connect to Client: " + hostname + " " + port + ".";
			else
				return "Connection could not be established. Client: "+ hostname + ":" + port + ".";
		}
		if(mode.equalsIgnoreCase("server"))
		{
			int _port = convertToPort(port);
			InetAddress _addr = convertToAddr(hostname);
			if(_port == -1)
			{
				return "Wrong port. Please check this.";
			}
			if(_addr == null)
			{
				return "Wrong hostname. Please check this.";
			}
			
			connectionMode = new LLRPReaderModuleConnectionType(true,_addr,_port);
			reader.setConnection(connectionMode);
			return "Server started on  "+ hostname +":"+ port + ".";

		}
		return "Error while parsing mode command.";
	}
	
	public String cmd_help(){
		StringBuffer buffer = new StringBuffer();
		Set<String> cmds = console.cmdList.keySet();
		buffer.append("Available Commands are:" + NEW_LINE);
		for(String cmd : cmds)
		{
			//TODO find a better way to print it to the Client
			buffer.append("* " + cmd + NEW_LINE);
		}
		buffer.append("For more help type : help [command]");
		return buffer.toString();
	}
	
	public String cmd_help(String command){

		HashMap<String, String> help = new HashMap<String,String>();
	
		help.put("mode", "Set the mode how you want to connect " + NEW_LINE 
				+ " * mode server [port]" + NEW_LINE 
				+ " * mode client [ip] [port]");
		help.put("quit", "Quit the Administration Interface.");
		help.put("help", "This help.");
		
		String msg = help.get(command.toLowerCase());
		if(msg == null)
			return "No help available on this command";
		return msg;
	}
	
	public String cmd_quit(){
		//TODO Think if this is the best way to close the connection
		console.closeCurConnection();
		return null;
	}
}
