/*
 *  ReaderRegistry.java
 *
 *  Created:	Mar 2, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.ui.common.reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.rifidi.common.utilities.ByteAndHexConvertingUtility;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.emulator.rmi.server.ReaderModuleManagerInterface;
import org.rifidi.services.tags.enums.TagGen;
import org.rifidi.services.tags.factory.TagCreationPattern;
import org.rifidi.services.tags.factory.TagFactory;
import org.rifidi.services.tags.id.TagType;
import org.rifidi.services.tags.impl.RifidiTag;
import org.rifidi.ui.common.reader.callback.UIReaderCallbackManager;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * The UI representation of a reader.
 * 
 * As this class extends the GeneralReaderPropertyHolder it can be used to
 * create the reader over RMI. (Use getGeneralPropertyReader())
 * 
 * @author Jochen Mader - jochen@pramari.com
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class UIReader extends GeneralReaderPropertyHolder {

	/**
	 * SerialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The state of the reader - can be running, stopped or suspended
	 */
	// FIXME Use enums, not this
	private String readerState = "NEW";

	/**
	 * This is the selection from the wizard Page
	 */
	@XmlElement
	private String readerType;

	/**
	 * This holds the reference to the real reader
	 */
	private ReaderModuleManagerInterface readerManager;

	/**
	 * This is the callback interface implementation it is used to get response
	 * from the reader without the need of polling it
	 */
	private UIReaderCallbackManager readerCallbackManager;

	/**
	 * UI representation of the Antenna Fields
	 */
	private HashMap<Integer, UIAntenna> antennas;

	public UIReader(ReaderModuleManagerInterface readerManager, GeneralReaderPropertyHolder grph, String readerType){
		this.readerManager = readerManager;
		this.setNumAntennas( grph.getNumAntennas());
		this.setNumGPIs(grph.getNumGPIs());
		this.setNumGPOs(grph.getNumGPOs());
		this.setPropertiesMap(grph.getPropertiesMap());
		this.setReaderName(grph.getReaderName());
		this.setReaderClassName(grph.getReaderClassName());
		antennas = new HashMap<Integer, UIAntenna>();
		for(int i=0; i<getNumAntennas(); i++){
			antennas.put(i, new UIAntenna(readerManager,i));
		}
	}
	
	public void start() {
		readerState = "running";
		try {
			readerManager.turnReaderOn();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		readerState = "stopped";
		try {
			readerManager.turnReaderOff();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void suspend() {
		// TODO implment functionality
		readerState = "suspended";
		try {
			readerManager.suspendReader();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void resume() {
		// TODO implment functionality
		readerState = "running";
		try {
			readerManager.resumeReader();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the readerType
	 */
	public String getReaderType() {
		return readerType;
	}

	/**
	 * @param readerType
	 *            the readerType to set
	 */
	public void setReaderType(String readerType) {
		this.readerType = readerType;
	}

	/**
	 * @return the readerState
	 */
	public String getReaderState() {
		return readerState;
	}

	/**
	 * @param readerState
	 *            the readerState to set
	 */
	public void setReaderState(String readerState) {
		this.readerState = readerState;
	}

	/**
	 * @return the readerManager
	 */
	public ReaderModuleManagerInterface getReaderManager() {
		return readerManager;
	}

	/**
	 * @return the antennas
	 */
	public HashMap<Integer, UIAntenna> getAntennas() {
		return antennas;
	}

	/**
	 * Returns a the Antenna with this id.
	 * 
	 * @param id
	 *            of the Antenna
	 * @return UIAntenna if the antenna exist. Otherwise there will be
	 *         <code>null</code> returned.
	 */
	public UIAntenna getAntenna(Integer id) {
		return antennas.get(id);
	}


	/**
	 * GeneralPropertyHolder describing this Reader for creation in the Emulator
	 * 
	 * @return GeneralPropertyHolder of the reader
	 */
	public GeneralReaderPropertyHolder getGeneralReaderPropertyHolder() {
		GeneralReaderPropertyHolder grph = new GeneralReaderPropertyHolder();
		grph.setNumAntennas(this.getNumAntennas());
		grph.setNumGPIs(this.getNumGPIs());
		grph.setNumGPOs(this.getNumGPOs());
		grph.setReaderClassName(this.getReaderClassName());
		grph.setReaderName(this.getReaderName());
		grph.setPropertiesMap(this.getPropertiesMap());
		return grph;
	}

	/**
	 * @return the readerCallbackManager
	 */
	public UIReaderCallbackManager getReaderCallbackManager() {
		return readerCallbackManager;
	}

	/**
	 * @param readerCallbackManager
	 *            the readerCallbackManager to set
	 */
	public void setReaderCallbackManager(
			UIReaderCallbackManager readerCallbackManager) {
		this.readerCallbackManager = readerCallbackManager;
	}

	public void writeObject(Element out) {
		Document doc = out.getOwnerDocument();

		out.setNodeValue("UIReader");
		Element rdName = doc.createElement("name");
		Element rdClass = doc.createElement("class");
		Element rdAntenna = doc.createElement("antennas");

		rdName.setTextContent(getReaderName());
		rdClass.setTextContent(getReaderClassName());
		rdAntenna.setTextContent("" + getNumAntennas());

		Element prop = doc.createElement("properties");

		Map<String, String> props = getPropertiesMap();
		for (String k : props.keySet()) {
			String v = props.get(k);

			Element crt = doc.createElement("prop");
			Attr at = doc.createAttribute("key");
			at.setTextContent(k);
			crt.setAttributeNode(at);
			at = doc.createAttribute("value");
			at.setTextContent(v);
			crt.setAttributeNode(at);
			prop.appendChild(crt);
		}

		Element ante = doc.createElement("antennaList");

		Map<Integer, UIAntenna> ants = getAntennas();

		for (Integer k : ants.keySet()) {
			UIAntenna a = ants.get(k);

			List<RifidiTag> tags = a.getTagList();

			Element crt = doc.createElement("ant");
			Attr at = doc.createAttribute("id");
			at.setTextContent(k.toString());
			crt.setAttributeNode(at);

			for (RifidiTag tg : tags) {
				Element tgcrt = doc.createElement("tag");
				tagToXML(tg, tgcrt);
				crt.appendChild(tgcrt);
			}

			ante.appendChild(crt);
		}

		out.appendChild(rdName);
		out.appendChild(rdClass);
		out.appendChild(rdAntenna);
		out.appendChild(prop);
		out.appendChild(ante);

	}

	public static void tagToXML(RifidiTag tg, Element out) {
		Document doc = out.getOwnerDocument();

		out.setNodeValue("tag");
		Element rdtype = doc.createElement("type");
		Element rdval = doc.createElement("value");
		Element rdgen = doc.createElement("tagGeneration");
		Element rdIDForm = doc.createElement("idFormat");

		rdtype.setTextContent(tg.getTagType().name());
		rdval.setTextContent(ByteAndHexConvertingUtility.toHexString(tg
				.getTag().getId()));
		rdgen.setTextContent(tg.getTag().getTagGeneration().name());
		rdIDForm.setTextContent(tg.getTagType().name());

		out.appendChild(rdtype);
		out.appendChild(rdval);
		out.appendChild(rdgen);
		out.appendChild(rdIDForm);
	}

	public void readAntennas(Element in) {
		Element ants = (Element) in.getElementsByTagName("antennaList").item(0);
		NodeList as = ants.getElementsByTagName("ant");
		int n = as.getLength();

		for (int i = 0; i < n; ++i) {
			Element crta = (Element) as.item(i);
			String id = crta.getAttribute("id");

			NodeList tags = crta.getElementsByTagName("tag");

			int m = tags.getLength();
			List<RifidiTag> antTags = new ArrayList<RifidiTag>();

			for (int j = 0; j < m; ++j) {
				Element crtt = (Element) tags.item(j);
				RifidiTag tagr = TagFactory.generateTags(xmlToTag(crtt)).get(0);
				antTags.add(tagr);
			}

			UIAntenna uiAnt = new UIAntenna(this.readerManager, Integer.parseInt(id));
			uiAnt.addTag(antTags);

			this.antennas.put(i, uiAnt);
		}
	}

	public void readObject(Element in) {

		String name = in.getElementsByTagName("name").item(0).getTextContent();
		String cls = in.getElementsByTagName("class").item(0).getTextContent();
		String ant = in.getElementsByTagName("antennas").item(0)
				.getTextContent();

		this.setReaderClassName(cls);
		this.setReaderName(name);
		this.setNumAntennas(Integer.parseInt(ant));

		Element props = (Element) in.getElementsByTagName("properties").item(0);

		NodeList ps = props.getElementsByTagName("prop");
		int n = ps.getLength();

		for (int i = 0; i < n; ++i) {
			Element e = (Element) ps.item(i);
			String key = e.getAttribute("key");
			String value = e.getAttribute("value");

			this.setProperty(key, value);
		}
	}

	public static TagCreationPattern xmlToTag(Element in) {

		TagGen tg;

		String type = in.getElementsByTagName("type").item(0).getTextContent();
		String value = in.getElementsByTagName("value").item(0)
				.getTextContent();
		String format = in.getElementsByTagName("idFormat").item(0)
				.getTextContent();

		if (type.equals("GEN1"))
			tg = TagGen.GEN1;
		else
			tg = TagGen.GEN2;

		TagCreationPattern pattern = new TagCreationPattern();
		pattern.setNumberOfTags(1);
		pattern.setPrefix(value);
		pattern.setTagGeneration(tg);
		pattern.setTagType(TagType.valueOf(format));
		return pattern;
	}

}
