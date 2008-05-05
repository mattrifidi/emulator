package org.rifidi.emulator.tags.sandbox;

import org.rifidi.common.utilities.ByteAndHexConvertingUtility;

public class Sandbox {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		byte[] b1 = {0x00, 0x01, 0x02};
		printItOut(b1, b1.clone());

	}
	
	public static void printItOut(byte[] b1, byte[] b2){
		System.out.println("value: " + ByteAndHexConvertingUtility.toHexString(b1));
		b2[0] = 0x02;
		System.out.println("value: " + ByteAndHexConvertingUtility.toHexString(b1));
	}
}
