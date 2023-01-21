package io.extact.msa.rms.console.external;

import io.extact.msa.rms.console.external.dto.UserAccountClientDto;
import io.extact.msa.rms.platform.fw.exception.BusinessFlowException;

/**
 * レンタル予約アプリケーションのログインインターフェース
 */
public interface LoginServer {
    /**
     * ユーザをパスワードで認証する。
     * <p>
     * @param loginId 認証するユーザのログインID
     * @param password 認証パスワード
     * @return 認証ユーザ。認証できなかった場合はnull
     * @throws BusinessFlowException ユーザIDまたはパスワードに一致するユーザがいない
     */
    UserAccountClientDto authenticate(String loginId, String password) throws BusinessFlowException;
}