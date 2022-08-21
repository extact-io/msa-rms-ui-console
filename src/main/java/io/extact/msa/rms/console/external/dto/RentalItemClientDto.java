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
public class RentalItemClientDto {

    private Integer id;
    private String serialNo;
    private String itemName;

    public static RentalItemClientDto from(RentalItemClientModel model) {
        if (model == null) {
            return null;
        }
        var dto = new RentalItemClientDto();
        dto.setId(model.getId());
        dto.setSerialNo(model.getSerialNo());
        dto.setItemName(model.getItemName());
        return dto;
    }

    public RentalItemClientModel toModel() {
        return RentalItemClientModel.of(id, serialNo, itemName);
    }
}
