package io.extact.msa.rms.console.external.dto;

import java.time.LocalDateTime;

import io.extact.msa.rms.console.model.ReservationClientModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter @Setter @ToString
public class ReservationClientDto {

    private Integer id;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String note;
    private int rentalItemId;
    private int userAccountId;
    private RentalItemClientDto rentalItemDto;
    private UserAccountClientDto userAccountDto;

    public static ReservationClientDto from(ReservationClientModel model) {
        if (model == null) {
            return null;
        }
        var dto = new ReservationClientDto();
        dto.setId(model.getId());
        dto.setStartDateTime(model.getStartDateTime());
        dto.setEndDateTime(model.getEndDateTime());
        dto.setNote(model.getNote());
        dto.setRentalItemId(model.getRentalItemId());
        dto.setUserAccountId(model.getUserAccountId());
        if (model.getRentalItemModel() != null) {
            dto.setRentalItemDto(model.getRentalItemModel().transform(RentalItemClientDto::from));
        }
        if (model.getUserAccountModel() != null) {
            dto.setUserAccountDto(model.getUserAccountModel().transform(UserAccountClientDto::from));
        }
        return dto;
    }

    public ReservationClientModel toModel() {
        var reservation = ReservationClientModel.of(id, startDateTime, endDateTime, note, rentalItemId, userAccountId);
        if (rentalItemDto != null) {
            reservation.setRentalItemModel(rentalItemDto.toModel());
        }
        if (userAccountDto != null) {
            reservation.setUserAccountModel(userAccountDto.toModel());
        }
        return reservation;
    }
}
