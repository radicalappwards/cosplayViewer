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
	public Context context;
	public float volume;

	public int saveSound;
	public int pageTurnSound;

	public int currentPosition;

	public boolean playSaveSound = true;
	public boolean saveSoundLoaded;

	public boolean playChimeSound = true;
	public boolean chimeLoaded;

	public boolean playMusic = true;

	public boolean playPageTurnSound = true;
	public boolean pageTurnLoaded;

	public static CommonVariables getInstance() {
		if (instance == null)
			synchronized (CommonVariables.class) {
				if (instance == null)
					instance = new CommonVariables();
			}
		return instance;
	}

}
