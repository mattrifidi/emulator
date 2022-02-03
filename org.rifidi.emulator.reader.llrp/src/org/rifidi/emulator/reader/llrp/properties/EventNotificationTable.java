package org.rifidi.emulator.reader.llrp.properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The EventNotificationStateTable holds boolean values that tell whether or not
 * an event is generated for each of the event types
 * 
 * @author kyle
 * 
 */
public class EventNotificationTable {

	private boolean hoppingToNextChanel;

	private boolean GPIEvent;

	private boolean ROSpecEvent;

	private boolean reportBufferFillWarning;

	private boolean readerExceptionEvent;

	private boolean RFSurveyEvent;

	private boolean AISpecEvent;

	private boolean AISpecEventWithSingulationDetails;

	private boolean antennaEvent;

	public final static int HOPPING_TO_NEXT_CHANNEL_TYPE = 0;

	public final static int GPI_EVENT_TYPE = 1;

	public final static int RO_SPEC_EVENT_TYPE = 2;

	public final static int REPORT_BUFFER_FILL_WARNING_TYPE = 3;

	public final static int READER_EXCEPTION_EVENT_TYPE = 4;

	public final static int RF_SURVEY_EVENT_TYPE = 5;

	public final static int AI_SPEC_EVENT_TYPE = 6;

	public final static int AI_SPEC_EVENT_WITH_SINGULATION_DETAILS_TYPE = 7;

	public final static int ANTENNNA_EVENT_TYPE = 8;

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory.getLog(EventNotificationTable.class);

	public void setEventNotificaiton(int eventType, boolean notificationState) {
		switch (eventType) {

		case HOPPING_TO_NEXT_CHANNEL_TYPE:
			this.hoppingToNextChanel = notificationState;
			break;
		case GPI_EVENT_TYPE:
			this.GPIEvent = notificationState;
			break;
		case RO_SPEC_EVENT_TYPE:
			this.ROSpecEvent = notificationState;
			break;
		case REPORT_BUFFER_FILL_WARNING_TYPE:
			this.reportBufferFillWarning = notificationState;
			break;
		case READER_EXCEPTION_EVENT_TYPE:
			this.readerExceptionEvent = notificationState;
			break;
		case RF_SURVEY_EVENT_TYPE:
			this.RFSurveyEvent = notificationState;
			break;
		case AI_SPEC_EVENT_TYPE:
			this.AISpecEvent = notificationState;
			break;
		case AI_SPEC_EVENT_WITH_SINGULATION_DETAILS_TYPE:
			this.AISpecEventWithSingulationDetails = notificationState;
			break;
		case ANTENNNA_EVENT_TYPE:
			this.antennaEvent = notificationState;
			break;
		default:
			logger
					.error(eventType
							+ " is an Invalid event type.  Valid event types range is 0-8");
			break;
		}
	}

	public boolean getEventNotificaiton(int eventType) {
		switch (eventType) {

		case HOPPING_TO_NEXT_CHANNEL_TYPE:
			return this.hoppingToNextChanel;
		case GPI_EVENT_TYPE:
			return this.GPIEvent;
		case RO_SPEC_EVENT_TYPE:
			return this.ROSpecEvent;
		case REPORT_BUFFER_FILL_WARNING_TYPE:
			return this.reportBufferFillWarning;
		case READER_EXCEPTION_EVENT_TYPE:
			return this.readerExceptionEvent;
		case RF_SURVEY_EVENT_TYPE:
			return this.RFSurveyEvent;
		case AI_SPEC_EVENT_TYPE:
			return this.AISpecEvent;
		case AI_SPEC_EVENT_WITH_SINGULATION_DETAILS_TYPE:
			return this.AISpecEventWithSingulationDetails;
		case ANTENNNA_EVENT_TYPE:
			return this.antennaEvent;

		default:
			logger
					.error(eventType
							+ " is an Invalid event type.  Valid event types range is 0-8");
			return false;
		}
	}

}
