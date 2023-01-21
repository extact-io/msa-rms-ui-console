package io.extact.msa.rms.console.external.stub;

import java.util.Set;

import io.extact.msa.rms.platform.core.jwt.provider.JwtProvideResponseFilter;
import io.extact.msa.rms.platform.fw.webapi.RmsApplication;

public class LoginServerRemoteStubApplication extends RmsApplication {
    @Override
    protected Set<Class<?>> getWebApiClasses() {
        return Set.of(
                    JwtProvideResponseFilter.class,
                    RmsServerRemoteStub.class
                );
    }
}
