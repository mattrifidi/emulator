package org.rifidi.ui.streamer.composites.items;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.rifidi.services.tags.enums.TagGen;
import org.rifidi.services.tags.factory.TagCreationPattern;
import org.rifidi.services.tags.id.TagType;

public class TagPatternComposite extends Composite {

	private TagCreationPattern tagPattern;

	private Spinner numberSpinner;
	private Text prefixText;
	private Combo tagGenCombo;
	private Combo tagTypeCombo;
	private ArrayList<String> tagGenValues;
	private ArrayList<String> tagTypeValues;

	public TagPatternComposite(Composite parent, int style,
			TagCreationPattern tagPattern, int tagPatternNumber) {
		super(parent, style);
		this.tagPattern = tagPattern;
		setLayout(new GridLayout(1, false));

		Group group = new Group(this, SWT.NONE);
		group.setText("Tag Pattern " + tagPatternNumber);
		group.setLayout(new GridLayout(2, false));

		Label numberLabel = new Label(group, SWT.NONE);
		numberLabel.setText("Number of Tags:");
		numberSpinner = new Spinner(group, SWT.BORDER);
		numberSpinner.setMaximum(100000);

		Label tagGenLabel = new Label(group, SWT.NONE);
		tagGenLabel.setText("Tag generation:");
		tagGenCombo = new Combo(group, SWT.NONE);

		Label tagTypeLabel = new Label(group, SWT.NONE);
		tagTypeLabel.setText("Tag type:");
		tagTypeCombo = new Combo(group, SWT.NONE);

		Label prefixLabel = new Label(group, SWT.NONE);
		prefixLabel.setText("Tag prefix:");
		prefixText = new Text(group, SWT.BORDER);
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

		if (tagPattern != null) {
			if (tagPattern.getPrefix() != null) {
				prefixText.setText(tagPattern.getPrefix());
			}
			if (tagPattern.getTagGeneration() != null) {
				tagGenCombo.select(tagGenValues.indexOf(tagPattern
						.getTagGeneration().name()));
			}
			if (tagPattern.getTagType() != null) {
				if (tagPattern.getTagType() == TagType.CustomEPC96) {
					prefixText.setEnabled(true);
				}
				tagTypeCombo.select(tagTypeValues.indexOf(tagPattern
						.getTagType().name()));
			}
			numberSpinner.setSelection(tagPattern.getNumberOfTags());
		}

	}

	public void saveChanges() {
		tagPattern.setNumberOfTags(numberSpinner.getSelection());
		if (!tagGenCombo.getText().isEmpty()) {
			tagPattern.setTagGeneration(TagGen.valueOf(tagGenCombo.getText()));
		}
		if (!tagTypeCombo.getText().isEmpty()) {
			tagPattern.setTagType(TagType.valueOf(tagTypeCombo.getText()));
		}
		tagPattern.setPrefix(prefixText.getText());
	}

}
