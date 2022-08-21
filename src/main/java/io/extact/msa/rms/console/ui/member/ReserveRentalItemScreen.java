package io.extact.msa.rms.console.ui.member;

import static io.extact.msa.rms.console.ui.ClientConstants.*;
import static io.extact.msa.rms.console.ui.textio.TextIoUtils.*;

import io.extact.msa.rms.console.model.RentalItemClientModel;
import io.extact.msa.rms.console.model.ReservationClientModel;
import io.extact.msa.rms.console.model.UserAccountClientModel;
import io.extact.msa.rms.console.service.ClientApplicationService;
import io.extact.msa.rms.console.ui.ClientConstants;
import io.extact.msa.rms.console.ui.TransitionMap.RmsScreen;
import io.extact.msa.rms.console.ui.TransitionMap.Transition;
import io.extact.msa.rms.console.ui.textio.TextIoUtils;
import io.extact.msa.rms.platform.fw.exception.BusinessFlowException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReserveRentalItemScreen implements RmsScreen {

    private final ClientApplicationService service;

    @Override
    public Transition play(UserAccountClientModel loginUser, boolean printHeader) {

        if (printHeader) {
            printScreenHeader(loginUser, "レンタル品予約画面");
        }

        // レンタル品一覧を表示
        var items = service.getAllRentalItems();
        println(ENTRY_RESERVATION_INFORMATION);
        items.forEach(dto ->
            println(RENATL_ITEM_FORMAT.format(dto))
        );
        blankLine();

        // 予約するレンタル品の選択
        var selectedItem = newIntInputReader()
                .withSelectableValues(
                        items.stream()
                            .map(RentalItemClientModel::getId)
                            .toList(),
                        SCREEN_BREAK_VALUE)
                .read("レンタル品番号");
        if (TextIoUtils.isBreak(selectedItem)) {
            return Transition.MEMBER_MAIN;
        }

        // 利用開始日時の入力
        var startDateTime = newLocalDateTimeReader()
                .withFutureNow()
                .read("利用開始日時（入力例－2020/04/01 09:00）:");

        // 利用終了日時の入力
        var endDateTime = newLocalDateTimeReader()
                .withFutureThan(startDateTime)
                .read("利用終了日時（入力例－2020/04/01 18:00）:");

        // 備考の入力
        var note = newStringInputReader()
                .withMaxLength(15)
                .withDefaultValue("")
                .read("備考（空白可）");

        blankLine();

        // レンタル品予約の実行
        try {
            var addReservation = ReservationClientModel.ofTransient(startDateTime, endDateTime, note, selectedItem, loginUser.getId());
            var newReservation = service.addReservation(addReservation);
            printResultInformation(newReservation);
            return Transition.MEMBER_MAIN;

        } catch (BusinessFlowException e) {
            printServerError(e);
            return play(loginUser, false); // start over!!

        }
    }

    private void printResultInformation(ReservationClientModel newReservation) {
        println("***** 予約確定結果 *****");
        printf(ClientConstants.RESERVATION_FORMAT.format(newReservation));
        blankLine();
        blankLine();
        waitPressEnter();
    }
}
