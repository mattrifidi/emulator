/*
 *  CommandHandlerInvoker.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.commandhandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.command.exception.GenericExceptionHandler;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;

/**
 * The purpose of this class is to use reflection to call a method stored in the
 * argument. <br />
 * The class name and method name is extracted from the CommandObject, and then
 * the method is called via reflection.
 * 
 * 
 * @author Matthew Dean
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class CommandHandlerInvoker {

	/**
	 * Logger for exception handling
	 */
	private static Log logger = LogFactory.getLog(CommandHandlerInvoker.class);

	/**
	 * This method takes in a CommandObject as the parameter and then calls the
	 * handler method stored in it via reflection. <br />
	 * <br />
	 * The handler method has several assumptions: - It takes a CommandObject as
	 * a parameter <br /> - It returns a CommandObject <br />
	 * 
	 * 
	 * If any of the preceding are not met, an exception will be thrown and the
	 * parameter will be returned unmodified.
	 * 
	 * @param newObject
	 *            CommandObject that specifies the method and arguments
	 * @return Returns CommandObject with the operation's result
	 */
	@SuppressWarnings("unchecked")
	public static CommandObject invokeHandler(CommandObject newObject,
			AbstractReaderSharedResources asr, GenericExceptionHandler geh) {
		String className = newObject.getHandlerClass();
		String methodName = newObject.getHandlerMethod();
		Class c = null;
		try {
			c = Class.forName(className);
		} catch (ClassNotFoundException e) {
			logger.error(e.getLocalizedMessage());
		}

		// Force the single parameter to be a CommandObject. If you
		// want different arguments, this class must be rewritten.
		Class[] parameter = new Class[] { CommandObject.class,
				AbstractReaderSharedResources.class };
		Method commandMethod;
		Object[] arguments = new Object[] { newObject, asr };

		try {
			commandMethod = c.getMethod(methodName, parameter);
			// You must invoke reflection on an instance of this object
			Object instance = null;
			instance = c.newInstance();
			// Force the return type to be a CommandObject. ;
			newObject = (CommandObject) commandMethod.invoke(instance,
					arguments);
		} catch (NoSuchMethodException e) {
			logger.error(e.getLocalizedMessage());
			logger.debug("error caused by: " + methodName);
			newObject =  geh.methodInvocationError(new ArrayList<Object>(), newObject);
		} catch (IllegalAccessException e) {
			logger.error(e.getLocalizedMessage());
			logger.debug("error caused by: " + methodName);
			newObject =  geh.methodInvocationError(new ArrayList<Object>(), newObject);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			logger.error(e.getCause());
			logger.debug("error caused by: " + methodName);
			logger.error(e.getTargetException());
			newObject =  geh.methodInvocationError(new ArrayList<Object>(), newObject);
		} catch (InstantiationException e) {
			logger.debug("error caused by: " + methodName);
			logger.error(e.getLocalizedMessage());
			newObject =  geh.methodInvocationError(new ArrayList<Object>(), newObject);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("error caused by: " + methodName);
			logger.error("Something is defined in the XML that is not"
					+ " defined by a handler method: "
					+ newObject.getHandlerMethod());
			newObject =  geh.methodInvocationError(new ArrayList<Object>(), newObject);
		}

		return newObject;
	}
}
