package cosplay.viewer;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher.ViewFactory;

public class MainActivity extends Activity {

	// link for building template out
	// http://www.tutorialspoint.com/android/android_imageswitcher.htm

	ImageSwitcher is;
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
	int currentImage = 0;

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
	}

	public void left(View v) {
		// set left image in
		currentImage--;
		if (currentImage < 0)
			currentImage = pics.length - 1;

		Animation in = AnimationUtils.loadAnimation(this,
				android.R.anim.slide_in_left);
		Animation out = AnimationUtils.loadAnimation(this,
				android.R.anim.slide_out_right);
		is.setInAnimation(in);
		is.setOutAnimation(out);
		is.setImageResource(pics[currentImage]);

	}

	public void right(View v) {
		// set right image out
		currentImage++;
		if (currentImage > pics.length - 1)
			currentImage = 0;

		Animation in = AnimationUtils.loadAnimation(this,
				android.R.anim.slide_out_right);
		Animation out = AnimationUtils.loadAnimation(this,
				android.R.anim.slide_in_left);
		is.setInAnimation(out);
		is.setOutAnimation(in);
		is.setImageResource(pics[currentImage]);
	}
}
