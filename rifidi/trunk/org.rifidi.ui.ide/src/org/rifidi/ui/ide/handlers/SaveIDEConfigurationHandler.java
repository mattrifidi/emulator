package org.rifidi.ui.ide.handlers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

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
import org.rifidi.ui.common.reader.UIAntenna;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.common.registry.ReaderRegistry;
import org.rifidi.ui.ide.configuration.AntennaTagMap;
import org.rifidi.ui.ide.configuration.IDEConfiguration;
import org.rifidi.ui.ide.configuration.ReaderAntennaTagMap;

/**
 * This class is the handler for saving an IDE Configuration.
 * 
 * 
 * A special thanks to Costin Boldisor from the University of Applied
 * Sciences Regensburg for contributing this functionality
 * 
 * @author Costin Boldisor - costinbb@live.de
 * 
 */
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
		
		if(filename==null){
			return null;
		}

		ReaderRegistry reg = ReaderRegistry.getInstance();
		List<UIReader> readers = reg.getReaderList();
		List<RifidiTag> tags = tagReg.getTags();

		logger.debug("Perform save to:" + filename);

		try {

			IDEConfiguration configuration = new IDEConfiguration();
			List<GeneralReaderPropertyHolder> readerList = new ArrayList<GeneralReaderPropertyHolder>();
			ReaderAntennaTagMap readerAntennaTagMap = new ReaderAntennaTagMap();
			// step through each reader to save it and the tags that are on its
			// antennas
			for (UIReader reader : readers) {
				// save reader itself
				readerList.add((GeneralReaderPropertyHolder) reader);

				// step through each antenna and save tags on that antenna
				AntennaTagMap antennaTagMap = new AntennaTagMap();
				for (UIAntenna antenna : reader.getAntennas().values()) {
					ArrayList<Long> tagIDs = new ArrayList<Long>();
					for (RifidiTag t : antenna.getTagList()) {
						tagIDs.add(t.getTagEntitiyID());
					}
					antennaTagMap.addEntry(antenna.getId(), tagIDs);
				}
				readerAntennaTagMap.addEntry(reader.getReaderName(),
						antennaTagMap);
			}
			configuration.setReaders(readerList);
			configuration.setTags(tags);
			configuration.setReaderAntennaTagMap(readerAntennaTagMap);

			ArrayList<Class> classes = new ArrayList<Class>();
			classes.add(IDEConfiguration.class);
			classes.add(GeneralReaderPropertyHolder.class);
			classes.add(C0G1Tag.class);
			classes.add(C1G1Tag.class);
			classes.add(C1G2Tag.class);
			classes.add(RifidiTag.class);

			ByteArrayOutputStream fileOutput = new ByteArrayOutputStream();

			JAXBContext context = JAXBContext.newInstance(classes
					.toArray(new Class[0]));
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(configuration, fileOutput);

			File xmlOutputFile = new File(filename);
			FileOutputStream stream = new FileOutputStream(xmlOutputFile);
			fileOutput.writeTo(stream);

			// saveXMLDocument(filename, doc);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Inject
	public void setTagRegistry(ITagRegistry tagReg) {
		this.tagReg = tagReg;
	}

}
