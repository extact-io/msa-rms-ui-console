package io.extact.msa.rms.console.external.dto;

import io.extact.msa.rms.console.model.UserAccountClientModel;
import io.extact.msa.rms.platform.fw.domain.vo.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter @Setter @ToString
public class AddUserAccountRequestDto {

    private String loginId;
    private String password;
    private String userName;
    private String phoneNumber;
    private String contact;
    private UserType userType;

    public static AddUserAccountRequestDto from(UserAccountClientModel model) {
        if (model == null) {
            return null;
        }
        return AddUserAccountRequestDto.of(model.getLoginId(), model.getPassword(), model.getUserName(),
                model.getPhoneNumber(), model.getContact(), model.getUserType());
    }
}
