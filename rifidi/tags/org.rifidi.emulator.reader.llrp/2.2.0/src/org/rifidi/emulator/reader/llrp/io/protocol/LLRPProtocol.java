package org.rifidi.emulator.reader.llrp.io.protocol;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.io.protocol.Protocol;
import org.rifidi.emulator.io.protocol.ProtocolValidationException;
import org.rifidi.emulator.reader.llrp.util.LLRPUtilities;

/**
 * This class handles the transition to and from the TCP socket buffer for LLRP
 * 
 * @author kyle
 * 
 */
public class LLRPProtocol implements Protocol {

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(LLRPProtocol.class);

	public byte[] addProtocol(byte[] data) {
		return data;
	}

	/**
	 * Because LLRP is a binary format, we don't know whether or not a stream of
	 * bits that we recieved on the TCP socket buffer is a single LLRP message
	 * or multiple messages until we examine it. This method examines a chunk of
	 * bytes that the socket recieved and parses them into a list of LLRP
	 * message chunk. That is, each element in the list is a complete LLRP
	 * message, though we don't know which one.
	 */
	public List<byte[]> removeProtocol(byte[] data)
			throws ProtocolValidationException {

		ByteArrayInputStream bais = new ByteArrayInputStream(data);

		ArrayList<byte[]> retVal = new ArrayList<byte[]>();
		boolean more = true;
		while (more) {
			if (bais.available() > 0) {
				byte[] first = new byte[6];
				bais.read(first, 0, 6);
				int sizeOfMsg = LLRPUtilities.calculateLLRPMessageLength(first);
				byte[] msg = new byte[sizeOfMsg];
				byte[] rest;

				rest = new byte[sizeOfMsg - 6];
				bais.read(rest, 0, sizeOfMsg - 6);
				System.arraycopy(first, 0, msg, 0, 6);
				System.arraycopy(rest, 0, msg, 6, rest.length);
				retVal.add(msg);
			}
			else {
				more = false;
			}
		}
		return retVal;
	}

}
