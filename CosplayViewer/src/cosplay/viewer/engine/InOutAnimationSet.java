package cosplay.viewer.engine;

import cosplay.viewer.R;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class InOutAnimationSet {
	// contains a set of animations
	public Animation outAnimation;
	public Animation inAnimation;
	CommonVariables common = CommonVariables.getInstance();

	public final static int EXPAND_COLLAPSE = 0;
	public final static int WARP_VERTICAL = 1;
	public final static int COLLAPSE_ROTATE_CENTER = 2;

	public Animation getOutAnimationRight(int anim) {
		switch (anim) {
		case WARP_VERTICAL:
			outAnimation = AnimationUtils.loadAnimation(common.context,
					R.anim.warp_vertical);
			break;
		case COLLAPSE_ROTATE_CENTER:
			outAnimation = AnimationUtils.loadAnimation(common.context,
					R.anim.collapse_stack_right);
			break;
		case EXPAND_COLLAPSE:
			outAnimation = AnimationUtils.loadAnimation(common.context,
					R.anim.collapse_rotate_right);
			break;
		}
		return outAnimation;
	}

	public Animation getOutAnimationLeft(int anim) {
		switch (anim) {
		case WARP_VERTICAL:
			outAnimation = AnimationUtils.loadAnimation(common.context,
					R.anim.warp_vertical);
			break;
		case COLLAPSE_ROTATE_CENTER:
			outAnimation = AnimationUtils.loadAnimation(common.context,
					R.anim.collapse_stack_left);
			break;
		case EXPAND_COLLAPSE:
			outAnimation = AnimationUtils.loadAnimation(common.context,
					R.anim.collapse_rotate_left);
			break;
		}
		return outAnimation;
	}

	public Animation getInAnimationRight(int anim) {
		switch (anim) {
		case WARP_VERTICAL:
			inAnimation = AnimationUtils.loadAnimation(common.context,
					R.anim.fade_in);
			break;
		case COLLAPSE_ROTATE_CENTER:
			inAnimation = AnimationUtils.loadAnimation(common.context,
					R.anim.top_right_corner_rotate_in);
			break;
		case EXPAND_COLLAPSE:
			inAnimation = AnimationUtils.loadAnimation(common.context,
					R.anim.expand);
			break;
		}
		return inAnimation;
	}

	public Animation getInAnimationLeft(int anim) {
		switch (anim) {
		case WARP_VERTICAL:
			inAnimation = AnimationUtils.loadAnimation(common.context,
					R.anim.fade_in);
			break;
		case COLLAPSE_ROTATE_CENTER:
			inAnimation = AnimationUtils.loadAnimation(common.context,
					R.anim.top_left_corner_rotate_in);
			break;
		case EXPAND_COLLAPSE:
			inAnimation = AnimationUtils.loadAnimation(common.context,
					R.anim.expand);
			break;
		}
		return inAnimation;
	}

	public Animation getInAnimationStart() {
		inAnimation = AnimationUtils.loadAnimation(common.context,
				R.anim.fade_in);
		return inAnimation;
	}

	public Animation getOutAnimationStart() {
		outAnimation = AnimationUtils.loadAnimation(common.context,
				R.anim.fade_out);
		return outAnimation;
	}
}
