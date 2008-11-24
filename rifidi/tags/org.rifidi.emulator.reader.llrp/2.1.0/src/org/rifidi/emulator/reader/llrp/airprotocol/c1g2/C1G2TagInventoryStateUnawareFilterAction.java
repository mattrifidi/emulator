package org.rifidi.emulator.reader.llrp.airprotocol.c1g2;

public class C1G2TagInventoryStateUnawareFilterAction {

	/**
	 * The action to take for matching and non-matching tags. Possible values:
	 * 
	 * 0 - Select Matching tags, Unselect non-matching tags
	 * 
	 * 1 - Select Matching tags, Do nothin with non-matching tags
	 * 
	 * 2 - Do nothing with Matching tags, Unselect non-matching tags
	 * 
	 * 3 - Unselect Matching tags, Do nothing with non-matching tags
	 * 
	 * 4 - Unselect Matching tags, Do nothing with non-matching tags
	 * 
	 * 5 - Do nothing with Matching tags, Select non-matching tags
	 */
	public int action;

	public C1G2TagInventoryStateUnawareFilterAction() {

	}

	/**
	 * Convience contructor
	 * 
	 * @param action -
	 *            The action to take for matching and non-matching tags. See
	 *            15.2.1.2.1.1.3
	 */
	public C1G2TagInventoryStateUnawareFilterAction(int action) {

		this.action = action;

	}

}
