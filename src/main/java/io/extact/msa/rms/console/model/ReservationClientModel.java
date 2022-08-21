package io.extact.msa.rms.console.model;

import java.time.LocalDateTime;

import io.extact.msa.rms.platform.fw.domain.Transformable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter @Setter
@EqualsAndHashCode
@ToString
public class ReservationClientModel implements Transformable {

    private Integer id;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String note;
    private int rentalItemId;
    private int userAccountId;
    private RentalItemClientModel rentalItemModel;
    private UserAccountClientModel userAccountModel;

    public static ReservationClientModel of(Integer id, LocalDateTime startDateTime, LocalDateTime endDateTime,
            String note, int rentalItemId, int userAccountId) {
        return of(id, startDateTime, endDateTime, note, rentalItemId, userAccountId, null, null);
    }

    public static ReservationClientModel ofTransient(LocalDateTime startDateTime, LocalDateTime endDateTime,
            String note, int rentalItemId, int userAccountId) {
        return of(null, startDateTime, endDateTime, note, rentalItemId, userAccountId, null, null);
    }
}
