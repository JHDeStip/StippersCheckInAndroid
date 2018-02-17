package stip.stipperscheckin.services;

import javax.inject.Inject;

import stannieman.rest.Scheme;
import stannieman.rest.models.KeyAuthRestClientFactoryConfig;

public class ConfigService implements IConfigService {
    private static final KeyAuthRestClientFactoryConfig keyAuthRestClientFactoryConfig;
    static {
        keyAuthRestClientFactoryConfig = new KeyAuthRestClientFactoryConfig();
        keyAuthRestClientFactoryConfig.setScheme(Scheme.HTTPS);
        keyAuthRestClientFactoryConfig.setHost("www.stip.be");
        keyAuthRestClientFactoryConfig.setPort(443);
        keyAuthRestClientFactoryConfig.setApiBasePath("members/api");
        keyAuthRestClientFactoryConfig.setKeyParameterName("key");
        keyAuthRestClientFactoryConfig.setTimeout(10000);
    }

    @Inject
    public ConfigService() {}

    @Override
    public KeyAuthRestClientFactoryConfig getKeyAuthRestClientFactoryConfig() {
        return keyAuthRestClientFactoryConfig;
    }
}
