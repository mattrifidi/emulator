/**
 * 
 */
package org.rifidi.emulator.reader.alien.gpio;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.sharedrc.GPIO.GPIOController;

/**
 * @author Kyle
 * 
 */
public class GPOController {

	/**
	 * Message logger
	 */
	private static Log logger = LogFactory.getLog(GPIController.class);

	private ArrayList<Integer> selectedPorts;
	private ArrayList<Integer> unselectedPorts;

	private GPIOController gpiocontroller;

	public GPOController(GPIOController gpioController) {
		selectedPorts = new ArrayList<Integer>();
		unselectedPorts = new ArrayList<Integer>();

		this.gpiocontroller = gpioController;
	}

	public void setListeningPorts(int portBitMap) {
		GPOController.setPorts(selectedPorts, unselectedPorts, portBitMap);
	}
	
	public static void setPorts(List<Integer> selectedPorts,
			List<Integer> unselectedPorts, int bitMap) {
		selectedPorts.clear();
		unselectedPorts.clear();
		if ((bitMap & 1) == 1) {
			selectedPorts.add(0);
		} else {
			unselectedPorts.add(0);
		}
		if ((bitMap & 2) == 2) {
			selectedPorts.add(1);
		} else {
			unselectedPorts.add(1);
		}
		if ((bitMap & 4) == 4) {
			selectedPorts.add(2);
		} else {
			unselectedPorts.add(2);
		}
		if ((bitMap & 8) == 8) {
			selectedPorts.add(3);
		} else {
			unselectedPorts.add(3);
		}
		if ((bitMap & 16) == 16) {
			selectedPorts.add(4);
		} else {
			unselectedPorts.add(4);
		}
		if ((bitMap & 32) == 32) {
			selectedPorts.add(5);
		} else {
			unselectedPorts.add(5);
		}
		if ((bitMap & 64) == 64) {
			selectedPorts.add(6);
		} else {
			unselectedPorts.add(6);
		}
		if ((bitMap & 128) == 128) {
			selectedPorts.add(7);
		} else {
			unselectedPorts.add(7);
		}
	}

	public void setPins() {
		for (int i : selectedPorts) {
			gpiocontroller.setGPOHight(i);
		}
		for (int i : unselectedPorts) {
			gpiocontroller.setGPOLow(i);
		}
	}

	public void unsetPins() {
		for (int i : selectedPorts) {
			gpiocontroller.setGPOLow(i);
		}
	}
}
