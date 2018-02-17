package stip.stipperscheckin.helpers;

import android.app.Activity;
import android.content.pm.ActivityInfo;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class ScreenOrientationHelper implements IScreenOrientationHelper {

    private static final List<Integer> VALID_ORIENTATIONS = Arrays.asList(
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,
            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE,
            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
    );

    @Inject
    public ScreenOrientationHelper() {}

    public void setScreenOrientation(Activity activity, int screenOrientation) {
        if (VALID_ORIENTATIONS.contains(screenOrientation)) {
            activity.setRequestedOrientation(screenOrientation);
        }
    }
}
