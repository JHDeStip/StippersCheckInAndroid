package stip.stipperscheckin.helpers;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class HelpersModule {
    @Singleton
    @Binds
    public abstract IScreenOrientationHelper bindIScreenOrientationHelper (ScreenOrientationHelper screenOrientationHelper);
}
