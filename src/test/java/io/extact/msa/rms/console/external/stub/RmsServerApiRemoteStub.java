package io.extact.msa.rms.console.external.stub;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Path;

import io.extact.msa.rms.console.external.RmsServerApiRestClient;
import io.extact.msa.rms.console.external.dto.AddRentalItemRequestDto;
import io.extact.msa.rms.console.external.dto.AddReservationRequestDto;
import io.extact.msa.rms.console.external.dto.AddUserAccountRequestDto;
import io.extact.msa.rms.console.external.dto.RentalItemClientDto;
import io.extact.msa.rms.console.external.dto.ReservationClientDto;
import io.extact.msa.rms.console.external.dto.UserAccountClientDto;
import io.extact.msa.rms.platform.core.jwt.consumer.Authenticated;
import io.extact.msa.rms.platform.core.jwt.login.LoginUserUtils;
import io.extact.msa.rms.platform.core.jwt.provider.GenerateToken;
import io.extact.msa.rms.platform.core.validate.ValidateGroup;
import io.extact.msa.rms.platform.core.validate.ValidateParam;
import io.extact.msa.rms.platform.fw.domain.constraint.RmsId;
import io.extact.msa.rms.platform.fw.domain.constraint.ValidationGroups.Add;
import io.extact.msa.rms.platform.fw.domain.vo.UserType;
import io.extact.msa.rms.platform.test.stub.RentalItemMemoryStub;
import io.extact.msa.rms.platform.test.stub.ReservationMemoryStub;
import io.extact.msa.rms.platform.test.stub.UserAccountMemoryStub;
import io.extact.msa.rms.platform.test.stub.dto.AddRentalItemStubDto;
import io.extact.msa.rms.platform.test.stub.dto.AddReservationStubDto;
import io.extact.msa.rms.platform.test.stub.dto.AddUserAccountStubDto;
import io.extact.msa.rms.platform.test.stub.dto.RentalItemStubDto;
import io.extact.msa.rms.platform.test.stub.dto.ReservationStubDto;
import io.extact.msa.rms.platform.test.stub.dto.UserAccountStubDto;

@ApplicationScoped
@ValidateParam
@Path("/rms")
public class RmsServerApiRemoteStub implements RmsServerApiRestClient {

    private static final String ADMIN_ROLE = "ADMIN";
    private static final String MEMBER_ROLE = "MEMBER";

    private RentalItemMemoryStub itemStub = new RentalItemMemoryStub();
    private UserAccountMemoryStub userStub = new UserAccountMemoryStub();
    private ReservationMemoryStub reservationStub = new ReservationMemoryStub(itemStub, userStub);

    @PostConstruct
    public void init() {
        itemStub.init();
        userStub.init();
        reservationStub.init();
    }

    @GenerateToken
    @Override
    public UserAccountClientDto authenticate(Map<String, String> paramMap) {
        return userStub.authenticate(paramMap.get("loginId"), paramMap.get("password"))
                .transform(this::convertUserAccountDto);
    }

    @Authenticated
    @RolesAllowed(MEMBER_ROLE)
    @Override
    // @RmsIdと@NotNullはparameter errorのテストのためにココだけ付けている
    public List<ReservationClientDto> findReservation(@RmsId int itemId, @NotNull LocalDate targetDate) {
        return reservationStub.findByRentalItemAndStartDate(itemId, targetDate).stream()
                .map(this::convertReservationDto)
                .toList();
    }

    @Authenticated
    @RolesAllowed(MEMBER_ROLE)
    @Override
    public List<ReservationClientDto> findReservationByReserverId(Integer reserverId) {
        return reservationStub.findByReserverId(reserverId).stream()
                .map(this::convertReservationDto)
                .toList();
    }

    @Authenticated
    @RolesAllowed(MEMBER_ROLE)
    @Override
    public List<ReservationClientDto> getOwnReservations() {
        return reservationStub.findByReserverId(LoginUserUtils.get().getUserId()).stream()
                .map(this::convertReservationDto)
                .toList();
    }

    @Authenticated
    @RolesAllowed({ MEMBER_ROLE, ADMIN_ROLE })
    @Override
    public List<RentalItemClientDto> getAllRentalItems() {
        return itemStub.getAll().stream()
                .map(this::convertRentalItemDto)
                .toList();
    }

    @Authenticated
    @RolesAllowed(ADMIN_ROLE)
    @Override
    public List<UserAccountClientDto> getAllUserAccounts() {
        return userStub.getAll().stream()
                .map(this::convertUserAccountDto)
                .toList();
    }

    @Authenticated
    @RolesAllowed(MEMBER_ROLE)
    @ValidateGroup(groups = Add.class)
    @Override
    public ReservationClientDto addReservation(AddReservationRequestDto requestDto) {
        return reservationStub.add(convertAddReservationDto(requestDto))
                .transform(this::convertReservationDto);
    }

    @Authenticated
    @RolesAllowed(ADMIN_ROLE)
    @Override
    public RentalItemClientDto addRentalItem(AddRentalItemRequestDto requestDto) {
        return itemStub.add(convertAddRentalItemDto(requestDto))
                .transform(this::convertRentalItemDto);
    }

    @Authenticated
    @RolesAllowed(ADMIN_ROLE)
    @Override
    public UserAccountClientDto addUserAccount(AddUserAccountRequestDto requestDto) {
        return userStub.add(convertAddUserAccountDto(requestDto))
                .transform(this::convertUserAccountDto);
    }

    @Authenticated
    @RolesAllowed(MEMBER_ROLE)
    @Override
    public void cancelReservation(Integer reservationId) {
        reservationStub.cancel(reservationId, LoginUserUtils.get().getUserId());
    }

    @Authenticated
    @RolesAllowed(ADMIN_ROLE)
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
