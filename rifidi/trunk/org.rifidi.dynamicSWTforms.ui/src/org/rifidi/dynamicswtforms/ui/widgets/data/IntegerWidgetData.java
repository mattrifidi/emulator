package org.rifidi.dynamicswtforms.ui.widgets.data;

import org.jdom.Element;
import org.rifidi.dynamicswtforms.xml.constants.FormElementData;

public class IntegerWidgetData extends AbstractWidgetData {

	public IntegerWidgetData(Element element) {
		super(element);
	}
	
	public int maxValue(){
		return Integer.parseInt(element.getChildText(FormElementData.MAX.name()));
	}
	
	public int minValue(){
		return Integer.parseInt(element.getChildText(FormElementData.MIN.name()));
	}

}
