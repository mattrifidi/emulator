package org.rifidi.ui.ide.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;
import org.rifidi.services.tags.impl.RifidiTag;
import org.rifidi.services.tags.registry.ITagRegistry;
import org.rifidi.ui.common.reader.UIAntenna;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.common.registry.ReaderRegistry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SaveIDEConfigurationHandler extends AbstractHandler {

	private static Log logger = LogFactory
			.getLog(ConnectionSettingsHandler.class);

	private ITagRegistry tagReg;

	public SaveIDEConfigurationHandler() {
		super();
		ServiceRegistry.getInstance().service(this);
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		logger.debug("Save current test suite");

		Display display = Display.getCurrent();
		Shell shell = display.getActiveShell();

		FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		dialog.setFilterNames(new String[] { "*.rfts", "All Files (*.*)" });
		dialog.setFilterExtensions(new String[] { "*.rfts", "*.*" }); // Windows
		// wild
		// cards
		dialog.setFilterPath("c:\\"); // Windows path
		dialog.setFileName("test01.rfts");

		String filename = dialog.open();

		ReaderRegistry reg = ReaderRegistry.getInstance();
		List<UIReader> readers = reg.getReaderList();
		List<RifidiTag> tags = tagReg.getTags();

		logger.debug("Perform save to:" + filename);

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Document doc = db.getDOMImplementation().createDocument(null, "root",
				null);

		Element rds = doc.createElement("readers");
		Element tgs = doc.createElement("tags");

		try {
			JAXBContext context = JAXBContext.newInstance(UIReader.class,UIAntenna.class,
					RifidiTag.class);
			Marshaller marshaller = context.createMarshaller();

			for (UIReader ur : readers) {
				marshaller.marshal(ur, rds);
				logger.debug("marshalled: " + ur);
			}
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		for (RifidiTag tg : tags) {
			Element crt = doc.createElement("tag");

			UIReader.tagToXML(tg, crt);
			tgs.appendChild(crt);
			logger.debug("Saving tag:" + tg.getTag().getId());
		}

		doc.getDocumentElement().appendChild(rds);

		doc.getDocumentElement().appendChild(tgs);

		saveXMLDocument(filename, doc);
		return null;
	}

	/**
	 * Saves XML Document into XML file.
	 * 
	 * @param fileName
	 *            XML file name
	 * @param doc
	 *            XML document to save
	 * @return <B>true</B> if method success <B>false</B> otherwise
	 */
	public boolean saveXMLDocument(String fileName, Document doc) {

		logger.debug("Saving XML file... " + fileName);
		File f = new File(fileName);
		if (f.exists())
			f.delete();

		// open output stream where XML Document will be saved
		File xmlOutputFile = new File(fileName);
		FileOutputStream fos;
		Transformer transformer;
		try {
			fos = new FileOutputStream(xmlOutputFile);
		} catch (FileNotFoundException e) {
			logger.debug("Error occured: " + e.getMessage());
			return false;
		}

		// Use a Transformer for output
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		try {
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		} catch (TransformerConfigurationException e) {
			logger.debug("Transformer configuration error: " + e.getMessage());
			return false;
		}
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(fos);
		// transform source into result will do save
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			logger.debug("Error transform: " + e.getMessage());
		}
		logger.debug("XML file saved.");
		return true;
	}

	@Inject
	public void setTagRegistry(ITagRegistry tagReg) {
		this.tagReg = tagReg;
	}

}
