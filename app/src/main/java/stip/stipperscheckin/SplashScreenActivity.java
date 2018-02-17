package stip.stipperscheckin;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import javax.inject.Inject;

import stannieman.mvvm.navigation.INavigationService;
import stip.stipperscheckin.views.ScannerView;

public class SplashScreenActivity extends Activity {
    private static final int PERMISSION_REQUEST_CAMERA = 0;

    @Inject
    INavigationService navigationService;

    public SplashScreenActivity() {
        App.getAppComponent().inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA }, PERMISSION_REQUEST_CAMERA);
                navigateToMainActivity();
                return;
            }

            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA }, PERMISSION_REQUEST_CAMERA);
            return;
        }

        navigateToMainActivity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                navigateToMainActivity();
                return;
            }

            Toast.makeText(getApplicationContext(), R.string.starting_NoCameraPermission, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void navigateToMainActivity() {
        navigationService.navigateTo(ScannerView.class);
        finish();
    }
}
