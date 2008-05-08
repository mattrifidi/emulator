package org.rifidi.designer.library.basemodels.conveyor90;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.rifidi.designer.library.basemodels.Activator;

public class Conveyor90EntityWorkbenchAdapter implements IWorkbenchAdapter {

	public Object[] getChildren(Object o) {
		return new Object[]{};
	}

	public ImageDescriptor getImageDescriptor(Object object) {
		return Activator.getDefault().getImageRegistry().getDescriptor(Conveyor90Entity.class.getName());
	}

	public String getLabel(Object o) {
		return ((Conveyor90Entity)o).getName();
	}

	public Object getParent(Object o) {
		// TODO Auto-generated method stub
		return null;
	}

}
