/**
 * 
 */
package org.rifidi.ui.ide.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.rifidi.ui.common.reader.UIReader;

/**
 * This is the wrapperObject you need to use the ReaderEditor. It's holding the
 * UIReader reference.
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class ReaderEditorInput implements IEditorInput {

	/**
	 * This holds the reference to the created reader
	 **/
	private UIReader reader;

	public ReaderEditorInput(UIReader reader) {
		this.reader = reader;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#exists()
	 */
	@Override
	public boolean exists() {
		// Not used because no reason yet
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		// Not used
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getName()
	 */
	@Override
	public String getName() {
		return reader.getReaderName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getPersistable()
	 */
	@Override
	public IPersistableElement getPersistable() {
		// Not yet supported
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getToolTipText()
	 */
	@Override
	public String getToolTipText() {
		return "Reader " + reader.getReaderName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter(Class adapter) {
		// Not needed
		return null;
	}

	/**
	 * Get the UIReader associated with this EditorInput
	 * 
	 * @return the UIReader associated with this EditorInput
	 */
	public UIReader getUIReader() {
		return reader;
	}

}
