/*
 *  AutoModeControler.java
 *
 *  Created:	September 14, 2008
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.thingmagic.automodecontoler;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.io.comm.Communication;
import org.rifidi.emulator.io.comm.CommunicationException;
import org.rifidi.emulator.reader.thingmagic.commandobjects.Command;

/**
 * @author Jerry Maine - jerry@pramari.com
 *
 */
/*
 * Here is an example of how to make a custom AutoMode 
 * (a.k.a. Autonomous Mode) controller. 
 * For the version in the RIFIDI Core look at AutonomousCommandExecuter
 * and AutonomousCommandController
 */
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
			/*
			 * we takes the commands and execute them
			 * one by one and when we run out of commands
			 * we repeat or terminate.
			 */
			for (Command command: commands){
				//TODO: should we test if we should be running here?
				
				Long oldTime = System.currentTimeMillis();
				
				StringBuffer buff = new StringBuffer();
				
				List<Object> output = command.execute();
				
				/*
				 * every object we get back in the 
				 * list is one and only one line of text,
				 * including but not limited to, empty strings.
				 */
				
				//TODO should we send each object as a new TCP packet?
				for (Object o: output){
					buff.append(o + "\n");
				}
				
				/*
				 * now we try to convert the contents of the
				 * buffer into bytes and send them through the socket.
				 */
				try {
					comm.sendBytes(buff.toString().getBytes());
				} catch (CommunicationException e) {
					// TODO Auto-generated catch block
					logger.debug(e);
					// no point in going on if communication fails.
					running = false;
				}
				
				long delay = repeat - (System.currentTimeMillis() - oldTime);
				
				/*
				 * There is no logical use for waiting zero or negative seconds
				 * here. And besides, waiting "zero seconds" means something special
				 * in Java and we are not trying to do that. 
				 * (If you wan to know what that is look up Object.wait() in the 
				 * Official Java API documentation.)
				 */
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
