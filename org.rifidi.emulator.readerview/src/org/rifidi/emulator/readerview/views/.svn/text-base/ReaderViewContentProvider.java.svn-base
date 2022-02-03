/**
 * 
 */
package org.rifidi.emulator.readerview.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Map;

import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.rifidi.ui.common.reader.UIAntenna;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.common.registry.ReaderRegistryService;
import org.rifidi.ui.common.registry.RegistryChangeListener;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class ReaderViewContentProvider implements ITreeContentProvider,
		RegistryChangeListener, PropertyChangeListener {

	/** The Viewer that this content provider works with */
	private AbstractTreeViewer treeViewer;
	/** A registry of UIReader */
	private ReaderRegistryService readerRegistry;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.
	 * Object)
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof ReaderRegistryService) {
			return ((ReaderRegistryService) parentElement).getReaderList()
					.toArray();
		}
		if (parentElement instanceof UIReader) {
			UIReader r = ((UIReader) parentElement);
			ArrayList<Object> retVal = new ArrayList<Object>();
			retVal.addAll(r.getAntennas().values());
			retVal.add("Type: " + r.getReaderType());
			retVal.add(r.getPropertiesMap() );
			return retVal.toArray();
		}
		if (parentElement instanceof Map) {
			return ((Map) parentElement).entrySet().toArray();
		}
		if (parentElement instanceof Map.Entry) {
			return new Object[] { ((Map.Entry) parentElement).getValue() };
		}

		return new Object[] {};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object
	 * )
	 */
	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.
	 * Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof ReaderRegistryService) {
			return !((ReaderRegistryService) element).getReaderList().isEmpty();
		} else if (element instanceof UIReader) {
			return true;
		} else if (element instanceof Map)
			return true;
		else if (element instanceof Map.Entry)
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java
	 * .lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface
	 * .viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (viewer instanceof AbstractTreeViewer) {
			this.treeViewer = (AbstractTreeViewer) viewer;
		} else {
			throw new RuntimeException("Viewer not AbstractTreeViewer");
		}
		if (oldInput != null) {
			if (oldInput instanceof ReaderRegistryService) {
				((ReaderRegistryService) oldInput).removelListener(this);
			}
		}
		if (newInput != null) {
			if (newInput instanceof ReaderRegistryService) {
				ReaderRegistryService readerReg = (ReaderRegistryService) newInput;
				this.readerRegistry = readerReg;
				this.readerRegistry.addListener(this);
			}
		} else {
			this.readerRegistry = null;
		}

		this.treeViewer.refresh();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.ui.common.registry.RegistryChangeListener#add(java.lang.Object
	 * )
	 */
	@Override
	public void add(Object event) {
		if (event instanceof UIReader) {
			((UIReader) event).addPropertyChangeListener(this);
			treeViewer.add(readerRegistry, event);
		}
		treeViewer.refresh();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.ui.common.registry.RegistryChangeListener#remove(java.lang
	 * .Object)
	 */
	@Override
	public void remove(Object event) {
		if (event instanceof UIReader) {
			((UIReader) event).removePropertyChangeListener(this);
			treeViewer.remove(event);
		}
		treeViewer.refresh();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.ui.common.registry.RegistryChangeListener#update(java.lang
	 * .Object)
	 */
	@Override
	public void update(Object event) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejava.beans.PropertyChangeListener#propertyChange(java.beans.
	 * PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		treeViewer.refresh();
	}

}
