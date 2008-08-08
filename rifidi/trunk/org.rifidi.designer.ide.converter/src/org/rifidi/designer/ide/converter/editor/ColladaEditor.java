/*
 *  ColladaEditor.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.ide.converter.editor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IReusableEditor;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.rifidi.designer.ide.converter.editor.game.EditorGame;
import org.rifidi.jmeswt.SWTBaseGame;

import com.jme.image.Texture;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.export.binary.BinaryExporter;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.collada.ColladaImporter;
import com.jmex.model.converters.MaxToJme;
import com.jmex.model.converters.Md2ToJme;
import com.jmex.model.converters.Md3ToJme;
import com.jmex.model.converters.MilkToJme;
import com.jmex.model.converters.ObjToJme;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - May 22, 2008
 * 
 */
public class ColladaEditor extends EditorPart implements IReusableEditor {
	/**
	 * Logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(ColladaEditor.class
			.getName());
	/**
	 * The GLCanvas for this editor
	 */
	private GLCanvas glCanvas;
	/**
	 * True if the whole editor is intialized (important for the resizer).
	 */
	private static boolean initialized = false;
	/**
	 * The game instance of this editor.
	 */
	private SWTBaseGame game;
	/**
	 * The loaded model.
	 */
	private Node model;

	private ResizeListener resizeListener;

	private URI uri;

	private static IPartListener partListener = null;

	private InputStream fileInput = null;

	private static boolean created = false;

