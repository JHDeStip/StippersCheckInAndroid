package stip.stipperscheckin.viewmodels;

import android.content.pm.ActivityInfo;
import android.databinding.Bindable;
import android.view.View;

import javax.inject.Inject;
import javax.inject.Singleton;

import stannieman.mvvm.ViewModelBase;
import stip.stipperscheckin.BR;
import stip.stipperscheckin.messages.ApiKeyChangedMessage;
import stip.stipperscheckin.services.ISettingsService;

@Singleton
public class SettingsViewModel extends ViewModelBase {

    @Inject
    ISettingsService settingsService;

    //region apiKey

    private String apiKey;

    @Bindable
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        if (this.apiKey != apiKey) {
            this.apiKey = apiKey;
            apiKeyChanged = true;
            notifyPropertyChanged(BR.apiKey);
        }
    }

    //endregion

    //region torchOn

    private boolean torchOn;

    @Bindable
    public boolean isTorchOn() {
        return torchOn;
    }

    public void setTorchOn(boolean torchOn) {
        if (this.torchOn != torchOn) {
            this.torchOn = torchOn;
            notifyPropertyChanged(BR.torchOn);
        }
    }

    //endregion

    private boolean apiKeyChanged;
    private int orrientation;

    @Inject
    public SettingsViewModel() {}

    @Override
    public void onResume() {
        super.onResume();

        apiKeyChanged = false;

        setApiKey(settingsService.getApiKey());
        setTorchOn(settingsService.getTorchOn());
        orrientation = settingsService.getDisplayOrrientation();
    }

    @Override
    public void onPause() {
        settingsService.setApiKey(apiKey);
        settingsService.setTorchOn(torchOn);
        settingsService.setDisplayOrrientation(orrientation);

        if (apiKeyChanged) {
            getMessenger().publish(new ApiKeyChangedMessage(apiKey));
        }

        super.onPause();
    }

    public void onUp(View view) {
        orrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    public void onDown(View view) {
        orrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
    }

    public void onLeft(View view) {
        orrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    }

    public void onRight(View view) {
        orrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
    }
}
