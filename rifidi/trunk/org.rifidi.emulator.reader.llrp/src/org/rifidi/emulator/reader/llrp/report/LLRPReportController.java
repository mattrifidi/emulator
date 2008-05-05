/*
 *  LLRPReportController.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.llrp.report;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.io.comm.Communication;
import org.rifidi.emulator.io.comm.CommunicationException;
import org.rifidi.emulator.io.comm.ip.tcpclient.TCPClientCommunication;
import org.rifidi.emulator.io.comm.ip.tcpserver.TCPServerCommunication;
import org.rifidi.emulator.reader.llrp.module.LLRPReaderSharedResources;
import org.rifidi.emulator.tags.enums.TagGen;
import org.rifidi.emulator.tags.impl.RifidiTag;

import edu.uark.csce.llrp.AntennaID;
import edu.uark.csce.llrp.C1G2CRC;
import edu.uark.csce.llrp.C1G2PC;
import edu.uark.csce.llrp.ChannelIndex;
import edu.uark.csce.llrp.CloseConnectionResponse;
import edu.uark.csce.llrp.ConnectionCloseEvent;
import edu.uark.csce.llrp.EPC96;
import edu.uark.csce.llrp.EPCData;
import edu.uark.csce.llrp.FirstSeenTimestampUTC;
import edu.uark.csce.llrp.FirstSeenTimestampUptime;
import edu.uark.csce.llrp.InventoryParameterSpecID;
import edu.uark.csce.llrp.KeepAlive;
import edu.uark.csce.llrp.LLRPStatus;
import edu.uark.csce.llrp.LastSeenTimestampUTC;
import edu.uark.csce.llrp.LastSeenTimestampUptime;
import edu.uark.csce.llrp.PeakRSSI;
import edu.uark.csce.llrp.ROAccessReport;
import edu.uark.csce.llrp.ReaderEventNotification;
import edu.uark.csce.llrp.ReaderEventNotificationData;
import edu.uark.csce.llrp.SpecIndex;
import edu.uark.csce.llrp.TagReportData;
import edu.uark.csce.llrp.TagSeenCount;
import edu.uark.csce.llrp.UTCTimestamp;

/**
 * This class will form a report when triggered, and send it out over the
 * communication it has been given.
 * 
 * @author Matthew Dean - matt@pramari.com
 */
public class LLRPReportController {

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(LLRPReportController.class);

	/**
	 * The communication that this ReportController has.
	 */
	private Communication comm;
	
	private boolean locked=false;

	/**
	 * Makes a new LLRPReportController.
	 * 
	 * @param comm
	 */
	LLRPReportController() {
	}

	/**
	 * Sends a report of tags TagList using the parameters specified in format.
	 * 
	 * @param tagList
	 *            The list of tags that will be sent out.
	 * @param format
	 *            The format that defines how the report looks.
	 * @param ROSpecID
	 *            The ROSpec that generated the report.
	 */
	public void sendAllReports(LLRPReaderSharedResources llrpsr) {

		byte[] retVal = null;
		ROAccessReport report = new ROAccessReport();
		for (TagReportData trd : llrpsr.getTagReportDataEntries()) {
			report.addTagReportDataParam(trd);
		}

		try {
			retVal = report.serialize();
			if (comm.isConnected() && !this.locked) {
				logger.debug("Sending a report...");
				comm.sendBytes(retVal);
				synchronized (this) {
					llrpsr.getTagReportDataEntries().clear();
				}
			} else {
				logger.error("There is a lost report");
			}
		} catch (CommunicationException e) {
			logger.error("There was a problem when "
					+ "trying to send the report message");
		} catch (IOException e) {
			logger.error("There was a problem when "
					+ "trying to serialize the Report message");
			e.printStackTrace();
		}
	}

	public void sendReport(TagReportData trd) {
		byte[] retVal = null;
		ROAccessReport report = new ROAccessReport();
		report.addTagReportDataParam(trd);
		try {
			retVal = report.serialize();
			if (comm.isConnected() && !this.locked) {
				logger.debug("Sending a Report...");
				comm.sendBytes(retVal);
			} else {
				logger.error("Could not send report");
			}
		} catch (CommunicationException e) {
			logger.error("There was a problem when "
					+ "trying to send the report message");
		} catch (IOException e) {
			logger.error("There was a problem when "
					+ "trying to serialize the Report message");
			e.printStackTrace();
		}
	}

