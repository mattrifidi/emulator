package org.rifidi.emulator.reader.llrp.airprotocol.c1g2;

/**
 * This param is used to manage the tag state during an inventory operation. In
 * order to use it, the TagInventoryStateAware flag is set to true.
 * 
 * @author kyle
 * 
 */
public class C1G2TagInventoryStateAwareFilterAction {

	/**
	 * Indicates which flag in the tag to modify.
	 * 
	 * Possible Values:
	 * 
	 * 0 - SL
	 * 
	 * 1 - Inventoried state for session S0
	 * 
	 * 2 - Inventoried state for session S1
	 * 
	 * 3 - Inventoried state for session S2
	 * 
	 * 4 - Inventoried state for session S3
	 */
	public int target;

	/**
	 * The action to take. possible values = 0-7. Please see section
	 * 15.2.1.2.1.1.2 in the LLRP spec for a better explanation
	 */
	public int action;

	public C1G2TagInventoryStateAwareFilterAction() {

	}

	/**
	 * A convenience constructor for this class. See 15.2.1.2.1.1.2 for more
	 * information
	 * 
	 * @param target -
	 *            which flag in the tag to modify
	 * @param action -
	 *            what action to take.
	 */
	public C1G2TagInventoryStateAwareFilterAction(int target, int action) {
		this.target = target;
		this.action = action;
	}

}
