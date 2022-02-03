package org.rifidi.emulator.reader.llrp.airprotocol.c1g2;

import java.util.BitSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.tags.enums.TagConstants;
import org.rifidi.tags.impl.C1G2Tag;
import org.rifidi.tags.impl.RifidiTag;

public class _C1G2TargetTag {

	private int memoryBank;

	private short pointer;

	private byte[] mask;

	private byte[] tagData;

	private boolean match;

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(_C1G2TargetTag.class);

	public _C1G2TargetTag(int memoryBank, short pointer, byte[] mask,
			byte[] tagData, boolean match) {
		this.memoryBank = memoryBank;
		this.pointer = pointer;
		this.mask = mask;
		this.tagData = tagData;
		this.match = match;
	}

	/**
	 * If bit i in the mask is zero, then bit i of the target tag is a don't
	 * care (X); if bit i in the mask is one, then bit i of the target tag is
	 * bit i of the tag pattern. For example, all tags is specified using a
	 * mask length of zero.
	 * 
	 * Each tag pattern has a match or a non-match flag
	 * 
	 * @param tag
	 * @return
	 */
	public boolean matchTag(RifidiTag tag) {
		if (mask == null || tagData == null) {
			return true && match;
		}

		byte[] memory = null;
		if (memoryBank == 0) {
			memory = ((C1G2Tag) tag.getTag()).getMem().get(
					TagConstants.MemoryReserved);
		} else if (memoryBank == 1) {
			memory = ((C1G2Tag) tag.getTag()).getMem().get(
					TagConstants.MemoryEPC);
		} else if (memoryBank == 2) {
			memory = ((C1G2Tag) tag.getTag()).getMem().get(
					TagConstants.MemoryTID);
		} else if (memoryBank == 3) {
			memory = ((C1G2Tag) tag.getTag()).getMem().get(
					TagConstants.MemoryUser);
		} else {
			logger.debug("Incorrect Memory Bank: " + memoryBank);
		}

		if (memory != null) {
			BitSet memBits = fromByteArray(memory);
			memBits = resizeBitSet(memBits, pointer, mask.length);
			if (memBits != null) {
				BitSet maskBits = fromByteArray(mask);
				BitSet tagDataBits = fromByteArray(tagData);
				memBits.xor(tagDataBits);
				memBits.and(maskBits);
				return ((memBits.cardinality() == 0) && match)
						|| (memBits.cardinality() != 0 && !match);
			} else {
				logger.debug("Pointer and mask length are out "
						+ "of range of tag memory");
			}
		}

		return false;

	}

	/**
	 * Convert from a byte array to a BitSet
	 * @param bytes
	 * @return
	 */
	private BitSet fromByteArray(byte[] bytes) {
		BitSet bits = new BitSet(bytes.length * 8);
		for (int i = 0; i < bytes.length * 8; i++) {
			if ((bytes[bytes.length - i / 8 - 1] & (1 << (i % 8))) > 0) {
				bits.set(bytes.length * 8 - i - 1);
			}
		}
		return bits;
	}

	/**
	 * Resizes a bit set
	 * @param bits Bits to resize
	 * @param index The index within the bits set to start from
	 * @param length the number of places to copy to the new set
	 * @return A new bit array that contians bits index to index+length
	 */
	private BitSet resizeBitSet(BitSet bits, int index, int length) {
		BitSet b = new BitSet(length);

		if (index + length + 2 > bits.size()) {
			return null;
		}

		for (int i = 0; i < length; i++) {
			if (bits.get(index)) {
				b.set(i);
			}
			index++;
		}

		return b;
	}

}
