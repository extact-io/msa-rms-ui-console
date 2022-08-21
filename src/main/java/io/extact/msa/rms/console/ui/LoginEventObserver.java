package io.extact.msa.rms.console.ui;

import io.extact.msa.rms.console.model.UserAccountClientModel;

public interface LoginEventObserver {
    void onEvent(UserAccountClientModel loginUser);
    UserAccountClientModel getLoginUser();
}
