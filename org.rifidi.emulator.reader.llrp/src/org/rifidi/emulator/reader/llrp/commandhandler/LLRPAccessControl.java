package org.rifidi.emulator.reader.llrp.commandhandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.manager.ClientCallbackInterface;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.llrp.accessspec._AccessSpec;
import org.rifidi.emulator.reader.llrp.accessspec._OpSpec;
import org.rifidi.emulator.reader.llrp.airprotocol.AirProtocolEnums;
import org.rifidi.emulator.reader.llrp.airprotocol.c1g2._C1G2BlockErase;
import org.rifidi.emulator.reader.llrp.airprotocol.c1g2._C1G2BlockWrite;
import org.rifidi.emulator.reader.llrp.airprotocol.c1g2._C1G2Kill;
import org.rifidi.emulator.reader.llrp.airprotocol.c1g2._C1G2LLRPCapabilities;
import org.rifidi.emulator.reader.llrp.airprotocol.c1g2._C1G2Lock;
import org.rifidi.emulator.reader.llrp.airprotocol.c1g2._C1G2LockPayload;
import org.rifidi.emulator.reader.llrp.airprotocol.c1g2._C1G2Read;
import org.rifidi.emulator.reader.llrp.airprotocol.c1g2._C1G2TagSpec;
import org.rifidi.emulator.reader.llrp.airprotocol.c1g2._C1G2TargetTag;
import org.rifidi.emulator.reader.llrp.airprotocol.c1g2._C1G2Write;
import org.rifidi.emulator.reader.llrp.module.LLRPReaderSharedResources;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.sharedrc.radio.Antenna;
import org.rifidi.emulator.reader.sharedrc.radio.generic.GenericRadio;

import edu.uark.csce.llrp.AccessCommand;
import edu.uark.csce.llrp.AccessSpec;
import edu.uark.csce.llrp.AddAccessSpec;
import edu.uark.csce.llrp.AddAccessSpecResponse;
import edu.uark.csce.llrp.C1G2BlockErase;
import edu.uark.csce.llrp.C1G2BlockWrite;
import edu.uark.csce.llrp.C1G2Kill;
import edu.uark.csce.llrp.C1G2Lock;
import edu.uark.csce.llrp.C1G2OpSpec;
import edu.uark.csce.llrp.C1G2Read;
import edu.uark.csce.llrp.C1G2TagSpec;
import edu.uark.csce.llrp.C1G2TargetTag;
import edu.uark.csce.llrp.C1G2Write;
import edu.uark.csce.llrp.DeleteAccessSpec;
import edu.uark.csce.llrp.DeleteAccessSpecResponse;
import edu.uark.csce.llrp.DisableAccessSpec;
import edu.uark.csce.llrp.DisableAccessSpecResponse;
import edu.uark.csce.llrp.EnableAccessSpec;
import edu.uark.csce.llrp.EnableAccessSpecResponse;
import edu.uark.csce.llrp.ErrorMessage;
import edu.uark.csce.llrp.GetAccessSpecs;
import edu.uark.csce.llrp.GetAccessSpecsResponse;
import edu.uark.csce.llrp.LLRPStatus;
import edu.uark.csce.llrp.Message;

public class LLRPAccessControl {

