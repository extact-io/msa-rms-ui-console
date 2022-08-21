package io.extact.msa.rms.console.ui.member;

import static io.extact.msa.rms.console.ui.ClientConstants.*;
import static io.extact.msa.rms.console.ui.textio.TextIoUtils.*;

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
public class CancelReservationScreen implements RmsScreen {

    private final ClientApplicationService service;

    @Override
    public Transition play(UserAccountClientModel loginUser, boolean printHeader) {

        if (printHeader) {
            printScreenHeader(loginUser, "レンタル品予約キャンセル画面");
        }

        var ownReservations = service.getOwnReservations();

        // キャンセル可能な予約がない場合はメニューへ戻る
        if (ownReservations.isEmpty()) {
            printWarningInformation(CANNOT_CANCEL_RESERVATION_INFORMATION);
            waitPressEnter();
            return Transition.MEMBER_MAIN;
        }

        // キャンセル可能な予約一覧を表示
        println(CANCEL_RESERVATION_INFORMATION);
        ownReservations.forEach(dto ->
            println(ClientConstants.RESERVATION_FORMAT.format(dto))
        );
        blankLine();

        // キャンセルする予約を選択
        var selectedReservation = newIntInputReader()
                .withSelectableValues(
                        ownReservations.stream()
                            .map(ReservationClientModel::getId)
                            .toList(),
                        SCREEN_BREAK_VALUE)
                .read("予約番号");
        if (TextIoUtils.isBreak(selectedReservation)) {
            return Transition.MEMBER_MAIN;
        }

        blankLine();

        // レンタル品の予約キャンセルの実行
        try {
            service.cancelReservation(selectedReservation);
            printResultInformation(selectedReservation);
            return Transition.MEMBER_MAIN;

        } catch (BusinessFlowException e) {
            printServerError(e);
            return play(loginUser, false); // start over!!

        }
    }

    private void printResultInformation(Integer selectedItem) {
        println("***** 予約キャンセル確定結果 *****");
        printf("[%s]の予約をキャンセルしました", selectedItem);
        blankLine();
        blankLine();
        waitPressEnter();
    }
}
