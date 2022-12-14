package io.extact.msa.rms.console.it;

import io.extact.msa.rms.console.external.stub.RmsServerApiRemoteStub;
import io.extact.msa.rms.console.external.stub.RmsServerApiRemoteStubApplication;
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
@AddBean(RmsServerApiRemoteStub.class)
@AddBean(RmsServerApiRemoteStubApplication.class)
//configuredCdi.register.0.classは/main/resource/application.yamlで定義済み
@AddConfig(key = "configuredCdi.register.1.class", value = "io.extact.msa.rms.platform.core.jwt.impl.jose4j.Jose4jJwtGenerator")
@AddConfig(key = "configuredCdi.register.2.class", value = "io.extact.msa.rms.platform.core.jwt.impl.jose4j.Jose4jPrivateSecretedTokenValidator")
@AddConfig(key = "server.port", value = "7001") // for RemoteStub Server port
@AddConfig(key = "web-api/mp-rest/url", value = "http://localhost:7001") // for REST Client
class StubClientScenarioTest extends AbstractClientScenarioTest {
}
