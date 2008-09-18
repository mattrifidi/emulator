package org.rifidi.dynamicswtforms.ui.widgets.standard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jdom.Element;
import org.rifidi.dynamicswtforms.ui.exceptions.DynamicSWTFormInvalidXMLException;
import org.rifidi.dynamicswtforms.ui.form.AbstractDynamicSWTForm;
import org.rifidi.dynamicswtforms.ui.widgets.DynamicSWTFormWidget;
import org.rifidi.dynamicswtforms.ui.widgets.data.ChoiceWidgetData;
import org.rifidi.dynamicswtforms.ui.widgets.data.FloatWidgetData;
import org.rifidi.dynamicswtforms.ui.widgets.data.IntegerWidgetData;
import org.rifidi.dynamicswtforms.ui.widgets.data.StringWidgetData;
import org.rifidi.dynamicswtforms.ui.widgets.standard.impl.ChoiceWidget;
import org.rifidi.dynamicswtforms.ui.widgets.standard.impl.NumberWidget;
import org.rifidi.dynamicswtforms.ui.widgets.standard.impl.StringWidget;
import org.rifidi.dynamicswtforms.xml.constants.FormElementType;

public class StandardDynamicSWTForm extends AbstractDynamicSWTForm {

	private boolean displayErrors;
	
	private ArrayList<Label> errorIcons;
	
	public StandardDynamicSWTForm(Element formRoot, boolean displayErrors) {
		super(formRoot);
		this.displayErrors = displayErrors;
		this.errorIcons = new ArrayList<Label>();
	}
	
	public StandardDynamicSWTForm(String xml, boolean displayErrors) throws DynamicSWTFormInvalidXMLException{
		super(xml);
		this.displayErrors = displayErrors;
		this.errorIcons = new ArrayList<Label>();
	}
	
	
	public void createControls(Composite parent) {

		Composite formComposite = new Composite(parent, SWT.NONE);
		GridLayout formCompositeLayout = new GridLayout(1, false);
		formCompositeLayout.marginHeight=0;
		formComposite.setLayout(formCompositeLayout);
		
		Composite widgetCompsoite;
		
		if (this.displayErrors) {		
			widgetCompsoite = new Composite(formComposite, SWT.NONE);
			GridLayout widgetCompositeLayout = new GridLayout();
			widgetCompositeLayout.makeColumnsEqualWidth = false;
			widgetCompositeLayout.numColumns = 3;
			widgetCompsoite.setLayout(widgetCompositeLayout);


		} else {
			widgetCompsoite = new Composite(formComposite, SWT.NONE);
			GridLayout widgetCompositeLayout = new GridLayout();
			widgetCompositeLayout.makeColumnsEqualWidth = false;
			widgetCompositeLayout.numColumns = 2;
			if(formRoot.getChildren().size()==0){
				widgetCompositeLayout.verticalSpacing=0;
				widgetCompositeLayout.marginHeight=1;
			}
			widgetCompsoite.setLayout(widgetCompositeLayout);
		}

		List<Element> children = formRoot.getChildren();
		for (Element child : children) {
			// createWidget
			FormElementType type = null;
			DynamicSWTFormWidget widget = null;
			try {
				type = FormElementType.valueOf(child.getName());
			} catch (IllegalArgumentException ex) {
				// TODO: do something here
			}

			if (this.displayErrors) {
				Label imageLabel = new Label(widgetCompsoite, SWT.NONE);
				// TODO: remove this and add image manually
				Image x = JFaceResources.getImage("dialog_message_error_image");
				imageLabel.setImage(x);
				imageLabel.setVisible(false);
				errorIcons.add(imageLabel);
			}

			switch (type) {
			case BOOLEAN:
				// widget = new BooleanWidget(new BooleanWidgetData(child));
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
			widget.createLabel(widgetCompsoite);
			widget.createControl(widgetCompsoite);
			widget.addListener(this);

		}

	}
	
	public void setError(String widgetName, String message) {
		for(DynamicSWTFormWidget w : widgets){
			if(w.getElementName().equalsIgnoreCase(widgetName)){
				errorIcons.get(widgets.indexOf(w)).setVisible(true);
			}
		}
	}

	public void unsetError(String widgetName) {
		for(DynamicSWTFormWidget w : widgets){
			if(w.getElementName().equalsIgnoreCase(widgetName)){
				errorIcons.get(widgets.indexOf(w)).setVisible(false);
			}
		}
	}

}
