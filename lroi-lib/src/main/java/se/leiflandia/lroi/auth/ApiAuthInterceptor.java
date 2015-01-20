package se.leiflandia.lroi.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;

import retrofit.RequestInterceptor;
import se.leiflandia.lroi.utils.AuthUtils;

public class ApiAuthInterceptor implements RequestInterceptor {

    private final Application app;
    private final AccountManager accountManager;
    private final String authtokenType;
    private String accountType;

    public ApiAuthInterceptor(Application application, AccountManager accountManager, String authtokenType, String accountType) {
        this.app = application;
        this.accountManager = accountManager;
        this.authtokenType = authtokenType;
        this.accountType = accountType;
    }

    @Override
    public void intercept(RequestFacade request) {
        Account account = AuthUtils.getActiveAccount(app, accountType);
        String token = null;
        if (account != null) {
            token = accountManager.peekAuthToken(account, authtokenType);
        }
        // Do this even if token is null because otherwise we get 403 and the
        // RequestInterceptor won't fire.
        request.addHeader("Authorization", "Bearer " + token);
    }
}
