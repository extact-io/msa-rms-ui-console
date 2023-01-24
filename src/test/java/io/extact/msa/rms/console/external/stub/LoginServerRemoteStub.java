package io.extact.msa.rms.console.external.stub;

import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;

import io.extact.msa.rms.console.external.LoginServerRestClient;
import io.extact.msa.rms.console.external.dto.UserAccountClientDto;
import io.extact.msa.rms.platform.core.jwt.provider.GenerateToken;
import io.extact.msa.rms.platform.core.validate.ValidateParam;
import io.extact.msa.rms.platform.fw.domain.vo.UserType;
import io.extact.msa.rms.platform.test.stub.dto.UserAccountStubDto;

@Path("login")
@ValidateParam
@ApplicationScoped
public class LoginServerRemoteStub implements LoginServerRestClient {

    private Authenticator authenticator;

    @Inject
    public LoginServerRemoteStub(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    @GenerateToken
    @Override
    public UserAccountClientDto authenticate(Map<String, String> paramMap) {
        return authenticator.authenticate(paramMap.get("loginId"), paramMap.get("password"))
                .transform(this::convertUserAccountDto);
    }

    // ----------------------------------------------------- convert methods

    private UserAccountClientDto convertUserAccountDto(UserAccountStubDto src) {
        return UserAccountClientDto.of(src.getId(), src.getLoginId(), src.getPassword(), src.getUserName(),
                src.getPhoneNumber(), src.getContact(), UserType.valueOf(src.getUserType()));
    }
}
