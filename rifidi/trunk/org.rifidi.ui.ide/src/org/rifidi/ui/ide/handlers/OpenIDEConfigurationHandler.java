package org.rifidi.ui.ide.handlers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;
import org.rifidi.services.tags.factory.TagCreationPattern;
import org.rifidi.services.tags.registry.ITagRegistry;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.common.registry.ReaderRegistry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class OpenIDEConfigurationHandler extends AbstractHandler {

	public static final String ID= "org.rifidi.ui.ide.handlers.OpenIDEConfigurationHandler"; 
	
	private ITagRegistry tagRegistry;

	private static Log logger = LogFactory
			.getLog(OpenIDEConfigurationHandler.class);

	public OpenIDEConfigurationHandler() {
		super();
		ServiceRegistry.getInstance().service(this);
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		logger.debug("Load saved test suite");

		Display display = Display.getCurrent();
		Shell shell = display.getActiveShell();

		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		dialog.setFilterNames(new String[] { "*.rfts", "All Files (*.*)" });
		dialog.setFilterExtensions(new String[] { "*.rfts", "*.*" }); // Windows
		// wild
		// cards

		String filename = dialog.open();

		logger.debug("Loading from:" + filename);

		ReaderRegistry reg = ReaderRegistry.getInstance();

		tagRegistry.remove(tagRegistry.getTags());
		List<UIReader> prevReaderList = reg.getReaderList();
		for (UIReader u : prevReaderList) {
			reg.remove(u);
		}

		File file = new File(filename);

		boolean success = true;

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			success = false;
		}

		Document doc = null;
		try {
			doc = db.parse(file);

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			success = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			success = false;
		}
		doc.getDocumentElement().normalize();

		Element root = doc.getDocumentElement();
		NodeList nl = root.getElementsByTagName("readers");

		try {

			if (nl.getLength() == 1) {
				Element readers = (Element) nl.item(0);

				NodeList rds = readers.getElementsByTagName("reader");

				int n = rds.getLength();

				for (int i = 0; i < n; ++i) {
					Element reader = (Element) rds.item(i);
					//TODO: create reader

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		}

		nl = root.getElementsByTagName("tags");

		try {

			if (nl.getLength() == 1) {
				Element readers = (Element) nl.item(0);

				NodeList rds = readers.getElementsByTagName("tag");

				int n = rds.getLength();

				for (int i = 0; i < n; ++i) {
					Element tag = (Element) rds.item(i);
					TagCreationPattern pattern = UIReader.xmlToTag(tag);
					tagRegistry.createTags(pattern);
				}
				// TODO: may need to refresh tag view

			}

		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		}

		logger.debug("Finished loading. Success:" + success);
		return null;
	}

	@Inject
	public void setTagRegistry(ITagRegistry tagReg) {
		this.tagRegistry = tagReg;
	}

}
