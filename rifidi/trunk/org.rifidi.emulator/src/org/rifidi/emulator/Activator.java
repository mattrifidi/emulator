package org.rifidi.emulator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	public static BundleContext context;
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext contextt) throws Exception {
		System.out.println("Starting org.rifidi.emulator");
		context=contextt;
//		System.out.println("Starting Emulator");
//		try{
//			RifidiMain.main(new String[]{});
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
//		System.out.println("Done");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		System.out.println("Stopping org.rifidi.emulator");
	}

}
