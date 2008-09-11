package org.rifidi.dynamicswtforms.ui.widgets.data;

import org.jdom.Element;
import org.rifidi.dynamicswtforms.xml.constants.FormElementData;
import org.rifidi.dynamicswtforms.xml.constants.FormElementType;

public class AbstractWidgetData {
	
	protected Element element;

	public AbstractWidgetData(Element element){
		this.element = element;
	}
	
	public String getName() {
		return element.getChildText(FormElementData.ELEMENT_NAME.name());
	}

	public String getDisplayName() {
		return element.getChildText(FormElementData.DISPLAY_NAME.name());
	}


	public String getDefaultValue() {
		return element.getChildText(FormElementData.DEFAULT_VALUE.name());
	}
	
	public boolean isEditable(){
		String editable = element.getChildText(FormElementData.EDITABLE.name());
		return editable.equalsIgnoreCase("true");
	}
	
	public FormElementType getType(){
		return FormElementType.valueOf(element.getName());
	}

}
