package io.extact.msa.rms.console.service;

import java.time.LocalDate;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import io.extact.msa.rms.console.external.RmsServerApi;
import io.extact.msa.rms.console.external.dto.AddRentalItemRequestDto;
import io.extact.msa.rms.console.external.dto.AddReservationRequestDto;
import io.extact.msa.rms.console.external.dto.AddUserAccountRequestDto;
import io.extact.msa.rms.console.external.dto.RentalItemClientDto;
import io.extact.msa.rms.console.external.dto.ReservationClientDto;
import io.extact.msa.rms.console.external.dto.UserAccountClientDto;
import io.extact.msa.rms.console.model.RentalItemClientModel;
import io.extact.msa.rms.console.model.ReservationClientModel;
import io.extact.msa.rms.console.model.UserAccountClientModel;
import io.extact.msa.rms.platform.fw.exception.BusinessFlowException;

@ApplicationScoped
public class ClientApplicationService {

    private RmsServerApi api;

    @Inject
    public ClientApplicationService(RmsServerApi api) {
        this.api = api;
    }

    public UserAccountClientModel authenticate(String loginId, String password) throws BusinessFlowException {
        return api.authenticate(loginId, password).toModel();
    }

    public List<ReservationClientModel> findReservationByRentalItemAndStartDate(Integer rentalItemId,
            LocalDate startDate) throws BusinessFlowException {
        return api.findReservationByRentalItemAndStartDate(rentalItemId, startDate).stream()
                .map(ReservationClientDto::toModel)
                .toList();
    }

    public List<ReservationClientModel> findReservationByReserverId(int reserverId) {
        return api.findReservationByReserverId(reserverId).stream()
                .map(ReservationClientDto::toModel)
                .toList();
    }

    public List<ReservationClientModel> getOwnReservations() {
        return api.getOwnReservations().stream()
                .map(ReservationClientDto::toModel)
                .toList();
    }

    public List<RentalItemClientModel> getAllRentalItems() {
        return api.getAllRentalItems().stream()
                .map(RentalItemClientDto::toModel)
                .toList();
    }

    public List<UserAccountClientModel> getAllUserAccounts() {
        return api.getAllUserAccounts().stream()
                .map(UserAccountClientDto::toModel)
                .toList();
    }

    public ReservationClientModel addReservation(ReservationClientModel addModel) throws BusinessFlowException {
        return api.addReservation(addModel.transform(AddReservationRequestDto::from)).toModel();
    }

    public RentalItemClientModel addRentalItem(RentalItemClientModel addModel) throws BusinessFlowException {
        return api.addRentalItem(addModel.transform(AddRentalItemRequestDto::from)).toModel();
    }

    public UserAccountClientModel addUserAccount(UserAccountClientModel addModel)
            throws BusinessFlowException {
        return api.addUserAccount(addModel.transform(AddUserAccountRequestDto::from)).toModel();
    }

    public void cancelReservation(int reservationId) throws BusinessFlowException {
        api.cancelReservation(reservationId);
    }

    public UserAccountClientModel updateUserAccount(UserAccountClientModel updateModel) {
        return api.updateUserAccount(updateModel.transform(UserAccountClientDto::from)).toModel();
    }
}
