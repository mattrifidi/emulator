package org.rifidi.emulator.reader.llrp.module;

import java.util.Observable;
import java.util.Observer;

import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.reader.llrp.report.LLRPReportController;
import org.rifidi.emulator.reader.llrp.report.LLRPReportControllerFactory;

import edu.uark.csce.llrp.ConnectionAttemptEvent;
import edu.uark.csce.llrp.ReaderEventNotificationData;

/**
 * ConnectionEventObserver sends out an event notification upon connection
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class ConnectionEventObserver implements Observer {

	ControlSignal<Boolean> connectionSignal;

	String nameOfReader;

	public ConnectionEventObserver(ControlSignal<Boolean> connectionSignal,
			String nameOfReader) {
		this.connectionSignal = connectionSignal;
		this.nameOfReader = nameOfReader;
		connectionSignal.addObserver(this);

	}

	public void update(Observable arg0, Object arg1) {
		if (connectionSignal.getControlVariableValue()) {
			LLRPReportController controller = LLRPReportControllerFactory
					.getInstance().getReportController(nameOfReader);
			controller.unlock();
			ReaderEventNotificationData rend = new ReaderEventNotificationData();
			ConnectionAttemptEvent cae = new ConnectionAttemptEvent();
			cae.setStatus((short)0);
			rend.setConnectionAttemptEventParam(cae);
			controller.sendEvent(rend);

		}
	}

}
