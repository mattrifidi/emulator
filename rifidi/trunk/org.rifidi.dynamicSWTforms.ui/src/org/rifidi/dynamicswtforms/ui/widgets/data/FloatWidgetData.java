package org.rifidi.dynamicswtforms.ui.widgets.data;

import org.jdom.Element;
import org.rifidi.dynamicswtforms.xml.constants.FormElementData;

public class FloatWidgetData extends AbstractWidgetData {

	public FloatWidgetData(Element element) {
		super(element);
	}

	public Float maxValue(){
		return Float.valueOf(element.getChildText(FormElementData.MAX.name()));
	}
	
	public Float minValue(){
		return Float.valueOf(element.getChildText(FormElementData.MIN.name()));
	}
	
	public int getNumDecimalPlaces() {
		return Integer.valueOf(element
				.getChildText(FormElementData.DECIMAL_PLACES.name()));
	}

}
