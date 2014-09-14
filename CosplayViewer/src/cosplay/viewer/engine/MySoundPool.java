package cosplay.viewer.engine;

import cosplay.viewer.R;
import android.media.SoundPool;

/**
 * 
 * Sound Pool extension includes loading sound and playing sounds.
 * 
 * @author Rick
 *
 */
public class MySoundPool extends SoundPool {

	CommonVariables cv = CommonVariables.getInstance();

	public MySoundPool(int maxStreams, int streamType, int srcQuality) {
		super(maxStreams, streamType, srcQuality);
		// create a new sound pool and set up sounds
		this.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				if (sampleId == 1)
					cv.pageTurnLoaded = true;
				else if (sampleId == 2)
					cv.saveSoundLoaded = true;
			}
		});

		cv.saveSound = load(cv.context, R.raw.imagesaved, 1);
		cv.pageTurnSound = load(cv.context, R.raw.pageturn, 1);
	}

	public void playChimeSound() {
		// check for sound file to be loaded and wanting to be player
		if (cv.chimeLoaded && cv.playChimeSound) {
			play(cv.saveSound, cv.volume, cv.volume, 1, 0, 1f);
		}
	}

	public void playPageTurnSound() {
		// check for tap sound to be loaded and it in preferences
		if (cv.pageTurnLoaded && cv.playPageTurnSound) {
			play(cv.pageTurnSound, cv.volume, cv.volume, 1, 0, 1f);
		}
	}
}
