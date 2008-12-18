package org.rifidi.ui.streamer.utils;

import java.util.List;

public class ChangeListOrderUtil {

	@SuppressWarnings("unchecked")
	public static boolean moveUp(List list, Object o) {
		int index = list.indexOf(o);
		if (index >= 0) {
			try {
				Object temp = list.set(index - 1, o);
				list.set(index, temp);
				return true;
			} catch (IndexOutOfBoundsException e) {
				return false;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static boolean moveDown(List list, Object o) {
		int index = list.indexOf(o);
		if (index >= 0) {
			try {
				Object temp = list.set(index + 1, o);
				list.set(index, temp);
				return true;
			} catch (IndexOutOfBoundsException e) {
				return false;
			}
		}
		return false;
	}
}
