/**
 * 
 */
package org.rifidi.dynamicswtforms.ui.widgets.abstractwidgets;

import java.util.List;

import org.eclipse.swt.widgets.Combo;
import org.rifidi.dynamicswtforms.ui.widgets.AbstractWidget;
import org.rifidi.dynamicswtforms.ui.widgets.data.AbstractWidgetData;
import org.rifidi.dynamicswtforms.ui.widgets.data.ChoiceWidgetData;

/**
 * @author kyle
 *
 */
public abstract class AbstractChoiceWidget extends AbstractWidget {

	protected Combo combo;
	
	/**
	 * @param data
	 */
	public AbstractChoiceWidget(AbstractWidgetData data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.rifidi.dynamicswtforms.ui.widgets.DynamicSWTFormWidget#disable()
	 */
	@Override
	public void disable() {
		this.combo.setEnabled(false);

	}

	/* (non-Javadoc)
	 * @see org.rifidi.dynamicswtforms.ui.widgets.DynamicSWTFormWidget#enable()
	 */
	@Override
	public void enable() {
		if (data.isEditable()) {
			this.combo.setEnabled(true);
		}
	}

	/* (non-Javadoc)
	 * @see org.rifidi.dynamicswtforms.ui.widgets.DynamicSWTFormWidget#getValue()
	 */
	@Override
	public String getValue() {
		try {
			return combo.getItem(combo.getSelectionIndex());
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.rifidi.dynamicswtforms.ui.widgets.DynamicSWTFormWidget#setValue(java.lang.String)
	 */
	@Override
	public String setValue(String value) {
		List<String> choices = ((ChoiceWidgetData) data).possibleChoices();

		int index = choices.indexOf(value);
		if (index == -1) {
			return value + " is not a valid choice";
		}
		combo.select(choices.indexOf(value));
		return null;
	}

	/* (non-Javadoc)
	 * @see org.rifidi.dynamicswtforms.ui.widgets.DynamicSWTFormWidget#validate()
	 */
	@Override
	public String validate() {
		return null;
	}

}
