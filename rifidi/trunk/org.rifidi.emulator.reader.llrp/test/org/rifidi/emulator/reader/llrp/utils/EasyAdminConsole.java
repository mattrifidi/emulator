package org.rifidi.emulator.reader.llrp.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class EasyAdminConsole {
	
	private String hostname;
	private int port;
	
	private Socket connection = null;
	
	private InputStream in = null;
	private BufferedWriter out = null;
	
	public EasyAdminConsole(String hostname, int port)
	{
		this.hostname = hostname;
		this.port = port;
	}
	
	public boolean connect()
	{
		try {
			connection = new Socket(hostname, port);
		} catch (UnknownHostException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		try {
			in = connection.getInputStream();
			out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
		} catch (IOException e) {
			return false;
		}
		// Get welcome Message
		String welcome_msg = getInput();
		if(welcome_msg == null)
		{
			return false;
		}
		print(welcome_msg);
		return true;
	}
	
	public boolean close()
	{
		try {
			connection.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	private void print(String msg)
	{
		System.out.println(msg);
	}
	
	private String getInput()
	{
		StringBuffer buffer = new StringBuffer();
		try {
			int c = in.read();
			if(c == -1)
				return null;
			buffer.append((char)c);
			while(in.available() > 0)
				{
					if((c = in.read()) == -1)
						return buffer.toString();
					else
					{
						buffer.append((char)c);
					}
				}
		} catch (IOException e) {
			return null;
		}
		//TODO Find a way to find out if the connection has been closed. 
		String ret = buffer.toString();
		return ret;
	}
	
	public boolean modeServer(int port)
	{
		try {
			out.write("mode server " + port);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			return false;
		}
		String response = getInput();
		if(response.startsWith("Server started on port:"))
		{
			return true;
		}
		return false;
	}
	
	public boolean modeClient(String hostname, int port)
	{
		try {
			out.write("mode client " + hostname + " " + port);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			return false;
		}
		String response = getInput();
		if(response.startsWith("Connect to Client:"))
		{
			return true;
		}
		return false;
	}
	
	public boolean quit()
	{
		try {
			out.write("quit");
			out.newLine();
			out.flush();
			if(connection.isClosed())
			{
				return true;
			}
		} catch (IOException e) {
			return false;
		}
		
		return false;
	}
}
