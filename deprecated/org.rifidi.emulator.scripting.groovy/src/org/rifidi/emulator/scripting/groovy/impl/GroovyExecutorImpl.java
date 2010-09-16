/**
 * 
 */
package org.rifidi.emulator.scripting.groovy.impl;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.rifidi.emulator.manager.ReaderManager;
import org.rifidi.emulator.scripting.groovy.GroovyExecutor;

/**
 * Implementation of the groovy executor service.
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class GroovyExecutorImpl implements GroovyExecutor {

	/** Logger for this class. */
	private static final Logger logger = LogManager
			.getLogger(GroovyExecutorImpl.class);
	/** Executor that handles the running shells. */
	private final ScheduledThreadPoolExecutor executor;
	/**
	 * Map that contains all currently running shells by using their id as key
	 * and their future as value.
	 */
	private final Map<Integer, Future<Object>> idToFutureMap;
	/** Thread safe id counter. */
	private final AtomicInteger counter = new AtomicInteger(0);
	/** Reference to the service that manages the reader emulation. */
	private volatile ReaderManager readerManager;

	/**
	 * Constructor.
	 */
	public GroovyExecutorImpl() {
		executor = new ScheduledThreadPoolExecutor(1);
		idToFutureMap = new ConcurrentHashMap<Integer, Future<Object>>();
	}

	/**
	 * @param readerManager
	 *            the readerManager to set
	 */
	public void setReaderManager(ReaderManager readerManager) {
		this.readerManager = readerManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.scripting.groovy.GroovyExecutor#clear()
	 */
	@Override
	public synchronized void clear() {
		executor.shutdownNow();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.scripting.groovy.GroovyExecutor#getCurrentScriptIDs()
	 */
	@Override
	public Set<Integer> getCurrentScriptIDs() {
		return idToFutureMap.keySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.scripting.groovy.GroovyExecutor#killScript(java.lang
	 * .Integer)
	 */
	@Override
	public synchronized void killScript(Integer scriptID) {
		Future<?> future = idToFutureMap.remove(scriptID);
		future.cancel(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.scripting.groovy.GroovyExecutor#submitScript(java
	 * .lang.String)
	 */
	@Override
	public Integer submitScriptAsString(String script) {
		Integer val = counter.incrementAndGet();
		idToFutureMap
				.put(val, executor.submit(new GroovyCallable(script, val)));
		return val;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.scripting.groovy.GroovyExecutor#getResultFromScript
	 * (java.lang.Integer)
	 */
	@Override
	public synchronized String getResultFromScript(Integer scriptID) {
		Future<Object> future = idToFutureMap.get(scriptID);
		if (future.isDone()) {
			idToFutureMap.remove(scriptID);
			try {
				return future.get().toString();
			} catch (InterruptedException e) {
				return e.toString();
			} catch (ExecutionException e) {
				return e.toString();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.scripting.groovy.GroovyExecutor#purge()
	 */
	@Override
	public synchronized Map<Integer, String> purge() {
		Map<Integer, String> ret = new HashMap<Integer, String>();
		for (Integer id : idToFutureMap.keySet()) {
			String res = getResultFromScript(id);
			if (res != null) {
				ret.put(id, res);
			}
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.scripting.groovy.GroovyExecutor#submitScriptAsFile
	 * (java.io.File)
	 */
	@Override
	public Integer submitScriptAsFile(File script) {
		if (script.canRead()) {
			try {
				BufferedReader input = new BufferedReader(
						new FileReader(script));
				String line = input.readLine();
				StringBuilder builder = new StringBuilder();
				while (line != null) {
					builder.append(line);
					line = input.readLine();
				}
				return submitScriptAsString(builder.toString());
			} catch (FileNotFoundException e) {
				logger.error(e);
			} catch (IOException e) {
				logger.error(e);
			}
		}
		return null;
	}

	/**
	 * Callable that runs a groovy shell.
	 * 
	 * @author Jochen mader - jochen@pramari.com
	 * 
	 */
	private class GroovyCallable implements Callable<Object> {

		/**
		 * The script to execute.
		 */
		private String script;
		/**
		 * Id of this callable.
		 */
		private Integer id;

		/**
		 * Constructor.
		 * 
		 * @param script
		 */
		public GroovyCallable(String script, Integer id) {
			this.script = script;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public Object call() throws Exception {
			try {
				Binding binding = new Binding();
				binding.setVariable("readerManager", readerManager);
				GroovyShell shell = new GroovyShell(binding);
				return shell.evaluate(script);
			} catch (Exception e) {
				e.printStackTrace();
				logger.warn(e);
				return e.toString();
			}
		}
	}

}
