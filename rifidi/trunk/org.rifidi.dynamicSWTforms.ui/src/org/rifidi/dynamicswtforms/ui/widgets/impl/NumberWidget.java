package org.rifidi.dynamicswtforms.ui.widgets.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;
import org.rifidi.dynamicswtforms.ui.widgets.AbstractWidget;
import org.rifidi.dynamicswtforms.ui.widgets.data.AbstractWidgetData;
import org.rifidi.dynamicswtforms.ui.widgets.data.FloatWidgetData;
import org.rifidi.dynamicswtforms.ui.widgets.data.IntegerWidgetData;
import org.rifidi.dynamicswtforms.xml.constants.FormElementType;

public class NumberWidget extends AbstractWidget {

	private Spinner spinner;
	private boolean dirty = false;

	public NumberWidget(AbstractWidgetData data) {
		super(data);
	}

	@Override
	public void createControl(Composite parent) {
		spinner = new Spinner(parent, SWT.BORDER);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.minimumWidth = 150;
		spinner.setLayoutData(gridData);

		if (data.getType() == FormElementType.INTEGER) {
			buildIntegerSpinner();
		} else {
			buildFloatSpinner();
		}

		spinner.setEnabled(data.isEditable());

	}

	private void buildIntegerSpinner() {
		IntegerWidgetData intData = (IntegerWidgetData) data;
		if (intData.maxValue() > Integer.MAX_VALUE) {
			spinner.setMaximum(Integer.MAX_VALUE);
		} else {
			spinner.setMaximum(intData.maxValue());
		}

		if (intData.minValue() < Integer.MIN_VALUE) {
			spinner.setMinimum(Integer.MIN_VALUE);
		} else {
			spinner.setMinimum(intData.minValue());
		}

		spinner.setSelection(Integer.parseInt(intData.getDefaultValue()));

		spinner.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				dirty = true;

			}

		});

		spinner.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.character == SWT.CR) {
					if (dirty == true) {
						dirty = false;
						notifyListenersDataChanged(spinner.getText());
					}
				} else
					notifyListenersKeyReleased();

			}

		});
	}

	private void buildFloatSpinner() {
		FloatWidgetData floatData = (FloatWidgetData) data;
		spinner.setDigits(floatData.getNumDecimalPlaces());
		int offset = (int) Math.pow(10, floatData.getNumDecimalPlaces());
		int max = floatData.maxValue().intValue() * offset;
		int min = floatData.minValue().intValue() * offset;
		int value = Integer.valueOf(data.getDefaultValue()) * offset;

		if (max > Integer.MAX_VALUE) {
			spinner.setMaximum(Integer.MAX_VALUE);
		} else {
			spinner.setMaximum(max);
		}

		if (min < Integer.MIN_VALUE) {
			spinner.setMinimum(min);
		} else {
			spinner.setMinimum(min);
		}

		spinner.setSelection(value);
	}

	@Override
	public String getValue() {
		return spinner.getText();

	}

	@Override
	public String setValue(String value) {
		try {
			int intval = Integer.parseInt(value);
			this.spinner.setSelection(intval);

		} catch (NumberFormatException ex) {
			return "Cannot convert " + value + " to integer";

		}
		return null;

	}

	@Override
	public String validate() {
		return null;
	}

	@Override
	public void disable() {
		this.spinner.setEnabled(false);

	}

	@Override
	public void enable() {
		if (this.data.isEditable()) {
			this.spinner.setEnabled(true);
		}

	}

}
