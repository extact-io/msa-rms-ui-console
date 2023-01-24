package io.extact.msa.rms.console.external.stub;

import io.extact.msa.rms.platform.test.stub.dto.UserAccountStubDto;

public interface Authenticator {
    UserAccountStubDto authenticate(String loginId, String password);
}
