package io.extact.msa.rms.console.external.dto;

import java.util.Set;

import io.extact.msa.rms.console.model.UserAccountClientModel;
import io.extact.msa.rms.platform.core.jwt.provider.UserClaims;
import io.extact.msa.rms.platform.fw.domain.vo.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter @Setter @ToString
public class UserAccountClientDto implements UserClaims {

    private Integer id;
    private String loginId;
    private String password;
    private String userName;
    private String phoneNumber;
    private String contact;
    private UserType userType;

    public static UserAccountClientDto from(UserAccountClientModel model) {
        if (model == null) {
            return null;
        }
        var dto = new UserAccountClientDto();
        dto.setId(model.getId());
        dto.setLoginId(model.getLoginId());
        dto.setPassword(model.getPassword());
        dto.setUserName(model.getUserName());
        dto.setPhoneNumber(model.getPhoneNumber());
        dto.setContact(model.getContact());
        dto.setUserType(model.getUserType().name());
        return dto;
    }

    public UserAccountClientModel toModel() {
        return UserAccountClientModel.of(id, loginId, password, userName, phoneNumber, contact, userType);
    }

    // original getter
    public String getUserType() {
        return userType.name();
    }

    // original setter
    public void setUserType(String userType) {
        this.userType = UserType.valueOf(userType);
    }


    // --------------------------------------------- implements UserClaims

    @Override
    public String getUserId() {
        return String.valueOf(getId());
    }
    @Override
    public String getUserPrincipalName() {
        return getContact() + "@rms.com";
    }
    @Override
    public Set<String> getGroups() {
        return Set.of(getUserType());
    }
}
