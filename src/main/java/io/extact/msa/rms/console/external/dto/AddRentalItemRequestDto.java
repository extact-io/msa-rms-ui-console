package io.extact.msa.rms.console.external.dto;

import io.extact.msa.rms.console.model.RentalItemClientModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter @Setter @ToString
public class AddRentalItemRequestDto {

    private String serialNo;
    private String itemName;

    public static AddRentalItemRequestDto from(RentalItemClientModel model) {
        if (model == null) {
            return null;
        }
        return AddRentalItemRequestDto.of(model.getSerialNo(), model.getItemName());
    }
}
