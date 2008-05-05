/*
 *  CommandObject.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.command;

import java.util.ArrayList;

import org.rifidi.emulator.Globals;
import org.rifidi.emulator.reader.sharedrc.properties.BooleanReaderProperty;
import org.rifidi.emulator.reader.sharedrc.properties.DoubleReaderProperty;
import org.rifidi.emulator.reader.sharedrc.properties.IntegerReaderProperty;
import org.rifidi.emulator.reader.sharedrc.properties.LongReaderProperty;
import org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty;
import org.rifidi.emulator.reader.sharedrc.properties.ShortReaderProperty;
import org.rifidi.emulator.reader.sharedrc.properties.StringReaderProperty;
import org.rifidi.emulator.reader.sharedrc.tagmemory.formatter.TagListFormatReaderProperty;
import org.rifidi.emulator.reader.sharedrc.tagmemory.formatter.TagListFormatValues;

/**
 * This class represents a command and all related information that will be sent
 * to a Reader. <br />
 * All of the arguments and return values are stored in this object as well and
 * sent back and forth between the adapters and handlers. <br />
 * This class corresponds to the command xml and the elements in this class map
 * to the command xml of a reader. Please look at the reference xml for more
 * information on each field. <br />
 * 
 * @author Matthew Dean
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class CommandObject {

	private ReaderProperty readerproperty;

	/**
	 * The arguments classes that the method takes, for instance, <br />
	 * <br />
	 * foo(String, String, int) <br />
	 * <br />
	 * should have the arg types {"String","String","int"}
	 */
	private String argumentType;

	/**
	 * The arguments that the method must have in order to be invoked. <br />
	 * The arguemnts must be in the same order that they occur in the
	 * argumentType[] array. <br />
	 * Each specific argument is its own element in the ArrayList
	 */
	private ArrayList<Object> arguments;

	/**
	 * The current value of the method. Use this to store the current value of
	 * the Object
	 */
	// private String currentValue;
	/**
	 * The default value of the method.
	 */
	private String defaultValue;

	/**
	 * The display name of the command.
	 */
	private String displayName;

	/**
	 * The handler class the command has.
	 */
	private String handlerClass;

	/**
	 * The method the command refers to.
	 */
	private String handlerMethod;

	/**
	 * The package that the class is in.
	 */
	private String handlerPackage;

	/**
	 * The mode that the method is in, either "get", "set", or "default"
	 */
	private String mode;

	/**
	 * The method that was recently called; "setPersistTime"
	 */
	private String currentQueryName;

	/**
	 * The name of the command.
	 */
	private ArrayList<String> name;

	/**
	 * The action that should take place in the properties view of the GUI:
	 * Button, Ignore, or Read_Only.
	 */
	private String actionTag;

	/**
	 * This boolean determines if the prompt will be sent back from the command
	 * for the Alien reader. <br />
	 * <br />
	 * The prompt should be set to <br />
	 * false <br />
	 * if the prompt should be sent back, and it should be set to <br />
	 * true <br />
	 * if the prompt should not be sent back.<br />
	 */
	private boolean promptSuppress;

	/**
	 * The the return value the method returns after being invoked.
	 * 
	 * Each specific return value is its own element in the ArrayList.
	 * 
	 * For example; a getTagList could return each tag as an element in the
	 * ArrayList.
	 */
	private ArrayList<Object> returnValue;

	/**
	 * The commandState that the object is in.
	 */
	private String commandState;

	/**
	 * The category of the CommandObject
	 */
	private String category;

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Default constructor for CommandObject.
	 */
	public CommandObject() {
		name = new ArrayList<String>();
		arguments = new ArrayList<Object>();
		returnValue = new ArrayList<Object>();
	}

	/**
	 * Adds an argument to the ArrayList of arguments.
	 * 
	 * @param arg
	 *            The argument to add
	 */
	public void addArgument(Object arg) {
		this.arguments.add(arg);
	}

	/**
	 * Adds a name to the ArrayList.
	 * 
	 * @param name
	 *            The name to add
	 */
	public void addName(String name) {
		this.name.add(name);
	}

	/**
	 * Finds out if current object is equal to parameter.
	 * 
	 * @param other
	 *            The object to check equality with
	 * @return True if the objects are equal, false if they are not equal
	 */
	public boolean equals(CommandObject other) {
		boolean retVal = false;
		if (this.name.equals(other.getName())
				&& this.commandState.equals(other.getCommandState())
				&& this.argumentType.equals(other.getArgumentType())
				&& this.arguments.equals(other.getArguments())
				&& this.displayName.equals(other.getDisplayName())
				&& this.handlerClass.equals(other.getHandlerClass())
				// && this.currentValue.equals(other.getCurrentValue())
				&& this.defaultValue.equals(other.getDefaultValue())
				&& this.handlerMethod.equals(other.getHandlerMethod())
				&& this.handlerPackage.equals(other.getHandlerPackage())) {
			retVal = true;
		}
		return retVal;
	}

	/**
	 * @return Returns the argumentType.
	 */
	public String getArgumentType() {
		return argumentType;
	}

	/**
	 * @return Returns the value.
	 */
	public ArrayList<Object> getArguments() {
		return arguments;
	}

	/**
	 * @return Returns the currentValue.
	 */
	// public String getCurrentValue() {
	// return currentValue;
	// }
	/**
	 * @return Returns the defaultValue.
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @return Returns the displayName.
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @return Returns the handlerClass.
	 */
	public String getHandlerClass() {
		return handlerClass;
	}

	/**
	 * @return Returns the handlerMethod.
	 */
	public String getHandlerMethod() {
		return handlerMethod;
	}

	/**
	 * @return Returns the packageName.
	 */
	public String getHandlerPackage() {
		return handlerPackage;
	}

	/**
	 * @return Returns the mode.
	 */
	public String getMode() {
		return mode;
	}

	/**
	 * @return Returns the name.
	 */
	public ArrayList<String> getName() {
		return name;
	}

	/**
	 * @return Returns the prompt.
	 */
	public boolean getPromptSuppress() {
		return promptSuppress;
	}

	/**
	 * @return Returns the returnValue.
	 */
	public ArrayList<Object> getReturnValue() {
		return returnValue;
	}

	/**
	 * @return Returns the commandState.
	 */
	public String getCommandState() {
		return commandState;
	}

	/**
	 * @param argumentType
	 *            The argumentType to set.
	 */
	public void setArgumentType(String argTypes) {
		if (argTypes.equalsIgnoreCase(Globals.INTEGER_VALUE)) {
			this.readerproperty = new IntegerReaderProperty(0);
		} else if (argTypes.equalsIgnoreCase(Globals.DOUBLE_VALUE)) {
			this.readerproperty = new DoubleReaderProperty(0.0d);
		} else if (argTypes.equalsIgnoreCase(Globals.FORMAT_PROPERTY)) {
			this.readerproperty = new TagListFormatReaderProperty(
					TagListFormatValues.TEXT);
		} else if (argTypes.equalsIgnoreCase(Globals.BOOLEAN_VALUE)) {
			this.readerproperty = new BooleanReaderProperty(false);
		}else if (argTypes.equalsIgnoreCase(Globals.LONG_VALUE)) {
			this.readerproperty = new LongReaderProperty(0l);
		}else if (argTypes.equalsIgnoreCase(Globals.SHORT_VALUE)) {
			this.readerproperty = new ShortReaderProperty((short)0);
		} else {
			this.readerproperty = new StringReaderProperty("");
		}
		this.argumentType = argTypes;
	}

	/**
	 * @param value
	 *            The value to set.
	 */
	public void setArguments(ArrayList<Object> value) {
		this.arguments = value;
	}

	/**
	 * @param displayName
	 *            The displayName to set.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @param handlerClass
	 *            The handlerClass to set.
	 */
	public void setHandlerClass(String handlerClass) {
		this.handlerClass = handlerClass;
	}

	/**
	 * @param handlerMethod
	 *            The handlerMethod to set.
	 */
	public void setHandlerMethod(String handlerMethod) {
		this.handlerMethod = handlerMethod;
	}

	/**
	 * @param packageName
	 *            The packageName to set.
	 */
	public void setHandlerPackage(String handlerPackage) {
		this.handlerPackage = handlerPackage;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(ArrayList<String> name) {
		this.name = name;
	}

	/**
	 * @param prompt
	 *            The prompt to set.
	 */
	public void setPromptSuppress(boolean prompt) {
		this.promptSuppress = prompt;
	}

	/**
	 * @param returnValue
	 *            The returnValue to set.
	 */
	public void setReturnValue(ArrayList<Object> returnValue) {
		this.returnValue = returnValue;
	}

	/**
	 * TODO: Think of a better way to handler errors here, as opposed to the
	 * current way, which is to "not handle" the error.
	 * 
	 * @param commandState
	 *            The commandState to set.
	 */
	public void setCommandState(String state) {
		this.commandState = state;
	}

	/**
	 * Converts this CommandObject to a string. Debug purposes only.
	 * 
	 * @return string representation of this object
	 */
	@Override
	public String toString() {
		String retVal = "";
		for (int i = 0; i < name.size(); i++) {
			retVal += name.get(i);
			retVal += "\n";
		}

		retVal += "displayName = ";
		retVal += displayName;

		retVal += "\nhandlerMethod = ";
		retVal += handlerMethod;

		retVal += "\nhandlerClass = ";
		retVal += handlerClass;

		retVal += "\nhandlerPackage = ";
		retVal += handlerPackage;

		retVal += "\ndefaultValue = ";
		retVal += defaultValue;

		return retVal;
	}

	/**
	 * Reset the CommandObject after the method has been invoked. This clears
	 * the arguements array.
	 */
	public void reset() {
		arguments.clear();
		returnValue.clear();
		currentQueryName = "";
	}

	/**
	 * @param defaultValue
	 *            The defaultValue to set.
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
		this.readerproperty.setPropertyDefaultValue(defaultValue);
		this.readerproperty.setPropertyValue(defaultValue);
	}

	/**
	 * @return Returns the currentQueryName.
	 */
	public String getCurrentQueryName() {
		return currentQueryName;
	}

	/**
	 * @param currentQueryName
	 *            The currentQueryName to set.
	 */
	public void setCurrentQueryName(String currentQueryName) {
		this.currentQueryName = currentQueryName;
	}

	/**
	 * @return the actionTag
	 */
	public String getActionTag() {
		return actionTag;
	}

	/**
	 * @param actionTag
	 *            the actionTag to set
	 */
	public void setActionTag(String actionTag) {
		this.actionTag = actionTag;
	}

	/**
	 * @return the readerproperty
	 */
	public ReaderProperty getReaderProperty() {
		return readerproperty;
	}
}
