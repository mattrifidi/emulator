/**
 * 
 */
package org.rifidi.streamer.registry;

import java.rmi.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.emulator.rmi.server.ReaderModuleManagerInterface;
import org.rifidi.streamer.exceptions.DublicateObjectException;
import org.rifidi.streamer.xml.BatchSuite;
import org.rifidi.streamer.xml.ComponentSuite;
import org.rifidi.streamer.xml.LoadTestSuite;
import org.rifidi.streamer.xml.ScenarioSuite;
import org.rifidi.streamer.xml.TestUnitSuite;
import org.rifidi.streamer.xml.batch.Batch;
import org.rifidi.streamer.xml.components.ReaderComponent;
import org.rifidi.streamer.xml.scenario.Scenario;
import org.rifidi.streamer.xml.testSuite.TestUnit;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.common.registry.ReaderRegistry;
import org.rifidi.ui.common.registry.RegistryChangeListener;

/**
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class InputObjectRegistry {

	private Log logger = LogFactory.getLog(InputObjectRegistry.class);

	private HashMap<Integer, TestUnit> testUnitList = new HashMap<Integer, TestUnit>();
	private HashMap<Integer, Scenario> scenarioList = new HashMap<Integer, Scenario>();
	private HashMap<Integer, ReaderComponent> componentList = new HashMap<Integer, ReaderComponent>();
	private HashMap<Integer, Batch> batchList = new HashMap<Integer, Batch>();

	public ReaderRegistry readerRegistry;
	public boolean isConnected = false;
	public HashMap<Integer, UIReader> readerComponents;
	public HashMap<Integer, Integer> readerUsage;

	// public HashMap<Integer, ScenarioExecuter> scenarioExecuterList;

	private ArrayList<RegistryChangeListener> listeners = new ArrayList<RegistryChangeListener>();

	public InputObjectRegistry() {
		readerComponents = new HashMap<Integer, UIReader>();
		readerUsage = new HashMap<Integer, Integer>();
		readerRegistry = ReaderRegistry.getInstance();

		connectReaderRegsitry("127.0.0.1", 1198);
		// scenarioExecuterList = new HashMap<Integer, ScenarioExecuter>();
	}

	public ScenarioSuite getScenarioSuite() {
		if (scenarioList.isEmpty())
			return null;
		ScenarioSuite scenarioSuite = new ScenarioSuite();
		ArrayList<Scenario> scenarios = new ArrayList<Scenario>(scenarioList
				.values());
		scenarioSuite.setScenarios(scenarios);
		return scenarioSuite;
	}

	public ComponentSuite getComponentSuite() {
		if (componentList.isEmpty())
			return null;
		ComponentSuite componentSuite = new ComponentSuite();
		ArrayList<ReaderComponent> components = new ArrayList<ReaderComponent>(
				componentList.values());
		componentSuite.setReaderComponents(components);
		return componentSuite;
	}

	public BatchSuite getBatchSuite() {
		if (batchList.isEmpty())
			return null;
		BatchSuite batchSuite = new BatchSuite();
		ArrayList<Batch> batches = new ArrayList<Batch>(batchList.values());
		batchSuite.setBatches(batches);
		return batchSuite;
	}

	@Deprecated
	public LoadTestSuite getLoadTestSuite() {
		if (testUnitList.isEmpty())
			return null;
		LoadTestSuite loadTestSuite = new LoadTestSuite();
		ArrayList<TestUnit> testUnits = new ArrayList<TestUnit>(testUnitList
				.values());
		loadTestSuite.setTestUnits(testUnits);
		return loadTestSuite;
	}

	public TestUnitSuite getTestUnitSuite() {
		if (testUnitList.isEmpty())
			return null;
		TestUnitSuite testUnitSuite = new TestUnitSuite();
		ArrayList<TestUnit> testUnits = new ArrayList<TestUnit>(testUnitList
				.values());
		testUnitSuite.setTestUnits(testUnits);
		return testUnitSuite;
	}

	public void registerScenarioSuite(ScenarioSuite scenarioSuite)
			throws DublicateObjectException {
		logger.info("Registering " + scenarioSuite.getScenarios().size()
				+ " Scenarios");
		for (Scenario scenario : scenarioSuite.getScenarios()) {
			// TODO negative ID's shouldn't be allowed
			int scenarioID = scenario.getID();
			if (scenarioList.containsKey(scenarioID)) {
				throw new DublicateObjectException("A scenario with this ID "
						+ scenarioID + " already exists.",
						DublicateObjectException.Type.SCENARIO);
			}
			scenarioList.put(scenarioID, scenario);
			addEvent(scenario);
		}
	}

	public void registerComponentSuite(ComponentSuite componentSuite)
			throws DublicateObjectException {
		logger.info("Registering "
				+ componentSuite.getReaderComponents().size() + " Components");
		for (ReaderComponent readerComponent : componentSuite
				.getReaderComponents()) {
			// TODO negaive ID's shouldn't be allowed
			int readerComponentID = readerComponent.getID();
			if (componentList.containsKey(readerComponentID)) {
				throw new DublicateObjectException("A reader with this ID "
						+ readerComponentID + " already exists.",
						DublicateObjectException.Type.COMPONENT);
			}
			componentList.put(readerComponentID, readerComponent);
			addEvent(readerComponent);
		}
	}

	public void registerBatchSuite(BatchSuite batchSuite)
			throws DublicateObjectException {
		logger.info("Registering " + batchSuite.getBatches().size()
				+ " Batches");
		for (Batch batch : batchSuite.getBatches()) {
			// TODO negaive ID's shouldn't be allowed
			int batchID = batch.getID();
			if (batchList.containsKey(batchID)) {
				throw new DublicateObjectException("A batch with this ID ("
						+ batchID + ") already exists.",
						DublicateObjectException.Type.BATCH);
			}
			batchList.put(batchID, batch);
			addEvent(batch);
		}
	}

	public void registerTestUnitSuite(TestUnitSuite testUnitSuite)
			throws DublicateObjectException {
		logger.info("Registering " + testUnitSuite.getTestUnits().size()
				+ " TestUnit's");
		for (TestUnit testUnit : testUnitSuite.getTestUnits()) {
			// TODO negaive ID's shouldn't be allowed
			int testUnitID = testUnit.getID();
			if (testUnitList.containsKey(testUnitID)) {
				throw new DublicateObjectException("A testUnit with this ID "
						+ testUnitID + " already exists.",
						DublicateObjectException.Type.COMPONENT);
			}
			testUnitList.put(testUnitID, testUnit);
			addEvent(testUnit);
		}
	}

	public void registerTestUnit(TestUnit testUnit)
			throws DublicateObjectException {
		int testUnitID = testUnit.getID();
		logger.info("Registering TestUnit " + testUnitID);
		if (testUnitList.containsKey(testUnitID)) {
			throw new DublicateObjectException("A TestUnit with this ID ("
					+ testUnitID + ") already exists.",
					DublicateObjectException.Type.TESTUNIT);
		}
		testUnitList.put(testUnit.getID(), testUnit);
		addEvent(testUnit);
	}

	public void registerBatch(Batch batch) throws DublicateObjectException {
		int batchID = batch.getID();
		logger.info("Registering Batch " + batchID);
		if (batchList.containsKey(batchID)) {
			throw new DublicateObjectException("A TestUnit with this ID ("
					+ batchID + ") already exists.",
					DublicateObjectException.Type.BATCH);
		}
		batchList.put(batchID, batch);
		addEvent(batch);
	}

	public void registerComponent(ReaderComponent component)
			throws DublicateObjectException {
		int componentID = component.getID();
		logger.info("Registering Component " + componentID);
		if (componentList.containsKey(componentID)) {
			throw new DublicateObjectException("A TestUnit with this ID ("
					+ componentID + ") already exists.",
					DublicateObjectException.Type.BATCH);
		}
		componentList.put(componentID, component);
		addEvent(component);
	}

	public void registerScenario(Scenario scenario)
			throws DublicateObjectException {

		int scenarioID = scenario.getID();
		logger.info("Registering Scenario " + scenarioID);
		if (scenarioList.containsKey(scenarioID)) {
			throw new DublicateObjectException("A TestUnit with this ID ("
					+ scenarioID + ") already exists.",
					DublicateObjectException.Type.SCENARIO);
		}
		scenarioList.put(scenarioID, scenario);
		addEvent(scenario);
	}

	public void unRegisterReader(int ID) {
		Integer usage = readerUsage.get(ID);
		usage = usage - 1;
		readerUsage.put(ID, usage);
		if (usage == 0) {
			UIReader reader = readerComponents.get(ID);
			try {
				logger.debug("stopping Reader " + ID
						+ " because no use anymore");
				reader.getReaderManager().turnReaderOff();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			readerRegistry.remove(reader);
			readerUsage.remove(ID);
			readerComponents.remove(ID);
		}
	}

	public ReaderModuleManagerInterface registerReader(int ID) {
		if (readerComponents.containsKey(ID)) {
			Integer usage = readerUsage.get(ID);
			usage = usage + 1;
			readerUsage.put(ID, usage);
			return readerComponents.get(ID).getReaderManager();
		} else {
			// Create Reader and mark it as used
			ReaderComponent readerComponent = getComponent(ID);
			GeneralReaderPropertyHolder grph = readerComponent.getReader();
			try {
				readerRegistry.create(grph);
				UIReader reader = readerRegistry.getReaderByName(grph
						.getReaderName());

				Integer usage = new Integer(1);
				readerUsage.put(ID, usage);
				readerComponents.put(ID, reader);

				reader.getReaderManager().turnReaderOn();
				return reader.getReaderManager();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public Batch getBatch(int id) {
		return batchList.get(id);
	}

	public Scenario getScenario(int id) {
		return scenarioList.get(id);
	}

	public ReaderComponent getComponent(int id) {
		return componentList.get(id);
	}

	public void connectReaderRegsitry(String hostname, int port) {
		try {
			readerRegistry.connect(hostname, port);
		} catch (ConnectException e) {
			// TODO Handle this Exception correct.
			e.printStackTrace();
		}
		isConnected = true;
	}

	public void addListener(RegistryChangeListener listener) {
		logger.debug("Registering Listener: "
				+ listener.getClass().getSimpleName());
		listeners.add(listener);
	}

	public void removeListener(RegistryChangeListener listener) {
		logger.debug("Unregistering Listener: "
				+ listener.getClass().getSimpleName());
		listeners.remove(listener);
	}

	private void addEvent(Object event) {
		logger.debug("Informing listeners of change in registry!");
		for (RegistryChangeListener listener : listeners) {
			listener.add(event);
		}
	}

	// TODO needs to be implemented
	private void removeEvent(Object event) {
		logger.debug("Informing listeners of change!");
		for (RegistryChangeListener listener : listeners) {
			listener.remove(event);
		}
	}

	// TODO needs to be implemented
	private void updateEvent(Object event) {
		logger.debug("Informing listeners of change!");
		for (RegistryChangeListener listener : listeners) {
			listener.update(event);
		}
	}

	public void removeBatch(Batch batch) {
		batchList.remove(batch.getID());
		removeEvent(batch);
	}

	public void removeScenario(Scenario scenario) {
		scenarioList.remove(scenario.getID());
		removeEvent(scenario);
	}

	public void removeComponent(ReaderComponent readerComponent) {
		componentList.remove(readerComponent.getID());
		removeEvent(readerComponent);
	}

	public void removeTestUnit(TestUnit testUnit) {
		testUnitList.remove(testUnit.getID());
		removeEvent(testUnit);
	}

	/**
	 * @return the isConnected
	 */
	public boolean isConnected() {
		return isConnected;
	}

}
