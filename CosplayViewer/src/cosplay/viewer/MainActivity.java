package cosplay.viewer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import cosplay.viewer.engine.CosplayViewer;

/**
 * 
 * Starting point for the application.
 * 
 * @author Rick
 *
 */
public class MainActivity extends Activity {

	// Allow activity to quiet if headphones are unplugged
	NoisyAudioStreamReceiver myNoisyAudioStreamReceiver = new NoisyAudioStreamReceiver();

	// start of receiver inner class to handle headphones becoming unplugged
	private class NoisyAudioStreamReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent
					.getAction())) {
				// quiet the media player
				cosplayViewer.quietSound();
			}
		}
	}

	private IntentFilter intentFilter = new IntentFilter(
			AudioManager.ACTION_AUDIO_BECOMING_NOISY);

	private void startPlayback() {
		registerReceiver(myNoisyAudioStreamReceiver, intentFilter);
	}

	private void stopPlayback() {
		unregisterReceiver(myNoisyAudioStreamReceiver);
	}

	CosplayViewer cosplayViewer = new CosplayViewer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// get new headphone unplugged listener
		myNoisyAudioStreamReceiver = new NoisyAudioStreamReceiver();

		// start up the view
		cosplayViewer.init(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return cosplayViewer.options(item);
	}

	public void imageSwitcherClick(View view) {
		cosplayViewer.imageSwitcher(view);
	}

	public void LeftArrowClick(View view) {
		cosplayViewer.left(view);
	}

	public void RightArrowClick(View view) {
		cosplayViewer.right(view);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// register headphone listener
		myNoisyAudioStreamReceiver = new NoisyAudioStreamReceiver();
		startPlayback();
		cosplayViewer.resume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// unregister headphone listener
		stopPlayback();
		cosplayViewer.pause();
	}

	@Override
	protected void onStop() {
		super.onStop();
		cosplayViewer.stop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		cosplayViewer.destroy();
	}
}