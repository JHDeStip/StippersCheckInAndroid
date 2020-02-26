package stip.stipperscheckin.views;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.WindowManager;

import javax.inject.Inject;

import stannieman.mvvm.ViewBase;
import stip.stipperscheckin.App;
import stip.stipperscheckin.R;
import stip.stipperscheckin.databinding.CheckInResultViewBinding;
import stip.stipperscheckin.helpers.IScreenOrientationHelper;
import stip.stipperscheckin.viewmodels.CheckInResultViewModel;

public class CheckInResultView extends ViewBase<CheckInResultViewModel> {

    @Inject
    IScreenOrientationHelper screenOrientationHelper;

    public CheckInResultView() {
        App.getAppComponent().inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        ((CheckInResultViewBinding)DataBindingUtil.setContentView(this, R.layout.check_in_result_view)).setViewModel(getViewModel());

        screenOrientationHelper.setScreenOrientation(this, getViewModel().getDisplayOrrientation());
    }
}
