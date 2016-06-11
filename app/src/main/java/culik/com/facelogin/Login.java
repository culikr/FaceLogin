package culik.com.facelogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class Login extends AppCompatActivity {

    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private final int RC_SIGN_IN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        callbackManager = CallbackManager.Factory.create();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null) {


            LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.e("CURSO", "toString: " + loginResult.toString());
                    Log.e("CURSO", "accesstoken: " + loginResult.getAccessToken());
                    Log.e("CURSO", "token: " + loginResult.getAccessToken().getToken());
                    Log.e("CURSO", "granted: " + loginResult.getRecentlyGrantedPermissions());
                    Log.e("CURSO", "denied: " + loginResult.getRecentlyDeniedPermissions());
                    ChamaTela();
                }

                @Override
                public void onCancel() {
                }

                @Override
                public void onError(FacebookException error) {
                    Log.d("Erro", error.toString());
                }
            });
        } else {
            ChamaTela();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }

    private void ChamaTela() {
         Intent i = new Intent(this,MapsActivity.class);
        startActivity(i);
        finish();
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d("CURSO", "displayname: " + acct.getDisplayName());
            Log.d("CURSO", "displayname: " + acct.getEmail());
            ChamaTela();
        } else {
            Log.d("CURSO", "sign out");
        }


    }
}
