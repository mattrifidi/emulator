/*
 *  AntennaCollisionThread.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library.basemodels.antennafield;

import java.util.ArrayList;
import java.util.List;

import org.rifidi.designer.entities.Entity;

/**
 * Thread for managing collision events.
 * @author dan
 */
public class AntennaCollisionThread extends Thread {

	private AntennaFieldEntity antennaField;
	private boolean running = false;
	private boolean paused = true;

	public AntennaCollisionThread(AntennaFieldEntity antennaField) {
		this.antennaField = antennaField;
	}

	@Override
	public void run() {
		running = true;
		while (running) {
			if (!paused) {

				// find tags that have left the field
				// for ( Entity ent : new
				// HashMap<Entity,RifidiTag>(antennaField.getSeen()).keySet() )
				// {
				// if ( ent instanceof VisualEntity ) {
				// if (
				// !antennaField.getNode().hasCollision(((VisualEntity)ent).getNode(),
				// true) ) {
				// System.out.println("removing "+ent.getName());
				// antennaField.getSeen().remove( ent );
				// }
				// }
				// }

				// figure out which tags should be removed
				List<byte[]> remByte = new ArrayList<byte[]>();
				for (Entity ent : new ArrayList<Entity>(antennaField.getSeen()
						.keySet())) {
					Long l = antennaField.getSeen().get(ent);
					// System.out.println(l);
					if (antennaField.getSeen().get(ent) > 75) {
						// System.out.println(antennaField.getSeenTags());
						// System.out.println(antennaField.getSeenTags().get(ent));
						// System.out.println(antennaField.getSeenTags().get(ent).getTag());
						if (antennaField.getSeenTags().get(ent) != null)
							remByte.add(antennaField.getSeenTags().get(ent)
									.getTag().readId());
						antennaField.getSeenTags().remove(ent);
						antennaField.getSeen().remove(ent);
					} else
						antennaField.getSeen().put(ent, l + 1);
				}

				// remove non-colliding tags from reader
				try {
					if (remByte.size() > 0)
						antennaField.getReaderInterface()
								.removeTags(0, remByte);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			try {
				sleep(2);
			} catch (InterruptedException e) {
				// sure, whatever
			}
		}
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * @param running
	 *            the running to set
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}

	/**
	 * @param paused
	 *            the paused state of the thread
	 */
	public void setPaused(boolean paused) {
		this.paused = paused;
	}
}