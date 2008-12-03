/*
 *  EventsServiceImpl.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.services.core.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.rifidi.designer.services.core.events.WorldEvent.Colors;
import org.rifidi.services.registry.ServiceRegistry;

/**
 * Base implementation of the events service.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 27, 2008
 * 
 */
public class EventsServiceImpl implements EventsService {
	/** Logger for this class. */
	private static Log logger = LogFactory.getLog(EventsServiceImpl.class);
	/** Stack of events that need to be processed. */
	private Stack<WorldEvent> eventStack;
	/** Thread for processing the events. */
	private ProcessingThread thread;
	/** List of event types to record. */
	private List<Class<?>> eventTypes;
	/** Map containing a timestamp as key and the event as value. */
	private Map<Long, WorldEvent> recordedEvents;
	/** Set if the events should be recorded. */
	private boolean recording = false;
	/** Used for deferred initialization. */
	private AtomicBoolean inited = new AtomicBoolean(false);
	/** Map of console output streams by colors */
	private Map<Colors, MessageConsoleStream> streams;

	/**
	 * Constructor.
	 */
	public EventsServiceImpl() {
		logger.debug("EventsService created");
		ServiceRegistry.getInstance().service(this);
		eventStack = new Stack<WorldEvent>();
		eventTypes = Collections.synchronizedList(new ArrayList<Class<?>>());
		streams = new HashMap<Colors, MessageConsoleStream>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.registry.core.events.EventsService#publish(org.rifidi
	 * .services.registry.core.events.WorldEvent)
	 */
	@Override
	public void publish(WorldEvent worldEvent) {
		// We need a running eclipse which is not available when the service
		// gets instantiated.
		if (inited.compareAndSet(false, true)) {
			Display.getCurrent().asyncExec(new Runnable() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.lang.Runnable#run()
				 */
				@Override
				public void run() {
					MessageConsole fMessageConsole = new MessageConsole(
							"TagMessages", null);
					ConsolePlugin.getDefault().getConsoleManager().addConsoles(
							new IConsole[] { fMessageConsole });
					streams.put(Colors.RED, fMessageConsole.newMessageStream());
					streams.get(Colors.RED).setColor(

					Display.getCurrent().getSystemColor(SWT.COLOR_RED));
					streams.put(Colors.BLACK, fMessageConsole
							.newMessageStream());
					streams.get(Colors.BLACK).setColor(

					Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
					streams.put(Colors.GREEN, fMessageConsole
							.newMessageStream());
					streams.get(Colors.GREEN).setColor(

					Display.getCurrent().getSystemColor(SWT.COLOR_GREEN));
					recordedEvents = new HashMap<Long, WorldEvent>();
					thread = new ProcessingThread("eventProcessingThread");
					thread.start();
				}

			});
		}
		eventStack.push(worldEvent);
		if (eventTypes.contains(worldEvent.getClass())) {
			recordedEvents.put(System.currentTimeMillis(), worldEvent);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.registry.core.events.EventsService#record(org.rifidi
	 * .services.registry.core.events.EventTypes[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void record(Class<?>... eventType) {
		recordedEvents.clear();
		eventTypes.clear();
		for (Class<?> type : eventType) {
			eventTypes.add((Class<WorldEvent>) type);
		}
		recording = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.registry.core.events.EventsService#stopRecording()
	 */
	@Override
	public void stopRecording() {
		// eventTypes.clear();
		// recording = false;
		// try {
		// List<Class> classes = new ArrayList<Class>();
		// classes.add(ComponentSuite.class);
		// JAXBContext context = JAXBContext.newInstance(classes
		// .toArray(new Class[0]));
		// Marshaller marsh = context.createMarshaller();
		// ByteArrayOutputStream by = new ByteArrayOutputStream();
		// marsh.marshal(assembleReaders(), by);
		// System.out.println(by.toString());
		// } catch (JAXBException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.registry.core.events.EventsService#clearRecords()
	 */
	@Override
	public void clearRecords() {
		recordedEvents.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.events.EventsService#isRecording()
	 */
	@Override
	public boolean isRecording() {
		return recording;
	}

	private class ProcessingThread extends Thread {

		/**
		 * Constructor.
		 * 
		 * @param name
		 * @param eventStack
		 */
		public ProcessingThread(String name) {
			super(name);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			while (true) {
				while (!eventStack.empty()) {
					WorldEvent worldEvent = eventStack.pop();
					streams.get(worldEvent.getColor()).println(
							worldEvent.toString());
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}

	}
}
