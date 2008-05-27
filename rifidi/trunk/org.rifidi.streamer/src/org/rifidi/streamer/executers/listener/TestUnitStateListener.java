package org.rifidi.streamer.executers.listener;

public interface TestUnitStateListener {
	
	public void nextIteration(int iteration);
	
	public void testUnitFinishedEvent();
	
}
