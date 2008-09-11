package org.rifidi.dynamicswtforms.ui.widgets.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.rifidi.dynamicswtforms.ui.widgets.AbstractWidget;
import org.rifidi.dynamicswtforms.ui.widgets.data.AbstractWidgetData;
import org.rifidi.dynamicswtforms.ui.widgets.data.StringWidgetData;

public class StringWidget extends AbstractWidget {

	private Text text;
	private Pattern regexPattern;
	private Matcher matcher;
	private boolean dirty;
	
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
		
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				dirty = true;

			}

		});

		text.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void focusLost(FocusEvent e) {
				if (dirty == true) {
					dirty = false;
					notifyListenersDataChanged(text.getText());
				}

			}

		});

		text.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.character == SWT.CR) {
					if (dirty == true) {
						dirty = false;
						notifyListenersDataChanged(text.getText());
					}
				}
				else notifyListenersKeyReleased();
			}
		});
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

	@Override
	public void disable() {
		this.text.setEditable(false);
		
	}

	@Override
	public void enable() {
		if(data.isEditable()){
			this.text.setEditable(true);
		}
		
	}

}
