/*
 *  View3d.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.ide.converter.views;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.ViewPart;
import org.rifidi.designer.ide.converter.editor.FixedDirectoryResourceLocator;
import org.rifidi.designer.ide.converter.editor.game.EditorGame;
import org.rifidi.jmeswt.SWTBaseGame;

import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.export.binary.BinaryExporter;
import com.jme.util.export.binary.BinaryImporter;
import com.jme.util.resource.ResourceLocatorTool;
import com.jmex.model.collada.ColladaImporter;
import com.jmex.model.converters.MaxToJme;
import com.jmex.model.converters.Md2ToJme;
import com.jmex.model.converters.Md3ToJme;
import com.jmex.model.converters.MilkToJme;
import com.jmex.model.converters.ObjToJme;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Aug 7, 2008
 * 
 */
public class View3d extends ViewPart {
	public static final String ID="org.rifidi.designer.ide.converter.view3d";
	/**
	 * Logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(View3d.class
			.getName());
	/**
	 * The game instance of this editor.
	 */
	private SWTBaseGame game;

	/**
	 * The GLCanvas for this editor
	 */
	private GLCanvas glCanvas;
	/**
	 * The node that contains the model.
	 */
	private Node model = null;
	/**
	 * Workspace root.
	 */
	private String wsRoot;
	/**
	 * Just some references to keep track of resourcelocators we have
	 * initialized.
	 */
	private List<IContainer> containers = new ArrayList<IContainer>();

