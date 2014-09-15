package cosplay.viewer.engine;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.Gravity;
import android.widget.Toast;
import cosplay.viewer.data.Data;

/**
 * 
 * A class to extend Media Player and implement handling for interfaces needed.
 * I also started implementing the ability to handle the sound changes due to
 * incoming notification sounds like phone or message alerts *
 * 
 * Should handle errors with headphones becoming unplugged and media player
 * states with opening and closing of application.
 * 
 * @author Rick
 * 
 */
public class MyMediaPlayer extends MediaPlayer implements
		MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
		AudioManager.OnAudioFocusChangeListener,
		MediaPlayer.OnCompletionListener {

	CommonVariables cv = CommonVariables.getInstance();
	public MediaPlayer mediaPlayer;
	Uri path = Uri.parse(Data.PATH + Data.TRACK_01);
	public Toast toast;

	public void init() {
		mediaPlayer = new MediaPlayer();

		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnErrorListener(this);
	}

	public void start() {
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(cv.context, path);
			mediaPlayer.setLooping(true);
			mediaPlayer.setVolume(cv.volume, cv.volume);
			mediaPlayer.prepareAsync();
		} catch (IllegalArgumentException | SecurityException
				| IllegalStateException | IOException e) {
			// try is for the setting of the data source
			e.printStackTrace();
		}
	}

	@Override
	public void onPrepared(MediaPlayer player) {
		// check for option to play music and resume last position
		if (cv.playMusic) {
			if (cv.currentSoundPosition > 0)
				mediaPlayer.seekTo(cv.currentSoundPosition);
			player.start();
		}
	}

	public void setSoundStopped() {
		// set current position to be the media player position
		if (mediaPlayer != null) {
			cv.currentSoundPosition = mediaPlayer.getCurrentPosition();

			// check if position is over the valid duration
			if (cv.currentSoundPosition >= mediaPlayer.getDuration()
					&& mediaPlayer.getDuration() != -1)
				cv.currentSoundPosition = 0;
		}
	}

	@Override
	public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
		// get position it error's and restart
		if (i == MediaPlayer.MEDIA_ERROR_SERVER_DIED
				|| i2 == MediaPlayer.MEDIA_ERROR_IO) {
			setSoundStopped();
			init();
			return true;
		}
		return false;
	}

	@Override
	public void onAudioFocusChange(int focusChange) {
		// Handle audio lowering and raising for other phone sounds
		switch (focusChange) {
		case AudioManager.AUDIOFOCUS_GAIN:
			// resume play back
			if (mediaPlayer == null)
				init();
			else if (!mediaPlayer.isPlaying()) {
				setNewVolume(1.0f);
				start();
			}
			break;
		case AudioManager.AUDIOFOCUS_LOSS:
			// lost focus for an unbounded amount of time. stop and release
			if (mediaPlayer != null) {
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.stop();
					setSoundStopped();
				}
				mediaPlayer.release();
				mediaPlayer = null;
			}
			break;
		case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
			// lost focus for a short time, but we have to stop play back.
			if (mediaPlayer != null && mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
				setSoundStopped();
			}
			break;
		case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
			if (mediaPlayer != null && mediaPlayer.isPlaying()) {
				setNewVolume(0.1f);
			}
			break;
		}
	}

	public void resume() {
		// media player should have been destroyed in last pause
		init();
		start();
	}

	public void pause() {
		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying())
				mediaPlayer.pause();
			setSoundStopped();
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	public void destroy() {
		// a final check when the app closes down for good
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	public void setNewVolume(Float setVolume) {
		// sets the new volume and updates the audio manager
		cv.volume = setVolume;
		mediaPlayer.setVolume(setVolume, setVolume);
	}

	public void toggleMusic() {
		if (cv.playMusic) {
			cv.playMusic = false;
			showToast(cv.context, "Music off");
			pause();
		} else {
			cv.playMusic = true;
			showToast(cv.context, "Music on/restarted");
			resume();
		}
	}

	public void showToast(Context cont, String message) {
		// create if not, or set text to it
		if (toast == null) {
			toast = Toast.makeText(cont, message, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
		}
		if (!toast.getView().isShown()) {
			toast.setText(message);
			toast.show();
		} else {
			toast.cancel();
			toast.setText(message);
			toast.show();
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// the media player may error out and reach here if not handled
		init();
		start();
	}

	public void quietSound() {
		if (mediaPlayer.isPlaying()) {
			setNewVolume(0.1f);
		}
	}
}