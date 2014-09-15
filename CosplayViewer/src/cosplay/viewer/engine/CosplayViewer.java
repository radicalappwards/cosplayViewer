package cosplay.viewer.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;
import cosplay.viewer.R;
import cosplay.viewer.data.Data;

/**
 * 
 * A class that is the main application implementation.
 * 
 * @author Rick
 *
 */
public class CosplayViewer {
	public int currentImage = 0, imageSaved, pageTurn;
	public ImageSwitcher is;
	public Toast toast;

	SharedPreferences sharedpreferences;
	AudioManager audioManager;

	boolean loaded;
	float actualVolume, maxVolume, volume;

	CommonVariables common = CommonVariables.getInstance();
	MyMediaPlayer myMediaPlayer = new MyMediaPlayer();
	MySoundPool mySoundPool;

	/**
	 * 
	 * Called to start the application by checking for previous state first and
	 * then recreating the rest.
	 * 
	 * @param context
	 */

	public void init(Context context) {
		final Activity act = (Activity) context;
		common.context = context;

		sharedpreferences = context.getSharedPreferences(Data.MY_PREFERENCES,
				Context.MODE_PRIVATE);

		if (sharedpreferences.contains(Data.CURRENT_IMAGE)) {
			currentImage = sharedpreferences.getInt(Data.CURRENT_IMAGE, 0);
		}

		if (sharedpreferences.contains(Data.CURRENT_POSITION)) {
			common.currentPosition = sharedpreferences.getInt(
					Data.CURRENT_POSITION, 0);
		}

		if (sharedpreferences.contains(Data.PLAY_MUSIC)) {
			common.playMusic = sharedpreferences.getBoolean(Data.PLAY_MUSIC,
					true);
		}

		mySoundPool = new MySoundPool(10, AudioManager.STREAM_MUSIC, 0);

		audioManager = (AudioManager) act
				.getSystemService(Context.AUDIO_SERVICE);
		actualVolume = (float) audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		maxVolume = (float) audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		volume = actualVolume / maxVolume;

		act.setVolumeControlStream(AudioManager.STREAM_MUSIC);

		is = (ImageSwitcher) act.findViewById(R.id.mainImageSwitcher);
		is.setFactory(new ViewFactory() {
			@Override
			public View makeView() {
				ImageView myView = new ImageView(act.getApplicationContext());
				myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
				myView.setLayoutParams(new ImageSwitcher.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				return myView;
			}
		});

		Animation in = AnimationUtils
				.loadAnimation(common.context, R.anim.prac);
		Animation out = AnimationUtils.loadAnimation(common.context,
				R.anim.prac_out);

		is.setInAnimation(in);
		is.setOutAnimation(out);

		is.setBackgroundColor(Color.RED);
		is.setImageResource(Data.PICS[currentImage]);
	}

	public void left(View v) {
		currentImage--;
		if (currentImage < 0)
			currentImage = Data.PICS.length - 1;

		Animation in = AnimationUtils
				.loadAnimation(common.context, R.anim.prac);
		Animation out = AnimationUtils.loadAnimation(common.context,
				R.anim.prac_out);

		is.setInAnimation(in);
		is.setOutAnimation(out);
		is.setImageResource(Data.PICS[currentImage]);

		mySoundPool.playPageTurnSound();
	}

	public void right(View v) {
		currentImage++;
		if (currentImage > Data.PICS.length - 1)
			currentImage = 0;

		Animation in = AnimationUtils.loadAnimation(common.context, R.anim.prac);
		Animation out = AnimationUtils.loadAnimation(common.context, R.anim.prac_out);

		is.setInAnimation(in);
		is.setOutAnimation(out);
		is.setImageResource(Data.PICS[currentImage]);

		mySoundPool.playPageTurnSound();
	}

	public void toggleMusic() {
		myMediaPlayer.toggleMusic();
	}

	public void saveImage(Context context) {
		String state = Environment.getExternalStorageState();
		boolean mExternalStorageAvailable, mExternalStorageWriteable;
		// check if writing is an option
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// Something else is wrong and we can neither read nor write.
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}

		if (mExternalStorageAvailable && mExternalStorageWriteable) {
			// then write picture to phone
			File path = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			String name = Data.ARTIST + currentImage + ".jpg";
			File file = new File(path, name);
			InputStream is = null;

			// check for file in directory
			if (file.exists()) {
				showToast(context, Data.SAVE_EXISTS);
			} else {

				try {
					// Make sure the Pictures directory exists.
					if (path.mkdirs() || path.exists()) {

						is = context.getResources().openRawResource(
								Data.PICS[currentImage]);

						OutputStream os = new FileOutputStream(file);
						byte[] data = new byte[is.available()];
						is.read(data);
						os.write(data);
						is.close();
						os.close();

						playSound(imageSaved);
						showToast(context, Data.SAVE_ABLE);

						// Make file immediately available.
						MediaScannerConnection
								.scanFile(
										common.context,
										new String[] { file.toString() },
										null,
										new MediaScannerConnection.OnScanCompletedListener() {
											public void onScanCompleted(
													String path, Uri uri) {
											}
										});
					} else {
						showToast(context, Data.SAVE_UNABLE);
					}
				} catch (IOException e) {
					showToast(context, Data.SAVE_UNABLE);
				}
			}
		}
	}

	public void showToast(Context cont, String message) {
		Toast toast = null;
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

	public void playSound(int sound) {
		if (loaded) {
			mySoundPool.play(sound, volume, volume, 1, 0, 1f);
		}
	}

	public void resume() {
		myMediaPlayer.resume();
	}

	public void stop() {
		Editor editor = sharedpreferences.edit();
		editor.putInt(Data.CURRENT_IMAGE, currentImage);
		editor.putBoolean(Data.PLAY_MUSIC, common.playMusic);
		if (myMediaPlayer != null && myMediaPlayer.mediaPlayer != null) {
			if (myMediaPlayer.mediaPlayer.isPlaying())
				myMediaPlayer.pause();
			common.currentPosition = myMediaPlayer.getCurrentPosition();
		}
		editor.putInt(Data.CURRENT_POSITION, common.currentPosition);
		editor.commit();
	}

	public void pause() {
		myMediaPlayer.pause();
	}

	public void destroy() {
		myMediaPlayer.destroy();
	}

	public boolean options(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_blog_devart:
			Intent i1 = new Intent(Intent.ACTION_VIEW,
					Uri.parse(Data.DEVART_LINK));
			common.context.startActivity(i1);
			return true;
		case R.id.menu_blog_facebook:
			Intent i2 = new Intent(Intent.ACTION_VIEW,
					Uri.parse(Data.FACEBOOK_LINK));
			common.context.startActivity(i2);
			return true;
		case R.id.menu_music_urb:
			Intent i3 = new Intent(Intent.ACTION_VIEW, Uri.parse(Data.URB_LINK));
			common.context.startActivity(i3);
			return true;
		case R.id.menu_save_image:
			saveImage(common.context);
			return true;
		case R.id.menu_music_toggle:
			toggleMusic();
			return true;
		default:
			return true;
		}
	}
}