	/**
	 * 
	 */
	public View3d() {
		wsRoot = ResourcesPlugin.getWorkspace().getRoot().getLocation()
				.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		game = new EditorGame(getPartName(), 640, 480, parent, 10, 20);
		game.start();
		glCanvas = game.getGlCanvas();
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT;
		DropTarget target = new DropTarget(glCanvas, operations);
		target.addDropListener(new DropTargetListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.dnd.DropTargetListener#dragEnter(org.eclipse.swt.dnd.DropTargetEvent)
			 */
			@Override
			public void dragEnter(DropTargetEvent event) {
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.dnd.DropTargetListener#dragLeave(org.eclipse.swt.dnd.DropTargetEvent)
			 */
			@Override
			public void dragLeave(DropTargetEvent event) {
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.dnd.DropTargetListener#dragOperationChanged(org.eclipse.swt.dnd.DropTargetEvent)
			 */
			@Override
			public void dragOperationChanged(DropTargetEvent event) {
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.dnd.DropTargetListener#dragOver(org.eclipse.swt.dnd.DropTargetEvent)
			 */
			@Override
			public void dragOver(DropTargetEvent event) {
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.dnd.DropTargetListener#drop(org.eclipse.swt.dnd.DropTargetEvent)
			 */
			@Override
			public void drop(DropTargetEvent event) {
				// to get the ifile we need to remove the root of the workspace
				// (ifiles are workspace relative)
				IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(
						new Path(((String[]) (event.data))[0].substring(wsRoot
								.length())));
				load(file);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.dnd.DropTargetListener#dropAccept(org.eclipse.swt.dnd.DropTargetEvent)
			 */
			@Override
			public void dropAccept(DropTargetEvent event) {
			}
		});
		FileTransfer fileTransfer = FileTransfer.getInstance();
		Transfer[] types = new Transfer[] { fileTransfer };
		target.setTransfer(types);
		ResizeListener resizeListener = new ResizeListener();
		glCanvas.addControlListener(resizeListener);
		MenuManager menuMgr = new MenuManager();
		menuMgr.add(
				new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		Menu menu = menuMgr.createContextMenu(glCanvas);
		glCanvas.setMenu(menu);	
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	private void resize() {
		game.getDisplaySys().setWidth(glCanvas.getSize().x);
		game.getDisplaySys().setHeight(glCanvas.getSize().y);
		game.getDisplaySys().getRenderer().reinit(glCanvas.getSize().x,
				glCanvas.getSize().y);
		game.getDisplaySys().getRenderer().getCamera().setFrustumPerspective(
				45.0f,
				(float) DisplaySystem.getDisplaySystem().getRenderer()
						.getWidth()
						/ (float) DisplaySystem.getDisplaySystem()
								.getRenderer().getHeight(), 1, 1000);
		game.getDisplaySys().getRenderer().getCamera().update();
	}

	private void load(final IFile file) {
		final String extension = file.getFileExtension();
		if (!containers.contains(file.getParent())) {
			ResourceLocatorTool.addResourceLocator(
					ResourceLocatorTool.TYPE_TEXTURE,
					new FixedDirectoryResourceLocator(file.getParent()));
			containers.add(file.getParent());
		}
		if(model!=null){
			model.removeFromParent();
			TextureManager.clearCache();
		}
		
		Job job = new Job("Load " + getPartName() + " job") {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
			 */
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				if ("dae".equals(extension)) {
					try {
						ColladaImporter.load(file.getContents(), "dae");
						model = ColladaImporter.getModel();
						ColladaImporter.cleanUp();
					} catch (CoreException e) {
						logger.warning(e.toString());
					}
				} else if ("jme".equals(extension)) {
					BinaryImporter im = new BinaryImporter();
					try {
						model = (Node) im.load(file.getContents());
					} catch (IOException e) {
						logger.warning("Unable to load jme file: " + file);
					} catch (CoreException e) {
						logger.warning(e.toString());
					}
				} else if ("obj".equals(extension)) {
					ObjToJme objloader = new ObjToJme();
					try {
						BinaryImporter im = new BinaryImporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						objloader.convert(file.getContents(), baos);
						model = new Node("objmodel");
						model
								.attachChild((Spatial) im.load(baos
										.toByteArray()));
					} catch (IOException e) {
						logger.warning("Unable to load obj file: " + file);
					} catch (CoreException e) {
						logger.warning(e.toString());
					}
				} else if ("3ds".equals(extension)) {
					MaxToJme objloader = new MaxToJme();
					try {
						BinaryImporter im = new BinaryImporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						objloader.convert(file.getContents(), baos);
						model = new Node("3dsmodel");
						model
								.attachChild((Spatial) im.load(baos
										.toByteArray()));
					} catch (IOException e) {
						logger.warning("Unable to load 3ds file: " + file);
					} catch (CoreException e) {
						logger.warning(e.toString());
					}
				} else if ("md3s".equals(extension)) {
					Md3ToJme objloader = new Md3ToJme();
					try {
						BinaryImporter im = new BinaryImporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						objloader.convert(file.getContents(), baos);
						model = new Node("md3model");
						model
								.attachChild((Spatial) im.load(baos
										.toByteArray()));
					} catch (IOException e) {
						logger.warning("Unable to load md3 file: " + file);
					} catch (CoreException e) {
						logger.warning(e.toString());
					}
				} else if ("ms3d".equals(extension)) {
					MilkToJme objloader = new MilkToJme();
					try {
						BinaryImporter im = new BinaryImporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						objloader.convert(file.getContents(), baos);
						model = new Node("milkshapemodel");
						model
								.attachChild((Spatial) im.load(baos
										.toByteArray()));
					} catch (IOException e) {
						logger.warning("Unable to load ms3d file: " + file);
					} catch (CoreException e) {
						logger.warning(e.toString());
					}
				} else if ("md2".equals(extension)) {
					Md2ToJme objloader = new Md2ToJme();
					try {
						BinaryImporter im = new BinaryImporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						objloader.convert(file.getContents(), baos);
						model = new Node("md2model");
						model
								.attachChild((Spatial) im.load(baos
										.toByteArray()));
					} catch (IOException e) {
						logger.warning("Unable to load md2 file: " + file);
					} catch (CoreException e) {
						logger.warning(e.toString());
					}
				}

				game.getUpdateQueue().enqueue(new Callable<Object>() {

					/*
					 * (non-Javadoc)
					 * 
					 * @see java.util.concurrent.Callable#call()
					 */
					@Override
					public Object call() throws Exception {
						game.getRootNode().attachChild(model);
						// Quaternion q = new Quaternion();
						// q
						// .fromAngleAxis((float) (Math.PI / 2), Vector3f.UNIT_X
						// .negate());
						// model.setLocalRotation(q);
						game.getRootNode().updateRenderState();
						game.getRootNode().updateWorldData(0);
						return null;
					}

				});
				return Status.OK_STATUS;
			}

		};
		job.schedule();

	}
	
	public void doSaveAs() {
		SaveAsDialog savy = new SaveAsDialog(glCanvas.getShell());
		savy.open();
		if (savy.getResult() != null) {
			final IFile file = ResourcesPlugin.getWorkspace().getRoot()
					.getFile(savy.getResult());
			game.getUpdateQueue().enqueue(new Callable<Object>() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.util.concurrent.Callable#call()
				 */
				@Override
				public Object call() throws Exception {
					try {
						ByteArrayOutputStream fileOutput = new ByteArrayOutputStream();
						BinaryExporter.getInstance().save(model, fileOutput);
						model.removeFromParent();
						file.create(new ByteArrayInputStream(fileOutput
								.toByteArray()), false, null);
						game.getRootNode().attachChild(model);
						game.getRootNode().updateRenderState();
						return null;
					} catch (IOException e) {
						e.printStackTrace();
					} catch (CoreException e) {
						e.printStackTrace();
					}
					return null;
				}

			});

		}
	}
	
	private class ResizeListener implements ControlListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.swt.events.ControlListener#controlMoved(org.eclipse.swt.events.ControlEvent)
		 */
		@Override
		public void controlMoved(ControlEvent e) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.swt.events.ControlListener#controlResized(org.eclipse.swt.events.ControlEvent)
		 */
		@Override
		public void controlResized(ControlEvent e) {
			resize();
		}

	}
}