	/*
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(LLRPAccessControl.class);

	/**
	 * This is the handler method for adding an access spec to the reader.
	 * Access Specs are stored in the AccessSpecs list in the shared resources
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject addAccessSpec(CommandObject arg,
			AbstractReaderSharedResources asr) {

		logger.debug("inside access control");
		LLRPReaderSharedResources llrpsr = (LLRPReaderSharedResources) asr;

		// status param to send back with the response message
		LLRPStatus stat = new LLRPStatus();
		stat.setErrorCode((short) 0);

		AddAccessSpecResponse aasr = new AddAccessSpecResponse();

		// decode the message
		Message m = null;
		byte[] rawMsg = (byte[]) arg.getArguments().get(0);
		try {
			m = Message.receive(new ByteArrayInputStream(rawMsg));
		} catch (IOException e) {
			stat.setErrorCode((short) 101);
			stat.setErrorDescription("Malformed ADD_ACCESSSPEC Message");
			logger.error("error when deserializing the ADD_ACCESSSPEC");
			aasr.setLLRPStatusParam(stat);
			arg.getReturnValue().add(aasr);
			return arg;
		}

		// Get the access spec from the incoming message
		AddAccessSpec aas = (AddAccessSpec) m;
		AccessSpec specIn = aas.getAccessSpecParam();
		_AccessSpec aSpec = null;

		int id = specIn.getAccessSpecID();
		short antennaID = specIn.getAntennaId();

		int stopTrig = specIn.getAccessSpecStopTriggerParam()
				.getAccessSpecStopTrigger();
		int opCount = 0;
		int rospecID = specIn.getROSpecID();

		// if we have a count based stop trigger, get the number of times it
		// should count
		if (stopTrig == 1) {
			opCount = specIn.getAccessSpecStopTriggerParam()
					.getOperationCountValue();
		}
		if (stopTrig < 0 || stopTrig > 1) {
			stat.setErrorCode((short) 100);
			stat
					.setErrorDescription("Stop Trigger for access spec is invalid.");
		}

		// if the client sent a report spec, add it here. Otherwise the global
		// report spec in the properties will be used.
		int accessReportTrigger = -1;
		if (specIn.getAccessReportSpecParam() != null) {
			accessReportTrigger = specIn.getAccessReportSpecParam()
					.getAccessReportTrigger();
		}
		
		// If the protocol is C1G2
		if (specIn.getProtocolId() == (byte)1) {
			C1G2TagSpec tagspecIn = (C1G2TagSpec) specIn
					.getAccessCommandParam().getTagSpecParam();

			_C1G2TagSpec tagspec = createC1G2TagSpec(tagspecIn, stat);

			// Set up each opSpec in the AccessSpec
						
			int antenna = Math.max((antennaID - 1), 0);
			
			ArrayList<_OpSpec> ops = createC1G2OpSpecList(specIn
					.getAccessCommandParam(), (_C1G2LLRPCapabilities) llrpsr
					.getProperties().getAirProtocolLLRPCapability(
							AirProtocolEnums.C1G2), stat,
					 llrpsr.getRadio(), llrpsr.getCallbackManager(), llrpsr.getRadio().getAntennas().get(antenna));

			// Create new acccessSpec
			aSpec = new _AccessSpec(id, antennaID, AirProtocolEnums.C1G2,
					stopTrig, opCount, rospecID, tagspec, ops,
					accessReportTrigger, llrpsr, specIn);
		} else {
			stat.setErrorCode((short) 101);
			stat.setErrorDescription("Cannon have AccessSpec "
					+ "for unknown Air Protocol");
		}

		boolean sucessful = false;
		// if there were no errors so far, add the accessSpec
		if (aSpec != null && stat.getErrorCode() == 0) {
			aSpec.currentState = false;
			sucessful = llrpsr.accessSpecs.addAccessSpec(aSpec);

		}

		// if there were no erros when adding the accessspec
		if (sucessful) {

			// Change ConfigurationStateVariable
			llrpsr.getProperties().LLRPConfiguraitonStateVariable++;
			stat.setErrorCode((short) 0);
			stat.setErrorDescription("Success");
		}

		aasr.setMessageID(aas.getMessageID());
		aasr.setLLRPStatusParam(stat);
		arg.getReturnValue().add(aasr);
		return arg;
	}

	/**
	 * This method puts the access spec into an enabled state so that it is
	 * executed
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject enableAccessSpec(CommandObject arg,
			AbstractReaderSharedResources asr) {

		boolean error = false;

		LLRPStatus stat = new LLRPStatus();

		LLRPReaderSharedResources llrpsr = (LLRPReaderSharedResources) asr;

		EnableAccessSpecResponse easr = new EnableAccessSpecResponse();

		// decode the message
		Message m = null;
		byte[] rawMsg = (byte[]) arg.getArguments().get(0);
		try {
			m = Message.receive(new ByteArrayInputStream(rawMsg));
		} catch (IOException e) {
			stat.setErrorCode((short) 101);
			stat.setErrorDescription("Malformed ENABLE_ACCESSSPEC Message");
			logger.error("error when deserializing the ENABLE_ACCESSSPEC");
			easr.setLLRPStatusParam(stat);
			arg.getReturnValue().add(easr);
			return arg;
		}

		EnableAccessSpec eas = (EnableAccessSpec) m;

		_AccessSpec a = llrpsr.accessSpecs.getAccessSpec(eas.getAccessSpecID());

		if (a != null) {
			if (!a.currentState) {
				a.currentState = true;
			} else {
				error = true;
				stat.setErrorCode((short) 100);
				stat.setErrorDescription("AccessSpec was already enabled");
			}
		} else {
			error = true;
			stat.setErrorCode((short) 100);
			stat.setErrorDescription("No Access Spec with ID "
					+ eas.getAccessSpecID() + " has been added");
		}

		if (!error) {

			stat.setErrorCode((short) 0);
			stat.setErrorDescription("Success");
		}
		easr.setLLRPStatusParam(stat);

		easr.setMessageID(eas.getMessageID());
		arg.getReturnValue().add(easr);
		return arg;
	}

	/**
	 * This method disables the access spec, so that it is not executed
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject disableAccessSpec(CommandObject arg,
			AbstractReaderSharedResources asr) {

		LLRPReaderSharedResources llrpsr = (LLRPReaderSharedResources) asr;
		LLRPStatus stat = new LLRPStatus();

		boolean error = false;

		DisableAccessSpecResponse dasr = new DisableAccessSpecResponse();

		// decode the message
		Message m = null;
		byte[] rawMsg = (byte[]) arg.getArguments().get(0);
		try {
			m = Message.receive(new ByteArrayInputStream(rawMsg));
		} catch (IOException e) {
			stat.setErrorCode((short) 101);
			stat.setErrorDescription("Malformed DISABLE_ACCESSSPEC Message");
			logger.error("error when deserializing the DISABLE_ACCESSSPEC");
			dasr.setLLRPStatusParam(stat);
			arg.getReturnValue().add(dasr);
			return arg;
		}

		DisableAccessSpec das = (DisableAccessSpec) m;

		_AccessSpec a = llrpsr.accessSpecs.getAccessSpec(das.getAccessSpecID());

		if (a != null) {
			if (a.currentState) {
				a.currentState = false;
			} else {
				error = true;
				stat.setErrorCode((short) 100);
				stat.setErrorDescription("AccessSpec was already disabled");
			}
		} else {
			error = true;
			stat.setErrorCode((short) 100);
			stat.setErrorDescription("No Access Spec with ID "
					+ das.getAccessSpecID() + " has been added");
		}

		if (!error) {
			stat.setErrorCode((short) 0);
			stat.setErrorDescription("Success");
		}
		dasr.setLLRPStatusParam(stat);
		dasr.setMessageID(das.getMessageID());
		arg.getReturnValue().add(dasr);
		return arg;
	}

	/**
	 * This method deletes the access spec from the reader
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject deleteAccessSpec(CommandObject arg,
			AbstractReaderSharedResources asr) {

		LLRPReaderSharedResources llrpsr = (LLRPReaderSharedResources) asr;

		LLRPStatus stat = new LLRPStatus();

		DeleteAccessSpecResponse dasr = new DeleteAccessSpecResponse();

		// decode the message
		Message m = null;
		byte[] rawMsg = (byte[]) arg.getArguments().get(0);
		try {
			m = Message.receive(new ByteArrayInputStream(rawMsg));
		} catch (IOException e) {
			stat.setErrorCode((short) 101);
			stat.setErrorDescription("Malformed DELETE_ACCESSSPEC Message");
			logger.error("error when deserializing the DELETE_ACCESSSPEC");
			dasr.setLLRPStatusParam(stat);
			arg.getReturnValue().add(dasr);
			return arg;
		}

		DeleteAccessSpec das = (DeleteAccessSpec) m;

		boolean successful = false;

		successful = llrpsr.accessSpecs.removeAccessSpec(das.getAccessSpecID());

		if (successful) {
			llrpsr.getProperties().LLRPConfiguraitonStateVariable++;
			stat.setErrorCode((short) 0);
			stat.setErrorDescription("Sucessful");

		} else {
			stat.setErrorCode((short) 100);
			stat.setErrorDescription("There was a problem when deleting "
					+ "access spec with ID " + das.getAccessSpecID());
		}
		dasr.setLLRPStatusParam(stat);
		dasr.setMessageID(das.getMessageID());
		arg.getReturnValue().add(dasr);
		return arg;
	}

	public CommandObject getAccessSpecs(CommandObject arg,
			AbstractReaderSharedResources asr) {

		LLRPReaderSharedResources llrpsr = (LLRPReaderSharedResources) asr;

		LLRPStatus stat = new LLRPStatus();
		stat.setErrorCode((short) 0);
		stat.setErrorDescription("Success");

		GetAccessSpecsResponse gasr = new GetAccessSpecsResponse();

		// decode the message
		Message m = null;
		byte[] rawMsg = (byte[]) arg.getArguments().get(0);
		try {
			m = Message.receive(new ByteArrayInputStream(rawMsg));
		} catch (IOException e) {
			stat.setErrorCode((short) 101);
			stat.setErrorDescription("Malformed GET_ACCESSSPECS Message");
			logger.error("error when deserializing the GET_ACCESSSPECS");
			gasr.setLLRPStatusParam(stat);
			arg.getReturnValue().add(gasr);
			return arg;
		}

		GetAccessSpecs gas = (GetAccessSpecs) m;

		for (int i = 0; i < llrpsr.accessSpecs.numAccessSpecs(); i++) {
			gasr.addAccessSpecParam(llrpsr.accessSpecs.getAccessSpecAt(i)
					.getLtkAccesSpec());
		}

		gasr.setLLRPStatusParam(stat);
		gasr.setMessageID(gas.getMessageID());
		arg.getReturnValue().add(gasr);
		return arg;
	}
	
	public CommandObject clientRequestOpResponse(CommandObject arg,
			AbstractReaderSharedResources asr) {
		
		LLRPReaderSharedResources llrpsr = (LLRPReaderSharedResources)asr;
		
		LLRPStatus stat = new LLRPStatus();
		stat.setErrorCode((short)100);
		
		if(llrpsr.getProperties().supportsClientRequestOpSpec){
			//TODO: process message
		}else{
			ErrorMessage m = new ErrorMessage();
			stat.setErrorDescription("This reader does not support client op requests");
			m.setLLRPStatusParam(stat);
			arg.getReturnValue().add(m);
		}

		return arg;
	}

	/**
	 * This method creates a C1G2TagSpec object
	 * 
	 * @param tagspecIn
	 *            The LLRPTK tagspec object
	 * @param stat
	 *            the staus of the return value
	 * @return
	 */
	private _C1G2TagSpec createC1G2TagSpec(C1G2TagSpec tagspecIn,
			LLRPStatus stat) {
		// Set up Tagspecs
		_C1G2TagSpec tagspec;
		_C1G2TargetTag pattern1 = createTagPattern(tagspecIn.getTagPattern1());

		if (tagspecIn.getTagPattern2() == null) {
			tagspec = new _C1G2TagSpec(pattern1);
		} else {
			_C1G2TargetTag pattern2 = createTagPattern(tagspecIn
					.getTagPattern1());


			tagspec = new _C1G2TagSpec(pattern1, pattern2);
		}

		return tagspec;
	}

