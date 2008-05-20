/**
 * 
 */
package org.rifidi.utilities;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import com.jme.util.export.Savable;
import com.jme.util.export.binary.BinaryExporter;
import com.jme.util.export.binary.BinaryImporter;

/**
 * @author dan
 */
public class JmonkeyDuplicator {
	public static Savable duplicate( Savable src ) {
		try {
			// create piped streamage
			PipedInputStream pis = new PipedInputStream();
			PipedOutputStream pos = new PipedOutputStream(pis);

			// export to piped stream
			BinaryExporter e = new BinaryExporter();
			e.save(src, pos);
			pos.close();

			// import from piped stream
			BinaryImporter i = new BinaryImporter();
			return i.load(pis);
		} catch ( IOException e1 ) {
			e1.printStackTrace();
			return null;
		}
	}
}
