/**
 * 
 */
package org.rifidi.prototyper.mapeditor.model;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 *
 */
public interface Container<T extends AbstractMapModelElement> {
	
	boolean contains(T element);
}
