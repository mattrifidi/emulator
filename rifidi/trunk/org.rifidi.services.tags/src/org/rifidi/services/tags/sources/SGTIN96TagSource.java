/*
 *  DoD96TagSource.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.services.tags.sources;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.rifidi.emulator.tags.enums.TagGen;
import org.rifidi.emulator.tags.id.TagType;
import org.rifidi.emulator.tags.impl.RifidiTag;
import org.rifidi.emulator.tags.utils.RifidiTagFactory;
import org.rifidi.services.tags.TagSource;
import org.rifidi.utilities.formatting.ByteAndHexConvertingUtility;

/**
 * TagSource for SGTIN-96 tags. 8 bits header (value for this tagtype is
 * 30) 3 bits filter 3 bits partition 20-40 bits company prefix 24-4 bits item
 * reference 38 bits serial
 * 
 * the partition denotes the number of bits available in company prefix and in
 * item reference
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class SGTIN96TagSource implements TagSource {
	/**
	 * Header bytes.
	 */
	public static final String header = "30";
	/**
	 * The source of unique numbers.
	 */
	private SecureRandom secureRandom = new SecureRandom();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.tags.TagSource#getRifidiTag()
	 */
	@Override
	public RifidiTag getRifidiTag() {
		return RifidiTagFactory.createTag(TagGen.GEN2, TagType.SGTIN96,
				ByteAndHexConvertingUtility.fromHexString(getNextID()), null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.tags.TagSource#getRifidiTags(int)
	 */
	@Override
	public List<RifidiTag> getRifidiTags(int number) {
		List<RifidiTag> ret = new ArrayList<RifidiTag>();
		for (int count = 0; count < number; count++) {
			ret.add(getRifidiTag());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.tags.TagSource#returnRifidiTag(org.rifidi.emulator.tags.impl.RifidiTag)
	 */
	@Override
	public void returnRifidiTag(RifidiTag rifidiTag) {
		// tags are random so nothing to do here
	}

	private String getNextID() {
		BigInteger random = new BigInteger("300000000000000000000000", 16);
		random = random.or(new BigInteger(3, secureRandom).shiftLeft(85));
		BigInteger part = new BigInteger(3, secureRandom);
		if (part.intValue() > 6) {
			part = new BigInteger("6");
		}
		random = random.or(part.shiftLeft(82));
		switch (part.intValue()) {
		case 0:
			random = random.or(new BigInteger(Long.toString((long) Math
					.sqrt(Math.pow(secureRandom.nextLong(), 2))
					% ((long) Math.pow(10, 12) - 1)), 10).shiftLeft(42));
			random = random.or(new BigInteger(Integer.toString(secureRandom
					.nextInt((int) Math.pow(10, 1))), 10).shiftLeft(38));
			break;
		case 1:
			random = random.or(new BigInteger(Long.toString((long) Math
					.sqrt(Math.pow(secureRandom.nextLong(), 2))
					% ((long) Math.pow(10, 11) - 1)), 10).shiftLeft(45));
			random = random.or(new BigInteger(Integer.toString(secureRandom
					.nextInt((int) Math.pow(10, 2))), 10).shiftLeft(38));
			break;
		case 2:
			random = random.or(new BigInteger(Long.toString((long) Math
					.sqrt(Math.pow(secureRandom.nextLong(), 2))
					% ((long) Math.pow(10, 10) - 1)), 10).shiftLeft(48));
			random = random.or(new BigInteger(Integer.toString(secureRandom
					.nextInt((int) Math.pow(10, 3))), 10).shiftLeft(38));
			break;
		case 3:
			random = random.or(new BigInteger(Long.toString((long) Math
					.sqrt(Math.pow(secureRandom.nextLong(), 2))
					% ((long) Math.pow(10, 9) - 1)), 10).shiftLeft(52));
			random = random.or(new BigInteger(Integer.toString(secureRandom
					.nextInt((int) Math.pow(10, 4))), 10).shiftLeft(38));
			break;
		case 4:
			random = random.or(new BigInteger(Long.toString((long) Math
					.sqrt(Math.pow(secureRandom.nextLong(), 2))
					% ((long) Math.pow(10, 8) - 1)), 10).shiftLeft(55));
			random = random.or(new BigInteger(Integer.toString(secureRandom
					.nextInt((int) Math.pow(10, 5))), 10).shiftLeft(38));
			break;
		case 5:
			random = random.or(new BigInteger(Long.toString((long) Math
					.sqrt(Math.pow(secureRandom.nextLong(), 2))
					% ((long) Math.pow(10, 7) - 1)), 10).shiftLeft(58));
			random = random.or(new BigInteger(Integer.toString(secureRandom
					.nextInt((int) Math.pow(10, 6))), 10).shiftLeft(38));
			break;
		case 6:
			random = random.or(new BigInteger(Long.toString((long) Math
					.sqrt(Math.pow(secureRandom.nextLong(), 2))
					% ((long) Math.pow(10, 6) - 1)), 10).shiftLeft(62));
			random = random.or(new BigInteger(Integer.toString(secureRandom
					.nextInt((int) Math.pow(10, 7))), 10).shiftLeft(38));
			break;
		}
		random = random.or(new BigInteger(38, secureRandom));
		return random.toString(16);
	}
}
