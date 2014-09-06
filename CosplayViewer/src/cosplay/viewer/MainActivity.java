package cosplay.viewer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

public class MainActivity extends Activity {

	// link for building template out
	// http://www.tutorialspoint.com/android/android_imageswitcher.htm

	public final static int[] pics = { R.drawable.image0, R.drawable.image1,
			R.drawable.image2, R.drawable.image3, R.drawable.image4,
			R.drawable.image5, R.drawable.image6, R.drawable.image7,
			R.drawable.image8, R.drawable.image9, R.drawable.image10,
			R.drawable.image11, R.drawable.image12, R.drawable.image13,
			R.drawable.image14, R.drawable.image15, R.drawable.image16,
			R.drawable.image17, R.drawable.image18, R.drawable.image19,
			R.drawable.image20, R.drawable.image21, R.drawable.image22,
			R.drawable.image23, R.drawable.image24, R.drawable.image25,
			R.drawable.image26, R.drawable.image27, R.drawable.image28,
			R.drawable.image29, R.drawable.image30, R.drawable.image31,
			R.drawable.image32, R.drawable.image33, R.drawable.image34,
			R.drawable.image35, R.drawable.image36, R.drawable.image37,
			R.drawable.image38, R.drawable.image39, R.drawable.image40,
			R.drawable.image41, R.drawable.image42, R.drawable.image43,
			R.drawable.image44, R.drawable.image45, R.drawable.image46,
			R.drawable.image47, R.drawable.image48, R.drawable.image49 };

	public final static String DEVART_LINK = "http://evieevangelion.deviantart.com/";
	public final static String FACEBOOK_LINK = "https://www.facebook.com/EvieEvangelion";
	public final static String ARTIST = "Evie-E";
	public final static String SAVE_UNABLE = "Unable to Save";
	public final static String SAVE_ABLE = "Photo Saved!";
	public final static String SAVE_EXISTS = "Photo Exists Already!";

	public int currentImage = 0;
	public ImageSwitcher is;
	public Toast toast;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_blog_devart:
			Intent i1 = new Intent(Intent.ACTION_VIEW, Uri.parse(DEVART_LINK));
			startActivity(i1);
			return true;
		case R.id.menu_blog_facebook:
			Intent i2 = new Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK_LINK));
			startActivity(i2);
			return true;
		case R.id.menu_save_image:
			saveImage(getBaseContext());
			return true;
		default:
			return true;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		is = (ImageSwitcher) findViewById(R.id.mainImageSwitcher);
		is.setFactory(new ViewFactory() {
			@Override
			public View makeView() {
				ImageView myView = new ImageView(getApplicationContext());
				myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
				myView.setLayoutParams(new ImageSwitcher.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				return myView;
			}
		});

		is.setBackgroundColor(Color.RED);
		is.setImageResource(pics[currentImage]);

		Animation in = AnimationUtils.loadAnimation(this,
				R.anim.my_left_anim_in);
		Animation out = AnimationUtils.loadAnimation(this,
				R.anim.my_right_anim_out);

		is.setImageResource(pics[currentImage]);
		is.setInAnimation(in);
		is.setOutAnimation(out);

	}

	public void left(View v) {
		currentImage--;
		if (currentImage < 0)
			currentImage = pics.length - 1;

		Animation in = AnimationUtils.loadAnimation(this,
				R.anim.my_left_anim_in);
		Animation out = AnimationUtils.loadAnimation(this,
				R.anim.my_right_anim_out);
		is.setImageResource(pics[currentImage]);
		is.setInAnimation(in);
		is.setOutAnimation(out);

	}

	public void right(View v) {
		currentImage++;
		if (currentImage > pics.length - 1)
			currentImage = 0;

		Animation in = AnimationUtils.loadAnimation(this,
				R.anim.my_right_anim_in);
		Animation out = AnimationUtils.loadAnimation(this,
				R.anim.my_left_anim_out);
		is.setImageResource(pics[currentImage]);
		is.setInAnimation(in);
		is.setOutAnimation(out);
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
			String name = ARTIST + currentImage + ".jpg";
			File file = new File(path, name);
			InputStream is = null;

			// check for file in directory
			if (file.exists()) {
				showToast(context, SAVE_EXISTS);
			} else {

				try {
					// Make sure the Pictures directory exists.
					if (path.mkdirs() || path.exists()) {

						is = getResources().openRawResource(pics[currentImage]);

						OutputStream os = new FileOutputStream(file);
						byte[] data = new byte[is.available()];
						is.read(data);
						os.write(data);
						is.close();
						os.close();

						showToast(context, SAVE_ABLE);

						// Make file immediately available.
						MediaScannerConnection
								.scanFile(
										this,
										new String[] { file.toString() },
										null,
										new MediaScannerConnection.OnScanCompletedListener() {
											public void onScanCompleted(
													String path, Uri uri) {
											}
										});
					} else {
						showToast(context, SAVE_UNABLE);
					}
				} catch (IOException e) {
					showToast(context, SAVE_UNABLE);
				}
			}
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

}