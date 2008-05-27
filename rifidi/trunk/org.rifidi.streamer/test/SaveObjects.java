import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.services.tags.enums.TagGen;
import org.rifidi.services.tags.id.TagType;
import org.rifidi.streamer.xml.BatchSuite;
import org.rifidi.streamer.xml.ComponentSuite;
import org.rifidi.streamer.xml.LoadTestSuite;
import org.rifidi.streamer.xml.ScenarioSuite;
import org.rifidi.streamer.xml.actions.Action;
import org.rifidi.streamer.xml.actions.BatchAction;
import org.rifidi.streamer.xml.actions.GPIAction;
import org.rifidi.streamer.xml.actions.TagAction;
import org.rifidi.streamer.xml.actions.WaitAction;
import org.rifidi.streamer.xml.batch.Batch;
import org.rifidi.streamer.xml.components.ReaderComponent;
import org.rifidi.streamer.xml.scenario.PathItem;
import org.rifidi.streamer.xml.scenario.Scenario;
import org.rifidi.streamer.xml.testSuite.FileUnit;
import org.rifidi.streamer.xml.testSuite.TestUnit;
import org.rifidi.streamer.xml.testSuite.FileUnit.FileType;

/**
 * 
 */

/**
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class SaveObjects {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//save(batch());
		save(scenario());
		//save(loadTestSuite());
		//save(components());
	}

	public static ComponentSuite components() {
		ComponentSuite componentSuite = new ComponentSuite();
		ArrayList<ReaderComponent> readerComponents = new ArrayList<ReaderComponent>();

		ReaderComponent reader1 = new ReaderComponent();
		reader1.setID(1);
		GeneralReaderPropertyHolder grph = new GeneralReaderPropertyHolder();
		grph.setNumAntennas(1);
		grph.setNumGPIs(4);
		grph.setNumGPOs(4);
		grph
				.setReaderClassName("org.rifidi.emulator.reader.llrp.module.LLRPReaderModule");
		grph.setReaderName("LLRPReader");
		HashMap<String, String> propertiesMap = new HashMap<String, String>();
		propertiesMap.put("servermode", "true");
		propertiesMap.put("inet_address", "127.0.0.1:10101");
		propertiesMap.put("llrp_inet_address", "127.0.0.1:5084");
		grph.setPropertiesMap(propertiesMap);
		reader1.setReader(grph);

		
		ReaderComponent reader2 = new ReaderComponent();
		reader2.setID(2);
		GeneralReaderPropertyHolder grph2 = new GeneralReaderPropertyHolder();
		grph2.setNumAntennas(1);
		grph2.setNumGPIs(4);
		grph2.setNumGPOs(4);
		grph2.setReaderClassName("org.rifidi.emulator.reader.llrp.module.LLRPReaderModule");
		grph2.setReaderName("LLRPReader");
		HashMap<String, String> propertiesMap2 = new HashMap<String, String>();
		propertiesMap2.put("servermode", "true");
		propertiesMap2.put("inet_address", "127.0.0.1:10102");
		propertiesMap2.put("llrp_inet_address", "127.0.0.1:5085");
		grph2.setPropertiesMap(propertiesMap);
		reader2.setReader(grph);

		readerComponents.add(reader1);
		readerComponents.add(reader2);
		componentSuite.setReaderComponents(readerComponents);
		
		return componentSuite;
	}

	public static LoadTestSuite loadTestSuite() {
		LoadTestSuite loadTestSuite = new LoadTestSuite();
		ArrayList<FileUnit> fileUnitList = new ArrayList<FileUnit>();
		ArrayList<TestUnit> testUnitList = new ArrayList<TestUnit>();
		
		loadTestSuite.setFileUnits(fileUnitList);
		loadTestSuite.setTestUnits(testUnitList);
		
		FileUnit fileUnit1 = new FileUnit();
		fileUnit1.setFileType(FileType.COMPONENT);
		fileUnit1.setFileName("components.xml");
		
		FileUnit fileUnit2 = new FileUnit();
		fileUnit2.setFileType(FileType.BATCH);
		fileUnit2.setFileName("batches.xml");
		
		FileUnit fileUnit3 = new FileUnit();
		fileUnit3.setFileType(FileType.SCENARIO);
		fileUnit3.setFileName("scenario.xml");
		
		fileUnitList.add(fileUnit1);
		fileUnitList.add(fileUnit2);
		fileUnitList.add(fileUnit3);
		
		TestUnit testUnit = new TestUnit();
		testUnit.setID(1);
		testUnit.setIterations(10);
		ArrayList<Action> actions = new ArrayList<Action>();
		testUnit.setActions(actions);
		testUnitList.add(testUnit);
		
		WaitAction waitAction = new WaitAction();
		waitAction.setMaxWaitTime(5000);
		waitAction.setMinWaitTime(3000);
		waitAction.setRandom(true);
		
		actions.add(waitAction);
		
		BatchAction batchAction1 = new BatchAction();
		batchAction1.setBatchID(1);
		batchAction1.setScenarioID(1);
		
		actions.add(batchAction1);
		
		BatchAction batchAction2 = new BatchAction();
		batchAction2.setBatchID(1);
		batchAction2.setScenarioID(2);
		
		actions.add(batchAction2);
		
		return loadTestSuite;
	}

	public static ScenarioSuite scenario() {
		ScenarioSuite scenarioSuite = new ScenarioSuite();
		ArrayList<Scenario> scenarioList = new ArrayList<Scenario>();
		scenarioSuite.setScenarios(scenarioList);
		
		Scenario scenario1 = new Scenario();
		scenario1.setID(1);
		ArrayList<PathItem> scenarioPath1 = new ArrayList<PathItem>();
		scenario1.setPathItems(scenarioPath1);
		
		PathItem pathItem1 = new PathItem();
		pathItem1.setReaderID(1);
		pathItem1.setTravelTime(0);
			
		scenarioPath1.add(pathItem1);
		
		Scenario scenario2 = new Scenario();
		scenario2.setID(2);
		ArrayList<PathItem> scenarioPath2 = new ArrayList<PathItem>();
		scenario2.setPathItems(scenarioPath2);
		
		PathItem pathItem2 = new PathItem();
		pathItem2.setReaderID(2);
		pathItem2.setTravelTime(0);
	
		scenarioPath2.add(pathItem2);
		
		scenarioList.add(scenario1);
		scenarioList.add(scenario2);
		
		return scenarioSuite;
	}

	public static BatchSuite batch() {
		BatchSuite batchSuite = new BatchSuite();
		ArrayList<Batch> batchList = new ArrayList<Batch>();
		batchSuite.setBatches(batchList);
		
		Batch batch1 = new Batch();
		batch1.setID(1);
		ArrayList<Action> actionList = new ArrayList<Action>();
		batch1.setActions(actionList);
		
		batchList.add(batch1);
		
		WaitAction waitAction = new WaitAction();
		waitAction.setMaxWaitTime(5000);
		waitAction.setMinWaitTime(3000);
		waitAction.setRandom(true);
		
		TagAction tagAction = new TagAction();
		tagAction.setExecDuration(2000);
		tagAction.setNumber(20);
		tagAction.setPrefix("eef");
		tagAction.setRegenerate(false);
		tagAction.setTagGen(TagGen.GEN2);
		tagAction.setTagType(TagType.CustomEPC96);
		
		WaitAction waitAction2 = new WaitAction();
		waitAction2.setMaxWaitTime(2000);
		waitAction2.setMinWaitTime(0);
		waitAction2.setRandom(false);
		
		GPIAction gpiAction = new GPIAction();
		gpiAction.setPort(1);
		gpiAction.setSignal(true);
		
		actionList.add(waitAction);
		actionList.add(tagAction);
		actionList.add(waitAction2);
		actionList.add(gpiAction);
		
		return batchSuite;
	}

	public static void save(Object o) {
		StringWriter writer = new StringWriter();
		JAXBContext context;

		try {
			context = JAXBContext.newInstance(o.getClass());
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(o, writer);
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		System.out.print(writer);
	}

}
