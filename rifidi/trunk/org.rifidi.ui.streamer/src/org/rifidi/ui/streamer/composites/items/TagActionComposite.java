package org.rifidi.ui.streamer.composites.items;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.rifidi.emulator.tags.enums.TagGen;
import org.rifidi.emulator.tags.id.TagType;
import org.rifidi.streamer.xml.actions.TagAction;

/**
 * @author Andreas Huebner - andreas@pramari.com
 *
 */
public class TagActionComposite extends Composite {

	private TagAction tagAction;
	private Spinner execDurationSpinner;
	private Spinner numberSpinner;
	private Text prefixText;
	private Combo tagGenCombo;
	private Combo tagTypeCombo;
	private ArrayList<String> tagGenValues;
	private ArrayList<String> tagTypeValues;
	private Button regenerateCheckbox;

	public TagActionComposite(Composite parent, int style, TagAction tagAction) {
		super(parent, style);

		this.tagAction = tagAction;

		setLayout(new GridLayout(2, false));

		Label execDurationLabel = new Label(this, SWT.NONE);
		execDurationLabel.setText("Excecute duration (ms):");
		execDurationSpinner = new Spinner(this, SWT.BORDER);
		execDurationSpinner.setMaximum(1000000000);

		Label numberLabel = new Label(this, SWT.NONE);
		numberLabel.setText("Number of Tags:");
		numberSpinner = new Spinner(this, SWT.BORDER);
		numberSpinner.setMaximum(100000);

		Label tagGenLabel = new Label(this, SWT.NONE);
		tagGenLabel.setText("Tag generation:");
		tagGenCombo = new Combo(this, SWT.NONE);

		Label tagTypeLabel = new Label(this, SWT.NONE);
		tagTypeLabel.setText("Tag type:");
		tagTypeCombo = new Combo(this, SWT.NONE);

		Label prefixLabel = new Label(this, SWT.NONE);
		prefixLabel.setText("Tag prefix:");
		prefixText = new Text(this, SWT.BORDER);
		prefixText.setTextLimit(5);
		prefixText.setEnabled(false);
		// Set the size of the Text Widget
		int characters = 6; // should be 5 but 6 because of average size
		GC gc = new GC(prefixText);
		FontMetrics fm = gc.getFontMetrics();
		int width = characters * fm.getAverageCharWidth();
		int height = fm.getHeight();
		gc.dispose();
		GridData prefixTextGridData = new GridData(width, height);
		prefixText.setLayoutData(prefixTextGridData);

		Label regenerateLabel = new Label(this, SWT.NONE);
		regenerateLabel.setText("regenerate Tags:");
		regenerateCheckbox = new Button(this, SWT.CHECK);
		regenerateCheckbox.setEnabled(false);
		regenerateCheckbox.setToolTipText("This feature is not yet available");

		Button saveButton = new Button(this, SWT.PUSH);
		saveButton.setText("Save");
		saveButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				saveChanges();
			}
		});

		tagTypeCombo.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				if (tagTypeCombo.getText().equals(TagType.CustomEPC96.name())) {
					prefixText.setEnabled(true);
				} else
					prefixText.setEnabled(false);

			}
		});

		prefixText.addVerifyListener(new VerifyListener() {

			@Override
			public void verifyText(VerifyEvent e) {
				e.doit = "0123456789abcdef".indexOf(e.text.toLowerCase()) >= 0;
			}

		});

		fillComboWidgets();

		updateWidgets();
	}

	private void fillComboWidgets() {
		tagGenValues = new ArrayList<String>();
		for (TagGen tagGen : TagGen.values()) {
			tagGenValues.add(tagGen.name());
		}
		tagGenCombo.setItems(tagGenValues.toArray(new String[tagGenValues
				.size()]));

		tagTypeValues = new ArrayList<String>();
		for (TagType tagType : TagType.values()) {
			tagTypeValues.add(tagType.name());
		}
		tagTypeCombo.setItems(tagTypeValues.toArray(new String[tagTypeValues
				.size()]));
	}

	private void updateWidgets() {
		execDurationSpinner.setSelection((int) tagAction.getExecDuration());
		numberSpinner.setSelection(tagAction.getNumber());
		if (tagAction.getPrefix() != null)
			prefixText.setText(tagAction.getPrefix());
		if (tagAction.getTagGen() != null)
			tagGenCombo.select(tagGenValues.indexOf(tagAction.getTagGen()
					.name()));
		if (tagAction.getTagType() != null)
		{
			if(tagAction.getTagType() == TagType.CustomEPC96)
				prefixText.setEnabled(true);
			tagTypeCombo.select(tagTypeValues.indexOf(tagAction.getTagType()
					.name()));
		}
		regenerateCheckbox.setSelection(tagAction.isRegenerate());
	}

	private void saveChanges() {
		tagAction.setExecDuration(new Integer(execDurationSpinner
				.getSelection()).longValue());
		tagAction.setNumber(numberSpinner.getSelection());
		tagAction.setPrefix(prefixText.getText());
		if (!tagGenCombo.getText().isEmpty())
			tagAction.setTagGen(TagGen.valueOf(tagGenCombo.getText()));
		if (!tagTypeCombo.getText().isEmpty())
			tagAction.setTagType(TagType.valueOf(tagTypeCombo.getText()));
	}

}
