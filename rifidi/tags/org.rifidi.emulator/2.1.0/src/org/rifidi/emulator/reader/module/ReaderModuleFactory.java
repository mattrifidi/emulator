/**
 * 
 */
package org.rifidi.emulator.reader.module;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.rifidi.emulator.Activator;
import org.rifidi.emulator.common.ControlSignal;

/**
 * ReaderModuleFactory creates ReaderModules
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class ReaderModuleFactory {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory.getLog(ReaderModuleFactory.class);

	/**
	 * 
	 * @param properties
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ReaderModule createReaderModule(
			GeneralReaderPropertyHolder properties) {
		try {
			Class<?> readerClass = Class.forName(properties
					.getReaderClassName());

			Class[] types = new Class[] { ControlSignal.class,
					GeneralReaderPropertyHolder.class };
			Constructor cons = readerClass.getConstructor(types);
			logger.debug("Calling constructor for reader: " + readerClass);
			Object[] args = new Object[] { new ControlSignal<Boolean>(true),
					properties };
			return (ReaderModule) cons.newInstance(args);
		} catch (NoSuchMethodException nsme) {
			logger.error("Failed instantiating reader module: " + nsme);
		} catch (InstantiationException ie) {
			logger.error("Failed instantiating reader module: " + ie);
		} catch (InvocationTargetException ite) {
			ite.printStackTrace();
			logger.error("Failed instantiating reader module: " + ite);
		} catch (IllegalAccessException iae) {
			logger.error("Failed instantiating reader module: " + iae);
		} catch (ClassNotFoundException e) {
			logger.error("Couldn't find class for reader " + properties.getReaderName());
			e.printStackTrace();
		}

		return null;
	}


	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Class> getSupportedReaders() {
		HashMap<String, Class> ret = new HashMap<String, Class>();
		for (Bundle bundle : Activator.context.getBundles()) {
			// TODO: fix, why don't they start anymore?
			if (bundle.getSymbolicName().contains("org.rifidi.emulator.reader")) {
				try {
					bundle.start();
				} catch (BundleException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			for (ServiceReference ser : Activator.context.getServiceReferences(
					ReaderModule.class.getName(), null)) {
				Class currReaderClass = ((ReaderModule) Activator.context
						.getService(ser)).getClass();
				try {
					ret.put((String) currReaderClass.getField("READERTYPE")
							.get(currReaderClass), currReaderClass);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}
			}
		} catch (InvalidSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 
	 * @param readerName
	 * @return
	 */
	public static String getReaderXMLDescription(String readerName) {

		Class<?> readerClass = getSupportedReaders().get(readerName);

		String pathName = null;
		try {
			pathName = readerClass.getField("XMLLOCATION").get(readerClass)
					+ "emulator.xml";
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (NoSuchFieldException e1) {
			e1.printStackTrace();
		}

		String stringData = " ";
		InputStream xml = null;

		try {
			xml = readerClass.getClassLoader().getResourceAsStream(pathName);
			InputStreamReader xmlStream = new InputStreamReader(xml);
			BufferedReader bufferedXml = new BufferedReader(xmlStream);
			boolean senti = true;
			while (senti) {
				int ichar = bufferedXml.read();
				if (ichar >= 0) {
					stringData = stringData + (char) ichar;
				} else {
					senti = false;
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return stringData;
	}

}
