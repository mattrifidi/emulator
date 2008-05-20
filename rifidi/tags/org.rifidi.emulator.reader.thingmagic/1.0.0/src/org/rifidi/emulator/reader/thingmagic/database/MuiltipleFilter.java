/**
 * 
 */
package org.rifidi.emulator.reader.thingmagic.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.thingmagic.commandhandler.RQLEncodedCommands;

/**
 * @author jmaine
 * Here we parse and create a B-Tree filter to filter the whatever
 * we have been given.
 */
public class MuiltipleFilter<T> implements IFilter<T> {
	
	private static Log logger = LogFactory.getLog(MuiltipleFilter.class);
	
	private String filter;
	
	private IFilter<T> root;
	private Comparator<T>[] comparators;
	
	public MuiltipleFilter (Comparator<T>[] c, String filter ){
		comparators = c;
		this.filter = filter;
		
		/* this algorithm is probably very inefficient being a direct/indirect
		 * algorithm... probably could come up with something better but...
		 * just want to get it working now.
		 */ 
		root = levelOr(null, filter.toLowerCase());
	}
	

	/* warning... indirect and direct recursion ahead. */
	
	/*
	 * First we try to split the around the 'or's or 'and's then 
	 * try to go to the level and then split again... if we can split we 
	 * restart the whole process by going to step no. 1. 
	 * This all stops when we get to the level: 'fundamental'. 
	 * On this level we do bare bones.
	 * 
	 *  Now a logic B-Tree is created from this algorithm from which
	 *  we will use for filtering.
	 * 
	 * 1) levelOr
	 * 2) levelAnd
	 * 3) fundamental
	 */
	
	/* we fix the grouping dealing with parentheses here */
	// FIXME this is very much broken!
	private String[] fixGrouping(String[] groups){
		ArrayList<String> newGrouping = new ArrayList<String>();
		int x = 0;
		boolean g = false;
		for (String s: groups){
			for (int y = 0; y < s.length(); y++){
				if (s.charAt(y) == '(') x++;
				if (s.charAt(y) == ')') x--;	
				
				if (x > 0) g = true;
				if (g) {
					if (!newGrouping.get(newGrouping.size()-1).endsWith(s) )
						newGrouping.add(newGrouping.remove(newGrouping.size()-1).concat(s));
				} else newGrouping.add(s);
				
				if (x == 0) g = false;
			}
			
		}
		
		if (x != 0) throw new IllegalArgumentException();
		
		
		
		//return newGrouping.toArray(groups);
		return groups;
	}
	
	private String[] group(String group){
		ArrayList<String> groups = new ArrayList<String>();
	
		return groups.toArray(new String[1]);
	}

	private IFilter<T> levelOr(FilterNode<T> parent, String arg){
		boolean inverted = false;
		if ( arg.matches(".*\\bor\\b.*")){
			String[] orGroup = fixGrouping(arg.split("\\bor\\b"));
			logger.debug(orGroup);
			IFilter<T>[] filters = new IFilter[orGroup.length];
			
			for (int x = 0; x < filters.length; x++){
				/*if (orGroup[x].contains("(") ){
					if (orGroup[x].matches("^\\s*\\bnot\\b.*")) {
						inverted = true;
					}
					orGroup[x]=orGroup[x].substring(
							orGroup[x].indexOf("(")+1, orGroup[x].lastIndexOf(")") );
				}*/				
				
				filters[x] = levelOr(parent, orGroup[x]);
			}
			
			return new FilterNode<T>(parent, filters, FilterConjunctionType.OR, inverted);
			
		} else return levelAnd(parent, arg);
	}
	
	private IFilter<T> levelAnd(FilterNode<T> parent, String arg){
		boolean inverted = false;
		if ( arg.matches(".*\\band\\b.*")){
			String[] andGroup = fixGrouping(arg.split("\\band\\b"));
			for (String s: andGroup){
				logger.debug(s);	
			}
			IFilter<T>[] filters = new IFilter[andGroup.length];
			
			// FIXME Hack!! This is a hack to get logic alloy to work. Needs to be removed.	
			if ( andGroup.length == 3 &&
				 andGroup[0].contains("protocol_id") &&
				 andGroup[1].contains("protocol_id") &&
				 andGroup[2].contains("protocol_id")
			   ){
				andGroup[0]=andGroup[0].substring(andGroup[0].indexOf('(') + 1);
				andGroup[2]=andGroup[2].substring(0, andGroup[2].indexOf(')') );
			}
			
			for (int x = 0; x < filters.length; x++){
				/*if (andGroup[x].contains("(") ){
					if (andGroup[x].matches("^\\s*\\bnot\\b.*")) {
						inverted = true;
					}
					andGroup[x]=andGroup[x].substring(
							andGroup[x].indexOf("(")+1, andGroup[x].lastIndexOf(")") );
				}*/
				
				filters[x] = levelOr(parent, andGroup[x]);
			}
			
			// FIXME Hack!! This is a hack to get logic alloy to work. Needs to be removed.	
			if ( andGroup.length == 3 &&
				 andGroup[0].contains("protocol_id") &&
				 andGroup[1].contains("protocol_id") &&
				 andGroup[2].contains("protocol_id")
			   ){
				
				return new FilterNode<T>(parent, filters, FilterConjunctionType.OR, inverted);
			}
			
			return new FilterNode<T>(parent, filters, FilterConjunctionType.AND, inverted);
			
		} else return fundamental(arg);
	}
	
	/* (sigh of releaf) we have finally gotten to a base case.
	 */
	private IFilter<T> fundamental(String arg){
		Comparator<T> c = null;
		
		EFilter filterType = EFilter.getFilterType(arg);
		
		String[] parts = arg.split(filterType.toString());
		logger.debug(parts[0] + filterType + parts[1]);
		
		for (Comparator<T> o : comparators){
			//logger.debug(o);
			if ( parts[0].toLowerCase().contains(o.toString().toLowerCase()) ){
				c = o;
				break;
			}
		}
		
		// TODO Check for null pointers!
		
		return new FilterMachine(c, filterType, parts[1]);
	}
	
	/* now we go through the B-Tree */
	@Override
	public Collection<T> filter(Collection<T> rows) {
		// TODO Auto-generated method stub
		return root.filter(rows);
	}
	
	/* this is a B-tree node which has as IFilter<T>.length children 
	 */
	private class FilterNode<T> implements IFilter<T>{
		
		boolean inverted;
		
		FilterNode<T> parent;
		
		IFilter<T>[] children;
		
		FilterConjunctionType conjunction;
		
		public FilterNode(FilterNode<T> p, IFilter<T>[] group, FilterConjunctionType c, boolean inv){
			parent = p;
			children = group;
			conjunction = c;
			inverted = inv;
		}

		/* we filter based on the information given to us by the constructor 
		 * (non-Javadoc)
		 * @see org.rifidi.emulator.reader.thingmagic.database.IFilter#filter(java.util.Collection)
		 */
		@Override
		public Collection<T> filter(Collection<T> rows) {
			// TODO Auto-generated method stub
			Collection<T> retVal = new ArrayList<T>();
			switch (conjunction){
				case AND:
					retVal = rows;
					for (IFilter<T> t: children){
						retVal = t.filter(retVal);
					}
				break;
				case OR:
					for (IFilter<T> t: children ){
						retVal.addAll(t.filter(rows));
					}
					rows = retVal;
				break;
			}
			if (inverted){
				List<T> retValNew = new ArrayList<T>(rows);
				
				retValNew.removeAll(retVal);
				
				retVal = retValNew;
			}
			return rows;
		}
		
	}
	
}
