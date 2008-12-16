package org.rifidi.emulator.reader.symbol.commandhandler;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.common.utilities.ByteAndHexConvertingUtility;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.sharedrc.radio.generic.GenericRadio;
import org.rifidi.emulator.reader.symbol.module.SymbolReaderSharedResources;
import org.rifidi.emulator.reader.symbol.tagbuffer.SymbolTagMemory;
import org.rifidi.tags.enums.TagGen;
import org.rifidi.tags.impl.RifidiTag;

/**
 * These are the handler methods for the bit encoded commands. Note that the
 * return values don't have the start of frame byte or the crc bytes. That will
 * be done in the formatter
 * 
 * @author Kyle
 */
public class BitEncodedCommands {

	/*
	 * The logger for this class.
	 */
	@SuppressWarnings("unused")
	private static Log logger = LogFactory.getLog(BitEncodedCommands.class);

	public CommandObject setSystemParameter(CommandObject arg,
			AbstractReaderSharedResources asr) {

		byte[] setSystemParam = { 0x04, 0x06, 0x27, 0x00 };

		arg.getReturnValue().add(setSystemParam);
		return arg;
	}

	public CommandObject setParameterBlock(CommandObject arg,
			AbstractReaderSharedResources asr) {
		/*
		 * TODO: this handler method may need some more functionality, because
		 * the command specifies tag filters and air protocols to use on
		 * different antennas
		 */
		byte[] setParamBlock = { 0x04, 0x06, 0x23, 0x00 };

		arg.getReturnValue().add(setParamBlock);
		return arg;
	}

	public CommandObject getReaderStatus(CommandObject arg,
			AbstractReaderSharedResources asr) {
		ArrayList<Byte> response = new ArrayList<Byte>();

		response.add((byte) 0x04); // unused
		response.add((byte) 0x25); // packet length: 37 bytes
		response.add((byte) 0x14); // command
		response.add((byte) 0x00); // status

		for (int i = 0; i < 8; i++) { // serial number
			response.add((byte) 0x00);
		}

		response.add((byte) 0x02); // version (major)
		response.add((byte) 0x00); // version (minor)
		response.add((byte) 0x12); // version (build)
		response.add((byte) 0x00); // reset flag

		response.add((byte) 0x00); // Ant Bit mask for port 1 & 5
		response.add((byte) 0x00); // Ant Bit mask for port 2 & 6
		response.add((byte) 0x00); // Ant Bit mask for port 3 & 7
		response.add((byte) 0x00); // Ant Bit mask for port 4 & 8

		GenericRadio rad = asr.getRadio();
		@SuppressWarnings("unused")
		int numOfAntennas = rad.getAntennas().size();

		// hard code for antenna 1 for right now
		response.add((byte) 0x01); // antenna status bit mask for port 1 & 5
		response.add((byte) 0x00); // antenna status bit mask for port 2 & 6
		response.add((byte) 0x00); // antenna status bit mask for port 3 & 7
		response.add((byte) 0x00); // antenna status bit mask for port 4 & 8

		response.add((byte) 0x00); // error message

		for (int i = 0; i < 10; i++) { // reserved bits
			response.add((byte) 0x00);
		}

		byte[] bytes = new byte[response.size()];

		for (int i = 0; i < response.size(); i++) {
			bytes[i] = response.get(i);
		}

		arg.getReturnValue().add(bytes);
		return arg;
	}

	public CommandObject readFullField(CommandObject arg,
			AbstractReaderSharedResources asr) {
		ArrayList<Byte> response = new ArrayList<Byte>();
		
		SymbolReaderSharedResources ssr = (SymbolReaderSharedResources) asr;

		byte[] command = (byte[]) arg.getArguments().get(0);
		
		SymbolTagMemory stm = (SymbolTagMemory)ssr.getTagMemory();
		
		Collection<RifidiTag> tags = stm.getTagReport();

		if (tags.size() != 0) {

			response.add((byte) 0x04); // not used
			response.add((byte) 0x00); // length - set later
			response.add((byte) 0x22); // mirror of command
			response.add((byte) 0x01); // status

			// Data packet
			response.add(command[4]); // mirror logical antenna port
			response.add((byte) tags.size()); // number of tags in packet
			
			for(RifidiTag rawtag : tags){
				
				rawtag.incrementReadCount();
				byte[]id = rawtag.getTag().readId();
				int tagLength = id.length;
				byte typeByte = (byte)0x00;
				typeByte = (byte) (typeByte | 0x00); //EPC Tag
				if(rawtag.getTagGen().equals(TagGen.GEN2)){
					typeByte=(byte)(typeByte | 0x04);
				}else{
					if(rawtag.getTagGen().equals(TagGen.GEN1)){
						typeByte = (byte)(typeByte | 0x08);
					}
				}
				//TODO: Maybe need to check if tagLength is 8 bytes long
				if(tagLength == 12){
					typeByte = (byte)(typeByte | 0x10);
				}
				
				response.add(typeByte);
				
				// Add tag number LSB first
				for(int i=0; i<tagLength; i++){
					response.add(id[tagLength - i - 1]);
				}
				
			}
			
			response.set(1, (byte)(response.size() + 2));
			
			byte[] bytes = new byte[response.size()];
			for(int i=0; i<response.size(); i++){
				bytes[i] = response.get(i);
			}
			
			arg.getReturnValue().add(bytes);
		}
		
		//Build final packet
		byte[] finalPacket = new byte[10];
		finalPacket[0] = 0x04; //unused
		finalPacket[1] = 0x0C; //packet length
		finalPacket[2] = 0x22; //mirror of command
		finalPacket[3] = 0x00; //status
		byte[] tagsRead = ByteAndHexConvertingUtility.intToByteArray(tags.size(), 2);
		finalPacket[4] = tagsRead[1];
		finalPacket[5] = tagsRead[0];
		finalPacket[6] = 0x00;
		finalPacket[7] = 0x00;
		finalPacket[8] = 0x00;
		finalPacket[9] = 0x00;
		
		arg.getReturnValue().add(finalPacket);
		return arg;
	}


}
