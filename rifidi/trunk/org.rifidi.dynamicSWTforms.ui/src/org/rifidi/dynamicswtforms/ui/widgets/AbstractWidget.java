package org.rifidi.dynamicswtforms.ui.widgets;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jdom.Element;
import org.rifidi.dynamicswtforms.ui.widgets.data.AbstractWidgetData;
import org.rifidi.dynamicswtforms.ui.widgets.listeners.DynamicSWTWidgetListener;

public abstract class AbstractWidget implements DynamicSWTFormWidget {

	private ArrayList<DynamicSWTWidgetListener> listners;
	protected AbstractWidgetData data;
	

	public AbstractWidget(AbstractWidgetData data) {
		this.data = data;
		listners = new ArrayList<DynamicSWTWidgetListener>();
	}

	@Override
	public String getElementName() {
		return data.getName();
	}

	@Override
	public void createLabel(Composite parent) {
		Label label = new Label(parent, SWT.None);
		label.setText(data.getDisplayName());

	}

	@Override
	public void addListener(DynamicSWTWidgetListener listener) {
		this.listners.add(listener);

	}

	@Override
	public void removeListener(DynamicSWTWidgetListener listener) {
		this.listners.remove(listener);

	}
	
	@Override
	public Element getXML(){
		Element e = new Element(data.getName());
		e.setText(this.getValue());
		return e;
	}
	
	protected void notifyListenersDataChanged(String data) {
		for (DynamicSWTWidgetListener l : listners) {
			l.dataChanged(data);
		}

	}
	
	protected void notifyListenersKeyReleased(){
		for (DynamicSWTWidgetListener l : listners) {
			l.keyReleased();
		}
	}

}
