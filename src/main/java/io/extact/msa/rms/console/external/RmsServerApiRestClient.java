package io.extact.msa.rms.console.external;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.extact.msa.rms.console.external.dto.AddRentalItemRequestDto;
import io.extact.msa.rms.console.external.dto.AddReservationRequestDto;
import io.extact.msa.rms.console.external.dto.AddUserAccountRequestDto;
import io.extact.msa.rms.console.external.dto.RentalItemClientDto;
import io.extact.msa.rms.console.external.dto.ReservationClientDto;
import io.extact.msa.rms.console.external.dto.UserAccountClientDto;
import io.extact.msa.rms.platform.core.jaxrs.converter.RmsTypeParameterFeature;
import io.extact.msa.rms.platform.core.jwt.client.JwtPropagateClientHeadersFactory;
import io.extact.msa.rms.platform.core.jwt.client.JwtRecieveResponseFilter;
import io.extact.msa.rms.platform.fw.webapi.client.ExceptionPropagateClientMapper;

@RegisterRestClient(configKey = "web-api")
@RegisterProvider(RmsTypeParameterFeature.class)
@RegisterProvider(ExceptionPropagateClientMapper.class)
@RegisterProvider(JwtRecieveResponseFilter.class)
@RegisterClientHeaders(JwtPropagateClientHeadersFactory.class)
@Path("/rms")
public interface RmsServerApiRestClient {

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    UserAccountClientDto authenticate(Map<String, String> paramMap);

    @GET
    @Path("/reservations/item/{itemId}/startdate/{startDate}")
    @Produces(MediaType.APPLICATION_JSON)
    List<ReservationClientDto> findReservation(@PathParam("itemId") int itemId, @PathParam("startDate") LocalDate targetDate);

    @GET
    @Path("/reservations/reserver/{reserverId}")
    @Produces(MediaType.APPLICATION_JSON)
    List<ReservationClientDto> findReservationByReserverId(@PathParam("reserverId") Integer reserverId);

    @GET
    @Path("/reservations/own")
    @Produces(MediaType.APPLICATION_JSON)
    List<ReservationClientDto> getOwnReservations();

    @GET
    @Path("/items")
    List<RentalItemClientDto> getAllRentalItems();

    @GET
    @Path("/users")
    List<UserAccountClientDto> getAllUserAccounts();

    @POST
    @Path("/reservations")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    ReservationClientDto addReservation(AddReservationRequestDto requestDto);

    @POST
    @Path("/items")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    RentalItemClientDto addRentalItem(AddRentalItemRequestDto requestDto);

    @POST
    @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    UserAccountClientDto addUserAccount(AddUserAccountRequestDto requestDto);

    @DELETE
    @Path("/reservations/own/{reservationId}")
    void cancelReservation(@PathParam("reservationId") Integer reservationId);

    @PUT
    @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    UserAccountClientDto updateUserAccount(UserAccountClientDto updateDto);
}
