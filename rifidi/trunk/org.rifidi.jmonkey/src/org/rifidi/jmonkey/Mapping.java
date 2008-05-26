/*
 *  Mapping.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.jmonkey;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.lwjgl.input.Keyboard;

/**
 * Helper class for converting SWT events to LWJGL events.
 * 
 * @author Jochen Mader - jochen@pramari.com - August 22, 2007
 * 
 */
public class Mapping {
	public static final int KEY_NONE = -1;
	public static final int KEY_ESCAPE = SWT.ESC;
	public static final int KEY_1 = '1';
	public static final int KEY_2 = '2';
	public static final int KEY_3 = '3';
	public static final int KEY_4 = '4';
	public static final int KEY_5 = '5';
	public static final int KEY_6 = '6';
	public static final int KEY_7 = '7';
	public static final int KEY_8 = '8';
	public static final int KEY_9 = '9';
	public static final int KEY_0 = '0';
	public static final int KEY_MINUS = '-';
	public static final int KEY_EQUALS = '=';
	public static final int KEY_BACK = SWT.BS;
	public static final int KEY_TAB = SWT.TAB;
	public static final int KEY_Q = 'q';
	public static final int KEY_W = 'w';
	public static final int KEY_E = 'e';
	public static final int KEY_R = 'r';
	public static final int KEY_T = 't';
	public static final int KEY_Y = 'y';
	public static final int KEY_U = 'u';
	public static final int KEY_I = 'i';
	public static final int KEY_O = 'o';
	public static final int KEY_P = 'p';
	public static final int KEY_LBRACKET = '[';
	public static final int KEY_RBRACKET = ']';
	public static final int KEY_RETURN = SWT.CR;
	public static final int KEY_LCONTROL = SWT.CONTROL;
	public static final int KEY_A = 'a';
	public static final int KEY_S = 's';
	public static final int KEY_D = 'd';
	public static final int KEY_F = 'f';
	public static final int KEY_G = 'g';
	public static final int KEY_H = 'h';
	public static final int KEY_J = 'j';
	public static final int KEY_K = 'k';
	public static final int KEY_L = 'l';
	public static final int KEY_SEMICOLON = ';';
	public static final int KEY_APOSTROPHE = '\'';
	public static final int KEY_GRAVE = '`';
	public static final int KEY_LSHIFT = SWT.SHIFT;
	public static final int KEY_BACKSLASH = '\\';
	public static final int KEY_Z = 'z';
	public static final int KEY_X = 'x';
	public static final int KEY_C = 'c';
	public static final int KEY_V = 'v';
	public static final int KEY_B = 'b';
	public static final int KEY_N = 'n';
	public static final int KEY_M = 'm';
	public static final int KEY_COMMA = ',';
	public static final int KEY_PERIOD = '.';
	public static final int KEY_SLASH = '/';
	public static final int KEY_RSHIFT = SWT.SHIFT;
	public static final int KEY_MULTIPLY = SWT.KEYPAD_MULTIPLY;
	public static final int KEY_LMENU = SWT.ALT;
	public static final int KEY_SPACE = ' ';
	public static final int KEY_CAPITAL = -1;
	public static final int KEY_F1 = SWT.F1;
	public static final int KEY_F2 = SWT.F2;
	public static final int KEY_F3 = SWT.F3;
	public static final int KEY_F4 = SWT.F4;
	public static final int KEY_F5 = SWT.F5;
	public static final int KEY_F6 = SWT.F6;
	public static final int KEY_F7 = SWT.F7;
	public static final int KEY_F8 = SWT.F8;
	public static final int KEY_F9 = SWT.F9;
	public static final int KEY_F10 = SWT.F10;
	public static final int KEY_NUMLOCK = SWT.NUM_LOCK;
	public static final int KEY_SCROLL = SWT.SCROLL_LOCK;
	public static final int KEY_NUMPAD7 = SWT.KEYPAD_7;
	public static final int KEY_NUMPAD8 = SWT.KEYPAD_8;
	public static final int KEY_NUMPAD9 = SWT.KEYPAD_9;
	public static final int KEY_SUBTRACT = SWT.KEYPAD_SUBTRACT;
	public static final int KEY_NUMPAD4 = SWT.KEYPAD_4;
	public static final int KEY_NUMPAD5 = SWT.KEYPAD_5;
	public static final int KEY_NUMPAD6 = SWT.KEYPAD_6;
	public static final int KEY_ADD = SWT.KEYPAD_ADD;
	public static final int KEY_NUMPAD1 = SWT.KEYPAD_1;
	public static final int KEY_NUMPAD2 = SWT.KEYPAD_2;
	public static final int KEY_NUMPAD3 = SWT.KEYPAD_3;
	public static final int KEY_NUMPAD0 = SWT.KEYPAD_0;
	public static final int KEY_DECIMAL = SWT.KEYPAD_DECIMAL;
	public static final int KEY_F11 = SWT.F11;
	public static final int KEY_F12 = SWT.F12;
	public static final int KEY_F13 = SWT.F13;
	public static final int KEY_F14 = SWT.F14;
	public static final int KEY_F15 = SWT.F15;
	public static final int KEY_KANA = -1; /* (Japanese keyboard) */
	public static final int KEY_CONVERT = -1; /* (Japanese keyboard) */
	public static final int KEY_NOCONVERT = -1; /* (Japanese keyboard) */
	public static final int KEY_YEN = -1; /* (Japanese keyboard) */
	public static final int KEY_NUMPADEQUALS = SWT.KEYPAD_EQUAL;
	public static final int KEY_CIRCUMFLEX = -1; /* (Japanese keyboard) */
	public static final int KEY_AT = -1; /* (NEC PC98) */
	public static final int KEY_COLON = -1; /* (NEC PC98) */
	public static final int KEY_UNDERLINE = -1; /* (NEC PC98) */
	public static final int KEY_KANJI = -1; /* (Japanese keyboard) */
	public static final int KEY_STOP = -1; /* (NEC PC98) */
	public static final int KEY_AX = -1; /* (Japan AX) */
	public static final int KEY_UNLABELED = -1; /* (J3100) */
	public static final int KEY_NUMPADENTER = SWT.KEYPAD_CR;
	public static final int KEY_RCONTROL = SWT.CONTROL;
	public static final int KEY_NUMPADCOMMA = -1; /*
													 * = on numeric keypad (NEC
													 * PC98)
													 */
	public static final int KEY_DIVIDE = SWT.KEYPAD_DIVIDE;
	public static final int KEY_SYSRQ = -1;
	public static final int KEY_RMENU = SWT.ALT;
	public static final int KEY_PAUSE = SWT.PAUSE;
	public static final int KEY_HOME = SWT.HOME;
	public static final int KEY_UP = SWT.ARROW_UP;
	public static final int KEY_PRIOR = SWT.PAGE_UP;
	public static final int KEY_LEFT = SWT.ARROW_LEFT;
	public static final int KEY_RIGHT = SWT.ARROW_RIGHT;
	public static final int KEY_END = SWT.END;
	public static final int KEY_DOWN = SWT.ARROW_DOWN;
	public static final int KEY_NEXT = SWT.PAGE_DOWN;
	public static final int KEY_INSERT = SWT.INSERT;
	public static final int KEY_DELETE = SWT.DEL;
	public static final int KEY_LWIN = SWT.COMMAND;
	public static final int KEY_RWIN = SWT.COMMAND;
	public static final int KEY_APPS = -1; /* AppMenu key */
	public static final int KEY_POWER = -1;
	public static final int KEY_SLEEP = -1;

	public static final Map<Integer, Integer> SWTtoLWJGL = new HashMap<Integer, Integer>();
	static {
		Field[] fields = Mapping.class.getFields();
		try {
			for (Field field : fields) {
				if (Modifier.isPublic(field.getModifiers())
						&& Modifier.isFinal(field.getModifiers())
						&& Modifier.isStatic(field.getModifiers())
						&& field.getType().equals(int.class)) {
					int key = field.getInt(null);
					int value = Keyboard.class.getField(field.getName())
							.getInt(null);
					if (key != -1) {
						SWTtoLWJGL.put(key, value);
					}
				}

			}
		} catch (Exception e) {
			// TODO: remove after a godd amount of testing :D
			System.out.println(e);
		}
	}
}