	/**
	 * Create a C1G2TargetTag object for use in creating the C1G2TagSpec
	 * 
	 * @param ts
	 * @return
	 */
	private _C1G2TargetTag createTagPattern(C1G2TargetTag ts) {

		return new _C1G2TargetTag(ts.getMB(), ts.getPointer(), ts.getMask(), ts
				.getData(), ts.getMatch());

	}

	/**
	 * This method creates a list of opspecs to be executed
	 * 
	 * @param accessCommand
	 *            The incoming llrptk accessCommand
	 * @param c1g2Capabilities
	 *            The capabilities of C1G2
	 * @param stat
	 *            the status of the return value
	 * @return A list of OPSpecs to be executed
	 */
	private ArrayList<_OpSpec> createC1G2OpSpecList(
			AccessCommand accessCommand,
			_C1G2LLRPCapabilities c1g2Capabilities, LLRPStatus stat,
			GenericRadio radio, ClientCallbackInterface callback, Antenna antenna) {

		int n = accessCommand.getNumOpSpecParams();
		ArrayList<_OpSpec> ops = new ArrayList<_OpSpec>();
		for (int i = 0; i < n; i++) {
			C1G2OpSpec opSpec = (C1G2OpSpec) accessCommand.getOpSpecParam(i);

			_OpSpec operation = null;

			// If it is a read opspec
			if (opSpec instanceof C1G2Read) {
				C1G2Read readOp = (C1G2Read) opSpec;
				operation = new _C1G2Read(readOp.getOpSpecID(), readOp.getMB(),
						readOp.getWordPointer(), readOp.getWordCount(), readOp
								.getAccessPassword());
			}
			// if it is a write opspec
			else if (opSpec instanceof C1G2Write) {
				C1G2Write writeOp = (C1G2Write) opSpec;
				short[] writeData = new short[writeOp.getWriteDataCount()];
				for (int j = 0; j < writeData.length; j++) {
					writeData[j] = writeOp.getWriteDataElement(j);
				}
				operation = new _C1G2Write(writeOp.getOpSpecID(), writeOp
						.getMB(), writeOp.getWordPointer(), writeData, writeOp
						.getAccessPassword(), callback, antenna);
			}
			// if it is a kill opspec
			else if (opSpec instanceof C1G2Kill) {
				C1G2Kill killOp = (C1G2Kill) opSpec;
				operation = new _C1G2Kill(killOp.getOpSpecID(), killOp
						.getKillPassword());
			}
			// if it is a lock opspec
			else if (opSpec instanceof C1G2Lock) {
				C1G2Lock lockOp = (C1G2Lock) opSpec;
				ArrayList<_C1G2LockPayload> payload = new ArrayList<_C1G2LockPayload>();
				for (int j = 0; j < lockOp.getNumC1G2LockPayloadParams(); j++) {
					int privilege = lockOp.getC1G2LockPayloadParam(j)
							.getPrivilege();
					int data = lockOp.getC1G2LockPayloadParam(j).getDataField();
					if (_C1G2LockPayload.isValidPrivilegeAndData(privilege,
							data)) {
						_C1G2LockPayload p = new _C1G2LockPayload(lockOp
								.getOpSpecID(), privilege, data);
						payload.add(p);
					} else {
						stat.setErrorCode((short) 101);
						stat.setErrorDescription("A C1G2LockPayload had "
								+ "an invalid privilege or data value");
					}
				}
				operation = new _C1G2Lock(lockOp.getOpSpecID(), payload, lockOp
						.getAccessPassword());
			}
			// if it is a block erase op spec
			else if (opSpec instanceof C1G2BlockErase) {
				if (c1g2Capabilities.canSupportBlockErase) {
					C1G2BlockErase eraseOp = (C1G2BlockErase) opSpec;
					operation = new _C1G2BlockErase(eraseOp.getOpSpecID(),
							eraseOp.getMB(), eraseOp.getWordPointer(), eraseOp
									.getWordCount(), eraseOp
									.getAccessPassword());
				} else {
					stat.setErrorCode((short) 101);
					stat.setErrorDescription("This reader does "
							+ "not support the BlockErase");
				}

			}
			// if it is a blockwrite op spec
			else if (opSpec instanceof C1G2BlockWrite) {
				if (c1g2Capabilities.canSupportBlockWrite) {
					C1G2BlockWrite blockWriteOp = (C1G2BlockWrite) opSpec;
					short[] writeData = new short[blockWriteOp
							.getWriteDataCount()];
					for (int j = 0; j < writeData.length; j++) {
						writeData[j] = blockWriteOp.getWriteDataElement(j);
					}

					operation = new _C1G2BlockWrite(blockWriteOp.getOpSpecID(),
							blockWriteOp.getMB(),
							blockWriteOp.getWordPointer(), writeData,
							blockWriteOp.getAccessPassword());
				} else {
					stat.setErrorCode((short) 101);
					stat.setErrorDescription("This reader does "
							+ "not support the BlockWrite");
				}
			}

			// add opspec to arraylist
			if (operation != null) {
				ops.add(operation);
			}

			// if none of the above, there is an error
			else {
				stat.setErrorCode((short) 101);
				stat.setErrorDescription("An unkown C1G2 Opsec was added");
			}
		}
		return ops;

	}

}
