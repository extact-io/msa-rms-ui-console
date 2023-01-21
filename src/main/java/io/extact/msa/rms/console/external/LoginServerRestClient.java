package io.extact.msa.rms.console.external;

import java.util.Map;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.extact.msa.rms.console.external.dto.UserAccountClientDto;
import io.extact.msa.rms.platform.core.jaxrs.converter.RmsTypeParameterFeature;
import io.extact.msa.rms.platform.fw.external.PropagateResponseExceptionMapper;
import io.extact.msa.rms.platform.fw.external.jwt.JwtRecieveResponseFilter;

@RegisterRestClient(configKey = "web-api")
@RegisterProvider(RmsTypeParameterFeature.class)
@RegisterProvider(PropagateResponseExceptionMapper.class)
@RegisterProvider(JwtRecieveResponseFilter.class)
@Path("/login")
public interface LoginServerRestClient {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    UserAccountClientDto authenticate(Map<String, String> paramMap);
}
