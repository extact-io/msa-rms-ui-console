package io.extact.msa.rms.console.it;

import static io.extact.msa.rms.console.external.ApiType.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.extact.msa.rms.console.external.stub.RmsServerApiRemoteStub;
import io.extact.msa.rms.console.external.stub.RmsServerApiRemoteStubApplication;
import io.extact.msa.rms.console.model.RentalItemClientModel;
import io.extact.msa.rms.console.service.ClientApplicationService;
import io.extact.msa.rms.platform.fw.exception.RmsValidationException;
import io.extact.msa.rms.platform.fw.webapi.SecurityConstraintException;
import io.extact.msa.rms.test.junit5.JulToSLF4DelegateExtension;
import io.helidon.microprofile.tests.junit5.AddBean;
import io.helidon.microprofile.tests.junit5.AddConfig;
import io.helidon.microprofile.tests.junit5.HelidonTest;

@HelidonTest
@AddBean(RmsServerApiRemoteStub.class)
@AddBean(RmsServerApiRemoteStubApplication.class)
// configuredCdi.register.0.classは/main/resource/application.yamlで定義済み
@AddConfig(key = "configuredCdi.register.1.class", value = "io.extact.msa.rms.platform.core.jwt.impl.jose4j.Jose4jJwtGenerator")
@AddConfig(key = "configuredCdi.register.2.class", value = "io.extact.msa.rms.platform.core.jwt.impl.jose4j.Jose4jPrivateSecretedTokenValidator")
@AddConfig(key = PROP_NAME, value = REAL)
@AddConfig(key = "server.port", value = "7001") // for REST server
@AddConfig(key = "web-api/mp-rest/url", value = "http://localhost:7001") // for REST Client
@ExtendWith(JulToSLF4DelegateExtension.class)
class SeverErrorResponseTest {

    @Inject
    private ClientApplicationService service;

    @Test
    void testHandleRmsValidationException() {
        service.authenticate("member2", "member2"); // 事前条件(ログイン)
        var thrown = catchThrowable(() -> service.findReservationByRentalItemAndStartDate(0, LocalDate.of(2022, 4, 1)));
        assertThat(thrown).isInstanceOf(RmsValidationException.class);
    }

    @Test
    void testHandleWebApplicationException() {
        // 事前条件(ログイン)
        service.authenticate("member2", "member2");
        // テスト実行
        var thrown = catchThrowable(() -> service.findReservationByRentalItemAndStartDate(1, null));
        assertThat(thrown).isInstanceOf(WebApplicationException.class);
    }

    @Test
    void testHandleSecurityErrorResponse() {

        // @Authenticatedのメソッドが認証前でエラーになること
        SecurityConstraintException actual = catchThrowableOfType(
            () -> service.getAllRentalItems(),
            SecurityConstraintException.class
        );
        assertThat(actual.getErrorStatus()).isEqualTo(Status.UNAUTHORIZED.getStatusCode());


        // 認証後に正常に呼び出せること
        service.authenticate("member1", "member1");
        service.getAllRentalItems();

        // @RollAllowedの権限がないメソッドが認可エラーになること
        actual = catchThrowableOfType(
            () -> service.addRentalItem(RentalItemClientModel.ofTransient("1234", "foo")),
            SecurityConstraintException.class
        );
        assertThat(actual.getErrorStatus()).isEqualTo(Status.FORBIDDEN.getStatusCode());

        // 権限があるユーザにログインしなおして正常に呼び出せること
        service.authenticate("admin", "admin");
        service.addRentalItem(RentalItemClientModel.ofTransient("1234", "foo"));
    }
}
