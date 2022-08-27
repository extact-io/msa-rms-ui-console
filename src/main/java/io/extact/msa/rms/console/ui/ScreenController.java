package io.extact.msa.rms.console.ui;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import io.extact.msa.rms.console.model.UserAccountClientModel;
import io.extact.msa.rms.console.service.ClientApplicationService;
import io.extact.msa.rms.console.ui.TransitionMap.RmsScreen;
import io.extact.msa.rms.console.ui.TransitionMap.Transition;
import io.extact.msa.rms.console.ui.admin.AdminMainScreen;
import io.extact.msa.rms.console.ui.admin.EditUserScreen;
import io.extact.msa.rms.console.ui.admin.EntryRentalItemScreen;
import io.extact.msa.rms.console.ui.admin.EntryUserScreen;
import io.extact.msa.rms.console.ui.member.CancelReservationScreen;
import io.extact.msa.rms.console.ui.member.InquiryReservationScreen;
import io.extact.msa.rms.console.ui.member.MemberMainScreen;
import io.extact.msa.rms.console.ui.member.ReserveRentalItemScreen;

/**
 * アプリケーションの画面遷移を制御するクラス
 */
@ApplicationScoped
public class ScreenController implements LoginEventObserver {

    private TransitionMap transitionMap;
    private UserAccountClientModel currentLoginUser;

    @Inject
    public ScreenController(ClientApplicationService service) {
        this.transitionMap = new TransitionMap();
        transitionMap.add(Transition.LOGIN, new LoginScreen(service, this));
        transitionMap.add(Transition.MEMBER_MAIN, new MemberMainScreen());
        transitionMap.add(Transition.INQUIRY_RESERVATION, new InquiryReservationScreen(service));
        transitionMap.add(Transition.ENTRY_RESERVATRION, new ReserveRentalItemScreen(service));
        transitionMap.add(Transition.CANCEL_RESERVATRION, new CancelReservationScreen(service));
        transitionMap.add(Transition.ADMIN_MAIN, new AdminMainScreen());
        transitionMap.add(Transition.ENTRY_RENTAL_ITEM, new EntryRentalItemScreen(service));
        transitionMap.add(Transition.ENTRY_USER, new EntryUserScreen(service));
        transitionMap.add(Transition.EDIT_USER, new EditUserScreen(service));
        transitionMap.add(Transition.END, new EndScreen());
    }

    public void start() {
        var loginScreen = transitionMap.stratScreen();
        doPlay(loginScreen);
    }

    private RmsScreen doPlay(RmsScreen screen) {
        var next = screen.play(currentLoginUser, true);
        return next != null ? doPlay(transitionMap.nextScreen(next)) : null;
    }


    // -------------------------------------------------- obsever methods.

    @Override
    public void onEvent(UserAccountClientModel loginUser) {
        this.currentLoginUser = loginUser;
    }

    @Override
    public UserAccountClientModel getLoginUser() {
        return this.currentLoginUser;
    }
}
