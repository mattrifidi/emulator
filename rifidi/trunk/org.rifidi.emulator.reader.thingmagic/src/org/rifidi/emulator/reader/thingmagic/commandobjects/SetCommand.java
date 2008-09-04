package org.rifidi.emulator.reader.thingmagic.commandobjects;

import java.util.ArrayList;

import org.rifidi.emulator.reader.thingmagic.module.ThingMagicReaderSharedResources;

public class SetCommand implements Command {

	private String command;
	private ThingMagicReaderSharedResources tmsr;

	public SetCommand(String command, ThingMagicReaderSharedResources tmsr) {
		// TODO Auto-generated constructor stub
		this.command = command;
		this.tmsr = tmsr;
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
