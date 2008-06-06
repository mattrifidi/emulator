package org.rifidi.edgeserver;

public abstract class AbstractSessionCreationPattern {
	public String getIPAddress(){
		return null;
	}
	
	public void setIPAddress(String IPAddress){
		
	}
	
	public int getPort(){
		return 0;
	}
	
	public void setPort(int port){
		
	}
	
	public abstract Class<?> getReaderAdapterType();
}
