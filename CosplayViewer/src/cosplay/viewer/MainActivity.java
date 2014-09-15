package cosplay.viewer;

import android.app.Activity;
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