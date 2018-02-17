package stip.stipperscheckin.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;

import javax.inject.Inject;

public class SettingsService implements ISettingsService {
    private static final String API_KEY = "apiKey";
    private static final String TORCH_ON = "torchOn";
    private static final String DISPLAY_ORRIENTATION = "displayOrrientation";
    private static final String PREFERENCES_FILE = "be.stip.stipperscheckin";

    private final SharedPreferences preferences;
    private final SharedPreferences.Editor preferencesEditor;

    @Inject
    public SettingsService(Context context) {
        preferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        preferencesEditor = preferences.edit();
    }

    @Override
    public String getApiKey() {
        return preferences.getString(API_KEY, null);
    }

    @Override
    public void setApiKey(String apiKey) {
        preferencesEditor.putString(API_KEY, apiKey);
        preferencesEditor.apply();
    }

    @Override
    public boolean getTorchOn() {
        return preferences.getBoolean(TORCH_ON, false);
    }

    @Override
    public void setTorchOn(boolean torchOn) {
        preferencesEditor.putBoolean(TORCH_ON, torchOn);
        preferencesEditor.apply();
    }

    @Override
    public int getDisplayOrrientation() {
        return preferences.getInt(DISPLAY_ORRIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public void setDisplayOrrientation(int displayOrrientation) {
        preferencesEditor.putInt(DISPLAY_ORRIENTATION, displayOrrientation);
        preferencesEditor.apply();
    }
}
