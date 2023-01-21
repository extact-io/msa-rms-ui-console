package io.extact.msa.rms.console.external.stub;

import java.time.LocalDate;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Path;

import io.extact.msa.rms.console.external.RmsServerRestClient;
import io.extact.msa.rms.console.external.dto.AddRentalItemRequestDto;
import io.extact.msa.rms.console.external.dto.AddReservationRequestDto;
import io.extact.msa.rms.console.external.dto.AddUserAccountRequestDto;
import io.extact.msa.rms.console.external.dto.RentalItemClientDto;
import io.extact.msa.rms.console.external.dto.ReservationClientDto;
import io.extact.msa.rms.console.external.dto.UserAccountClientDto;
import io.extact.msa.rms.platform.core.validate.ValidateGroup;
import io.extact.msa.rms.platform.core.validate.ValidateParam;
import io.extact.msa.rms.platform.fw.domain.constraint.RmsId;
import io.extact.msa.rms.platform.fw.domain.constraint.ValidationGroups.Add;
import io.extact.msa.rms.platform.fw.domain.vo.UserType;
import io.extact.msa.rms.platform.fw.login.LoginUserUtils;
import io.extact.msa.rms.platform.test.stub.RentalItemMemoryStub;
import io.extact.msa.rms.platform.test.stub.ReservationMemoryStub;
import io.extact.msa.rms.platform.test.stub.UserAccountMemoryStub;
import io.extact.msa.rms.platform.test.stub.dto.AddRentalItemStubDto;
import io.extact.msa.rms.platform.test.stub.dto.AddReservationStubDto;
import io.extact.msa.rms.platform.test.stub.dto.AddUserAccountStubDto;
import io.extact.msa.rms.platform.test.stub.dto.RentalItemStubDto;
import io.extact.msa.rms.platform.test.stub.dto.ReservationStubDto;
import io.extact.msa.rms.platform.test.stub.dto.UserAccountStubDto;

@Path("/rms")
@ValidateParam
@ApplicationScoped
public class RmsServerRemoteStub implements RmsServerRestClient {

    private RentalItemMemoryStub itemStub = new RentalItemMemoryStub();
    private UserAccountMemoryStub userStub = new UserAccountMemoryStub();
    private ReservationMemoryStub reservationStub = new ReservationMemoryStub(itemStub, userStub);

    @PostConstruct
    public void init() {
        itemStub.init();
        userStub.init();
        reservationStub.init();
    }

    @Override
    // @RmsIdと@NotNullはparameter errorのテストのためにココだけ付けている
    public List<ReservationClientDto> findReservation(@RmsId int itemId, @NotNull LocalDate targetDate) {
        return reservationStub.findByRentalItemAndStartDate(itemId, targetDate).stream()
                .map(this::convertReservationDto)
                .toList();
    }

    @Override
    public List<ReservationClientDto> findReservationByReserverId(Integer reserverId) {
        return reservationStub.findByReserverId(reserverId).stream()
                .map(this::convertReservationDto)
                .toList();
    }

    @Override
    public List<ReservationClientDto> getOwnReservations() {
        return reservationStub.findByReserverId(LoginUserUtils.get().getUserId()).stream()
                .map(this::convertReservationDto)
                .toList();
    }

    @Override
    public List<RentalItemClientDto> getAllRentalItems() {
        return itemStub.getAll().stream()
                .map(this::convertRentalItemDto)
                .toList();
    }

    @Override
    public List<UserAccountClientDto> getAllUserAccounts() {
        return userStub.getAll().stream()
                .map(this::convertUserAccountDto)
                .toList();
    }

    @ValidateGroup(groups = Add.class)
    @Override
    public ReservationClientDto addReservation(AddReservationRequestDto requestDto) {
        return reservationStub.add(convertAddReservationDto(requestDto))
                .transform(this::convertReservationDto);
    }

    @Override
    public RentalItemClientDto addRentalItem(AddRentalItemRequestDto requestDto) {
        return itemStub.add(convertAddRentalItemDto(requestDto))
                .transform(this::convertRentalItemDto);
    }

    @Override
    public UserAccountClientDto addUserAccount(AddUserAccountRequestDto requestDto) {
        return userStub.add(convertAddUserAccountDto(requestDto))
                .transform(this::convertUserAccountDto);
    }

    @Override
    public void cancelReservation(Integer reservationId) {
        reservationStub.cancel(reservationId, LoginUserUtils.get().getUserId());
    }

    @Override
    public UserAccountClientDto updateUserAccount(UserAccountClientDto updateDto) {
        return userStub.update(convertUserAccountStubDto(updateDto))
                .transform(this::convertUserAccountDto);
    }


    // ----------------------------------------------------- convert methods

    private RentalItemClientDto convertRentalItemDto(RentalItemStubDto src) {
        return RentalItemClientDto.of(src.getId(), src.getSerialNo(), src.getItemName());
    }
    private AddRentalItemStubDto convertAddRentalItemDto(AddRentalItemRequestDto src) {
        return AddRentalItemStubDto.of(src.getSerialNo(), src.getItemName());
    }

    private ReservationClientDto convertReservationDto(ReservationStubDto src) {
        return ReservationClientDto.of(src.getId(), src.getStartDateTime(), src.getEndDateTime(), src.getNote(),
                src.getRentalItemId(), src.getUserAccountId(), src.getRentalItemDto(this::convertRentalItemDto),
                src.getUserAccountDto(this::convertUserAccountDto));
    }

    private AddReservationStubDto convertAddReservationDto(AddReservationRequestDto src) {
        return AddReservationStubDto.of(src.getStartDateTime(), src.getEndDateTime(), src.getNote(),
                src.getRentalItemId(), src.getUserAccountId());
    }

    private UserAccountClientDto convertUserAccountDto(UserAccountStubDto src) {
        return UserAccountClientDto.of(src.getId(), src.getLoginId(), src.getPassword(), src.getUserName(),
                src.getPhoneNumber(), src.getContact(), UserType.valueOf(src.getUserType()));
    }
    private UserAccountStubDto convertUserAccountStubDto(UserAccountClientDto src) {
        return UserAccountStubDto.of(src.getId(), src.getLoginId(), src.getPassword(), src.getUserName(),
                src.getPhoneNumber(), src.getContact(), UserType.valueOf(src.getUserType()));
    }
    private AddUserAccountStubDto convertAddUserAccountDto(AddUserAccountRequestDto src) {
        return AddUserAccountStubDto.of(src.getLoginId(), src.getPassword(), src.getUserName(),
                src.getPhoneNumber(), src.getContact(), src.getUserType());
    }
}
