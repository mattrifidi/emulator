package org.rifidi.ui.streamer.data;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;

/**
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class ReaderCompositeContentProvider implements
		IStructuredContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof GeneralReaderPropertyHolder) {
			return ((GeneralReaderPropertyHolder) inputElement).getPropertiesMap().entrySet()
					.toArray();
		}
		return null;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		viewer.refresh();
	}

}
