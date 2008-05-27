import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * 
 */

/**
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class LoadObjects {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Batch b = (Batch) load("batch.xml", Batch.class);
		// System.out.println("analysing " + b.getClass().getSimpleName() +
		// "...");
		// System.out.println(b.getClass().getSimpleName() + " has size " +
		// b.getBatchUnitList().size());
		// for (BatchUnit unit : b.getBatchUnitList()) {
		// System.out.println(unit.getClass().getSimpleName());
		// unit.execute();
		// }
		//ScenarioList scenario = (ScenarioList) load("scenario2.xml", ScenarioList.class);
	}

	public static Object load(String filename, Class<?> o) {
		File fileName = new File(filename);
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(o);
			Unmarshaller unmarshaller = context.createUnmarshaller();

			return unmarshaller.unmarshal(fileName);
		} catch (JAXBException e) {
			// TODO Change the error Handling to throw an exception
			e.printStackTrace();
			return null;
		}
	}

}
