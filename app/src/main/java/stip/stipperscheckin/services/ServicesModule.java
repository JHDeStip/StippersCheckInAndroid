package stip.stipperscheckin.services;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ServicesModule {
    @Singleton
    @Binds
    public abstract IBarcodeImageProcessor bindIBarcodeImageProcessor(BarcodeImageProcessor barcodeImageProcessor);

    @Singleton
    @Binds
    public abstract IBarcodeTextValidator bindIBarcodeTextValidator(BarcodeTextValidator barcodeTextValidator);

    @Singleton
    @Binds
    public abstract IConfigService bindIConfigService(ConfigService configService);

    @Singleton
    @Binds
    public abstract ISettingsService bindISettingsService(SettingsService settingsService);
}
