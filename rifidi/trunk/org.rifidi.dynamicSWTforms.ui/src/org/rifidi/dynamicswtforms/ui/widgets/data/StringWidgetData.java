package org.rifidi.dynamicswtforms.ui.widgets.data;

import org.jdom.Element;
import org.rifidi.dynamicswtforms.xml.constants.FormElementData;

public class StringWidgetData extends AbstractWidgetData {
	
	public StringWidgetData(Element element) {
		super(element);
	}
	
	public String getRegex(){
		return element.getChildText(FormElementData.REGEX.name());
	}

}
