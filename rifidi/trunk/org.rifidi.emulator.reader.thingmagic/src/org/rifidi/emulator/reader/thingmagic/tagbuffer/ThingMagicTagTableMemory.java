package org.rifidi.emulator.reader.thingmagic.tagbuffer;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.sharedrc.tagmemory.TagMemory;
import org.rifidi.emulator.reader.thingmagic.database.IDBRow;
import org.rifidi.emulator.reader.thingmagic.database.IDBTable;
import org.rifidi.emulator.reader.thingmagic.formatter.ThingMagicRQLCommandFormatter;
import org.rifidi.emulator.reader.thingmagic.module.ThingMagicReaderSharedResources;
import org.rifidi.services.tags.impl.RifidiTag;

public class ThingMagicTagTableMemory extends AbstractList<IDBRow> implements
		IDBTable, TagMemory {
	private static Log logger = LogFactory.getLog(ThingMagicTagTableMemory.class);
	private List<TagRowData> tags = new ArrayList<TagRowData>();
	private boolean suspended = false;

	public ThingMagicTagTableMemory() {
		logger.debug("Creating Tag Memory...");
		clear();
	}

	@Override
	public IDBRow get(int index) {
		logger.debug("Getting tag at tag memmory location " + index);
		if (suspended)
			throw new ArrayIndexOutOfBoundsException(
					"Trying to use any tag memory index while tag memory is suspended");

		return tags.get(index);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		if (!suspended) {
			return tags.size();
		} else {
			return 0;
		}
	}

	@Override
	public Collection<RifidiTag> getTagReport() {
		logger.debug("Getting tag report.");
		List<RifidiTag> tagsToReturn = new ArrayList<RifidiTag>();
		if (!suspended) {
			for (TagRowData t : tags) {
				tagsToReturn.add(t.getTag());
			}
		}
		return tagsToReturn;
	}

	@Override
	public void resume() {
		this.suspended = false;
	}

	@Override
	public void suspend() {
		this.suspended = true;
	}

	@Override
	public void updateMemory(Collection<RifidiTag> tagsToAdd) {
		logger.debug("Updating tag memory with tags: " + tagsToAdd);
		// TODO Think of a better way of doing this.
		for (RifidiTag t : tagsToAdd) {
			TagRowData tagRowData = new TagRowData(t);
			if (tags.contains(tagRowData)) {
				// TODO increment read count
			} else {
				tags.add(tagRowData);
			}
		}
		
		/*
		 * Now update the tag data
		 */
		for (TagRowData t: tags){
			t.updateTagData();
		}

	}

	@Override
	public void clear() {
		logger.debug("Clearing tag memory.");
		tags.clear();
	}
}
