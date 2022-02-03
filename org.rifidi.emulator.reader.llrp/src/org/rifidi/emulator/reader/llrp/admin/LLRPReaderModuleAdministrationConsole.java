package org.rifidi.emulator.reader.llrp.admin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.llrp.module.LLRPReaderModule;

class LLRPReaderModuleAdministrationConsole implements Runnable
{
	private LLRPReaderModule reader;
	private static Log logger = LogFactory.getLog(LLRPReaderModule.class);
	
	//	 ConnectionType set by the AdminInterface
	//protected LLRPReaderModuleConnectionType connectionMode = null;
	
	protected boolean adminInterfaceRunning = false;
	protected boolean consoleRunning = false;
	
	private InetAddress addr;
	private int port;
	
	protected ServerSocket curServerSocket = null;
	protected Socket currentConnection = null;
	
	private SocketAddress serverSocketPort = null;
	
	private BufferedReader inStream = null;
	private BufferedWriter outStream = null;
	
	private LLRPReaderModuleAdministrationCommands commands;
	protected HashMap<String, HashMap<Integer ,Method>> cmdList;
	
	public LLRPReaderModuleAdministrationConsole(LLRPReaderModule reader, InetAddress addr, int port)
	{
		this.reader = reader;
		this.addr = addr;
		this.port = port;
		
		// Available Commands for the Console
		commands = new LLRPReaderModuleAdministrationCommands(reader, this);
		
		// Build a Hashmap containing all Methods and number of Arguments 
		// Hashmap< [commandname], Hashmap< [#arguments, method reference]>>
		cmdList = new HashMap<String, HashMap<Integer , Method> >();
		Method[] methods = commands.getClass().getMethods();
		
		for(Method m : methods)
		{
			String cmd_name = m.getName();
			String name = null;
			// Only record methods starting with cmd_ prefix
			if(cmd_name.startsWith("cmd_"))
			{
				name = cmd_name.substring(4);
				logger.debug("Found command : " + name);
				if(cmdList.containsKey(name))
				{
					HashMap<Integer, Method> l = cmdList.get(name);
					l.put(m.getParameterTypes().length,m);
				}
				else
				{
					HashMap<Integer, Method> l = new HashMap<Integer, Method>();
					l.put(m.getParameterTypes().length,m);
					cmdList.put(name, l);
				}
			}
		}
	}

	public void run() {
		
		// initialize the ServerSocket and set adminInterfaceRunning if sucessful 
		adminInterfaceRunning = initialize();

		/*  Waiting for a Client to connect at the AdminInterface and after
		 *  that run the client console (runConsole) */
		while (adminInterfaceRunning) {
			try {
				// Wait for a client to establish a connection
				currentConnection = curServerSocket.accept();
				logger.debug("AdminInterface established a connection with a client.");
				inStream = new BufferedReader(new InputStreamReader(
						currentConnection.getInputStream()));
				outStream = new BufferedWriter(new OutputStreamWriter(
						currentConnection.getOutputStream()));
				
				// Client connected so start console
				runConsole();
				
			} catch (IOException e) {
				// whenever we have a closeServerSocket()
				break;
			}
		}
		// Make sure all Connections are closed
		shutdown();
	}

	private void runConsole(){
		if(currentConnection.isConnected())
		{
			String command = null;
			consoleRunning = true;
			try {
				// Print the welcome Message
				welcomeMessage();
			} catch (IOException e) {
				consoleRunning = false;
				closeCurConnection();
				return;
			}
			
			logger.debug("AdminInterface is listening for Commands.");
			/* whenever a client is connected this loop is processed */
			while (consoleRunning) {
				try {
					writeSocket("cmd > ");
					command = readSocket();
					if (!parse(command))
						writeSocketNL("? Command not implemented");
					logger.debug("Command parsed. Waiting for next Command");			
				} catch (IOException e) {
					closeCurConnection();
					consoleRunning = false;
					return;
				}
			}
			//TODO Outcommented to find errors 
			//closeCurConnection();
			logger.debug("Client closed connection.");
		}
	}