	public static TagReportData formatTagReport(ROReportFormat format,
			RifidiTag tag, int ROSpecID, int specIndex,
			LLRPReaderSharedResources llrpsr) {

		if (format == null) {
			format = llrpsr.getProperties().roReportFormat_Global;
		}

		TagReportData trd = new TagReportData();

		byte[] bytes = tag.getTag().readId();
		if (bytes.length == 12) {
			EPC96 epcdata = new EPC96();
			epcdata.setData(bytes);
			trd.setEPCDataParam(epcdata);
		} else {
			// EPCTag
			EPCData epcdata = new EPCData();
			epcdata.setData(bytes);
			trd.setEPCDataParam(epcdata);
		}

		if (format.enableAntennaID) {
			AntennaID aid = new AntennaID();
			aid.setAntennaID((short) (tag.getAntennaLastSeen() + 1));
			trd.setAntennaIDParam(aid);
		}
		if (format.enableChannelIndex) {
			/* TODO: get channel index from property map */
			logger.debug("Channel Index reporting "
					+ "not yet supported.  Default is 0");
			ChannelIndex ci = new ChannelIndex();
			ci.setChannelIndex((short) 0);
			trd.setChannelIndexParam(ci);
		}
		if (format.enableFirstSeenTimestamp) {
			long readTime = tag.getDiscoveryDate().getTime();

			if (llrpsr.getProperties().hasUTCClockCapabilities) {
				FirstSeenTimestampUTC fstutc = new FirstSeenTimestampUTC();
				fstutc.setMicroseconds(readTime * 1000);
				trd.setFirstSeenTimestampUTCParam(fstutc);

			} else {
				FirstSeenTimestampUptime fstu = new FirstSeenTimestampUptime();

				fstu.setMicroseconds(llrpsr.getUptime(readTime) * 1000);
				trd.setFirstSeenTimestampUptimeParam(fstu);

			}

		}
		if (format.enableInventoryParamaterSpecID) {
			/* TODO: IPSpecID not yet supported */
			logger.debug("Inventory Paramater SpecID"
					+ " not yet supported.  Default is 0");
			InventoryParameterSpecID ipsid = new InventoryParameterSpecID();
			ipsid.setInventoryParamSpecID((short) 0);
			trd.setInventoryParameterSpecIDParam(ipsid);

		}
		if (format.enableLastSeenTimestamp) {
			long readTime = tag.getLastSeenDate().getTime();

			if (llrpsr.getProperties().hasUTCClockCapabilities) {
				LastSeenTimestampUTC lstutc = new LastSeenTimestampUTC();
				lstutc.setMicroseconds(readTime * 1000);
				trd.setLastSeenTimestampUTCParam(lstutc);

			} else {
				LastSeenTimestampUptime lstu = new LastSeenTimestampUptime();
				lstu.setMicroseconds(llrpsr.getUptime(readTime) * 1000);
				trd.setLastSeenTimestampUptimeParam(lstu);
			}

		}
		if (format.enablePeakRSSI) {
			/* TODO: get peakRSSI from property map */
			logger.debug("PeakRSSI reporting not yet supported.  Default is 0");
			PeakRSSI prssi = new PeakRSSI();
			prssi.setPeakRSSI((byte) 0);
			trd.setPeakRSSIParam(prssi);

		}
		if (format.enableROSpecID) {
			edu.uark.csce.llrp.ROSpecID rosid = new edu.uark.csce.llrp.ROSpecID();
			rosid.setROSpecID(ROSpecID);
			trd.setROSpecIDParam(rosid);

		}
		if (format.enableSpecIndex) {
			SpecIndex si = new SpecIndex();

			si.setSpecIndex((short) specIndex);
			trd.setSpecIndexParam(si);

		}
		if (format.enableTagSeenCount) {
			TagSeenCount tsc = new TagSeenCount();
			tsc.setTagCount(tag.getReadCount().shortValue());
			trd.setTagSeenCountParam(tsc);
		}

		if (tag.getTagGen() == TagGen.GEN2) {
			/* TODO: hardcoded Air Protocol Tag Data for C1G2 */
			if (format.enableCRC) {
				C1G2CRC crc = new C1G2CRC();
				// TODO
				// crc.setCRC(Short.parseShort(tag.getCRC()));
				crc.setCRC((short) 0);
				trd.addAirProtocolTagDataParam(crc);
			}
			if (format.enablePC) {
				C1G2PC pc = new C1G2PC();
				// pc.setPC((short) tag.getTagProtocol());
				pc.setPC((short) 0);
				trd.addAirProtocolTagDataParam(pc);
			}
		}
		return trd;
	}

