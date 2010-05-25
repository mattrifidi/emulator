/*
 *  MyResourceLocator.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.ide.converter.editor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Jun 9, 2008
 * 
 */
public class MyResourceLocator {
	private static String[] extensions = new String[]{".dds", ".tga", ".png", ".jpg", ".gif"};
	public static URL findTheCrap(String url){
		URL source=null;
		try {
			source = new URL(url);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(source!=null){
			for(String extension: extensions){
				try {
					URL urly=new URL(source.toString()+extension);
					urly.openStream();
					return urly;
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}	
		}
		
		return null;
	}
}
