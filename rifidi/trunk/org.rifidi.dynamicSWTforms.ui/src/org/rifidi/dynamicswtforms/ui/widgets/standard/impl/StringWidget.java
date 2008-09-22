package org.rifidi.dynamicswtforms.ui.widgets.standard.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.rifidi.dynamicswtforms.ui.widgets.abstractwidgets.AbstractStringWidget;
import org.rifidi.dynamicswtforms.ui.widgets.data.AbstractWidgetData;

public class StringWidget extends AbstractStringWidget {

	private boolean dirty;
	
	public StringWidget(AbstractWidgetData data) {
		super(data);
	}

	@Override
	public void createControl(Composite parent) {
		text = new Text(parent, SWT.MULTI|SWT.BORDER|SWT.WRAP);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace=true;
		gridData.widthHint=150;
		text.setLayoutData(gridData);
		text.setEditable(data.isEditable());
		text.setText(data.getDefaultValue());
		
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				dirty = true;

			}

		});
		
		text.addVerifyListener(new VerifyListener(){

			@Override
			public void verifyText(VerifyEvent e) {
				if(e.character==SWT.CR){
					if (dirty == true) {
						dirty = false;
						notifyListenersDataChanged(text.getText());
					}
					e.doit=false;
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
				if (e.character != SWT.CR) {
					notifyListenersKeyReleased();
				}

			}
		});
	}

}
