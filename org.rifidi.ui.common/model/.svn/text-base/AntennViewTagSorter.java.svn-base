/*
 *  AntennViewTagSorter.java
 *
 *  Created:	Feb 28, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.ide.views.antennaview.model;

import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.widgets.TableColumn;
import org.rifidi.ide.tag.IdeTag;
import org.rifidi.ide.views.antennaview.TagViewer;

/**
 * This sorter is used to sort a List of IdeTags by their properties.
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class AntennViewTagSorter extends ViewerSorter {
	private List<TableColumn> tableColumns;

	/**
	 * Constructor that takes the list of available columns in the tablkeviewer
	 * as its argument.
	 * 
	 * @param tableColums
	 *            the columns in the viewer
	 */
	public AntennViewTagSorter(List<TableColumn> tableColums) {
		super();
		this.tableColumns = tableColums;
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		// search for the column to sort and sort it
		for (TableColumn tableColumn : tableColumns) {
			if (tableColumn.equals(((TableViewer) viewer).getTable()
					.getSortColumn())) {
				if (tableColumns.indexOf(tableColumn) == TagViewer.Columns.GEN
						.ordinal()) {
					return (((IdeTag) e1).getTagGen()).compareTo(((IdeTag) e2)
							.getTagGen());
				} else if (tableColumns.indexOf(tableColumn) == TagViewer.Columns.DATATYPE
						.ordinal()) {
					return (((IdeTag) e1).getTagFormat())
							.compareTo(((IdeTag) e2).getTagFormat());
				} else if (tableColumns.indexOf(tableColumn) == TagViewer.Columns.TAGID
						.ordinal()) {
					(((IdeTag) e1).getTagData()).compareTo(((IdeTag) e2)
							.getTagData());
				}
			}
		}
		return 1;
	}

}
