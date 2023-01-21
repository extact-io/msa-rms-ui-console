package io.extact.msa.rms.console.service;

import java.time.LocalDate;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import io.extact.msa.rms.console.external.LoginServer;
import io.extact.msa.rms.console.external.RmsServer;
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
public class ClientService {

    private RmsServer rmsServer;
    private LoginServer loginServer;

    @Inject
    public ClientService(LoginServer loginServer, RmsServer api) {
        this.loginServer = loginServer;
        this.rmsServer = api;
    }

    public UserAccountClientModel authenticate(String loginId, String password) throws BusinessFlowException {
        return loginServer.authenticate(loginId, password).toModel();
    }

    public List<ReservationClientModel> findReservationByRentalItemAndStartDate(Integer rentalItemId,
            LocalDate startDate) throws BusinessFlowException {
        return rmsServer.findReservationByRentalItemAndStartDate(rentalItemId, startDate).stream()
                .map(ReservationClientDto::toModel)
                .toList();
    }

    public List<ReservationClientModel> findReservationByReserverId(int reserverId) {
        return rmsServer.findReservationByReserverId(reserverId).stream()
                .map(ReservationClientDto::toModel)
                .toList();
    }

    public List<ReservationClientModel> getOwnReservations() {
        return rmsServer.getOwnReservations().stream()
                .map(ReservationClientDto::toModel)
                .toList();
    }

    public List<RentalItemClientModel> getAllRentalItems() {
        return rmsServer.getAllRentalItems().stream()
                .map(RentalItemClientDto::toModel)
                .toList();
    }

    public List<UserAccountClientModel> getAllUserAccounts() {
        return rmsServer.getAllUserAccounts().stream()
                .map(UserAccountClientDto::toModel)
                .toList();
    }

    public ReservationClientModel addReservation(ReservationClientModel addModel) throws BusinessFlowException {
        return rmsServer.addReservation(addModel.transform(AddReservationRequestDto::from)).toModel();
    }

    public RentalItemClientModel addRentalItem(RentalItemClientModel addModel) throws BusinessFlowException {
        return rmsServer.addRentalItem(addModel.transform(AddRentalItemRequestDto::from)).toModel();
    }

    public UserAccountClientModel addUserAccount(UserAccountClientModel addModel)
            throws BusinessFlowException {
        return rmsServer.addUserAccount(addModel.transform(AddUserAccountRequestDto::from)).toModel();
    }

    public void cancelReservation(int reservationId) throws BusinessFlowException {
        rmsServer.cancelReservation(reservationId);
    }

    public UserAccountClientModel updateUserAccount(UserAccountClientModel updateModel) {
        return rmsServer.updateUserAccount(updateModel.transform(UserAccountClientDto::from)).toModel();
    }
}
