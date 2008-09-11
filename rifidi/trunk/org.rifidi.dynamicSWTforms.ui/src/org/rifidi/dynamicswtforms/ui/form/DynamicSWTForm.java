package org.rifidi.dynamicswtforms.ui.form;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.rifidi.dynamicswtforms.ui.exceptions.DynamicSWTFormInvalidXMLException;
import org.rifidi.dynamicswtforms.ui.widgets.DynamicSWTFormWidget;
import org.rifidi.dynamicswtforms.ui.widgets.data.ChoiceWidgetData;
import org.rifidi.dynamicswtforms.ui.widgets.data.FloatWidgetData;
import org.rifidi.dynamicswtforms.ui.widgets.data.IntegerWidgetData;
import org.rifidi.dynamicswtforms.ui.widgets.data.StringWidgetData;
import org.rifidi.dynamicswtforms.ui.widgets.impl.ChoiceWidget;
import org.rifidi.dynamicswtforms.ui.widgets.impl.NumberWidget;
import org.rifidi.dynamicswtforms.ui.widgets.impl.StringWidget;
import org.rifidi.dynamicswtforms.ui.widgets.listeners.DynamicSWTWidgetListener;
import org.rifidi.dynamicswtforms.xml.constants.FormData;
import org.rifidi.dynamicswtforms.xml.constants.FormElementType;

public class DynamicSWTForm implements DynamicSWTWidgetListener{

	private Document document;
	private ArrayList<DynamicSWTFormWidget> widgets;
	private boolean displayErrors;
	private Label errorTextLabel;
	private ArrayList<Label> errorIcons;
	private ArrayList<DynamicSWTWidgetListener> listeners;
	private String name;

	public DynamicSWTForm(String xml, boolean displayErrors)
			throws DynamicSWTFormInvalidXMLException {
		SAXBuilder builder = new SAXBuilder();
		Reader reader = new StringReader(xml);
		try {
			this.listeners = new ArrayList<DynamicSWTWidgetListener>();
			this.displayErrors = displayErrors;
			this.errorIcons = new ArrayList<Label>();
			widgets = new ArrayList<DynamicSWTFormWidget>();
			this.document = builder.build(reader);
			this.name = document.getRootElement().getAttributeValue(FormData.NAME.name());
		} catch (JDOMException e) {
			throw new DynamicSWTFormInvalidXMLException(e);
		} catch (IOException e) {
			throw new DynamicSWTFormInvalidXMLException(e);
		}
	}

	public void createControls(Composite parent) {

		Composite formComposite = new Composite(parent, SWT.NONE);
		if (this.displayErrors) {
			GridLayout formCompositeLayout = new GridLayout();
			formCompositeLayout.makeColumnsEqualWidth = false;
			formCompositeLayout.numColumns = 3;
			formComposite.setLayout(formCompositeLayout);

			errorTextLabel = new Label(formComposite, SWT.NONE);
			GridData errorTextGridData = new GridData();
			errorTextGridData.horizontalSpan = 3;
			errorTextLabel.setLayoutData(errorTextGridData);
			errorTextLabel.setVisible(false);
		} else {
			GridLayout formCompositeLayout = new GridLayout();
			formCompositeLayout.makeColumnsEqualWidth = false;
			formCompositeLayout.numColumns = 2;
			formComposite.setLayout(formCompositeLayout);
		}

		List<Element> children = document.getRootElement().getChildren();
		for (Element child : children) {
			// createWidget
			FormElementType type = null;
			DynamicSWTFormWidget widget = null;
			try {
				type = FormElementType.valueOf(child.getName());
			} catch (IllegalArgumentException ex) {
				//TODO: do something here
			}

			if (this.displayErrors) {
				Label imageLabel = new Label(formComposite, SWT.NONE);
				// TODO: remove this and add image manually
				Image x = JFaceResources.getImage("dialog_message_error_image");
				imageLabel.setImage(x);
				imageLabel.setVisible(false);
				errorIcons.add(imageLabel);
			}

			switch (type) {
			case BOOLEAN:
				//widget = new BooleanWidget(new BooleanWidgetData(child));
				break;
			case CHOICE:
				widget = new ChoiceWidget(new ChoiceWidgetData(child));
				break;
			case FLOAT:
				widget = new NumberWidget(new FloatWidgetData(child));
				break;
			case INTEGER:
				widget = new NumberWidget(new IntegerWidgetData(child));
				break;
			case STRING:
				widget = new StringWidget(new StringWidgetData(child));
				break;
			}
			
			this.widgets.add(widget);
			widget.createLabel(formComposite);
			widget.createControl(formComposite);
			widget.addListener(this);

		}

	}

	public String setValue(String widgetName, String value) {
		for(DynamicSWTFormWidget widget : widgets){
			if(widget.getElementName().equalsIgnoreCase(widgetName)){
				return widget.setValue(value);
			}
		}
		return "Form does not contain widget with name " + widgetName;
	}

	public String getValue(String widgetName) {
		for(DynamicSWTFormWidget widget : widgets){
			if(widget.getElementName().equalsIgnoreCase(widgetName)){
				return widget.getValue();
			}
		}
		return null;
	}

	public void setError(String widgetName, String message) {

	}

	public void unsetError(String widgetName) {

	}

	public String validate() {
		for(DynamicSWTFormWidget widget : widgets){
			String s = widget.validate();
			if(s!=null){
				return s;
			}
		}
		return null;
	}
	
	public void addListner(DynamicSWTWidgetListener listener){
		this.listeners.add(listener);
	}
	
	public void removeListner(DynamicSWTWidgetListener listener){
		this.listeners.remove(listener);
	}

	@Override
	public void dataChanged(String newData) {
		for(DynamicSWTWidgetListener l : listeners){
			l.dataChanged(newData);
		}
	}

	@Override
	public void keyReleased() {
		for(DynamicSWTWidgetListener l : listeners){
			l.keyReleased();
		}
		
	}
	
	public Element getXML(){
		Element e = new Element(name);
		for(DynamicSWTFormWidget widget : widgets){
			e.addContent(widget.getXML());
		}
		return e;
	}
	
	public String getXMLAsString(boolean compact){
		Element e = getXML();
		XMLOutputter outputter;
		if(compact){
			outputter = new XMLOutputter(Format.getCompactFormat());
		}else{
			outputter= new XMLOutputter(Format.getPrettyFormat());
		}
		String s = outputter.outputString(e);
		return s;
	}

}
