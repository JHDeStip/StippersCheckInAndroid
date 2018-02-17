package stip.stipperscheckin.rest.restservices;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RestServicesModule {
    @Singleton
    @Binds
    public abstract ICheckInService bindICheckInService (CheckInService checkInService);
}
