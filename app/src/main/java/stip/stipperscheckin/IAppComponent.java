package stip.stipperscheckin;

import javax.inject.Singleton;

import dagger.Component;
import stannieman.mvvm.MvvmModule;
import stip.stipperscheckin.helpers.HelpersModule;
import stip.stipperscheckin.rest.RestModule;
import stip.stipperscheckin.rest.restservices.RestServicesModule;
import stip.stipperscheckin.resultCodeTranslators.CheckInServiceResultCodeTranslator;
import stip.stipperscheckin.resultCodeTranslators.ResultCodeTranslatorsModule;
import stip.stipperscheckin.services.ServicesModule;
import stip.stipperscheckin.views.CheckInResultView;
import stip.stipperscheckin.views.ScannerView;
import stip.stipperscheckin.views.SettingsView;

@Singleton
@Component(modules = {AppModule.class, ServicesModule.class, HelpersModule.class, ResultCodeTranslatorsModule.class, RestModule.class, RestServicesModule.class, MvvmModule.class})
public interface IAppComponent {
    void inject(App app);

    void inject(SplashScreenActivity splashScreenActivity);

    void inject(ScannerView scannerView);
    void inject(CheckInResultView checkInResultView);
    void inject(SettingsView settingsView);

    void inject(CheckInServiceResultCodeTranslator checkInServiceResultCodeTranslator);
}
