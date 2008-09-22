/**
 * 
 */
package org.rifidi.dynamicswtforms.ui.widgets.abstractwidgets;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.widgets.Text;
import org.rifidi.dynamicswtforms.ui.widgets.AbstractWidget;
import org.rifidi.dynamicswtforms.ui.widgets.data.AbstractWidgetData;
import org.rifidi.dynamicswtforms.ui.widgets.data.StringWidgetData;

/**
 * @author kyle
 *
 */
public abstract class AbstractStringWidget extends AbstractWidget {

	protected Text text;
	private Pattern regexPattern;
	private Matcher matcher;
	
	/**
	 * @param data
	 */
	public AbstractStringWidget(AbstractWidgetData data) {
		super(data);
		String regexString = ((StringWidgetData)data).getRegex();
		if(regexString!=null){
			regexPattern = Pattern.compile(regexString);
		}
	}

	/* (non-Javadoc)
	 * @see org.rifidi.dynamicswtforms.ui.widgets.DynamicSWTFormWidget#disable()
	 */
	@Override
	public void disable() {
		this.text.setEnabled(false);

	}

	/* (non-Javadoc)
	 * @see org.rifidi.dynamicswtforms.ui.widgets.DynamicSWTFormWidget#enable()
	 */
	@Override
	public void enable() {
		if(data.isEditable()){
			this.text.setEnabled(true);
		}
	}

	/* (non-Javadoc)
	 * @see org.rifidi.dynamicswtforms.ui.widgets.DynamicSWTFormWidget#getValue()
	 */
	@Override
	public String getValue() {
		return text.getText();
	}

	/* (non-Javadoc)
	 * @see org.rifidi.dynamicswtforms.ui.widgets.DynamicSWTFormWidget#setValue(java.lang.String)
	 */
	@Override
	public String setValue(String value) {
		text.setText(value);
		return null;
	}

	/* (non-Javadoc)
	 * @see org.rifidi.dynamicswtforms.ui.widgets.DynamicSWTFormWidget#validate()
	 */
	@Override
	public String validate() {
		if (regexPattern != null) {
			if (matcher == null) {
				matcher = regexPattern.matcher(text.getText());
			} else {
				matcher.reset(text.getText());
			}
			if (!matcher.matches()) {
				return data.getDisplayName() + " is invalid";
			}
		}
		return null;
	}

}
