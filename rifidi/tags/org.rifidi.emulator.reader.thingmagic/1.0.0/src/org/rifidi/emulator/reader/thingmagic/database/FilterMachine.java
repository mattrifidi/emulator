/**
 * 
 */
package org.rifidi.emulator.reader.thingmagic.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author jmaine
 * This class implements the simplest filter machine... 
 */
public class FilterMachine<T> implements IFilter<T> {
	
	private static Log logger = LogFactory.getLog(FilterMachine.class);
	
	T filter;
	EFilter conditionType;
	Comparator<T> comparator;
	//boolean isNegated;
	
	/**
	 * Constructor
	 * @param comparator The implementation for the machine to use.
	 * @param conditionType Equals, Not Equals... etc.
	 * @param filter what we are basing the filter on.
	 */
	public FilterMachine(Comparator<T> comparator, EFilter conditionType,  T filter){
		super();
		
		if (comparator == null || conditionType == null || filter == null)
			throw new NullPointerException();
		
		this.conditionType = conditionType;
		this.filter = filter;
		this.comparator = comparator;
	}	
	
	/**
	 * This is the command we use to filter a list
	 * based on the information we were given in the constructor
	 * @param things The list of things to operate to filter on.
	 */
	@Override
	public Collection<T> filter(Collection<T> things) {
		// TODO Auto-generated method stub
		List<T> newList = new ArrayList<T>();
		
		logger.debug(things);
		
		logger.debug("Comparator: " + comparator);
		
		logger.debug("Filter Reference: " + filter);
		
		logger.debug("ConditionType: " + conditionType);
		
		switch(conditionType){
			case Equales:
				for(T t: things){
					if (comparator.compare(t, filter) == 0)
						newList.add(t);
				}
			break;
			case NotEquales:
				for(T t: things){
					if (comparator.compare(t, filter) != 0)
						newList.add(t);
				}
			break;
			case LessThan:
				for(T t: things){
					if (comparator.compare(t, filter) < 0)
						newList.add(t);
				}
			break;
			case GreaterThan:
				for(T t: things){
					if (comparator.compare(t, filter) > 0)
						newList.add(t);
				}
			break;
			case LessThanOrEquales:
				for(T t: things){
					if (comparator.compare(t, filter) >= 0)
						newList.add(t);
				}
			break;
			case GreaterThanOrEquals:
				for(T t: things){
					if (comparator.compare(filter, t) <= 0)
						newList.add(t);
				}
			break;
		}
		
		logger.debug(newList);
		return newList;
	}

}
