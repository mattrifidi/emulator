/**
 * 
 */
package org.rifidi.emulator.reader.llrp.rospec;

/**
 * @author kyle
 *
 */
public enum ROSpecState {
	DISABLED, INACTIVE, ACTIVE;
	
	public ROSpecState parseState(int state){
		switch(state){
		case 0: return DISABLED;
		case 1: return INACTIVE;
		case 2: return ACTIVE;
		default: throw new IllegalStateException("State "+ state + " is not a valid ROSpec state");
		}
	}
}
