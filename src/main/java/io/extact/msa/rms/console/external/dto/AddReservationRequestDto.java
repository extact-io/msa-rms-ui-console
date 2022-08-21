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
public class AddReservationRequestDto {

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String note;
    private int rentalItemId;
    private int userAccountId;

    public static AddReservationRequestDto from(ReservationClientModel model) {
        if (model == null) {
            return null;
        }
        return AddReservationRequestDto.of(model.getStartDateTime(), model.getEndDateTime(), model.getNote(),
                model.getRentalItemId(), model.getUserAccountId());
    }
}
