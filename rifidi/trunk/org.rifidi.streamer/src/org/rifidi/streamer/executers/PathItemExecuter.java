package org.rifidi.streamer.executers;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.rmi.server.ReaderModuleManagerInterface;
import org.rifidi.emulator.tags.impl.RifidiTag;
import org.rifidi.emulator.tags.utils.RifidiTagFactory;
import org.rifidi.streamer.exceptions.NotInitializedException;
import org.rifidi.streamer.registry.InputObjectRegistry;
import org.rifidi.streamer.xml.actions.Action;
import org.rifidi.streamer.xml.actions.GPIAction;
import org.rifidi.streamer.xml.actions.TagAction;
import org.rifidi.streamer.xml.actions.WaitAction;
import org.rifidi.streamer.xml.batch.Batch;
import org.rifidi.streamer.xml.scenario.PathItem;

/**
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class PathItemExecuter implements Runnable {

	private Log logger = LogFactory.getLog(PathItemExecuter.class);

	private LinkedBlockingQueue<Batch> batchQueue = new LinkedBlockingQueue<Batch>();
	private Thread thread;
	private boolean running = false;
	private boolean initialized = false;

	private ReaderModuleManagerInterface reader;
	private int ID;
	private PathItem pathItem;
	private ScenarioExecuter scenario;
	private InputObjectRegistry registry;

	private PathItemExecuter nextPathItem;

	private String pathItemDescription;

	public PathItemExecuter(int ID, PathItem pathItem,
			InputObjectRegistry registry, ScenarioExecuter scenario) {
		this.ID = ID;
		this.pathItem = pathItem;
		this.scenario = scenario;
		this.registry = registry;
		// get corresponding reader
		reader = registry.registerReader(pathItem.getReaderID());

		pathItemDescription = "Scenario " + scenario.getID() + " - PathItem "
				+ ID;
	}

	public void start() throws NotInitializedException {
		if (reader != null) {
			running = true;
			thread = new Thread(this, "Scenario " + scenario.getID()
					+ " - PathItem " + ID);
			thread.start();
		} else {
			String error = "PathItem " + ID + " couldn't register Reader "
					+ pathItem.getReaderID();
			logger.error(error);
			throw new NotInitializedException(error);
		}
	}

	public void stop() {
		running = false;
		if (thread != null)
			if (thread.isAlive())
				thread.interrupt();
	}

	private void dispose() {
		running = false;
		logger.debug("-- Stopped PathItem " + ID);
		// free resources
		registry.unRegisterReader(pathItem.getReaderID());
		// TODO notify Scenario Executer
		scenario.pathItemEnded(this);
	}

	@Override
	public void run() {
		try {
			while (running) {
				Batch batch = batchQueue.take();
				logger.debug("Processing Batch " + batch.getID());
				processBatch(batch);
				logger.debug("Batch " + batch.getID()
						+ " is moving to the next PathItem ("
						+ pathItem.getTravelTime() + "ms)");
				Thread.sleep(pathItem.getTravelTime());
				toNextPathItem(batch);
			}
		} catch (InterruptedException e) {
			// This happens on purpose
		} catch (Exception e) {
			// Most likely RMI Exceptions
			e.printStackTrace();
		} finally {
			dispose();
		}
	}

	private void processBatch(Batch batch) throws Exception {
		for (Action action : batch.getActions()) {
			if (action instanceof WaitAction) {
				waitAction((WaitAction) action);
			}
			if (action instanceof TagAction) {
				tagAction((TagAction) action);
			}
			if (action instanceof GPIAction) {
				gpiAction((GPIAction) action);
			}
		}
	}

	private void gpiAction(GPIAction gpiAction) throws Exception {
		logger.info(pathItemDescription + " - executing GPIAction");
		if (gpiAction.isSignal()) {
			reader.setGPIHigh(gpiAction.getPort());
		} else
			reader.setGPILow(gpiAction.getPort());

	}

	private void tagAction(TagAction tagAction) throws Exception {
		logger.info(pathItemDescription + " - executing TagAction");
		ArrayList<RifidiTag> tags = null;
		if (tagAction.isRegenerate()) {
			tags = new ArrayList<RifidiTag>();
			for (int i = 0; i < tagAction.getNumber(); i++) {
				RifidiTag rifidiTag = RifidiTagFactory.createTag(tagAction
						.getTagGen(), tagAction.getTagType(), null, tagAction
						.getPrefix());
				tags.add(rifidiTag);
			}
		} else {
			tags = new ArrayList<RifidiTag>();
			logger.debug("This feature is not yet implemented");
		}
		int antennaNum = pathItem.getAntennaNum();
		reader.addTags(antennaNum, tags);
		Thread.sleep(tagAction.getExecDuration());
		ArrayList<byte[]> tagsToRemove = new ArrayList<byte[]>();
		for (RifidiTag tag : tags) {
			tagsToRemove.add(tag.toByte());
		}
		reader.removeTags(antennaNum, tagsToRemove);
	}

	private void waitAction(WaitAction waitAction) throws InterruptedException {
		logger.info(pathItemDescription + " - executing WaitAction");
		long waitTime = waitAction.getWaitTime();
		logger.debug("WaitAction: sleep for " + waitTime + "ms");
		Thread.sleep(waitTime);
	}

	public void addBatch(Batch batch) {
		batchQueue.add(batch);
	}

	public void setNextPathItem(PathItemExecuter nextPathItem) {
		this.nextPathItem = nextPathItem;
	}

	private void toNextPathItem(Batch batch) {
		if (nextPathItem == null) {
			logger.debug("Batch " + batch.getID() + " reached end of Scenario "
					+ scenario.getID() + "(Batches left: " + batchQueue.size()
					+ ")");
			scenario.batchReachedEnd();
		} else {
			// logger.debug("Giving Batch to next PathItem ("
			// + nextPathItem.getID() + ")");
			nextPathItem.addBatch(batch);
		}
	}

	public int getID() {
		return ID;
	}
}
