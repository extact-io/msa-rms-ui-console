package io.extact.msa.rms.console.model;

import io.extact.msa.rms.platform.fw.domain.Transformable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor // for JSON Seserialize
@AllArgsConstructor(staticName = "of")
@Getter @Setter
@EqualsAndHashCode
@ToString
public class RentalItemClientModel implements Transformable {

    private Integer id;
    private String serialNo;
    private String itemName;

    public static RentalItemClientModel ofTransient(String serialNo, String itemName) {
        return of(null, serialNo, itemName);
    }
}
