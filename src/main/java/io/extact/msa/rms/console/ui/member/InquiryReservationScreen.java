package io.extact.msa.rms.console.ui.member;

import static io.extact.msa.rms.console.ui.ClientConstants.*;
import static io.extact.msa.rms.console.ui.textio.TextIoUtils.*;

import java.time.LocalDate;
import java.util.List;

import io.extact.msa.rms.console.model.RentalItemClientModel;
import io.extact.msa.rms.console.model.ReservationClientModel;
import io.extact.msa.rms.console.model.UserAccountClientModel;
import io.extact.msa.rms.console.service.ClientService;
import io.extact.msa.rms.console.ui.TransitionMap.RmsScreen;
import io.extact.msa.rms.console.ui.TransitionMap.Transition;
import io.extact.msa.rms.console.ui.textio.TextIoUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InquiryReservationScreen implements RmsScreen {

    private final ClientService service;

    @Override
    public Transition play(UserAccountClientModel loginUser, boolean printHeader) {

        if (printHeader) {
            printScreenHeader(loginUser, "予約照会画面");
        }

        // レンタル品一覧を表示
        var items = service.getAllRentalItems();
        println(INQUIRY_RESERVATION_INFORMATION);
        items.forEach(dto ->
            println(RENATL_ITEM_FORMAT.format(dto))
        );
        blankLine();

        // 照会するレンタル品を選択
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

        // 照会する日付を入力
        var inputedDate = newLocalDateReader()
                .read("日付（入力例－2020/10/23）");

        // 照会の実行
        var results = service.findReservationByRentalItemAndStartDate(selectedItem, inputedDate);
        // 該当データなしの場合は最初から
        if (results.isEmpty()) {
            printErrorInformation(DATA_NOT_FOUND_INFORMATION);
            return play(loginUser, false); // start over!!
        }
        // 該当データありの場合は結果出力してメインメニューへ
        printResultList(selectedItem, inputedDate, results);
        return Transition.MEMBER_MAIN;
    }

    private void printResultList(int selectedItem, LocalDate inputedDate, List<ReservationClientModel> reservations) {
        blankLine();
        println("***** 予約検索結果 *****");
        println("選択レンタル品番号：" + selectedItem);
        println("入力日付：" + DATE_FORMAT.format(inputedDate));
        reservations.forEach(r -> println(RESERVATION_FORMAT.format(r)));
        blankLine();
        waitPressEnter();
    }
}