	/**
	 * 
	 */
	public ColladaEditor() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite,
	 *      org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {

		if (input instanceof FileEditorInput) {
			try {
				fileInput = ((FileEditorInput) input).getFile().getContents();
			} catch (CoreException e) {
				throw new PartInitException("Unable to load file.", e);
			}
		} else if (input instanceof FileStoreEditorInput) {
			FileStoreEditorInput _input = (FileStoreEditorInput) input;
			URI _uri = _input.getURI();
			Path _p = new Path(_uri.getPath());
			IFileStore _ifs = EFS.getLocalFileSystem().getStore(_p);
			try {
				fileInput = _ifs.openInputStream(EFS.NONE, null);
			} catch (CoreException e) {
				throw new PartInitException("Unable to load file.", e);
			}
		}
		uri = ((IURIEditorInput) input).getURI();

		// ResourceLocatorTool.addResourceLocator(
		// ResourceLocatorTool.TYPE_TEXTURE,
		// new FixedDirectoryResourceLocator(uri));

		setInput(input);
		setSite(site);
		int pos = uri.toString().lastIndexOf('/');
		setPartName(uri.toString().substring(pos + 1));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return true;
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

		if (!(getEditorInput() instanceof IURIEditorInput)) {
			logger.warning("Not a IRUIEditorInput: " + getEditorInput());
		}
		Job job = new Job("Load " + getPartName() + " job") {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
			 */
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				String nameNoEnding = uri.toString().substring(0,
						uri.toString().lastIndexOf('.'));
				if (((IURIEditorInput) getEditorInput()).getURI().toString()
						.endsWith(".dae")) {

					ColladaImporter.load(fileInput,
							((IURIEditorInput) getEditorInput()).getURI()
									.toString());
					model = ColladaImporter.getModel();
					ColladaImporter.cleanUp();
					// if (MyResourceLocator.findTheCrap(nameNoEnding) != null)
					// {
					// TextureState ts = game.getDisplaySys().getRenderer()
					// .createTextureState();
					// ts.setEnabled(true);
					// ts.setTexture(TextureManager.loadTexture(
					// MyResourceLocator.findTheCrap(nameNoEnding),
					// Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR));
					// model.setRenderState(ts);
					// }
				} else if (((IURIEditorInput) getEditorInput()).getURI()
						.toString().endsWith(".jme")) {
					BinaryImporter im = new BinaryImporter();
					try {
						model = (Node) im.load(fileInput);
					} catch (IOException e) {
						logger.warning("Unable to load jme file: "
								+ getEditorInput());
					}
				} else if (((IURIEditorInput) getEditorInput()).getURI()
						.toString().endsWith(".obj")) {
					ObjToJme objloader = new ObjToJme();
					try {
						BinaryImporter im = new BinaryImporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						objloader.convert(fileInput, baos);
						model = new Node("objmodel");
						model
								.attachChild((Spatial) im.load(baos
										.toByteArray()));
						if (MyResourceLocator.findTheCrap(nameNoEnding) != null) {
							TextureState ts = game.getDisplaySys()
									.getRenderer().createTextureState();
							ts.setEnabled(true);
							ts.setTexture(TextureManager
									.loadTexture(MyResourceLocator
											.findTheCrap(nameNoEnding),
											Texture.MM_LINEAR_LINEAR,
											Texture.FM_LINEAR));
							model.setRenderState(ts);
						}
					} catch (IOException e) {
						logger.warning("Unable to load obj file: "
								+ getEditorInput());
					}
				} else if (((IURIEditorInput) getEditorInput()).getURI()
						.toString().endsWith(".3ds")) {
					MaxToJme objloader = new MaxToJme();
					try {
						BinaryImporter im = new BinaryImporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						objloader.convert(fileInput, baos);
						model = new Node("3dsmodel");
						model
								.attachChild((Spatial) im.load(baos
										.toByteArray()));
						if (MyResourceLocator.findTheCrap(nameNoEnding) != null) {
							TextureState ts = game.getDisplaySys()
									.getRenderer().createTextureState();
							ts.setEnabled(true);
							ts.setTexture(TextureManager
									.loadTexture(MyResourceLocator
											.findTheCrap(nameNoEnding),
											Texture.MM_LINEAR_LINEAR,
											Texture.FM_LINEAR));
							model.setRenderState(ts);
						}
					} catch (IOException e) {
						logger.warning("Unable to load 3ds file: "
								+ getEditorInput());
					}
				} else if (((IURIEditorInput) getEditorInput()).getURI()
						.toString().endsWith(".md3")) {
					Md3ToJme objloader = new Md3ToJme();
					try {
						BinaryImporter im = new BinaryImporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						objloader.convert(fileInput, baos);
						model = new Node("md3model");
						model
								.attachChild((Spatial) im.load(baos
										.toByteArray()));
						if (MyResourceLocator.findTheCrap(nameNoEnding) != null) {
							TextureState ts = game.getDisplaySys()
									.getRenderer().createTextureState();
							ts.setEnabled(true);
							ts.setTexture(TextureManager
									.loadTexture(MyResourceLocator
											.findTheCrap(nameNoEnding),
											Texture.MM_LINEAR_LINEAR,
											Texture.FM_LINEAR));
							model.setRenderState(ts);
						}
					} catch (IOException e) {
						logger.warning("Unable to load md3 file: "
								+ getEditorInput());
					}
				} else if (((IURIEditorInput) getEditorInput()).getURI()
						.toString().endsWith(".ms3d")) {
					MilkToJme objloader = new MilkToJme();
					try {
						BinaryImporter im = new BinaryImporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						objloader.convert(fileInput, baos);
						model = new Node("milkshapemodel");
						model
								.attachChild((Spatial) im.load(baos
										.toByteArray()));
						if (MyResourceLocator.findTheCrap(nameNoEnding) != null) {
							TextureState ts = game.getDisplaySys()
									.getRenderer().createTextureState();
							ts.setEnabled(true);
							ts.setTexture(TextureManager
									.loadTexture(MyResourceLocator
											.findTheCrap(nameNoEnding),
											Texture.MM_LINEAR_LINEAR,
											Texture.FM_LINEAR));
							model.setRenderState(ts);
						}
					} catch (IOException e) {
						logger.warning("Unable to load ms3d file: "
								+ getEditorInput());
					}
				} else if (((IURIEditorInput) getEditorInput()).getURI()
						.toString().endsWith(".md2")) {
					Md2ToJme objloader = new Md2ToJme();
					try {
						BinaryImporter im = new BinaryImporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						objloader.convert(fileInput, baos);
						model = new Node("md2model");
						model
								.attachChild((Spatial) im.load(baos
										.toByteArray()));
						if (MyResourceLocator.findTheCrap(nameNoEnding) != null) {
							TextureState ts = game.getDisplaySys()
									.getRenderer().createTextureState();
							ts.setEnabled(true);
							ts.setTexture(TextureManager
									.loadTexture(MyResourceLocator
											.findTheCrap(nameNoEnding),
											Texture.MM_LINEAR_LINEAR,
											Texture.FM_LINEAR));
							model.setRenderState(ts);
						}
					} catch (IOException e) {
						logger.warning("Unable to load md2 file: "
								+ getEditorInput());
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
		// create the partlistener for listening to focus events
		if (partListener == null) {
			partListener = new IPartListener() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.ui.IPartListener#partActivated(org.eclipse.ui.IWorkbenchPart)
				 */
				@Override
				public void partActivated(IWorkbenchPart part) {
					if (part instanceof ColladaEditor) {
						((ColladaEditor) part).enable();
					}
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.ui.IPartListener#partBroughtToTop(org.eclipse.ui.IWorkbenchPart)
				 */
				@Override
				public void partBroughtToTop(IWorkbenchPart part) {
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.ui.IPartListener#partClosed(org.eclipse.ui.IWorkbenchPart)
				 */
				@Override
				public void partClosed(IWorkbenchPart part) {
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.ui.IPartListener#partDeactivated(org.eclipse.ui.IWorkbenchPart)
				 */
				@Override
				public void partDeactivated(IWorkbenchPart part) {
					if (part instanceof ColladaEditor) {
						((ColladaEditor) part).disable();
					}
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.ui.IPartListener#partOpened(org.eclipse.ui.IWorkbenchPart)
				 */
				@Override
				public void partOpened(IWorkbenchPart part) {
				}

			};
			getEditorSite().getPage().addPartListener(partListener);
		}
		// init3d();
		enable();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		if (initialized) {
			resize();
		}
	}

	public void disable() {
		glCanvas.removeControlListener(resizeListener);
		game.stopRendering();
	}

	public void enable() {
		game.resumeRendering();
		if (resizeListener == null) {
			resizeListener = new ResizeListener();
		}
		glCanvas.addControlListener(resizeListener);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		game.finish();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void setInput(IEditorInput input) {
		System.out.println("hit");
		super.setInput(input);
	}

}