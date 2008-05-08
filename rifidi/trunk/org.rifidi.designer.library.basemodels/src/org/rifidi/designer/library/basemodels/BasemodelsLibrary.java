/*
 *  BasemodelsLibrary.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library.basemodels;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.rifidi.designer.library.EntityLibrary;
import org.rifidi.designer.library.EntityLibraryReference;
import org.rifidi.designer.library.basemodels.antennafield.AntennaFieldEntity;
import org.rifidi.designer.library.basemodels.boxproducer.BoxproducerEntity;
import org.rifidi.designer.library.basemodels.cardbox.CardboxEntity;
import org.rifidi.designer.library.basemodels.conveyor.ConveyorEntity;
import org.rifidi.designer.library.basemodels.gate.GateEntity;
import org.rifidi.designer.library.basemodels.gate.GateEntityWizard;
import org.rifidi.designer.library.basemodels.infrared.InfraredEntity;
import org.rifidi.designer.library.basemodels.pusharm.PusharmEntity;

/**
 * Library descriptor for the rifidi base models
 * 
 * @author Jochen Mader Oct 8, 2007
 * 
 */
public class BasemodelsLibrary implements EntityLibrary {
	/**
	 * Library references for the models.
	 */
	private List<EntityLibraryReference> library;

	/**
	 * Constructor.
	 */
	public BasemodelsLibrary() {
		this.library = new ArrayList<EntityLibraryReference>();
		EntityLibraryReference conveyorRef = new EntityLibraryReference();
		conveyorRef.setId(ConveyorEntity.class.getName());
		conveyorRef.setImageDescriptor(Activator.getDefault()
				.getImageRegistry().getDescriptor(
						ConveyorEntity.class.getName()));
		conveyorRef.setLibrary(BasemodelsLibrary.class);
		conveyorRef.setName("Conveyor");
		conveyorRef.setWizard(null);
		conveyorRef.setEntityClass(ConveyorEntity.class);
		conveyorRef.setHidden(false);
		library.add(conveyorRef);
		EntityLibraryReference pusharmRef = new EntityLibraryReference();
		pusharmRef.setId(PusharmEntity.class.getName());
		pusharmRef.setImageDescriptor(Activator.getDefault().getImageRegistry()
				.getDescriptor(PusharmEntity.class.getName()));
		pusharmRef.setLibrary(BasemodelsLibrary.class);
		pusharmRef.setName("Pusharm");
		pusharmRef.setWizard(null);
		pusharmRef.setEntityClass(PusharmEntity.class);
		pusharmRef.setHidden(false);
		library.add(pusharmRef);
		EntityLibraryReference boxRef = new EntityLibraryReference();
		boxRef.setId(CardboxEntity.class.getName());
		boxRef.setImageDescriptor(Activator.getDefault().getImageRegistry()
				.getDescriptor(CardboxEntity.class.getName()));
		boxRef.setLibrary(BasemodelsLibrary.class);
		boxRef.setName("Cardboard Box");
		boxRef.setWizard(null);
		boxRef.setEntityClass(CardboxEntity.class);
		boxRef.setHidden(false);
		library.add(boxRef);
		EntityLibraryReference prodRef = new EntityLibraryReference();
		prodRef.setId(BoxproducerEntity.class.getName());
		prodRef.setImageDescriptor(Activator.getDefault().getImageRegistry()
				.getDescriptor(BoxproducerEntity.class.getName()));
		prodRef.setLibrary(BasemodelsLibrary.class);
		prodRef.setName("Producer");
		prodRef.setWizard(null);
		prodRef.setEntityClass(BoxproducerEntity.class);
		prodRef.setHidden(false);
		library.add(prodRef);
		EntityLibraryReference gateRef = new EntityLibraryReference();
		gateRef.setId(GateEntity.class.getName());
		gateRef.setImageDescriptor(Activator.getDefault().getImageRegistry()
				.getDescriptor(GateEntity.class.getName()));
		gateRef.setLibrary(BasemodelsLibrary.class);
		gateRef.setName("Gate");
		gateRef.setWizard(GateEntityWizard.class);
		gateRef.setEntityClass(GateEntity.class);
		gateRef.setHidden(false);
		library.add(gateRef);
		EntityLibraryReference antennaRef = new EntityLibraryReference();
		antennaRef.setId(AntennaFieldEntity.class.getName());
		antennaRef.setImageDescriptor(Activator.getDefault().getImageRegistry()
				.getDescriptor(AntennaFieldEntity.class.getName()));
		antennaRef.setLibrary(BasemodelsLibrary.class);
		antennaRef.setName("Antenna");
		antennaRef.setWizard(null);
		antennaRef.setEntityClass(AntennaFieldEntity.class);
		antennaRef.setHidden(true);
		library.add(antennaRef);

		EntityLibraryReference infraredRef = new EntityLibraryReference();
		infraredRef.setId(InfraredEntity.class.getName());
		infraredRef.setImageDescriptor(Activator.getDefault()
				.getImageRegistry().getDescriptor(
						InfraredEntity.class.getName()));
		infraredRef.setLibrary(BasemodelsLibrary.class);
		infraredRef.setName("Infrared");
		infraredRef.setWizard(null);
		infraredRef.setEntityClass(InfraredEntity.class);
		infraredRef.setHidden(false);
		library.add(infraredRef);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.library.EntityLibrary#getLibraryReferences()
	 */
	public List<EntityLibraryReference> getLibraryReferences() {
		return library;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.library.EntityLibrary#getName()
	 */
	public String getName() {
		return "RFID Base Models";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.library.EntityLibrary#getImageDescriptor()
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return Activator.getDefault().getImageRegistry().getDescriptor(
				BasemodelsLibrary.class.getName());
	}

}
