package org.rifidi.emulator.reader.thingmagic.database;

public interface IDBColumn <E>{
	public E get(int index);
	public E[] get();
	
	public void set(int index, E item);
	
	public int size();
}
