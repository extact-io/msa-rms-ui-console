package io.extact.msa.rms.console.it;

import static io.extact.msa.rms.console.external.ApiType.*;

import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import io.helidon.microprofile.tests.junit5.AddConfig;

/**
 * コンテナを立ち上げProduction相当の構成でClientServiceクラスをテストする。
 * <pre>
 * ・テスト対象：CID Bean(ClientApplicationService)
 * ・実物：RestClient(RmsServerApi)
 *     ↓ HTTP
 * ・実物：コンテナ(msa-applicaiton)
 *     ↓ HTTP
 * ・実物：コンテナ(msa-service-item)
 * ・実物：コンテナ(msa-service-reservation)
 * ・実物：コンテナ(msa-service-user)
 * </pre>
 */
@EnabledIfSystemProperty(named = "mvn.docker.profile", matches = "on") // execute maven only
@AddConfig(key = PROP_NAME, value = REAL)
@AddConfig(key = "server.port", value = "8889") // dummy port setting for Server started by HelidonTest
@AddConfig(key = "web-api/mp-rest/url", value = "http://localhost:7001") // for REST Client
class ContainerClientScenarioIT extends AbstractClientScenarioTest {
}