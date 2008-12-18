/**
 * 
 */
package org.rifidi.streamer;


/**
 * This class is only for testing purposes
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class Application {
	//
	// private Log logger = LogFactory.getLog(Application.class);
	// private LoadTestSuite loadTestSuite;
	//
	// public static void main(String[] args) {
	// new Application();
	// }
	//
	// public Application() {
	//
	// logger.debug("====++++++ Starting Rifidi Streamer 2.0 ++++++====");
	//
	// InputObjectRegistry inputObjectRegistry = new InputObjectRegistry();
	//
	// String hostname = "localhost";
	// int port = 1198;
	//
	// // RMI ReaderManagement startup and connection management
	// try {
	// RifidiManager.startManager(hostname, port);
	// inputObjectRegistry.connectReaderRegsitry(hostname, port);
	// } catch (UnknownHostException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (RemoteException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// try {
	// loadTestSuite = FileLoadHandler.openTestSuite("testsuite.xml");
	// for (FileUnit fileUnit : loadTestSuite.getFileUnits()) {
	// if (fileUnit.getFileType() == FileUnit.FileType.BATCH) {
	// BatchSuite batchSuite = FileLoadHandler
	// .openBatchSuite(fileUnit.getFileName());
	// inputObjectRegistry.registerBatchSuite(batchSuite);
	// }
	// if (fileUnit.getFileType() == FileUnit.FileType.SCENARIO) {
	// ScenarioSuite scenarioSuite = FileLoadHandler
	// .openScenarioSuite(fileUnit.getFileName());
	// inputObjectRegistry.registerScenarioSuite(scenarioSuite);
	// }
	// if (fileUnit.getFileType() == FileUnit.FileType.COMPONENT) {
	// ComponentSuite componentSuite = FileLoadHandler
	// .openComponentSuite(fileUnit.getFileName());
	// inputObjectRegistry.registerComponentSuite(componentSuite);
	// }
	// }
	// } catch (JAXBException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// } catch (DublicateObjectException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// logger.debug("LoadTestSuite loaded....");
	// for (TestUnit testUnit : loadTestSuite.getTestUnits()) {
	// TestUnitExecuter testUnitExecuter = new TestUnitExecuter(testUnit,
	// this, inputObjectRegistry);
	// testUnitExecuter.start();
	// }
	// try {
	// synchronized (this) {
	// this.wait();
	// }
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	//		}
	//		logger.debug("End of Application");
	//		System.exit(0);
	//	}
}
