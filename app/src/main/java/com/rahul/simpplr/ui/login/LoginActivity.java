package com.rahul.simpplr.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.navigation.NavigationView;
import com.rahul.simpplr.R;
import com.rahul.simpplr.base.BaseActivity;
import com.rahul.simpplr.databinding.ActivityLoginBinding;
import com.rahul.simpplr.databinding.ActivityMainBinding;
import com.rahul.simpplr.db.AppPreferencesHelper;
import com.rahul.simpplr.db.PreferencesHelper;
import com.rahul.simpplr.ui.main.MainActivity;
import com.rahul.simpplr.ui.main.MainViewModel;
import com.rahul.simpplr.utility.ViewModelFactory;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import javax.inject.Inject;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel>  {

    private static final int AUTH_TOKEN_REQUEST_CODE = 123;
    @Inject
    ViewModelFactory viewModelFactory;

    @Inject
    PreferencesHelper preferencesHelper;

    @Inject
    Context context;

    private LoginViewModel viewModel;
    private String mAccessToken="";

    @Override
    protected int setLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginViewModel setViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel.class);
        return viewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manageCondition();
        subscribeObserver();
    }

    private void subscribeObserver() {
        viewModel.getToastObservable().observe(this, this::showToast);
        viewModel.getOutputObservable().observe(this, integer -> {

            if(integer == 1){
                preferencesHelper.setBooleanData(AppPreferencesHelper.PREF_KEY_IS_LOGGED_IN, true);

                navigateToMainActivity();
            }
        });
    }

    private void manageCondition() {
       boolean isAlreadyLoggedIn =  preferencesHelper.getBooleanData(AppPreferencesHelper.PREF_KEY_IS_LOGGED_IN);
       String accessToken =  preferencesHelper.getStringData(AppPreferencesHelper.PREF_KEY_ACCESS_TOKEN);

       if(isAlreadyLoggedIn && accessToken !=null && accessToken.length()>0){
           navigateToMainActivity();
       }
    }

    public void onLoginButtonClick(View view) {
        final AuthenticationRequest request = viewModel.getAuthenticationRequest();
        AuthenticationClient.openLoginActivity(this, AUTH_TOKEN_REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);

        if (requestCode == AUTH_TOKEN_REQUEST_CODE) {
            mAccessToken = response.getAccessToken();

            if(mAccessToken!=null && mAccessToken.length()>0) {
                System.out.println("::: Token: " + mAccessToken);
                preferencesHelper.setStringData(AppPreferencesHelper.PREF_KEY_ACCESS_TOKEN, mAccessToken);
                viewModel.fetchProfileInfo();

            }else{
                showToast(getString(R.string.invalid_token));
            }
        }
    }

    private void navigateToMainActivity(){
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
