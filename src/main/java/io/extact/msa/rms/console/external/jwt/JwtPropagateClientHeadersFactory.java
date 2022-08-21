package io.extact.msa.rms.console.external.jwt;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * サーバから発行されたJsonWebTokenをリクエストヘッダに付加するクラス
 */
@ApplicationScoped
public class JwtPropagateClientHeadersFactory implements ClientHeadersFactory {

    private static final Logger LOG = LoggerFactory.getLogger(JwtPropagateClientHeadersFactory.class);
    private Pair<String, String> jwtHeader;

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> incomingHeaders,
            MultivaluedMap<String, String> clientOutgoingHeaders) {

        if (jwtHeader == null) {
            return clientOutgoingHeaders;
        }

        var newHeadersMap = new MultivaluedHashMap<String, String>(clientOutgoingHeaders);
        newHeadersMap.add(jwtHeader.getKey(), jwtHeader.getValue());
        return newHeadersMap;
    }

    void onEvent(@Observes JwtRecieveEvent event) {
        LOG.info("ヘッダに追加するJWTを受信しました");
        this.jwtHeader = event.getJwtHeader();
    }
}