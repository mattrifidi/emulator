package org.rifidi.emulator.reader.thingmagic.automodecontoler;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.io.comm.Communication;
import org.rifidi.emulator.io.comm.CommunicationException;
import org.rifidi.emulator.reader.thingmagic.commandobjects.Command;


public class AutoModeControler implements Runnable{
	private static Log logger = LogFactory
	.getLog(AutoModeControler.class);

	private Communication comm;
	
	private boolean running = false;

	private List<Command> commands;

	private long repeat = 0;

	private long cursorListRepeat = 0;
	
	private Thread thread;

	public AutoModeControler(Communication comm) {
		// TODO Auto-generated constructor stub
		this.comm = comm;
		
		
	}
	
	public void start(List<Command> commands, long repeat){
		this.commands = commands;
		this.repeat = repeat;
		
		running = true;
		
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while(running){
			for (Command command: commands){
				Long oldTime = System.currentTimeMillis();
				
				StringBuffer buff = new StringBuffer();
				List<Object> output = command.execute();
				for (Object o: output){
					buff.append(o + "\n");
				}
				try {
					comm.sendBytes(buff.toString().getBytes());
				} catch (CommunicationException e) {
					// TODO Auto-generated catch block
					logger.debug(e);
					// no point in going on if communication fails.
					running = false;
				}
				
				long delay = repeat - (System.currentTimeMillis() - oldTime);
				
				if (delay > 0) {
					try {
						wait(delay);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
				}
			}
			
			if (cursorListRepeat > 0) {
				try {
					wait(cursorListRepeat);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			}
			
		}
		
	}

	
	public void setCursorListRepeat(long cursorListRepeat){
		this.cursorListRepeat = cursorListRepeat;
	}
	public void stop(){
		running = false;
	}
	
}
