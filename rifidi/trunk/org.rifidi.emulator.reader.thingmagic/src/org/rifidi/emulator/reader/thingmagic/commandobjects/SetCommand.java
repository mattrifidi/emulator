package org.rifidi.emulator.reader.thingmagic.commandobjects;

import java.util.ArrayList;

public class SetCommand implements Command {

	private String command;

	public SetCommand(String command) {
		// TODO Auto-generated constructor stub
		this.command = command;
	}

	@Override
	public ArrayList<Object> execute() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toCommandString() {
		// TODO Auto-generated method stub
		return command;
	}

}
