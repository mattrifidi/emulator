package org.rifidi.emulator.reader.thingmagic.conditional;

import java.util.List;

import org.rifidi.emulator.reader.thingmagic.database.IDBRow;

public interface IFilter {
	List<IDBRow> filter(List<IDBRow> rows);
}
