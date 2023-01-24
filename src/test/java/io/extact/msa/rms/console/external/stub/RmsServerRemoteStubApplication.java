package io.extact.msa.rms.console.external.stub;

import java.util.Set;

import jakarta.ws.rs.ApplicationPath;

import org.eclipse.microprofile.auth.LoginConfig;

import io.extact.msa.rms.platform.fw.login.LoginUserFromJwtRequestFilter;
import io.extact.msa.rms.platform.fw.webapi.RmsApplication;

@LoginConfig(authMethod = "MP-JWT")
@ApplicationPath("api")
public class RmsServerRemoteStubApplication extends RmsApplication {
    @Override
    protected Set<Class<?>> getWebApiClasses() {
        return Set.of(
                LoginUserFromJwtRequestFilter.class,
                RmsServerRemoteStub.class
                );
    }
}
