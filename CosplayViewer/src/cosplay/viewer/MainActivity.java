package cosplay.viewer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import cosplay.viewer.data.Data;
import cosplay.viewer.engine.CosplayViewer;

public class MainActivity extends Activity {

	CosplayViewer cosplayViewer = new CosplayViewer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
		switch (item.getItemId()) {
		case R.id.menu_blog_devart:
			Intent i1 = new Intent(Intent.ACTION_VIEW,
					Uri.parse(Data.DEVART_LINK));
			startActivity(i1);
			return true;
		case R.id.menu_blog_facebook:
			Intent i2 = new Intent(Intent.ACTION_VIEW,
					Uri.parse(Data.FACEBOOK_LINK));
			startActivity(i2);
			return true;
		case R.id.menu_music_urb:
			Intent i3 = new Intent(Intent.ACTION_VIEW, Uri.parse(Data.URB_LINK));
			startActivity(i3);
			return true;
		case R.id.menu_save_image:
			cosplayViewer.saveImage(getBaseContext());
			return true;
		case R.id.menu_music_toggle:
			cosplayViewer.toggleMusic();
			return true;
		default:
			return true;
		}
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
		cosplayViewer.resume();
	}

	@Override
	protected void onPause() {
		super.onPause();
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