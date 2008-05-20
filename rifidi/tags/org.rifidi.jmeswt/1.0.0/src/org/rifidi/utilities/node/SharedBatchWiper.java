package org.rifidi.utilities.node;

import com.jme.scene.SharedMesh;
import com.jme.scene.state.RenderState;

public class SharedBatchWiper {
	/**
	 * Clears renderstates for all batches of a particular SharedMesh, to
	 * prevent inheritance issues TODO examine this further - works fine now but
	 * may cause problems with multi-batch meshes getting cleared in error
	 * 
	 * @param meshy
	 */
	public static void clearSharedBatchRenderstates(SharedMesh meshy) {
		for (int z = 0; z < meshy.getBatchCount(); z++) {
			for (int y = 0; y < RenderState.RS_MAX_STATE-1; y++) {
				meshy.getBatch(z).clearRenderState(y);
			}
		}
	}

}
