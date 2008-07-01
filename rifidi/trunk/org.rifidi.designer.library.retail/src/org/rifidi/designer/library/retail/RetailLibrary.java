/*
 *  RetailLibrary.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library.retail;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.rifidi.designer.library.EntityLibrary;
import org.rifidi.designer.library.EntityLibraryReference;
import org.rifidi.designer.library.FloorElement;
import org.rifidi.designer.library.retail.clothing.Clothing;
import org.rifidi.designer.library.retail.clothingrack.ClothingRack;
import org.rifidi.designer.library.retail.retailbox.RetailBox;
import org.rifidi.designer.library.retail.shelf.Shelf;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Apr 3, 2008
 * 
 */
public class RetailLibrary implements EntityLibrary {

	/**
	 * List containing all Entity Library References.
	 */
	private List<EntityLibraryReference> referencesList;

	/**
	 * Constructor.
	 */
	public RetailLibrary() {
		referencesList = new ArrayList<EntityLibraryReference>();
		EntityLibraryReference rackRef = new EntityLibraryReference();
		rackRef.setEntityClass(ClothingRack.class);
		rackRef.setImageDescriptor(Activator.getDefault().getImageRegistry()
				.getDescriptor(ClothingRack.class.getName()));
		rackRef.setHidden(false);
		rackRef.setId(ClothingRack.class.getName());
		rackRef.setLibrary(RetailLibrary.class);
		rackRef.setName("Clothing Rack");
		rackRef.setWizard(null);
		referencesList.add(rackRef);
		EntityLibraryReference clothRef = new EntityLibraryReference();
		clothRef.setEntityClass(Clothing.class);
		clothRef.setImageDescriptor(Activator.getDefault().getImageRegistry()
				.getDescriptor(Clothing.class.getName()));
		clothRef.setHidden(true);
		clothRef.setId(Clothing.class.getName());
		clothRef.setLibrary(RetailLibrary.class);
		clothRef.setName("Clothing");
		clothRef.setWizard(null);
		referencesList.add(clothRef);
		EntityLibraryReference shefRef = new EntityLibraryReference();
		shefRef.setEntityClass(Shelf.class);
		shefRef.setImageDescriptor(Activator.getDefault().getImageRegistry()
				.getDescriptor(Shelf.class.getName()));
		shefRef.setHidden(false);
		shefRef.setId(Shelf.class.getName());
		shefRef.setLibrary(RetailLibrary.class);
		shefRef.setName("Shelf");
		shefRef.setWizard(null);
		referencesList.add(shefRef);
		EntityLibraryReference retailBoxRef = new EntityLibraryReference();
		retailBoxRef.setEntityClass(RetailBox.class);
		retailBoxRef.setImageDescriptor(Activator.getDefault()
				.getImageRegistry().getDescriptor(RetailBox.class.getName()));
		retailBoxRef.setHidden(true);
		retailBoxRef.setId(RetailBox.class.getName());
		retailBoxRef.setLibrary(RetailLibrary.class);
		retailBoxRef.setName("Retail box");
		retailBoxRef.setWizard(null);
		referencesList.add(retailBoxRef);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.library.EntityLibrary#getLibraryReferences()
	 */
	@Override
	public List<EntityLibraryReference> getLibraryReferences() {
		return referencesList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.library.EntityLibrary#getName()
	 */
	@Override
	public String getName() {
		return "Retail Library";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.library.EntityLibrary#getImageDescriptor()
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return Activator.getDefault().getImageRegistry().getDescriptor(
				RetailLibrary.class.getName());
	}

	/* (non-Javadoc)
	 * @see org.rifidi.designer.library.EntityLibrary#getFloorElements()
	 */
	@Override
	public List<FloorElement> getFloorElements() {
		return null;
	}

}
