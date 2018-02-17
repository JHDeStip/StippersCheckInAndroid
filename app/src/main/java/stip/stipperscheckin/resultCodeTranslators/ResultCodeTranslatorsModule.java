package stip.stipperscheckin.resultCodeTranslators;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ResultCodeTranslatorsModule {
    @Singleton
    @Binds
    public abstract ICheckInServiceResultCodeTranslator bindICheckInServiceResultCodeTranslator(CheckInServiceResultCodeTranslator checkInServiceResultCodeTranslator);
}
