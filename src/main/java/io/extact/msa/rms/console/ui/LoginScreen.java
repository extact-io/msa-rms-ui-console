package io.extact.msa.rms.console.ui;

import static io.extact.msa.rms.console.ui.ClientConstants.*;
import static io.extact.msa.rms.console.ui.textio.TextIoUtils.*;

import io.extact.msa.rms.console.model.UserAccountClientModel;
import io.extact.msa.rms.console.service.ClientService;
import io.extact.msa.rms.console.ui.TransitionMap.RmsScreen;
import io.extact.msa.rms.console.ui.TransitionMap.Transition;
import io.extact.msa.rms.platform.core.env.Environment;
import io.extact.msa.rms.platform.fw.exception.BusinessFlowException;
import lombok.RequiredArgsConstructor;

/**
 * アプリケーション開始画面。
 * 開始処理としてのログインを担う
 */
@RequiredArgsConstructor
public class LoginScreen implements RmsScreen {

    private final ClientService service;
    private final LoginEventObserver loginObserver;

    @Override
    public Transition play(UserAccountClientModel dummy, boolean printHeader) {
        try {
            if (printHeader) {
                // 認証画面のヘッダーを表示する
                var jarInfo = Environment.getMainJarInfo();
                println("[version:" + jarInfo.getVersion() + "/build-time:" + jarInfo.getBuildtimeInfo() + "]");
                println(LOGIN_INFORMATION);
                blankLine();
            }

            var loginId = newStringInputReader()
                    .withMinLength(5)
                    .withMaxLength(10)
                    .withDefaultValue("edamame")
                    .read("ログインID");
            if (loginId.equals(SCREEN_BREAK_KEY)) {
                return Transition.END;
            }

            var password = newStringInputReader()
                    .withMinLength(5)
                    .withMaxLength(10)
                    .withDefaultValue("edamame")
                    .withInputMasking(true)
                    .read("パスワード");

            // ログイン実行
            var nowLoginUser = service.authenticate(loginId, password);
            // 成功したのでログインユーザを通知
            loginObserver.onEvent(nowLoginUser);

            return nowLoginUser.getUserType().isAdmin() ? Transition.ADMIN_MAIN : Transition.MEMBER_MAIN;

        } catch (BusinessFlowException e) {
            printServerError(e);
            return play(dummy, false);

        }
    }
}
