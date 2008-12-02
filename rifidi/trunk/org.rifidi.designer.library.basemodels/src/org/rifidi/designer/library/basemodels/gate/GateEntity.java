/*
 *  GateEntity.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library.basemodels.gate;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.designer.entities.RMIManager;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.annotations.Property;
import org.rifidi.designer.entities.databinding.annotations.MonitoredProperties;
import org.rifidi.designer.entities.gpio.IGPIO;
import org.rifidi.designer.entities.gpio.GPIPort;
import org.rifidi.designer.entities.gpio.GPOPort;
import org.rifidi.designer.entities.gpio.GPOPort.State;
import org.rifidi.designer.entities.grouping.IParentEntity;
import org.rifidi.designer.entities.interfaces.IHasSwitch;
import org.rifidi.designer.entities.rifidi.RifidiEntity;
import org.rifidi.designer.library.basemodels.antennafield.AntennaFieldEntity;
import org.rifidi.emulator.rmi.server.ReaderModuleManagerInterface;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.common.reader.callback.GPOEventCallbackInterface;
import org.rifidi.ui.common.reader.callback.UIReaderCallbackManager;

import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.SharedNode;
import com.jme.scene.SwitchNode;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.shape.Box;
import com.jme.util.export.binary.BinaryImporter;

/**
 * This entity is a gateway with an attached reader, through which a
 * conveyorbelt can pass. One or two antennas are attached to the sides for tag
 * detection depending on the number of antennas the associated reader has.
 * 
 * @author Jochen Mader Oct 8, 2007
 * @author Dan West - dan@pramari.com
 */
