package org.rifidi.utilities.parasitism;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jme.util.GameTaskQueueManager;

/**
 * Generic IThreadParasiteHost implementation. Contains a static instance for
 * simplicity, but is NOT a singleton.
 * 
 * @author Jeremy Choens - "Ghost" - jeremy@pramari.com
 * 
 */
public class GenericParasiteHost implements IThreadParasiteHost {
	private static Log loggy = LogFactory.getLog(GenericParasiteHost.class);

	private static GenericParasiteHost singleton = null;

	private List<IThreadParasite> theHosted = new ArrayList<IThreadParasite>();

	private boolean paused = false;

	public GenericParasiteHost() {

	}

	public static GenericParasiteHost getStaticGenericHost() {
		if (singleton == null) {
			singleton = new GenericParasiteHost();
		}
		return singleton;
	}

	public void parasiticUpdate(final long time) {
		if (!paused) {
			GameTaskQueueManager.getManager().render(new Callable<Object>() {
				public Object call() throws Exception {
					for(IThreadParasite tp : theHosted){
						tp.update(time);
					}
					return new Object();
				}
			});
		}
	}

	public void pauseUnpause(boolean pause) {
		paused = pause;
	}

	public void registerParasite(IThreadParasite parasite, boolean autostart) {
		if (!theHosted.contains(parasite)) {
			if (parasite.getHost() != null) {
				loggy.info("Unregistering parasite from previous host.");
				IThreadParasiteHost host = parasite.getHost();
				host.removeParasite(parasite, false);
			}
			theHosted.add(parasite);
			if(autostart){
				loggy.debug("initing parasite");
				parasite.init();
			}
		} else {
			loggy
					.warn("IThreadParasite is already hosted by this GenericParasiteHost, ignoring autostart flag.");
		}
	}

	public void removeParasite(IThreadParasite parasite, boolean autostop) {
		if (!theHosted.contains(parasite)) {
			theHosted.remove(parasite);
			if(autostop){
				parasite.deInit();
			}
			
		} else {
			loggy
					.warn("IThreadParasite is not hosted by this GenericParasiteHost, ignoring autostop flag.");
		}

	}

	public void setEnablement(boolean enabled) {
		for(IThreadParasite tp : theHosted){
			if(enabled){
				tp.init();
			} else {
				tp.deInit();
			}
		}
	}
}
