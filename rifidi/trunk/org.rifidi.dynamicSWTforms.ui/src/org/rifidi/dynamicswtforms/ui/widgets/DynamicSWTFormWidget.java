package org.rifidi.dynamicswtforms.ui.widgets;

import org.eclipse.swt.widgets.Composite;
import org.jdom.Element;
import org.rifidi.dynamicswtforms.ui.widgets.listeners.DynamicSWTWidgetListener;

public interface DynamicSWTFormWidget {
	
	public void createLabel(Composite parent);
	
	public void createControl(Composite parent);
	
	public String getElementName();
	
	public String setValue(String value);
	
	public String getValue();
	
	public String validate();
	
	public Element getXML();
	
	public void addListener(DynamicSWTWidgetListener listener);
	
	public void removeListener(DynamicSWTWidgetListener listener);
	
}
