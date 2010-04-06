/**
 * 
 */
package org.rifidi.prototyper.mapeditor.model;

/**
 * An interface for Model Elements to implement if they can contain other model
 * elements.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public interface Container<T extends AbstractMapModelElement> {

	/**
	 * Return true if this model element contains the given element
	 * 
	 * @param element
	 * @return
	 */
	boolean contains(T element);
}
