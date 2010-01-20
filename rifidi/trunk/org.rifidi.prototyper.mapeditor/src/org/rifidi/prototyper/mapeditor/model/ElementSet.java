/**
 * 
 */
package org.rifidi.prototyper.mapeditor.model;

import java.util.LinkedList;
import java.util.List;

/**
 * This is a set that can be used as a model object to store other model
 * objects. It is used to contain the hotspots and the items
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class ElementSet<T extends AbstractMapModelElement> extends
		AbstractMapModelElement {

	/***/
	private static final long serialVersionUID = 1L;
	private final List<T> elements;
	private final Class<T> elementType;

	/**
	 * Have to provide the class of the elements that will be stored in here so
	 * that the part factory can create a new part for this set. (have to do
	 * this since generics type information is erased at runtime.)
	 * 
	 * @param elementType
	 */
	public ElementSet(Class<T> elementType) {
		elements = new LinkedList<T>();
		this.elementType = elementType;
	}

	public void addElement(T element) {
		this.elements.add(element);
		fireChildAdded(element);
	}

	public void removeElement(T element) {
		boolean removed = this.elements.remove(element);
		if (removed)
			fireChildRemoved(element);
	}

	public List<T> getElements() {
		return new LinkedList<T>(elements);
	}

	public boolean contains(T element) {
		return elements.contains(element);
	}

	public Class<T> getType() {
		return elementType;
	}
}
