package io.extact.msa.rms.console.external;

import static io.extact.msa.rms.console.external.ApiType.*;

import java.util.HashMap;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.extact.msa.rms.console.external.dto.UserAccountClientDto;
import io.extact.msa.rms.platform.core.extension.EnabledIfRuntimeConfig;

@ApplicationScoped
@EnabledIfRuntimeConfig(propertyName = PROP_NAME, value = REAL)
public class LoginServerProxy implements LoginServer {

    private LoginServerRestClient login;

    @Inject
    public LoginServerProxy(@RestClient LoginServerRestClient client) {
        this.login = client;
    }

    @Override
    public UserAccountClientDto authenticate(String loginId, String password) {
        var paramMap = new HashMap<String, String>();
        paramMap.put("loginId", loginId);
        paramMap.put("password", password);
        return login.authenticate(paramMap);
    }
}
