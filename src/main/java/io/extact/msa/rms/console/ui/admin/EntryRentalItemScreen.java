package io.extact.msa.rms.console.ui.admin;

import static io.extact.msa.rms.console.ui.ClientConstants.*;
import static io.extact.msa.rms.console.ui.textio.TextIoUtils.*;

import io.extact.msa.rms.console.model.RentalItemClientModel;
import io.extact.msa.rms.console.model.UserAccountClientModel;
import io.extact.msa.rms.console.service.ClientService;
import io.extact.msa.rms.console.ui.ClientConstants;
import io.extact.msa.rms.console.ui.TransitionMap.RmsScreen;
import io.extact.msa.rms.console.ui.TransitionMap.Transition;
import io.extact.msa.rms.console.ui.textio.RmsStringInputReader.PatternMessage;
import io.extact.msa.rms.console.ui.textio.TextIoUtils;
import io.extact.msa.rms.platform.fw.exception.BusinessFlowException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EntryRentalItemScreen implements RmsScreen {

    private final ClientService service;

    @Override
    public Transition play(UserAccountClientModel loginUser, boolean printHeader) {

        if (printHeader) {
            printScreenHeader(loginUser, "レンタル品登録画面");
        }

        // 入力インフォメーションの表示
        println(ENTRY_RENTAL_ITEM_INFORMATION);

        // シリアル番号の入力
        var serialNo = newStringInputReader()
                .withMinLength(1)
                .withMaxLength(15)
                .withPattern(PatternMessage.SERIAL_NO)
                .read("シリアル番号");
        if (TextIoUtils.isBreak(serialNo)) {
            return Transition.ADMIN_MAIN;
        }

        // 品名の入力
        var itemName = newStringInputReader()
                .withMaxLength(15)
                .withDefaultValue("")
                .read("品名（空白可）");

        blankLine();

        // レンタル品登録の実行
        try {
            var addItem = RentalItemClientModel.ofTransient(serialNo, itemName);
            var newItem = service.addRentalItem(addItem);
            printResultInformation(newItem);
            return Transition.ADMIN_MAIN;

        } catch (BusinessFlowException e) {
            printServerError(e);
            return play(loginUser, false); // start over!!

        }
    }

    private void printResultInformation(RentalItemClientModel newItem) {
        println("***** レンタル品登録結果 *****");
        printf(ClientConstants.RENATL_ITEM_FORMAT.format(newItem));
        blankLine();
        waitPressEnter();
    }
}
