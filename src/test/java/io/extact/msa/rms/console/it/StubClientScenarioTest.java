package io.extact.msa.rms.console.it;

import io.extact.msa.rms.console.external.stub.LoginServerRemoteStub;
import io.extact.msa.rms.console.external.stub.LoginServerRemoteStubApplication;
import io.extact.msa.rms.console.external.stub.RmsServerRemoteStub;
import io.extact.msa.rms.console.external.stub.RmsServerRemoteStubApplication;
import io.helidon.microprofile.tests.junit5.AddBean;
import io.helidon.microprofile.tests.junit5.AddConfig;

/**
 * Remoteスタブを使ってClientServiceクラスをテストする。
 * <pre>
 * ・テスト対象：CID Bean(ClientApplicationService)
 * ・実物：RestClient(RmsServerApi)
 *     ↓ HTTP
 * ・スタブ：RestResource(RmsServerApiRemoteStub)
 * </pre>
 */
@AddBean(LoginServerRemoteStub.class)
@AddBean(LoginServerRemoteStubApplication.class)
@AddBean(RmsServerRemoteStub.class)
@AddBean(RmsServerRemoteStubApplication.class)
//rms.cdi.configuredCdi.register.0.classは/main/resource/application.yamlで定義済み
@AddConfig(key = "rms.cdi.configuredCdi.register.1.class", value = "io.extact.msa.rms.platform.core.jwt.provider.impl.Auth0RsaJwtGenerator")
@AddConfig(key = "server.port", value = "7001") // for RemoteStub Server port
@AddConfig(key = "web-api/mp-rest/url", value = "http://localhost:7001") // for REST Client
class StubClientScenarioTest extends AbstractClientScenarioTest {
}
