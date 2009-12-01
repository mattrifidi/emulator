package org.rifidi.prototyper.map.view.figures;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.rifidi.prototyper.map.Activator;
import org.rifidi.prototyper.model.HotspotViewModel;
import org.rifidi.ui.common.reader.UIReader;

public class HotspotFigure extends RectangleFigure implements PropertyChangeListener {
	private HotspotViewModel model;
	private GridLayout layout;
	private Color defaultColor;
	private UIReader uireader;
	private ImageFigure antennaImageFig;

	public HotspotFigure(HotspotViewModel model, UIReader uireader) {
		this.model = model;
		this.uireader = uireader;
		uireader.addPropertyChangeListener(this);
		layout = new GridLayout(2, false);
		setLayoutManager(layout);
		setBorder(new LineBorder(ColorConstants.black, 1));
		setAlpha(175);
		if (uireader.getReaderState().equals(UIReader.STATE_RUNNING)) {
			antennaImageFig = new ImageFigure(Activator.getDefault()
					.getImageRegistry().get(Activator.IMG_ANT_ON));
		} else {
			antennaImageFig = new ImageFigure(Activator.getDefault()
					.getImageRegistry().get(Activator.IMG_ANT_OFF));
		}
		add(antennaImageFig);

		RectangleFigure labelPanel = new RectangleFigure();
		labelPanel.setAlpha(0);
		labelPanel.setLayoutManager(new ToolbarLayout(false));

		Font font = new Font(null, "Arial", 11, SWT.None);
		Label readZoneLabel = new Label("Read Zone");
		readZoneLabel.setFont(font);
		labelPanel.add(readZoneLabel);

		Label readerNameLabel = new Label("Reader: " + model.getName());
		Font smallFont = new Font(null, "Arial", 9, SWT.None);
		readerNameLabel.setFont(smallFont);
		labelPanel.add(readerNameLabel);

		Label antennaLabel = new Label("Antenna: " + model.getAntennaID());
		antennaLabel.setFont(smallFont);
		labelPanel.add(antennaLabel);

		GridData layoutdata = new GridData(SWT.CENTER, SWT.TOP, true, true);
		add(labelPanel, layoutdata);

		setLocation(new Point(model.getX(), model.getY()));
		setMinimumSize(new Dimension(model.getMinimumWidth(), model
				.getMinimumHeight()));
		defaultColor = getBackgroundColor();
	}

	public Rectangle getConstraint() {
		int width = Math.max(getPreferredSize().width + 20, model
				.getMinimumWidth());
		int height = Math.max(getPreferredSize().height, model
				.getMinimumHeight());
		setMinimumSize(new Dimension(width, height));
		return new Rectangle(getLocation(), getMinimumSize());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "hotspot : " + model.getName();
	}

	public void move(Point toPoint) {
		Point p = this.getLocation();
		model.setX(toPoint.x);
		model.setY(toPoint.y);
		Dimension delta = toPoint.getDifference(p);
		super.translate(delta.width, delta.height);
		this.getParent().repaint();
		this.getParent().setConstraint(this, getConstraint());

	}

	public void select() {
		setBackgroundColor(ColorConstants.red);
	}

	public void unselect() {
		setBackgroundColor(defaultColor);
	}

	public String getHashstring() {
		return model.hashString();
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if(arg0.getPropertyName().equals(UIReader.PROP_STATE)){
			if(arg0.getNewValue().equals(UIReader.STATE_STOPPED)){
				antennaImageFig.setImage(Activator.getDefault()
						.getImageRegistry().get(Activator.IMG_ANT_OFF));
			}else{
				antennaImageFig.setImage(Activator.getDefault()
						.getImageRegistry().get(Activator.IMG_ANT_ON));
			}
		}
		
	}
	
	public void itemArrived(ItemFigure itemFigure){
		//uireader.getAntenna(model.getAntennaID()).addTag(tagsToAdd);
	}
	
	public void itemDeparted(ItemFigure itemFigure){
		//uireader.getAntenna(model.getAntennaID()).addTag(tagsToAdd);
	}
}
