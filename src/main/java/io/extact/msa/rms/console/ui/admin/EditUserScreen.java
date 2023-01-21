package io.extact.msa.rms.console.ui.admin;

import static io.extact.msa.rms.console.ui.ClientConstants.*;
import static io.extact.msa.rms.console.ui.textio.TextIoUtils.*;

import io.extact.msa.rms.console.model.UserAccountClientModel;
import io.extact.msa.rms.console.service.ClientService;
import io.extact.msa.rms.console.ui.ClientConstants;
import io.extact.msa.rms.console.ui.TransitionMap.RmsScreen;
import io.extact.msa.rms.console.ui.TransitionMap.Transition;
import io.extact.msa.rms.console.ui.textio.RmsStringInputReader.PatternMessage;
import io.extact.msa.rms.console.ui.textio.TextIoUtils;
import io.extact.msa.rms.platform.fw.domain.vo.UserType;
import io.extact.msa.rms.platform.fw.exception.BusinessFlowException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EditUserScreen implements RmsScreen {

    private final ClientService service;

    @Override
    public Transition play(UserAccountClientModel loginUser, boolean printHeader) {

        if (printHeader) {
            printScreenHeader(loginUser, "ユーザ情報編集画面");
        }

        // ユーザ一覧を表示
        var users = service.getAllUserAccounts();
        println(EDIT_USER_INFORMATION);
        users.forEach(dto ->
            println(ClientConstants.USER_ACCOUNT_FORMAT.format(dto))
        );
        blankLine();

        // 編集するユーザを選択
        var selectNumber = newIntInputReader()
                .withSelectableValues(
                        users.stream()
                            .map(UserAccountClientModel::getId)
                            .toList(),
                        SCREEN_BREAK_VALUE)
                .read("ユーザ番号");
        if (TextIoUtils.isBreak(selectNumber)) {
            return Transition.ADMIN_MAIN;
        }
        blankLine();

        var targetUser = users.stream()
                .filter(user -> user.getId().equals(selectNumber))
                .findFirst()
                .get();

        // パスワードの入力
        var password = newStringInputReader()
                .withDefaultValue(targetUser.getPassword())
                .withMinLength(5)
                .withMaxLength(15)
                .read("パスワード");
        targetUser.setPassword(password);

        // ユーザ名の入力
        var userName = newStringInputReader()
                .withDefaultValue(targetUser.getUserName())
                .withMinLength(1)
                .read("ユーザ名");
        targetUser.setUserName(userName);

        // 電話番号の入力
        var phoneNumber = newStringInputReader()
                .withDefaultValue(targetUser.getPhoneNumber())
                .withMaxLength(14)
                .withPattern(PatternMessage.PHONE_NUMBER)
                .read("電話番号（省略可）");
        targetUser.setPhoneNumber(phoneNumber);

        // 連絡先の入力
        var contact = newStringInputReader()
                .withDefaultValue(targetUser.getPhoneNumber())
                .withMaxLength(15)
                .read("連絡先（省略可）");
        targetUser.setContact(contact);

        // 連絡先の入力
        var userType = newEnumInputReader(UserType.class)
                .withDefaultValue(targetUser.getUserType())
                .read("権限");
        targetUser.setUserType(userType);

        // ユーザ情報の更新実行
        try {
            var updatedUser = service.updateUserAccount(targetUser);
            printResultInformation(updatedUser);
            return Transition.ADMIN_MAIN;

        } catch (BusinessFlowException e) {
            printServerError(e);
            return play(loginUser, false); // start over!!

        }
    }

    private void printResultInformation(UserAccountClientModel updatedUserAccount) {
        blankLine();
        println("***** ユーザ登録結果 *****");
        printf("[%s]のユーザ情報を更新しました", updatedUserAccount.getId());
        blankLine();
        blankLine();
        waitPressEnter();
    }
}
