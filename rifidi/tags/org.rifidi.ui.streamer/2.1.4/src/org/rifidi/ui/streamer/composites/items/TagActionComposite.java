package org.rifidi.ui.streamer.composites.items;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.rifidi.streamer.xml.actions.TagAction;
import org.rifidi.tags.factory.TagCreationPattern;
import org.rifidi.ui.streamer.composites.items.dialog.AbstractCustomAddDialog;
import org.rifidi.ui.streamer.data.EventAwareWrapper;

/**
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class TagActionComposite extends Composite {

	private TagAction tagAction;
	private Spinner execDurationSpinner;
	private Button regenerateCheckbox;
	private Composite drawPane;
	private List<TagPatternComposite> tagPatternComposites;

	public TagActionComposite(Composite parent, int style, TagAction tagAction) {
		super(parent, style | SWT.BORDER);

		this.tagAction = tagAction;
		this.tagPatternComposites = new ArrayList<TagPatternComposite>();
		setLayout(new GridLayout(1, true));
		draw();

	}

	private void updateWidgets() {
		execDurationSpinner.setSelection((int) tagAction.getExecDuration());
		regenerateCheckbox.setSelection(tagAction.isRegenerate());
	}

	private void saveChanges() {
		tagAction.setExecDuration(new Integer(execDurationSpinner
				.getSelection()).longValue());
		tagAction.setRegenerate(regenerateCheckbox.getSelection());
		for (TagPatternComposite c : tagPatternComposites) {
			c.saveChanges();
		}
	}

	private void addPattern() {
		for (TagPatternComposite c : tagPatternComposites) {
			c.saveChanges();
		}
		tagPatternComposites.clear();
		TagCreationPattern pattern = new TagCreationPattern();
		tagAction.getTagCreationPattern().add(pattern);
		draw();
		if (this.getParent() instanceof ScrolledComposite) {
			((ScrolledComposite) this.getParent()).setMinSize(this.computeSize(
					SWT.DEFAULT, SWT.DEFAULT));
		}
		this.layout();
	}

	private void remotePattern() {
		for (TagPatternComposite c : tagPatternComposites) {
			c.saveChanges();
		}
		RemovePatternDialog removePatternDialog = new RemovePatternDialog(this.getShell(), "Remove Tag Patterns", null);
		removePatternDialog.open();

	}

	private void draw() {
		if (drawPane != null) {
			drawPane.dispose();
		}

		drawPane = new Composite(this, SWT.NONE);
		drawPane.setLayout(new GridLayout(1, false));

		Composite widgetComposite = new Composite(drawPane, SWT.NONE);
		widgetComposite.setLayout(new GridLayout(2, false));

		Label execDurationLabel = new Label(widgetComposite, SWT.NONE);
		execDurationLabel.setText("Excecute duration (ms):");
		execDurationSpinner = new Spinner(widgetComposite, SWT.NONE);
		execDurationSpinner.setMaximum(1000000000);

		Label regenerateLabel = new Label(widgetComposite, SWT.NONE);
		regenerateLabel.setText("regenerate Tags:");
		regenerateCheckbox = new Button(widgetComposite, SWT.CHECK);
		regenerateCheckbox.setEnabled(false);
		regenerateCheckbox.setToolTipText("This feature is not yet available");

		if (tagAction.getTagCreationPattern() == null) {
			tagAction
					.setTagCreationPattern(new ArrayList<TagCreationPattern>());
		}

		Composite patternsComposite = new Composite(drawPane, SWT.NONE);
		patternsComposite.setLayout(new RowLayout());

		int i = 1;
		for (TagCreationPattern pattern : tagAction.getTagCreationPattern()) {
			tagPatternComposites.add(new TagPatternComposite(patternsComposite,
					SWT.NONE, pattern, i++));
		}

		Composite buttonComposite = new Composite(drawPane, SWT.NONE);
		buttonComposite.setLayout(new RowLayout());

		Button addPatternButton = new Button(buttonComposite, SWT.PUSH);
		addPatternButton.setText("Add Pattern");
		addPatternButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				addPattern();

			}

		});

		Button removePatternButton = new Button(buttonComposite, SWT.PUSH);
		removePatternButton.setText("Remove Pattern");
		removePatternButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				remotePattern();

			}

		});

		Button saveButton = new Button(buttonComposite, SWT.PUSH);
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

		updateWidgets();
	}
	
	private class RemovePatternDialog extends AbstractCustomAddDialog{

		Button[] buttons;
		
		public RemovePatternDialog(Shell parent, String title,
				EventAwareWrapper input) {
			super(parent, title, input);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void createControls(Composite parent) {
			parent.setLayout(new GridLayout(2, false));
			Composite c = new Composite(parent, SWT.BORDER);
			c.setLayout(new GridLayout(4, false));
			GridData gd = new GridData();
			gd.horizontalSpan =2;
			gd.horizontalAlignment =SWT.FILL;
			c.setLayoutData(gd);
			buttons = new Button[tagAction.getTagCreationPattern().size()];
			for(int i=0; i<tagAction.getTagCreationPattern().size(); i++){
				buttons[i] = new Button(c, SWT.CHECK);
				buttons[i].setText("Pattern " + (i+1));
			}
		}

		@Override
		public void saveChanges() {
			ArrayList<TagCreationPattern> patternsToRemove = new ArrayList<TagCreationPattern>();
			for(int i=0;i<buttons.length; i++){
				if (buttons[i].getSelection()){
					System.out.println("removing " + i);
					patternsToRemove.add(tagAction.getTagCreationPattern().get(i));
				}
			}
			tagAction.getTagCreationPattern().removeAll(patternsToRemove);
			
			tagPatternComposites.clear();
			draw();
			if (getParent() instanceof ScrolledComposite) {
				((ScrolledComposite) getParent()).setMinSize(computeSize(
						SWT.DEFAULT, SWT.DEFAULT));
			}
			layout();
			
		}
		
	}

}
