package io.extact.msa.rms.console.model;

import java.util.Set;

import io.extact.msa.rms.platform.fw.domain.Transformable;
import io.extact.msa.rms.platform.fw.domain.vo.UserType;
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
public class UserAccountClientModel implements Transformable {

    private Integer id;
    private String loginId;
    private String password;
    private String userName;
    private String phoneNumber;
    private String contact;
    private UserType userType;

    public static UserAccountClientModel ofTransient(String loginId, String password, String userName, String phoneNumber, String contact, UserType userType) {
        return of(null, loginId, password, userName, phoneNumber, contact, userType);
    }

    public Set<String> getRoles() {
        return Set.of(userType.name());
    }
}
