package org.rifidi.dynamicswtforms.ui.form;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.rifidi.dynamicswtforms.ui.exceptions.DynamicSWTFormInvalidXMLException;
import org.rifidi.dynamicswtforms.ui.widgets.DynamicSWTFormWidget;
import org.rifidi.dynamicswtforms.ui.widgets.listeners.DynamicSWTWidgetListener;
import org.rifidi.dynamicswtforms.xml.constants.FormData;

public abstract class AbstractDynamicSWTForm implements DynamicSWTWidgetListener {

	protected Element formRoot;
	
	protected ArrayList<DynamicSWTFormWidget> widgets;
	
	private ArrayList<DynamicSWTWidgetListener> listeners;
	private String name;

	public AbstractDynamicSWTForm(String xml)
			throws DynamicSWTFormInvalidXMLException {
		SAXBuilder builder = new SAXBuilder();
		Reader reader = new StringReader(xml);
		try {
			init(builder.build(reader).getRootElement());
		} catch (JDOMException e) {
			throw new DynamicSWTFormInvalidXMLException(e);
		} catch (IOException e) {
			throw new DynamicSWTFormInvalidXMLException(e);
		}
	}

	public AbstractDynamicSWTForm(Element formRoot) {
		init(formRoot);
	}
	
	private void init(Element formRoot){
		this.listeners = new ArrayList<DynamicSWTWidgetListener>();
		widgets = new ArrayList<DynamicSWTFormWidget>();
		this.formRoot = formRoot;
		this.name = formRoot.getAttributeValue(FormData.NAME.name());
	}
	
	public abstract void createControls(Composite parent);
	
	public abstract void setError(String widgetName, String message);

	public abstract void unsetError(String widgetName);

	public String setValue(String widgetName, String value) {
		for (DynamicSWTFormWidget widget : widgets) {
			if (widget.getElementName().equalsIgnoreCase(widgetName)) {
				return widget.setValue(value);
			}
		}
		return "Form does not contain widget with name " + widgetName;
	}

	public String getValue(String widgetName) {
		for (DynamicSWTFormWidget widget : widgets) {
			if (widget.getElementName().equalsIgnoreCase(widgetName)) {
				return widget.getValue();
			}
		}
		return null;
	}


	public String validate() {
		for (DynamicSWTFormWidget widget : widgets) {
			String s = widget.validate();
			if (s != null) {
				return s;
			}
		}
		return null;
	}

	public void addListner(DynamicSWTWidgetListener listener) {
		this.listeners.add(listener);
	}

	public void removeListner(DynamicSWTWidgetListener listener) {
		this.listeners.remove(listener);
	}

	@Override
	public void dataChanged(String newData) {
		for (DynamicSWTWidgetListener l : listeners) {
			l.dataChanged(newData);
		}
	}

	@Override
	public void keyReleased() {
		for (DynamicSWTWidgetListener l : listeners) {
			l.keyReleased();
		}

	}

	public Element getXML() {
		Element e = new Element(name);
		for (DynamicSWTFormWidget widget : widgets) {
			e.addContent(widget.getXML());
		}
		return e;
	}

	public String getXMLAsString(boolean compact) {
		Element e = getXML();
		XMLOutputter outputter;
		if (compact) {
			outputter = new XMLOutputter(Format.getCompactFormat());
		} else {
			outputter = new XMLOutputter(Format.getPrettyFormat());
		}
		String s = outputter.outputString(e);
		return s;
	}
	
	public void enable(){
		for(DynamicSWTFormWidget w : widgets){
			w.enable();
		}
	}
	
	public void disable(){
		for(DynamicSWTFormWidget w : widgets){
			w.disable();
		}
	}

}
