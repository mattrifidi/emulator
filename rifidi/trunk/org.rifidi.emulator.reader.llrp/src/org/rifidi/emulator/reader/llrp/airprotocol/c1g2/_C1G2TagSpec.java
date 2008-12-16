/*
 *  _C1G2TagSpec.java
 *
 *  Created:	Oct 9, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 *	Author: 	kyle
 */
package org.rifidi.emulator.reader.llrp.airprotocol.c1g2;

import org.rifidi.emulator.reader.llrp.accessspec._TagSpec;
import org.rifidi.tags.impl.RifidiTag;

/**
 * This parameter can carry up to two tag patterns. If more than one pattern is
 * present, a Boolean AND is implied. Each tag pattern has a match or a
 * non-match flag, allowing (A and B,!A and B, !A and !B, A and !B), where A and
 * B are the tag patterns.
 * 
 * 
 * @author kyle
 * 
 */
public class _C1G2TagSpec implements _TagSpec {

	_C1G2TargetTag pattern1 = null;

	_C1G2TargetTag pattern2 = null;

	public _C1G2TagSpec(_C1G2TargetTag pattern1) {
		this.pattern1 = pattern1;
	}

	public _C1G2TagSpec(_C1G2TargetTag pattern1, _C1G2TargetTag pattern2) {
		this.pattern1 = pattern1;
		this.pattern2 = pattern2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.llrp.accessspec._TagSpec#matchTag(org.rifidi.tags.impl.C1G1Tag)
	 */
	public boolean matchTag(RifidiTag tag) {
		if (pattern2 == null) {
			return pattern1.matchTag(tag);
		} else {
			return (pattern1.matchTag(tag) && pattern2.matchTag(tag));
		}
	}

}
