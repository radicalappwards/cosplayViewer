package cosplay.viewer.engine;

import android.content.Context;

/**
 * 
 * Singleton class used access variables that could modified and accessed by
 * other classes.
 * 
 * @author Rick
 *
 */
public class CommonVariables {

	private volatile static CommonVariables instance;
	
	public static final int MAX_TURN_MODES = 3;

	public Context context;
	public float volume;

	public int currentSoundPosition;
	public int currentImagePosition;

	public int saveSound;
	public int pageTurnSound;

	public boolean playSaveSound = true;
	public boolean saveSoundLoaded;

	public boolean playChimeSound = true;
	public boolean chimeLoaded;

	public boolean playPageTurnSound = true;
	public boolean pageTurnLoaded;

	public boolean playMusic = true;

	public int turnMode;	

	public static CommonVariables getInstance() {
		if (instance == null)
			synchronized (CommonVariables.class) {
				if (instance == null)
					instance = new CommonVariables();
			}
		return instance;
	}

}
