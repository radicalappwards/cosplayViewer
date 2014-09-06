package cosplay.viewer;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

/**
 * 
 * A class to extend Media Player and implement handing for interfaces needed.
 * 
 * @author Rick
 * 
 */
public class MyMediaPlayer extends MediaPlayer implements
		MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
		AudioManager.OnAudioFocusChangeListener {

	public MediaPlayer mediaPlayer;
	AudioManager audioManager;
	float actualVolume, maxVolume, volume;
	Uri path;
	Context context;
	public boolean playMusic = true;
	public int currentPosition = 0;

	public MyMediaPlayer() {
	}

	public void init(Context c) {
		context = c;
		audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		actualVolume = (float) audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		maxVolume = (float) audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		volume = actualVolume / maxVolume;
	}

	public void start() {
		try {
			path = Uri.parse(Data.PATH + Data.TRACK_02);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.reset();
			mediaPlayer.setDataSource(context, path);
			mediaPlayer.setLooping(true);
			mediaPlayer.setVolume(volume, volume);
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer.prepareAsync();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void pause() {
		mediaPlayer.pause();
		currentPosition = mediaPlayer.getCurrentPosition();
	}

	public void resume() {
		start();
	}

	@Override
	public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
		// handle errors when media player errors
		return false;
	}

	@Override
	public void onPrepared(MediaPlayer player) {
		if (playMusic) {
			if (currentPosition > 0) {
				player.seekTo(currentPosition);
			}
			player.start();
		}
	}

	@Override
	public void onAudioFocusChange(int focusChange) {
		// Handle audio lowering and raising for other phone sounds
		switch (focusChange) {
		case AudioManager.AUDIOFOCUS_GAIN:
			// resume play back
			if (mediaPlayer == null)
				mediaPlayer = new MediaPlayer();
			else if (!mediaPlayer.isPlaying())
				mediaPlayer.start();
			mediaPlayer.setVolume(volume, volume);
			break;
		case AudioManager.AUDIOFOCUS_LOSS:
			// lost focus for an unbounded amount of time. stop and release
			if (mediaPlayer != null) {
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.stop();
					currentPosition = mediaPlayer.getCurrentPosition();
				}
				mediaPlayer.release();
				mediaPlayer = null;
			}
			break;
		case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
			// lost focus for a short time, but we have to stop play back.
			if (mediaPlayer != null && mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
				currentPosition = mediaPlayer.getCurrentPosition();
			}
			break;
		case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
			if (mediaPlayer != null && mediaPlayer.isPlaying()) {
				mediaPlayer.setVolume(0.1f, 0.1f);
			}
			break;
		}
	}

	public void destroy() {
		// destroys the instance of media player and allows to be collected
		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying())
				mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	public void resume(Context c) {
		context = c;
		if (mediaPlayer == null) {
			mediaPlayer = new MediaPlayer();
		}
		init(context);
		start();
	}

	public void toggleMusic() {
		if (playMusic) {
			playMusic = false;
			pause();
		} else {
			playMusic = true;
			resume();
		}
	}
}