	/**
	 * Sends out an event notification message
	 * 
	 * @param event
	 * @return true if sent successfully, false otherwise
	 */
	public boolean sendEvent(ReaderEventNotificationData event) {
		ReaderEventNotification ren = new ReaderEventNotification();
		ren.setReaderEventNotificationDataParam(event);
		UTCTimestamp utcTimestamp = new UTCTimestamp();
		utcTimestamp.setMicroseconds(System.currentTimeMillis() * 1000);
		event.setTimestampParam(utcTimestamp);

		try {
			byte[] message = ren.serialize();
			if (comm.isConnected() && !this.locked)
				comm.sendBytes(message);
			else {
				// TODO Handle with lost events. Invent a Buffer architecture
				logger.error("There is a lost event");
			}
			return true;
		} catch (IOException e) {
			logger.debug(e.getMessage());
			return false;
		} catch (CommunicationException e) {
			logger.debug(e.getMessage());
			return false;
		}
	}

	public boolean sendCloseMessage() {
		this.lock();
		CloseConnectionResponse ccr = new CloseConnectionResponse();
		LLRPStatus status = new LLRPStatus();
		status.setErrorCode((short) 0);
		ccr.setLLRPStatusParam(status);

		ReaderEventNotification ren = new ReaderEventNotification();
		ReaderEventNotificationData rend = new ReaderEventNotificationData();
		ConnectionCloseEvent cce = new ConnectionCloseEvent();
		rend.setConnectionCloseEventParam(cce);
		ren.setReaderEventNotificationDataParam(rend);

		try {
			byte[] message = ccr.serialize();
			byte[] event = ren.serialize();
			if (comm.isConnected()) {
				comm.sendBytes(message);
				comm.sendBytes(event);
				/*
				 * TODO: this sleep is required because sometimes the
				 * communication shuts down before the event can be sent out,
				 * which is a big problem. However, this is a hack, because
				 * something else could put a message on the buffer during this
				 * sleep time, and then a message would be sent out after the
				 * close connection event, which violates the spec. Another
				 * problem is that the 1 second this thread sleeps may not be
				 * long enough, it depends on the thread scheduler, which is
				 * bad.
				 */
				if (comm instanceof TCPServerCommunication) {
					TCPServerCommunication tsc = (TCPServerCommunication) comm;
					Thread.sleep(500);
					tsc.suspendAddingToSendBuffer();
					Thread.sleep(500);
					logger.debug("disconnecting...");
					comm.disconnect();
					tsc.resumeAddingToSendBuffer();
				} else if (comm instanceof TCPClientCommunication) {
					TCPClientCommunication tsc = (TCPClientCommunication) comm;
					Thread.sleep(500);
					tsc.suspendAddingToSendBuffer();
					Thread.sleep(500);
					logger.debug("disconnecting...");
					comm.disconnect();
					tsc.resumeAddingToSendBuffer();
				}
			} else {
				// TODO Handle with lost events. Invent a Buffer architecture
				logger.error("close connection cannot send");
			}
			return true;
		} catch (IOException e) {
			logger.debug(e.getMessage());
			return false;
		} catch (CommunicationException e) {
			logger.debug(e.getMessage());
			return false;
		} catch (InterruptedException e) {
			logger.debug("Problem while disconnecting");
			return false;
		}

	}

	/**
	 * This method sends out a Keepalive message.
	 * 
	 * @return true if send was successful, false otherwise
	 */
	public boolean sendKeepAlive() {
		KeepAlive ka = new KeepAlive();

		try {
			byte[] message = ka.serialize();
			if (comm.isConnected())
				comm.sendBytes(message);
			else {
				// TODO Handle with lost events. Invent a Buffer architecture
				logger.error("keepalive could not be sent");
			}
			return true;
		} catch (IOException e) {
			logger.debug(e.getMessage());
			return false;
		} catch (CommunicationException e) {
			logger.debug(e.getMessage());
			return false;
		}

	}

	/**
	 * Gets the UTC Time.
	 * 
	 * @param dateString
	 * @param timeString
	 * @return time in milliseconds
	 */
	@SuppressWarnings("deprecation")
	private static long getUTCTimeInMillis(String dateString, String timeString) {

		/*
		 * TODO: baindaid solution until radio saves and returns java date and
		 * time objects
		 */
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

		Date date, time = null;
		try {
			date = dateFormat.parse(dateString);
			time = timeFormat.parse(timeString);
		} catch (ParseException e) {
			logger
					.error("There was a problem converting the date or time string into a Date object");
			date = new Date();
			time = new Date();
		}

		GregorianCalendar gc = new GregorianCalendar(date.getYear(), date
				.getMonth(), date.getDate(), time.getHours(),
				time.getMinutes(), time.getSeconds());

		return gc.getTimeInMillis();
	}

	/**
	 * @return the comm
	 */
	public Communication getComm() {
		return comm;
	}

	/**
	 * @param comm
	 *            the comm to set
	 */
	public void setComm(Communication comm) {
		this.comm = comm;
	}
	
	public void unlock(){
		locked=false;
	}
	
	public void lock(){
		locked=true;
	}
}
