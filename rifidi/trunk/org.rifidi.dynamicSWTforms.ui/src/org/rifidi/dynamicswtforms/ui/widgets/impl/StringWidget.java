package org.rifidi.dynamicswtforms.ui.widgets.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.rifidi.dynamicswtforms.ui.widgets.AbstractWidget;
import org.rifidi.dynamicswtforms.ui.widgets.data.AbstractWidgetData;
import org.rifidi.dynamicswtforms.ui.widgets.data.StringWidgetData;

public class StringWidget extends AbstractWidget {

	private Text text;
	private Pattern regexPattern;
	private Matcher matcher;
	
	public StringWidget(AbstractWidgetData data) {
		super(data);
		String regexString = ((StringWidgetData)data).getRegex();
		if(regexString!=null){
			regexPattern = Pattern.compile(regexString);
		}
		
	}

	@Override
	public void createControl(Composite parent) {
		text = new Text(parent, SWT.BORDER);
		text.setEditable(data.isEditable());
		text.setText(data.getDefaultValue());
	}

	@Override
	public String getValue() {
		return text.getText();

	}

	@Override
	public String setValue(String value) {
		text.setText(value);
		return null;

	}

	@Override
	public String validate() {
		if (regexPattern != null) {
			if (matcher == null) {
				matcher = regexPattern.matcher(text.getText());
			} else {
				matcher.reset(text.getText());
			}
			if (!matcher.matches()) {
				return data.getDisplayName() + " is invalid";
			}
		}
		return null;
	}

}
