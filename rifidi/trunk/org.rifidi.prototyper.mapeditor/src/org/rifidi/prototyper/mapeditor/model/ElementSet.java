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
		AbstractMapModelElement implements Container<T> {

	/** The serial version ID */
	private static final long serialVersionUID = 1L;
	/** The elements contained in this Set */
	private final List<T> elements;
	/** The type of elements contained in this set */
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

	/**
	 * Add an element to this set
	 * 
	 * @param element
	 */
	public void addElement(T element) {
		this.elements.add(element);
		fireChildAdded(element);
	}

	/**
	 * Remove an element from this set
	 * 
	 * @param element
	 */
	public void removeElement(T element) {
		boolean removed = this.elements.remove(element);
		if (removed)
			fireChildRemoved(element);
	}

	/***
	 * Get all the elements in this set
	 * 
	 * @return
	 */
	public List<T> getElements() {
		return new LinkedList<T>(elements);
	}

	@Override
	public boolean contains(T element) {
		if (element instanceof Container<?>) {
			for (T e : elements) {
				Container<T> c = (Container<T>) e;
				try {
					if (c.contains(element))
						return true;
				} catch (ClassCastException ex) {
					// ignore for now
				}
			}
			return false;
		} else {
			return elements.contains(element);
		}
	}

	/**
	 * Return the type of elements contained in this set. Needed because java
	 * erases type info at runtime.
	 * 
	 * @return
	 */
	public Class<T> getType() {
		return elementType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Element Set<" + getType().getSimpleName() + ">";
	}

}