	private boolean initialize() {
		try {
			// Log that the AdminPort is listening on Port to the ConsoleView of the reader
			reader.consoleLogger.info("LLRPReader administration interface running on port: " + port + ".");
			reader.consoleLogger.info("Please set communication mode in administration interface.");
			
			curServerSocket = new ServerSocket();
			serverSocketPort = new InetSocketAddress(addr, port);
			curServerSocket.bind(serverSocketPort,1);
			 
			return true;
			
		} catch (IOException e) {
			// Mostly catched when socket is allready bound
			e.printStackTrace();
			return false;
		}
	}

	private void welcomeMessage() throws IOException {
		writeSocketNL("++++++++++++++++++++++++++++++++++++++++++++");
		writeSocketNL("+   Welcome at Rifidi LLRP Admin Console   +");
		writeSocketNL("++++++++++++++++++++++++++++++++++++++++++++");
	}

	private String readSocket() {
		String line = null;
		try {
			line = inStream.readLine();
		} catch (IOException e) {
			// TODO Find a better way to tell that the Connection was closed
			closeCurConnection();
		}

		if (line == null) {
			// TODO Find a better way to tell that the Connection was closed
			closeCurConnection();
		}
		
		return line;
	}

	private void writeSocket(String msg) throws IOException {
		if(! currentConnection.isClosed())
		{
			outStream.write(msg);
			outStream.flush();
		}
	}

	private void writeSocketNL(String msg) throws IOException {
		if(! currentConnection.isClosed())
		{
			outStream.write(msg);
			outStream.newLine();
			outStream.flush();
		}
	}

	private boolean parse(String command) throws IOException {
		if(command == null)
			return false;
		if (command.length() < 1)
			return false;
		String[] cmds = command.split("\\s++");
		
		int argsLength = cmds.length-1;
		
		HashMap<Integer, Method> methodList = cmdList.get(cmds[0].toLowerCase());			
		if(methodList == null)
		{
			return false;
		}
		
		Method method = methodList.get(argsLength);
		if(method == null)
		{
			return false;
		}
		
		Object[] args = new Object[argsLength];
		for(int i=1; i < cmds.length;i++)
		{
			args[i-1] = cmds[i];
		}
		
		try {
			// Run the Method for the command 
			String ret = (String) method.invoke(commands, args);
			if(ret != null)
			{
				writeSocketNL(ret);
			}
		} catch (IllegalArgumentException e) {
			// happens whenever a user type in a command with wrong arguments
			e.printStackTrace();
			return false;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			closeCurConnection();
		}
		
		return true;
	}
		
	public boolean closeServerSocket() {
		adminInterfaceRunning = false;
		try {
			if(! curServerSocket.isClosed())
				curServerSocket.close();
		} catch (IOException e) {
			//	 TODO Check if ServerSocket allready closed
			return false;
		}
		return true;
	}

	public boolean closeCurConnection() {
		consoleRunning = false;
		try
		{
			if(! currentConnection.isClosed())
				currentConnection.close();
		}catch(IOException e)
		{
			// TODO Find out what can happen here?
			logger.debug("FIND OUT WHAT HAPPEND. " + e.getMessage());
			return false;
		}
		return true;
	}
			
	
	public boolean shutdown()
	{
		if(currentConnection != null)
		{
			if(currentConnection.isConnected())
			{
				try {
					writeSocketNL("> shutdown Administration Interface .....");
				} catch (IOException e) {
					/* Do nothing */
				}
			}
			boolean isCurConnectionClosed = true;
			boolean isCurServerSocketClosed = true;
			
			logger.debug("Administration Console SHUTDOWN");
			adminInterfaceRunning = false;
			if(! currentConnection.isClosed())
				isCurConnectionClosed = closeCurConnection();
			if(! curServerSocket.isClosed())
				isCurServerSocketClosed = closeServerSocket();
			return isCurConnectionClosed && isCurServerSocketClosed;
		}
		return true;
	}
}