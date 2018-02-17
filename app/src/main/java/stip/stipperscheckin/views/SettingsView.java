package stip.stipperscheckin.views;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import stannieman.mvvm.ViewBase;
import stip.stipperscheckin.App;
import stip.stipperscheckin.R;
import stip.stipperscheckin.databinding.SettingsViewBinding;
import stip.stipperscheckin.viewmodels.SettingsViewModel;

public class SettingsView extends ViewBase<SettingsViewModel> {

    public SettingsView() {
        App.getAppComponent().inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SettingsViewBinding) DataBindingUtil.setContentView(this, R.layout.settings_view)).setViewModel(getViewModel());
    }
}
