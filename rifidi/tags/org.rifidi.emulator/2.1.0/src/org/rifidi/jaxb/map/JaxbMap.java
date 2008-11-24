package org.rifidi.jaxb.map;

import java.util.ArrayList;
import java.util.List;

public class JaxbMap {
	private List<MapElement> values = new ArrayList<MapElement>();

	public String get( String key ) {
		for ( MapElement e : values )
			if ( e.key.equals(key) )
				return e.val;
		return null;
	}

	public void put( String key, String val ) {
		for ( MapElement e : values ) {
			if ( e.key.equals(key) ) {
				e.val = val;
				return;
			}
		}
		values.add( new MapElement(key,val) );
	}
}