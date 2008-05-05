package org.rifidi.emulator.reader.llrp.airprotocol.c1g2;

public class C1G2SingulationControl {

	/**
	 * The session to use. Possible values: 0-3
	 */
	public int session;

	/**
	 * estimate of the number of the tag population in view of the RF field of
	 * the antenna
	 */
	public short tagPopulation;

	/**
	 * estimate of the time a tag will typically remain in the RF field of the
	 * antenna in milisecond
	 */
	public int tagTransitTime;

	public C1G2TagInventoryStateAwareSingulationAction tagInvetorSatateAwareSingulationAction;

	public C1G2SingulationControl() {

	}

	public C1G2SingulationControl(int session, short tagPopulation,
			int tagTransitTime, C1G2TagInventoryStateAwareSingulationAction tisasa) {
		this.session = session;
		this.tagPopulation = tagPopulation;
		this.tagTransitTime = tagTransitTime;
		this.tagInvetorSatateAwareSingulationAction = tisasa;
	}

}
