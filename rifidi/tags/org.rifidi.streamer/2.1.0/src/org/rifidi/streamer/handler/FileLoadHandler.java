package org.rifidi.streamer.handler;

import java.io.File;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.rifidi.streamer.xml.BatchSuite;
import org.rifidi.streamer.xml.ComponentSuite;
import org.rifidi.streamer.xml.LoadTestSuite;
import org.rifidi.streamer.xml.MetaFile;
import org.rifidi.streamer.xml.ScenarioSuite;

/**
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class FileLoadHandler {
	
	public static boolean debugToConsole = false;

	public static MetaFile openMetaFile(String filename, String parentPath) throws JAXBException
	{
		return (MetaFile)load(filename, parentPath, MetaFile.class);
	}
	
	/**
	 * @param filename
	 * @return
	 * @throws JAXBException
	 */
	public static LoadTestSuite openTestSuite(String filename, String parentPath) throws JAXBException
	{
		return (LoadTestSuite)load(filename, parentPath, LoadTestSuite.class);
	}
	
	/**
	 * @param filename
	 * @return
	 * @throws JAXBException
	 */
	public static ScenarioSuite openScenarioSuite(String filename, String parentPath) throws JAXBException
	{
		return (ScenarioSuite)load(filename, parentPath, ScenarioSuite.class);
	}
	
	/**
	 * @param filename
	 * @return
	 * @throws JAXBException
	 */
	public static ComponentSuite openComponentSuite(String filename, String parentPath) throws JAXBException
	{
		return (ComponentSuite)load(filename, parentPath, ComponentSuite.class);
	}
	
	/**
	 * @param filename
	 * @return
	 * @throws JAXBException
	 */
	public static BatchSuite openBatchSuite(String filename, String parentPath) throws JAXBException
	{
		return (BatchSuite)load(filename, parentPath, BatchSuite.class);
	}
	
	/**
	 * @param filename
	 * @param o
	 * @return
	 * @throws JAXBException
	 */
	public static Object load(String filename, String parentPath, Class<?> clazz)
			throws JAXBException {
		File fileName = new File(filename);
		String t = fileName.getParent();
		if(t==null){
			if(parentPath != null)
				fileName = new File(parentPath + File.separator + fileName);
		}
		JAXBContext context;
		context = JAXBContext.newInstance(clazz);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		return unmarshaller.unmarshal(fileName);
	}
	
	/**
	 * @param o
	 */
	public static String save(Object o) {
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

		if(debugToConsole)
			System.out.print(writer);
		return writer.toString();
	}

}
