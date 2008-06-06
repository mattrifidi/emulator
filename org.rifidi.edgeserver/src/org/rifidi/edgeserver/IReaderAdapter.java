package org.rifidi.edgeserver;

public interface IReaderAdapter {
	public void connect();
	
	public void disconnect();
	
	public void sendCommand(); 
	
	public void startStreamTags();
	
	public void stopStreamTags();
}
