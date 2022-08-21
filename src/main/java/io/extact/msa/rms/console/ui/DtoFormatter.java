package io.extact.msa.rms.console.ui;

import static io.extact.msa.rms.console.ui.ClientConstants.*;

import io.extact.msa.rms.console.model.RentalItemClientModel;
import io.extact.msa.rms.console.model.ReservationClientModel;
import io.extact.msa.rms.console.model.UserAccountClientModel;

public interface DtoFormatter<T> {

    String format(T dto);

    static class RentalItemFormatter implements DtoFormatter<RentalItemClientModel> {
        @Override
        public String format(RentalItemClientModel dto) {
            return String.format("[%s]%s シリアル番号：%s",
                    dto.getId(),
                    dto.getItemName(),
                    dto.getSerialNo());
        }
    }

    static class ReservationFormatter implements DtoFormatter<ReservationClientModel> {
        @Override
        public String format(ReservationClientModel dto) {
            return String.format("[%s] %s - %s %s %s %s",
                    dto.getId(),
                    DATETIME_FORMAT.format(dto.getStartDateTime()),
                    DATETIME_FORMAT.format(dto.getEndDateTime()),
                    dto.getRentalItemModel().getItemName(),
                    dto.getUserAccountModel().getUserName(),
                    dto.getNote());
        }
    }

    static class UserAccountFormatter implements DtoFormatter<UserAccountClientModel> {
        @Override
        public String format(UserAccountClientModel dto) {
            return String.format("[%s] %s/%s %s %s",
                    dto.getId(),
                    dto.getLoginId(),
                    dto.getPassword(),
                    dto.getUserName(),
                    dto.getUserType());
        }
    }
}
