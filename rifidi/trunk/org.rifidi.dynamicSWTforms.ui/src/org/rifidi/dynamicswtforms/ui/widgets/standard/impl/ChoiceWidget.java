package org.rifidi.dynamicswtforms.ui.widgets.standard.impl;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.rifidi.dynamicswtforms.ui.widgets.abstractwidgets.AbstractChoiceWidget;
import org.rifidi.dynamicswtforms.ui.widgets.data.AbstractWidgetData;
import org.rifidi.dynamicswtforms.ui.widgets.data.ChoiceWidgetData;

public class ChoiceWidget extends AbstractChoiceWidget {

	private boolean dirty;

	public ChoiceWidget(AbstractWidgetData data) {
		super(data);
	}

	@Override
	public void createControl(Composite parent) {
		combo = new Combo(parent, SWT.BORDER);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.minimumWidth = 150;
		combo.setLayoutData(gridData);

		List<String> choices = ((ChoiceWidgetData) data).possibleChoices();
		for (String s : choices) {
			combo.add(s);
		}
		combo.select(choices.indexOf(data.getDefaultValue()));
		combo.setEnabled(data.isEditable());

		combo.addSelectionListener(new SelectionListener() {
			@Override
			// This method is called when enter is pressed
			public void widgetDefaultSelected(SelectionEvent e) {
				dirty = true;
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				dirty = true;
			}
		});

		combo.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.character == SWT.CR) {
					if (dirty == true) {
						dirty = false;
						notifyListenersDataChanged(combo.getItem(combo
								.getSelectionIndex()));
					}
				} else
					notifyListenersKeyReleased();

			}

		});

	}

}
