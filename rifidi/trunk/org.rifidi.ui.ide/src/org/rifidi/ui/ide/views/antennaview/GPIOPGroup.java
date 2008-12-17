package org.rifidi.ui.ide.views.antennaview;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.nebula.widgets.pgroup.PGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.common.reader.callback.GPOEventCallbackInterface;
import org.rifidi.ui.ide.Activator;
import org.rifidi.ui.ide.views.antennaview.exception.RifidiIndexDoesNotMatchException;

/**
 * GPIO View to display the actual GPIO's of a reader
 * 
 * @author Dan West - dan@pramari.com
 * @author Matthew Dean - matt@pramari.com
 */
public class GPIOPGroup extends PGroup implements GPOEventCallbackInterface {
	
	//TODO: Find an OSGI solution to referencing these images
	private static final String RED_FILENAME = "icons/red_16.png";
	private static final String GREEN_FILENAME = "icons/green_16.png";

	private static final Image RED_LED = Activator.getImageDescriptor(RED_FILENAME).createImage();
	private static final Image GREEN_LED = Activator.getImageDescriptor(GREEN_FILENAME).createImage();

	private Log logger = LogFactory.getLog(GPIOPGroup.class);

	private UIReader uiReader;

	private Display display;

	private ArrayList<Button> gpiButtonList = new ArrayList<Button>();
	private ArrayList<Label> gpoButtonList = new ArrayList<Label>();

	/**
	 * @param parent
	 * @param style
	 * @throws RifidiIndexDoesNotMatchException
	 */
	public GPIOPGroup(Composite parent, int style, UIReader reader)
			throws RifidiIndexDoesNotMatchException {
		super(parent, style);

		setText("GPIO Settings");
		setLayout(new GridLayout());
		this.uiReader = reader;
		display = parent.getShell().getDisplay();

		List<String> gpiStringList = reader.getReaderManager().getGPIList(
				reader.getNumGPIs());
		List<String> gpoStringList = reader.getReaderManager().getGPOList(
				reader.getNumGPOs());

		if (gpiStringList.size() != reader.getNumGPIs()
				|| gpoStringList.size() != reader.getNumGPOs()) {
			throw new RifidiIndexDoesNotMatchException();
		}

		// register Reader for GPIO events
		reader.getReaderCallbackManager().addGPOPortListener(this);

		Scroller scroller = new Scroller(this, SWT.H_SCROLL);
		scroller.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// entire base composite
		Composite base = new Composite(scroller, SWT.NONE);
		base.setLayoutData(new GridData(SWT.FILL | SWT.CENTER, SWT.FILL
				| SWT.CENTER, true, true));
		base.setLayout(new GridLayout(2, false));

		// base.setBackgroundImage(Activator.getImageDescriptor(
		// "icons/add-16x16.png").createImage());

		// gpi buttons label
		Label gpiLabel = new Label(base, SWT.NONE);
		gpiLabel
				.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		gpiLabel.setText("GPIs:");

		// composite for storing gpi buttons
		Composite gpiButtons = new Composite(base, SWT.NONE);
		gpiButtons.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,
				true));
		gpiButtons.setLayout(new GridLayout(reader.getNumGPIs(), true));

		// create gpi buttons
		for (int i = 0; i < reader.getNumGPIs(); i++) {
			Composite buttonComp = new Composite(gpiButtons, SWT.NONE);
			buttonComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					true));
			buttonComp.setLayout(new GridLayout(1, false));

			Label label = new Label(buttonComp, SWT.NONE);
			label
					.setLayoutData(new GridData(SWT.CENTER, SWT.NONE, false,
							false));
			if (gpiStringList == null) {
				label.setText(String.valueOf(i));
			} else {
				label.setText(gpiStringList.get(i));
			}

			Button check = new Button(buttonComp, SWT.CENTER | SWT.CHECK);
			check.setData(i);

			gpiButtonList.add(check);

			check.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (((Button) e.widget).getSelection()) {
						try {
							uiReader.getReaderManager().setGPIHigh(
									(Integer) e.widget.getData());
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					} else {
						try {
							uiReader.getReaderManager().setGPILow(
									(Integer) e.widget.getData());
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			});
		}

		// gpo buttons label
		Label gpoLabel = new Label(base, SWT.NONE);
		gpoLabel
				.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		gpoLabel.setText("GPOs:");

		// composite for storing gpo buttons
		Composite gpoButtons = new Composite(base, SWT.NONE);
		gpoButtons.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false));
		gpoButtons.setLayout(new GridLayout(reader.getNumGPOs(), true));

		// create gpo buttons
		for (int i = 0; i < reader.getNumGPOs(); i++) {
			Composite buttonComp = new Composite(gpoButtons, SWT.NONE);
			buttonComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					true));
			buttonComp.setLayout(new GridLayout(1, false));

			Label label = new Label(buttonComp, SWT.CENTER);
			label
					.setLayoutData(new GridData(SWT.CENTER, SWT.NONE, false,
							false));
			if (gpoStringList == null) {
				label.setText(String.valueOf(i));
			} else {
				label.setText(gpoStringList.get(i));
			}

			Label image_label = new Label(buttonComp, SWT.IMAGE_PNG);
			image_label.setImage(RED_LED);
			gpoButtonList.add(image_label);
		}

		base.pack();
		scroller.setWidget(base);
		scroller.pack();
		this.pack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.ui.common.reader.callback.GPOEventCallbackInterface#GPOPortSetHigh
	 * (int)
	 */
	public void GPOPortSetHigh(final int gpoPortNum) {
		display.syncExec(new Runnable() {
			public void run() {
				logger.debug("GPO PORT " + gpoPortNum
						+ " WAS SET TO HIGH IN THE IDE");

				gpoButtonList.get(gpoPortNum).setImage(GREEN_LED);

				gpoButtonList.get(gpoPortNum).redraw();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.ui.common.reader.callback.GPOEventCallbackInterface#GPOPortSetLow
	 * (int)
	 */
	public void GPOPortSetLow(final int gpoPortNum) {
		display.syncExec(new Runnable() {
			public void run() {
				logger.debug("GPO PORT " + gpoPortNum
						+ " WAS SET TO LOW IN THE IDE");
				gpoButtonList.get(gpoPortNum).setImage(RED_LED);

				gpoButtonList.get(gpoPortNum).redraw();
			}
		});
	}
}