package org.rifidi.designer.library.basemodels.conveyor90;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class Conveyor90EntityWizardPage extends WizardPage {
	
	private Composite pageComposite;

	private Text nameText;
	private Text speedText;
	
	public Conveyor90EntityWizardPage(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	public Conveyor90EntityWizardPage(String pageName) {
		super(pageName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		parent.setLayout(new FillLayout());
		pageComposite = new Composite(parent, SWT.NONE);
		GridLayout pageLayout = new GridLayout();
		pageLayout.numColumns = 2;
		pageComposite.setLayout(pageLayout);
		GridData layoutData = new GridData();
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.widthHint = 100;

		GridData grabTwo = new GridData();
		grabTwo.horizontalSpan=2;
		grabTwo.grabExcessHorizontalSpace = true;
		grabTwo.widthHint = 200;
		
		Label label=new Label(pageComposite,SWT.None);
		label.setText("Name");
		nameText = new Text(pageComposite, SWT.SINGLE | SWT.BORDER);
		nameText.setLayoutData(layoutData);

		label=new Label(pageComposite,SWT.None);
		label.setText("Speed");
		speedText = new Text(pageComposite, SWT.SINGLE | SWT.BORDER);
		speedText.setLayoutData(layoutData);
		speedText.addListener( SWT.Verify, new Listener() {
			public void handleEvent(Event event) {
				String text = event.text;
				char[] chars = new char[text.length()];
				text.getChars(0, chars.length, chars, 0);
				for ( int i = 0; i < chars.length; i++ ) {
					if ( !('0' <= chars[i] && chars[i] <= '9') && chars[i] != '-' && chars[i] != '.' ) {
						event.doit = false;
						return;
					}
				}
			}
		});

		setControl(pageComposite);
	}
	
	public String getName(){
		return nameText.getText();
	}
	
	public Float getSpeed(){
		return Float.valueOf(speedText.getText());
	}

}
