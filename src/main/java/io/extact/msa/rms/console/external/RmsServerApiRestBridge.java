package io.extact.msa.rms.console.external;

import static io.extact.msa.rms.console.external.ApiType.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.extact.msa.rms.console.external.dto.AddRentalItemRequestDto;
import io.extact.msa.rms.console.external.dto.AddReservationRequestDto;
import io.extact.msa.rms.console.external.dto.AddUserAccountRequestDto;
import io.extact.msa.rms.console.external.dto.RentalItemClientDto;
import io.extact.msa.rms.console.external.dto.ReservationClientDto;
import io.extact.msa.rms.console.external.dto.UserAccountClientDto;
import io.extact.msa.rms.platform.core.extension.EnabledIfRuntimeConfig;
import io.extact.msa.rms.platform.fw.exception.BusinessFlowException;

@ApplicationScoped
@EnabledIfRuntimeConfig(propertyName = PROP_NAME, value = REAL)
public class RmsServerApiRestBridge implements RmsServerApi {

    private RmsServerApiRestClient client;

    @Inject
    public RmsServerApiRestBridge(@RestClient RmsServerApiRestClient client) {
        this.client = client;
    }

    @Override
    public UserAccountClientDto authenticate(String loginId, String password) {
        var paramMap = new HashMap<String, String>();
        paramMap.put("loginId", loginId);
        paramMap.put("password", password);
        return client.authenticate(paramMap);
    }

    @Override
    public List<ReservationClientDto> findReservationByRentalItemAndStartDate(Integer targetRentalItemId, LocalDate targetDate) {
        return client.findReservation(targetRentalItemId, targetDate);
    }

    @Override
    public List<ReservationClientDto> findReservationByReserverId(int reserverId) {
        return client.findReservationByReserverId(reserverId);
    }

    @Override
    public List<ReservationClientDto> getOwnReservations() {
        return client.getOwnReservations();
    }

    @Override
    public List<RentalItemClientDto> getAllRentalItems() {
        return client.getAllRentalItems();
    }

    @Override
    public List<UserAccountClientDto> getAllUserAccounts() {
        return client.getAllUserAccounts();
    }

    @Override
    public ReservationClientDto addReservation(AddReservationRequestDto addDto) {
        return client.addReservation(addDto);
    }

    @Override
    public RentalItemClientDto addRentalItem(AddRentalItemRequestDto addDto) {
        return client.addRentalItem(addDto);
    }

    @Override
    public UserAccountClientDto addUserAccount(AddUserAccountRequestDto addDto) {
        return client.addUserAccount(addDto);
    }

    @Override
    public void cancelReservation(int reservationId) throws BusinessFlowException {
        client.cancelReservation(reservationId);
    }

    @Override
    public UserAccountClientDto updateUserAccount(UserAccountClientDto updateDto) {
        return client.updateUserAccount(updateDto);
    }
}
