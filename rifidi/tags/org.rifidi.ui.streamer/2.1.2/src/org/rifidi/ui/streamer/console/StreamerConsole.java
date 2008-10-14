package org.rifidi.ui.streamer.console;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.rifidi.emulator.rmi.server.RifidiManager;
import org.rifidi.streamer.exceptions.DublicateObjectException;
import org.rifidi.streamer.exceptions.NotInitializedException;
import org.rifidi.streamer.executers.TestUnitExecuter;
import org.rifidi.streamer.executers.listener.TestUnitStateListener;
import org.rifidi.streamer.handler.FileLoadHandler;
import org.rifidi.streamer.registry.InputObjectRegistry;
import org.rifidi.streamer.xml.BatchSuite;
import org.rifidi.streamer.xml.ComponentSuite;
import org.rifidi.streamer.xml.MetaFile;
import org.rifidi.streamer.xml.ScenarioSuite;
import org.rifidi.streamer.xml.TestUnitSuite;
import org.rifidi.streamer.xml.testSuite.TestUnit;
import org.rifidi.ui.streamer.Activator;

/**
 * Streamer Console Version (Hack) This is not working in a working environment
 * without a X-Server or Windows Desktop
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class StreamerConsole implements TestUnitStateListener {

	private static StreamerConsole console;

	private Log logger = LogFactory.getLog(StreamerConsole.class);
	private TestUnitExecuter testUnitExecuter;

	public static void startConsole(String[] args) {

		console = new StreamerConsole();

		//startUPTrayIcon();

		try {
			System.out.println();
			if (args.length == 2) {
				console.processFile(args[0], Integer.parseInt(args[1]));
			} else {
				console.displayHelp();
			}

		} catch (NumberFormatException e) {
			console.displayHelp();
		}
	}

	/**
	 * Initialize the SystemTrayIcon and make menus with actions for it
	 */
	private static void startUPTrayIcon() {
		Thread thread = new Thread(new Runnable() {

			private String imageDescription = "/icons/title_rifidi_16x16.gif";
			private Display display;

			@Override
			public void run() {

				display = new Display();

				// // Get rid of the SplashScreen (crazy stuff like using a axe
				// to cut the screen of)
				// String splashHandle = System
				// .getProperty("org.eclipse.equinox.launcher.splash.handle");
				// Method method;
				// try {
				// method = Shell.class.getMethod("internal_new", new Class[] {
				// Display.class, int.class });
				// Shell splashShell = (Shell) method
				// .invoke(null, new Object[] { display,
				// new Integer(splashHandle) });
				// splashShell.dispose();
				//
				// } catch (SecurityException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } catch (NoSuchMethodException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } catch (NumberFormatException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } catch (IllegalArgumentException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } catch (IllegalAccessException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } catch (InvocationTargetException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

				// display = new Display();
				Tray tray = display.getSystemTray();

				if (tray == null) {
					dispose();
					return;
				}

				TrayItem trayItem = new TrayItem(tray, SWT.NONE);
				Image icon = Activator.getImageDescriptor(imageDescription)
						.createImage();
				trayItem.setImage(icon);

				trayItem.setToolTipText("Rifidi TagStreamer Console running");
				trayItem.addListener(SWT.MenuDetect, new Listener() {
					public void handleEvent(Event event) {
						Shell shell = new Shell(event.display);
						Menu menu = new Menu(shell, SWT.POP_UP);
						MenuItem menuItem = new MenuItem(menu, SWT.NONE);
						menuItem.setText("Quit Test");
						menuItem.addListener(SWT.Selection, new Listener() {
							public void handleEvent(Event event) {
								display.dispose();
							}
						});
						menu.setVisible(true);
					};
				});
				while (!trayItem.isDisposed()) {

					if (!display.readAndDispatch())
						display.sleep();
				}
			}

			public void dispose() {
				display.dispose();
			}
		});
		// Start the created Thread
		thread.start();
	}

	public void processFile(String filename, int testUnitID) {

		logger.debug("====++++++ Starting Rifidi Streamer Console ++++++====");

		String hostname = "127.0.0.1";
		int port = 1198;

		// RMI ReaderManagement startup and connection management
		try {
			RifidiManager.startManager(hostname, port);
			// inputObjectRegistry.connectReaderRegsitry(hostname, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		InputObjectRegistry inputObjectRegistry = new InputObjectRegistry();

		MetaFile metaFile = null;
		TestUnitSuite testUnitSuite = null;

		try {
			String parentPath = (new File(filename)).getParent();
			metaFile = FileLoadHandler.openMetaFile(filename, parentPath);

			ScenarioSuite scenarioSuite = metaFile.getScenarioSuite();
			BatchSuite batchSuite = metaFile.getBatchSuite();
			ComponentSuite componentSuite = metaFile.getComponentSuite();
			testUnitSuite = metaFile.getTestUnitSuite();

			if (scenarioSuite != null)
				inputObjectRegistry.registerScenarioSuite(scenarioSuite);
			if (batchSuite != null)
				inputObjectRegistry.registerBatchSuite(batchSuite);
			if (componentSuite != null)
				inputObjectRegistry.registerComponentSuite(componentSuite);
			if (testUnitSuite != null)
				inputObjectRegistry.registerTestUnitSuite(testUnitSuite);

			logger.debug("LoadTestSuite loaded....");

			for (TestUnit testUnit : testUnitSuite.getTestUnits()) {
				if (testUnit.getID() == testUnitID) {
					testUnitExecuter = new TestUnitExecuter(testUnit, this,
							inputObjectRegistry);
					testUnitExecuter.createScenarios();
					try {
						testUnitExecuter.start();
					} catch (NotInitializedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			try {
				synchronized (this) {
					logger.debug("Wait for everything to finish");
					this.wait();
					logger.debug("Thread was notified!");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			logger.debug("End of Application");
			System.exit(0);

		} catch (JAXBException e) {
			logger.error(filename + " is not a valid XML");
		} catch (DublicateObjectException e) {
			e.printStackTrace();
		}

	}

	public void displayHelp() {
		System.out.println("Tag Streamer 2.0 Console Help");
		System.out.println("usage: TagStreamer.exe [filename] [#testunit]");
		System.out.println("");
	}

	@Override
	public void nextIteration(int iteration) {
		// TODO Auto-generated method stub
	}

	@Override
	public void testUnitFinishedEvent() {
		logger.debug("TestUnit reported that it is finished");
		synchronized (this) {
			this.notify();
		}
	}

	public void cancel() {
		testUnitExecuter.stop();
	}

}
