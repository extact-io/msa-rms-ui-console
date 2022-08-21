package io.extact.msa.rms.console.it;

import static io.extact.msa.rms.console.external.ApiType.*;
import static io.extact.msa.rms.test.assertj.ToStringAssert.*;
import static java.time.temporal.ChronoUnit.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import io.extact.msa.rms.console.model.RentalItemClientModel;
import io.extact.msa.rms.console.model.ReservationClientModel;
import io.extact.msa.rms.console.model.UserAccountClientModel;
import io.extact.msa.rms.console.service.ClientApplicationService;
import io.extact.msa.rms.platform.fw.domain.vo.UserType;
import io.extact.msa.rms.platform.fw.exception.BusinessFlowException;
import io.extact.msa.rms.platform.fw.exception.BusinessFlowException.CauseType;
import io.extact.msa.rms.test.junit5.JulToSLF4DelegateExtension;
import io.helidon.microprofile.tests.junit5.AddConfig;
import io.helidon.microprofile.tests.junit5.HelidonTest;

/**
 * ClientServiceクラスをテストするテストケース。
 * テスト実行時の構成はテストクラスのサブクラスごとに異なる。
 */
@HelidonTest
@AddConfig(key = PROP_NAME, value = REAL)
@ExtendWith(JulToSLF4DelegateExtension.class)
@TestMethodOrder(OrderAnnotation.class)
abstract class AbstractClientScenarioTest {

    @Inject
    private ClientApplicationService service;
    private ScenarioContext context = new ScenarioContext();

    static class ScenarioContext {
        private UserAccountClientModel loginUser;
        private RentalItemClientModel addedRentalItem;
    }

    static {
        // Fiddlerを有効にするときは@AddConfig(web-api/mp-rest/url)もコメントアウトする
        //System.setProperty("http.proxyHost", "localhost");
        //System.setProperty("http.proxyPort", "8888");
        //System.setProperty("web-api/mp-rest/url", "http://pmr216n.primo.mamezou.com:7001");
    }

    @Test
    @Order(1)
    void testAddUserAccount() {
        // -----------------------------------
        // ユーザの登録
        // -----------------------------------
        // ★:ADMINから開始
        service.authenticate("admin", "admin");

        UserAccountClientModel newUser = UserAccountClientModel.
                ofTransient("testUser", "userTest", "テスト太郎", "1234", "なぞなぞ部門", UserType.MEMBER);
        UserAccountClientModel addedUser = service.addUserAccount(newUser);

        // 登録を依頼した内容と登録された内容が同じか？
        // 登録したユーザに発番された番号が既存のレコード数(3) +1であること
        // その他のユーザ情報が登録したユーザの情報と同じか
        newUser.setId(3 + 1);
        assertThatToString(addedUser).isEqualTo(newUser);

        // -----------------------------------
        // 登録したユーザでログインできるか
        // -----------------------------------
        // ★:MEMBERに切り替え
        UserAccountClientModel loginUser = service.authenticate(addedUser.getLoginId(), addedUser.getPassword());

        // 取得したログインユーザの情報が登録したユーザの情報と同じであること
        assertThat(loginUser).isNotNull();
        assertThatToString(loginUser).isEqualTo(addedUser);

        // -----------------------------------
        // 登録したユーザを再登録（重複エラー）
        // -----------------------------------
        // ★:ADMINに切り替え
        service.authenticate("admin", "admin");
        // 名前が重複
        newUser.setId(null);
        var thrown = catchThrowableOfType(
                () -> service.addUserAccount(newUser),
                BusinessFlowException.class);
        assertThat(thrown).isNotNull();

        // @Order(3)の事前条件として設定
        context.loginUser = loginUser;
    }

    @Test
    @Order(2)
    void testAddRentalItem() {
        // -----------------------------------
        // レンタル品の登録
        // -----------------------------------
        RentalItemClientModel newItem = RentalItemClientModel.ofTransient("1234", "レンタル品");
        RentalItemClientModel addedItem = service.addRentalItem(newItem);

        // 登録を依頼した内容と登録された内容が同じか？
        // 登録したレンタル品に発番された番号が既存のレコード数(4) +1であること
        // その他のレンタル品情報が登録した情報と同じか
        newItem.setId(4 + 1);
        assertThatToString(addedItem).isEqualTo(newItem);

        // -----------------------------------
        // 登録したレンタル品を再登録（重複エラー）
        // -----------------------------------
        // シリアルNoが重複
        newItem.setId(null);
        var thrown = catchThrowableOfType(
                () -> service.addRentalItem(newItem),
                BusinessFlowException.class);
        assertThat(thrown).isNotNull();

        // @Order(3)の事前条件として設定
        context.addedRentalItem = addedItem;
    }

