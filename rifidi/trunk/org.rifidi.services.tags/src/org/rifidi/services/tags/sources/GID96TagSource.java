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
 * TagSource for GID96-Tags. 8 bits for header (value for this tagtype
 * is 35) 28 bits for general manager id 24 bits for object class 36 bits for
 * serial example: 2F-2222-1111-2222
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class GID96TagSource implements TagSource {
	/**
	 * Header bytes.
	 */
	public static final String header = "35";
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
		return RifidiTagFactory.createTag(TagGen.GEN2, TagType.GID96,
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
		BigInteger random = new BigInteger("350000000000000000000000", 16);
		random = random.or(new BigInteger(28, secureRandom).shiftLeft(60));
		random = random.or(new BigInteger(24, secureRandom).shiftLeft(36));
		random = random.or(new BigInteger(36, secureRandom));
		return random.toString(16);
	}
}
