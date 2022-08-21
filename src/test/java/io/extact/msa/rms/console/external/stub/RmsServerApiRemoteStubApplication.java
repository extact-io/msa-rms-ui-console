package io.extact.msa.rms.console.external.stub;

import java.util.Set;

import io.extact.msa.rms.platform.core.jwt.JwtSecurityFilterFeature;
import io.extact.msa.rms.platform.core.jwt.login.LoginUserRequestFilter;
import io.extact.msa.rms.platform.core.role.RoleSecurityDynamicFeature;
import io.extact.msa.rms.platform.fw.webapi.server.RmsApplication;

public class RmsServerApiRemoteStubApplication extends RmsApplication {
    @Override
    protected Set<Class<?>> getWebApiClasses() {
        return Set.of(
                JwtSecurityFilterFeature.class,
                RoleSecurityDynamicFeature.class,
                LoginUserRequestFilter.class,
                RmsServerApiRemoteStub.class
                );
    }
}
