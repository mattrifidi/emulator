package org.rifidi.ui.ide.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

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
import org.rifidi.tags.impl.C0G1Tag;
import org.rifidi.tags.impl.C1G1Tag;
import org.rifidi.tags.impl.C1G2Tag;
import org.rifidi.tags.impl.RifidiTag;
import org.rifidi.services.tags.registry.ITagRegistry;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.common.registry.ReaderRegistry;
import org.rifidi.ui.common.wizards.reader.exceptions.DuplicateReaderException;
import org.rifidi.ui.ide.configuration.AntennaTagMap;
import org.rifidi.ui.ide.configuration.IDEConfiguration;
import org.rifidi.ui.ide.configuration.ReaderAntennaTagMap;

/**
 * This class is the handler for opening an IDE Configuration.
 * 
 * 
 * A special thanks to Costin Boldisor from the University of Applied
 * Sciences Regensburg for contributing this functionality
 * 
 * @author Costin Boldisor - costinbb@live.de
 * 
 */
public class OpenIDEConfigurationHandler extends AbstractHandler {

	public static final String ID = "org.rifidi.ui.ide.handlers.OpenIDEConfigurationHandler";

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
		if (filename == null) {
			return null;
		}

		logger.debug("Loading from:" + filename);

		ReaderRegistry reg = ReaderRegistry.getInstance();

		tagRegistry.remove(tagRegistry.getTags());
		List<UIReader> prevReaderList = reg.getReaderList();
		for (UIReader u : prevReaderList) {
			reg.remove(u);
		}

		File file = new File(filename);

		boolean success = true;

		try {
			FileInputStream stream = new FileInputStream(file);

			ArrayList<Class> classes = new ArrayList<Class>();
			classes.add(IDEConfiguration.class);
			classes.add(GeneralReaderPropertyHolder.class);
			classes.add(C0G1Tag.class);
			classes.add(C1G1Tag.class);
			classes.add(C1G2Tag.class);
			classes.add(RifidiTag.class);

			JAXBContext context = JAXBContext.newInstance(classes
					.toArray(new Class[0]));

			Unmarshaller unmarshaller = context.createUnmarshaller();
			IDEConfiguration configuration = (IDEConfiguration) unmarshaller
					.unmarshal(stream);
			List<RifidiTag> tags = configuration.getTags();
			if (tags != null) {
				tagRegistry.initialize(tags);
			} else {
				logger.debug("No tags in IDE Configuration");
			}

			ReaderRegistry readerReg = ReaderRegistry.getInstance();
			for (UIReader reader : readerReg.getReaderList()) {
				readerReg.remove(reader);
			}
			for (GeneralReaderPropertyHolder reader : configuration
					.getReaders()) {
				try {
					readerReg.create(reader);
				} catch (DuplicateReaderException e) {
					logger.error("Already Have a reader with name "
							+ reader.getReaderName());
				}
			}
			ReaderAntennaTagMap readerAntennaTagMap = configuration
					.getReaderAntennaTagMap();
			if (readerAntennaTagMap != null) {
				for (GeneralReaderPropertyHolder reader : configuration
						.getReaders()) {
					AntennaTagMap antennaTagMap = readerAntennaTagMap
							.getEntry(reader.getReaderName());
					if (antennaTagMap != null) {
						for (int i = 0; i < reader.getNumAntennas(); i++) {
							UIReader uireader = readerReg
									.getReaderByName(reader.getReaderName());
							uireader.getAntenna(i).addTagsByID(
									antennaTagMap.getEntry(i));
						}
					}
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			success = false;
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
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
