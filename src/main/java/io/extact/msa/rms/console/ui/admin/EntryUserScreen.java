package io.extact.msa.rms.console.ui.admin;

import static io.extact.msa.rms.console.ui.ClientConstants.*;
import static io.extact.msa.rms.console.ui.textio.TextIoUtils.*;

import io.extact.msa.rms.console.model.UserAccountClientModel;
import io.extact.msa.rms.console.service.ClientService;
import io.extact.msa.rms.console.ui.TransitionMap.RmsScreen;
import io.extact.msa.rms.console.ui.TransitionMap.Transition;
import io.extact.msa.rms.console.ui.textio.RmsStringInputReader.PatternMessage;
import io.extact.msa.rms.console.ui.textio.TextIoUtils;
import io.extact.msa.rms.platform.fw.domain.vo.UserType;
import io.extact.msa.rms.platform.fw.exception.BusinessFlowException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EntryUserScreen implements RmsScreen {

    private final ClientService service;

    @Override
    public Transition play(UserAccountClientModel loginUser, boolean printHeader) {

        if (printHeader) {
            printScreenHeader(loginUser, "ユーザ登録画面");
        }

        // 入力インフォメーションの表示
        println(ENTRY_USER_INFORMATION);

        // ログインIDの入力
        var loginId = newStringInputReader()
                .withMinLength(5)
                .withMaxLength(15)
                .withExcludeCheckString(SCREEN_BREAK_KEY)
                .read("ログインID");
        if (TextIoUtils.isBreak(loginId)) {
            return Transition.ADMIN_MAIN;
        }

        // パスワードの入力
        var password = newStringInputReader()
                .withMinLength(5)
                .withMaxLength(15)
                .read("パスワード");

        // ユーザ名の入力
        var userName = newStringInputReader()
                .withMinLength(1)
                .read("ユーザ名");

        // 電話番号の入力
        var phoneNumber = newStringInputReader()
                .withMaxLength(14)
                .withPattern(PatternMessage.PHONE_NUMBER)
                .withDefaultValue("")
                .read("電話番号（省略可）");

        // 連絡先の入力
        var contact = newStringInputReader()
                .withMaxLength(15)
                .withDefaultValue("")
                .read("連絡先（省略可）");

        // 連絡先の入力
        var userType = newEnumInputReader(UserType.class)
                .withDefaultValue(UserType.MEMBER)
                .read("権限");

        // ユーザ登録の実行
        try {
            var addUserAccount = UserAccountClientModel.ofTransient(loginId, password, userName, phoneNumber, contact, userType);
            var newUserAccount = service.addUserAccount(addUserAccount);
            printResultInformation(newUserAccount);
            return Transition.ADMIN_MAIN;

        } catch (BusinessFlowException e) {
            printServerError(e);
            return play(loginUser, false); // start over!!

        }
    }

    private void printResultInformation(UserAccountClientModel newUserAccount) {
        blankLine();
        println("***** ユーザ登録結果 *****");
        println("ユーザ番号：" + newUserAccount.getId());
        println("ログインID：" + newUserAccount.getLoginId());
        println("パスワード：" + newUserAccount.getPassword());
        println("ユーザ名：" + newUserAccount.getUserName());
        println("電話番号：" + newUserAccount.getPhoneNumber());
        println("連絡先：" + newUserAccount.getContact());
        println("権限：" + newUserAccount.getUserType().name());
        blankLine();
        waitPressEnter();
    }
}
