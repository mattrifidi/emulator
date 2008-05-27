package org.rifidi.ui.streamer.data;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.rifidi.ui.common.registry.RegistryChangeListener;

public class EventAwareContentProvider implements IStructuredContentProvider,
		RegistryChangeListener {

	private Viewer viewer;

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof EventAwareWrapper) {
			EventAwareWrapper wrapper = (EventAwareWrapper) inputElement;
			return wrapper.getList().toArray();
		}
		return null;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (viewer != null) {
			this.viewer = viewer;
		}
		if (newInput instanceof EventAwareWrapper) {
			((EventAwareWrapper) newInput).addListener(this);
		}
		if (oldInput != null) {
			((EventAwareWrapper) oldInput).removeListener(this);
		}
	}

	@Override
	public void add(Object event) {
		if (viewer != null) {
			IStructuredSelection selection = new StructuredSelection(event);
			viewer.refresh();
			viewer.setSelection(selection, true);
		}
	}

	@Override
	public void remove(Object event) {
		if (viewer != null)
			viewer.refresh();
	}

	@Override
	public void update(Object event) {
		if (viewer != null)
			viewer.refresh();
	}

}