    @Test
    @Order(3)
    void testAddReservation() {

        // -----------------------------------
        // @Order(1)で登録したログインユーザと@Order(2)で登録したレンタル品を予約できるか
        // -----------------------------------

        // 事前条件の取得
        UserAccountClientModel loginUser = this.context.loginUser;
        RentalItemClientModel addedItem = this.context.addedRentalItem;

        // ★:MEMBERに切り替え
        service.authenticate(loginUser.getLoginId(), loginUser.getPassword());

        // 登録したレンタル品がレンタル品一覧に含まれていること
        List<RentalItemClientModel> items = service.getAllRentalItems();
        RentalItemClientModel selectedItem = items.stream()
                .filter(item -> item.getId() == addedItem.getId())
                .findFirst()
                .orElse(null);
        assertThat(selectedItem).isNotNull();

        // 登録したユーザとレンタル品で予約
        var startDateTime = LocalDateTime.now().plusDays(1).truncatedTo(MINUTES);
        var endDateTime = startDateTime.plusDays(1);
        var note = "備考";
        var newReservation = ReservationClientModel.ofTransient(startDateTime, endDateTime, note, addedItem.getId(), loginUser.getId());
        var addedReservation = service.addReservation(newReservation);

        newReservation.setId(3 + 1);
        newReservation.setRentalItemModel(addedItem);
        newReservation.setUserAccountModel(loginUser);
        assertThatToString(addedReservation).isEqualTo(newReservation);

        // -----------------------------------
        // 予約照会で登録した予約を参照できるか
        // -----------------------------------
        var reservations = service.findReservationByRentalItemAndStartDate(
                addedReservation.getRentalItemId(),
                addedReservation.getStartDateTime().toLocalDate());

        // 該当の予約が1件であること
        assertThat(reservations).hasSize(1);
        // 該当の予約が登録した予約と同じであることaddedReservation.toString()
        assertThatToString(reservations.get(0)).isEqualTo(addedReservation);
    }

    @Test
    @Order(4)
    void testAddReservationOnNotExists() {

        // -----------------------------------
        // 誤ったレンタル品IDで登録
        // -----------------------------------

        // 事前条件の取得
        var loginUser = this.context.loginUser;
        var addedItem = this.context.addedRentalItem;

        var startDateTime = LocalDateTime.now().plusDays(10).truncatedTo(MINUTES);
        var endDateTime = startDateTime.plusDays(1);
        var note = "備考";
        var newReservation = ReservationClientModel.ofTransient(startDateTime, endDateTime, note, 999, loginUser.getId());

        var thrown = catchThrowable(() -> service.addReservation(newReservation));
        assertThat(thrown).isInstanceOf(BusinessFlowException.class);
        assertThat(((BusinessFlowException) thrown).getCauseType()).isEqualTo(CauseType.NOT_FOUND);


        // -----------------------------------
        // 誤ったレンタル品IDで登録
        // -----------------------------------

        newReservation.setRentalItemId(addedItem.getId());
        newReservation.setUserAccountId(999);
        thrown = catchThrowable(() -> service.addReservation(newReservation));
        assertThat(thrown).isInstanceOf(BusinessFlowException.class);
        assertThat(((BusinessFlowException) thrown).getCauseType()).isEqualTo(CauseType.NOT_FOUND);
    }

    @Test
    @Order(5)
    void testCancelReservation() {
        // -----------------------------------
        // @Order(3)で登録した予約をキャンセルできるか
        // -----------------------------------
        // 事前条件の取得と設定
        var loginUser = context.loginUser;

        // ログインユーザの予約一覧を取得
        //var ownReservations = service.findReservationByReserverId(loginUser.getId());
        var ownReservations = service.getOwnReservations();
        // 該当の予約が1件であること
        assertThat(ownReservations).hasSize(1);

        // ログインユーザ以外の予約を削除
        var thrown = catchThrowableOfType(
            () -> service.cancelReservation(1),
            BusinessFlowException.class
        );
        assertThat(thrown).isNotNull();

        // キャンセル対象の予約を取得
        var cancelTarget = ownReservations.get(0);
        // キャンセルの実行
        service.cancelReservation(cancelTarget.getId());

        // 再度ログインユーザの予約一覧を取得
        ownReservations = service.findReservationByReserverId(loginUser.getId());
        // 自分の予約一覧に出てなこないこと
        assertThat(ownReservations).isEmpty();
    }

    @Test
    @Order(6)
    void testUpdateUserAccount() {
        // -----------------------------------
        // ユーザ情報を更新できるか
        // -----------------------------------
        // ★:ADMINに切り替え
        service.authenticate("admin", "admin");

        // 一覧を取得
        var users = service.getAllUserAccounts();
        assertThat(users).hasSize(4);

        // 一覧から更新対象を選択
        var updateTarget = users.get(0);
        // ユーザ名を変更
        updateTarget.setPassword("UPDATE");
        // 更新の実行
        var actual = service.updateUserAccount(updateTarget);

        assertThatToString(actual).isEqualTo(updateTarget);
    }
}
