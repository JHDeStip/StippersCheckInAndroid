package stip.stipperscheckin.viewmodels;

import android.os.Bundle;

import javax.inject.Inject;
import javax.inject.Singleton;

import stannieman.mvvm.ViewModelBase;
import stip.stipperscheckin.services.IBarcodeImageProcessor;
import stip.stipperscheckin.services.ISettingsService;
import stip.stipperscheckin.views.CheckInResultView;
import stip.stipperscheckin.views.SettingsView;

@Singleton
public class ScannerViewModel extends ViewModelBase {

    @Inject
    ISettingsService settingsService;

    //region barcodeImageProcessor

    private IBarcodeImageProcessor barcodeImageProcessor;

    public IBarcodeImageProcessor getBarcodeImageProcessor() {
        return barcodeImageProcessor;
    }

    @Inject
    public void setBarcodeImageProcessor(IBarcodeImageProcessor barcodeImageProcessor) {
        this.barcodeImageProcessor = barcodeImageProcessor;
    }

    //endregion

    //region torchOn

    private boolean torchOn;

    public boolean isTorchOn() {
        return torchOn;
    }

    private void setTorchOn(boolean torchOn) {
        this.torchOn = torchOn;
    }

    //endregion

    @Inject
    public ScannerViewModel() {}

    @Override
    public void onResume() {
        setTorchOn(settingsService.getTorchOn());
    }

    public void onScanned(int barcodeValue) {
        Bundle bundle = new Bundle();
        bundle.putInt(CheckInResultViewModel.CARD_NUMBER_PARAMETER_NAME, barcodeValue);
        getNavigationService().navigateTo(CheckInResultView.class, bundle);
    }

    public void navigateToSettingsView() {
        getNavigationService().navigateTo(SettingsView.class);
    }
}
