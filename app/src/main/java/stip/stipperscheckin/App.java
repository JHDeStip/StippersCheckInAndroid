package stip.stipperscheckin;

import android.app.Application;

import javax.inject.Inject;

import stannieman.mvvm.messaging.IHandle;
import stannieman.mvvm.messaging.IMessenger;
import stannieman.rest.IKeyAuthRestClientFactory;
import stannieman.rest.models.KeyAuthRestClientFactoryConfig;
import stip.stipperscheckin.messages.ApiKeyChangedMessage;
import stip.stipperscheckin.rest.messages.RecreateRestClientsMessage;
import stip.stipperscheckin.rest.restservices.ICheckInService;
import stip.stipperscheckin.services.IBarcodeTextValidator;
import stip.stipperscheckin.services.IConfigService;
import stip.stipperscheckin.services.ISettingsService;

public class App extends Application implements IHandle<ApiKeyChangedMessage> {
    private static IAppComponent appComponent;

    @Inject
    IBarcodeTextValidator barcodeTextValidator;
    @Inject
    IKeyAuthRestClientFactory keyAuthRestClientFactory;
    @Inject
    IConfigService configService;
    @Inject
    ISettingsService settingsService;
    @Inject
    ICheckInService checkInService;
    @Inject
    IMessenger messenger;

    private KeyAuthRestClientFactoryConfig keyAuthRestClientFactoryConfig;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerIAppComponent.builder()
                .appModule(new AppModule(getApplicationContext()))
                .build();

        appComponent.inject(this);

        loadKeyAuthRestClientFactoryConfig();
        checkInService.init();

        messenger.subscribe(this);
        messenger.subscribe(checkInService);
    }

    public static IAppComponent getAppComponent() {
        return appComponent;
    }

    private void loadKeyAuthRestClientFactoryConfig() {
        keyAuthRestClientFactoryConfig = configService.getKeyAuthRestClientFactoryConfig();
        keyAuthRestClientFactoryConfig.setKey(settingsService.getApiKey());
        keyAuthRestClientFactory.loadConfig(keyAuthRestClientFactoryConfig);
    }

    public void Handle(ApiKeyChangedMessage message) {
        keyAuthRestClientFactoryConfig.setKey(message.getApiKey());
        keyAuthRestClientFactory.loadConfig(keyAuthRestClientFactoryConfig);
        messenger.publish(new RecreateRestClientsMessage());
    }
}
