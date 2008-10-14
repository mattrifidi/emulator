package org.rifidi.ui.streamer;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.rifidi.ui.streamer.console.StreamerConsole;
import org.rifidi.ui.streamer.workbench.ApplicationWorkbenchAdvisor;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	private boolean isConsoleVersion = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	public Object start(IApplicationContext context) throws Exception {

		// decide which version to start
		
		Object o = context.getArguments().get("application.args");
		String[] args = null;
		if (o instanceof String[]) {
			args = (String[]) o;

			if (args.length > 0) {
				isConsoleVersion = true;
				StreamerConsole.startConsole(args);
			} else {
				Display display = PlatformUI.createDisplay();
				try {
					int returnCode = PlatformUI.createAndRunWorkbench(display,
							new ApplicationWorkbenchAdvisor());
					if (returnCode == PlatformUI.RETURN_RESTART)
						return IApplication.EXIT_RESTART;
					else
						return IApplication.EXIT_OK;
				} finally {
					display.dispose();
				}
			}
		}
		return 0;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		if (!isConsoleVersion) {
			final IWorkbench workbench = PlatformUI.getWorkbench();
			if (workbench == null)
				return;
			final Display display = workbench.getDisplay();
			display.syncExec(new Runnable() {
				public void run() {
					if (!display.isDisposed())
						workbench.close();
				}
			});
		}
	}
}
