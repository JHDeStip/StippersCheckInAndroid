package stip.stipperscheckin.rest;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import stannieman.rest.IKeyAuthRestClientFactory;
import stannieman.rest.KeyAuthRestClientFactory;

@Module
public class RestModule {
    @Singleton
    @Provides
    public IKeyAuthRestClientFactory provideIKeyAuthRestClientFactory(Context context) {
        return new KeyAuthRestClientFactory(context);
    }
}