@MonitoredProperties(names = { "name" })
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GateEntity extends VisualEntity implements RifidiEntity, IHasSwitch,
		IParentEntity, IGPIO, PropertyChangeListener, GPOEventCallbackInterface {

	/** logger for this class. */
	@XmlTransient
	private static Log logger = LogFactory.getLog(GateEntity.class);
	/** Model for shared meshes */
	@XmlTransient
	private static Node[] lod = null;
	/** Left antenna entity. */
	@XmlTransient
	private AntennaFieldEntity antennaL;
	/** Right antenna entity. */
	@XmlTransient
	private AntennaFieldEntity antennaR;
	/** Reader associated with this gate. */
	private UIReader reader;
	/** List of ChildEntites connected to this gate. */
	@XmlIDREF
	private List<VisualEntity> children;
	/** The multiplication factor for the field size */
	private float factor = 1.0f;
	/** Connection manager for rifidi. */
	@XmlTransient
	private RMIManager rmimanager;
	/** Reference to the reader management interface. */
	@XmlTransient
	private ReaderModuleManagerInterface readerModuleManagerInterface;
	/** State of the switch. */
	private boolean running = false;
	/** Node that contains the different lods. */
	@XmlTransient
	private SwitchNode switchNode;
	/** Available GPI ports. */
	private List<GPIPort> gpiPorts;
	/** Available GPO ports. */
	private List<GPOPort> gpoPorts;

	/**
	 * Constructor.
	 */
	public GateEntity() {
		gpiPorts = new ArrayList<GPIPort>();
		gpoPorts = new ArrayList<GPOPort>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#init()
	 */
	@Override
	public void init() {
		prepare();
		Node mainNode = new Node();
		mainNode.setModelBound(new BoundingBox());
		switchNode = new SwitchNode("maingeometry");
		switchNode.setLocalScale(new Vector3f(0.9f, 1.1f, 1f));
		switchNode.attachChildAt(new SharedNode("shared_gate", lod[0]), 0);
		switchNode.attachChildAt(new SharedNode("shared_gate", lod[1]), 1);
		switchNode.attachChildAt(new SharedNode("shared_gate", lod[2]), 2);
		switchNode.attachChildAt(new SharedNode("shared_gate", lod[2]), 3);
		switchNode.setActiveChild(0);

		mainNode.attachChild(switchNode);
		setNode(mainNode);

		// find the snappoints
		children = new ArrayList<VisualEntity>();
		if (reader.getNumAntennas() > 0) {
			// instantiate the right antenna
			antennaR = new AntennaFieldEntity(0, readerModuleManagerInterface);
			antennaR.setName("Right antenna");
			Vector3f baseTrans = new Vector3f(2.9f, 6f, 0f);
			Vector3f baseRot = new Vector3f(0, (float) (Math.PI * 1.5f), 0);
			antennaR.setBaseTranslation(baseTrans);
			antennaR.setBaseRotation(baseRot);
			children.add(antennaR);
		}

		if (reader.getNumAntennas() > 1) {
			// instantiate the left antenna
			antennaL = new AntennaFieldEntity(1, readerModuleManagerInterface);
			antennaL.setName("Left antenna");
			Vector3f baseTrans = new Vector3f(-2.9f, 6f, 0f);
			Vector3f baseRot = new Vector3f(0, (float) (Math.PI * 0.5f), 0);
			antennaL.setBaseTranslation(baseTrans);
			antennaL.setBaseRotation(baseRot);
			children.add(antennaL);
		}
		getNode().updateGeometricState(0, true);
		getNode().updateWorldBound();

		Node _node = new Node("hiliter");
		Box box = new Box("top", ((BoundingBox) getNode().getWorldBound())
				.getCenter().clone().subtractLocal(
						getNode().getLocalTranslation()).add(
						new Vector3f(0, 5, 0)), 3f, 1f, .5f);
		box.setModelBound(new BoundingBox());
		box.updateModelBound();
		_node.attachChild(box);
		box = new Box("left", ((BoundingBox) getNode().getWorldBound())
				.getCenter().clone().subtractLocal(
						getNode().getLocalTranslation()).add(
						new Vector3f(-2.5f, -.5f, 0)), .5f, 4.5f, .5f);
		box.setModelBound(new BoundingBox());
		box.updateModelBound();
		_node.attachChild(box);
		box = new Box("leftFoot", ((BoundingBox) getNode().getWorldBound())
				.getCenter().clone().subtractLocal(
						getNode().getLocalTranslation()).add(
						new Vector3f(-2.5f, -5.5f, 0)), .5f, .5f, 2f);
		box.setModelBound(new BoundingBox());
		box.updateModelBound();
		_node.attachChild(box);
		box = new Box("right", ((BoundingBox) getNode().getWorldBound())
				.getCenter().clone().subtractLocal(
						getNode().getLocalTranslation()).add(
						new Vector3f(2.5f, -.5f, 0)), .5f, 4.5f, .5f);
		box.setModelBound(new BoundingBox());
		box.updateModelBound();
		_node.attachChild(box);
		box = new Box("rightFoot", ((BoundingBox) getNode().getWorldBound())
				.getCenter().clone().subtractLocal(
						getNode().getLocalTranslation()).add(
						new Vector3f(2.5f, -5.5f, 0)), .5f, .5f, 2f);
		box.setModelBound(new BoundingBox());
		box.updateModelBound();
		_node.attachChild(box);

		_node.setModelBound(new BoundingBox());
		_node.updateModelBound();
		_node.setCullHint(CullHint.Always);
		getNode().attachChild(_node);
		try {
			for (int count = 0; count < reader.getNumGPIs(); count++) {
				GPIPort gpiPort = new GPIPort();
				gpiPort.setNr(count);
				gpiPort.setId(getEntityId() + "-gpi-" + count);
				gpiPorts.add(gpiPort);
			}
			for (int count = 0; count < reader.getNumGPOs(); count++) {
				GPOPort gpoPort = new GPOPort();
				gpoPort.setNr(count);
				gpoPort.setId(getEntityId() + "-gpo-" + count);
				gpoPorts.add(gpoPort);
			}
		} catch (Exception e) {
			logger.error("Problem while connecting to RMI: " + e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#loaded()
	 */
	@Override
	public void loaded() {
		prepare();
		switchNode = (SwitchNode) getNode().getChild("maingeometry");
		for (VisualEntity antennaFieldEntity : children) {
			((AntennaFieldEntity) antennaFieldEntity)
					.setReaderInterface(readerModuleManagerInterface);
		}
		if (running)
			turnOn();
	}

	private void prepare() {
		if (lod == null) {
			lod = new Node[4];
			URI modelpath = null;
			for (int count = 0; count < 4; count++) {
				try {
					modelpath = getClass().getClassLoader().getResource(
							"org/rifidi/designer/library/basemodels/gate/gate"
									+ count + ".jme").toURI();
				} catch (URISyntaxException e) {
					logger.debug(e);
				}
				try {
					lod[count] = (Node) BinaryImporter.getInstance().load(
							modelpath.toURL());
					lod[count].setLocalRotation(new Quaternion().fromAngleAxis(
							270 * FastMath.DEG_TO_RAD, Vector3f.UNIT_X));
					lod[count].setModelBound(new BoundingBox());
					lod[count].updateGeometricState(0f, true);
					lod[count].updateModelBound();
					lod[count].updateWorldBound();
					lod[count].setLocalScale(new Vector3f(0.87f, 1f, 1f));
				} catch (MalformedURLException e) {
					logger.debug(e);
				} catch (IOException e) {
					logger.debug(e);
				}
			}
		}
		try {
			readerModuleManagerInterface = rmimanager.createReader(reader
					.getGeneralReaderPropertyHolder());
			try {
				UIReaderCallbackManager readerCallbackManager;
				readerCallbackManager = new UIReaderCallbackManager(
						readerModuleManagerInterface.getClientProxy());
				reader.setReaderCallbackManager(readerCallbackManager);
				reader.getReaderCallbackManager().addGPOPortListener(this);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Problem connecting to RMI: " + e);
			}
		} catch (ClassNotFoundException e) {
			logger.fatal("Unable to create reader: " + e);
		} catch (InstantiationException e) {
			logger.fatal("Unable to create reader: " + e);
		} catch (IllegalAccessException e) {
			logger.fatal("Unable to create reader: " + e);
		} catch (MalformedURLException e) {
			logger.fatal("Unable to create reader: " + e);
		} catch (NotBoundException e) {
			logger.fatal("Unable to create reader: " + e);
		} catch (RemoteException e) {
			logger.fatal("Unable to create reader: " + e);
		} catch (IOException e) {
			logger.fatal("Unable to create reader: " + e);
		}
		for (GPIPort gpiPort : gpiPorts) {
			gpiPort.addPropertyChangeListener("state", this);
		}
	}

	/**
	 * Turns on the reader associated with the gate.
	 */
	public void turnOn() {
		Thread thr = new Thread(new Runnable() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Runnable#run()
			 */
			@Override
			public void run() {

				try {
					readerModuleManagerInterface.turnReaderOn();
					running = true;
					// attach antenna fields to the gate
					for (VisualEntity antenna : children) {
						((AntennaFieldEntity) antenna).turnOn();
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Problem turning on gate: " + e);
				}
			}

		});
		thr.run();
	}

	/**
	 * Turns off the reader associated with the gate.
	 */
	public void turnOff() {
		try {

			Thread thr = new Thread(new Runnable() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.lang.Runnable#run()
				 */
				@Override
				public void run() {

					try {
						readerModuleManagerInterface.turnReaderOff();
						running = false;
					} catch (Exception e) {
						logger.error("Problem turning on gate: " + e);
					}
				}

			});
			thr.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (VisualEntity entity : children) {
			((AntennaFieldEntity) entity).turnOff();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#destroy()
	 */
	@Override
	public void destroy() {
		getNode().removeFromParent();
		try {
			readerModuleManagerInterface.turnReaderOff();
		} catch (Exception e) {
			logger.warn("Unable to turn off reader: " + e);
		}
		rmimanager.removeReader(reader.getReaderName());
	}
	
	public void setRMIManager(RMIManager rmimanager) {
		this.rmimanager = rmimanager;
	}

	/**
	 * Set the running state of the entity.
	 * 
	 * @param newrunning
	 *            the running state
	 */
	public void setRunning(boolean newrunning) {
		running = newrunning;
	}

	/**
	 * Check if the entity is running.
	 * 
	 * @return the running state
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * @return the reader
	 */
	public UIReader getReader() {
		return reader;
	}

	/**
	 * @param reader
	 *            the reader to set
	 */
	public void setReader(UIReader reader) {
		this.reader = reader;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.entities.interfaces.ParentEntity#getChildEntites()
	 */
	@Override
	public List<VisualEntity> getChildEntites() {
		return children;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.entities.interfaces.ParentEntity#setChildEntites(
	 * java.util.List)
	 */
	@Override
	public void setChildEntites(List<VisualEntity> children) {
		this.children = children;
	}

	/**
	 * @return the factor
	 */
	public float getFactor() {
		return this.factor;
	}

	/**
	 * @param factor
	 *            the factor to set
	 */
	@Property(displayName = "Factor", description = "field mnultiplicator", readonly = false, unit = "")
	public void setFactor(float factor) {
		this.factor = factor;
		if (children != null) {
			for (VisualEntity antenna : children) {
				((AntennaFieldEntity) antenna).updateFactor(factor);
			}
		}
	}

	@Property(displayName = "Reader", description = "type of emulated reader", readonly = true, unit = "")
	public void setReaderType(String name) {

	}

	public String getReaderType() {
		return reader.getReaderType();
	}

	@Property(displayName = "Reader Connection", description = "connection details for the reader", readonly = true, unit = "")
	public void setConnectionDetails(String readerDetails) {

	}

	public String getConnectionDetails() {
		if (reader.getProperty("inet_address") != null) {
			return reader.getProperty("inet_address");
		}
		return "no connection info available";
	}

	/**
	 * Used to tell the gate that a tag just passed through it.
	 */
	public void tagSeen() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.entities.interfaces.RifidiEntity#getReaderInterface()
	 */
	@Override
	public ReaderModuleManagerInterface getReaderInterface() {
		return this.readerModuleManagerInterface;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.VisualEntity#setLOD(int)
	 */
	@Override
	public void setLOD(int lod) {
		if (switchNode != null) {
			switchNode.setActiveChild(lod);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.VisualEntity#getBoundingNode()
	 */
	@Override
	public Node getBoundingNode() {
		return (Node) getNode().getChild("hiliter");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.gpio.IGPIO#getGPIPorts()
	 */
	@Override
	public List<GPIPort> getGPIPorts() {
		return new ArrayList<GPIPort>(gpiPorts);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.gpio.IGPIO#getGPOPorts()
	 */
	@Override
	public List<GPOPort> getGPOPorts() {
		return new ArrayList<GPOPort>(gpoPorts);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejava.beans.PropertyChangeListener#propertyChange(java.beans.
	 * PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		GPIPort port = (GPIPort) evt.getSource();
		if (State.HIGH.equals(port.getState())) {
			try {
				readerModuleManagerInterface.setGPIHigh(port.getNr());
			} catch (Exception e) {
				logger.error("Unable to set port to high: " + e);
			}
		} else {
			try {
				readerModuleManagerInterface.setGPILow(port.getNr());
			} catch (Exception e) {
				logger.error("Unable to set port to low: " + e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.ui.common.reader.callback.GPOEventCallbackInterface#GPOPortSetHigh
	 * (int)
	 */
	@Override
	public void GPOPortSetHigh(int gpoPortNum) {
		gpoPorts.get(gpoPortNum).setState(State.HIGH);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.ui.common.reader.callback.GPOEventCallbackInterface#GPOPortSetLow
	 * (int)
	 */
	@Override
	public void GPOPortSetLow(int gpoPortNum) {
		gpoPorts.get(gpoPortNum).setState(State.LOW);
	}

}