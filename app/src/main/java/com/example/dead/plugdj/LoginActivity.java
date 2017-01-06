package com.example.dead.plugdj;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    static Boolean auth = false;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    WebView web;

    ProgressDialog progress;

    EditText email, password;

    final Handler handler = new Handler();

    TimerTask myTimerTask = new TimerTask() {
        @Override
        public void run() {
            // post a runnable to the handler
            handler.post(new Runnable() {
                @Override
                public void run() {
                    checkAuth();
                    Log.d("Timer", "Update data");
                }
            });
        }
    };
    Timer timer = new Timer();

    Context context = this;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progress = new ProgressDialog(this);
        progress.setMessage("Auth in progress, please be patient.");

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        final Button authBtn = (Button) findViewById(R.id.button_auth);

        OnClickListener oclBtnOk = new OnClickListener() {
            @Override
            public void onClick(View v) {
                auth();
            }
        };
        authBtn.setOnClickListener(oclBtnOk);

        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();


        //web = (WebView) findViewById(R.id.web);
        web = new WebView(this);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView web,String url) {
                //Log.d("Load",url);
            }

            ;
        });
        web.setWebChromeClient(new WebChromeClient());
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setAllowContentAccess(true);
        web.getSettings().setDatabaseEnabled(true);
        web.getSettings().setDomStorageEnabled(true);
        //web.getSettings().setMediaPlaybackRequiresUserGesture(false);
        web.getSettings().setUserAgentString("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.90 Safari/537.36");

        web.loadUrl("https://plug.dj/");

        timer.schedule(myTimerTask, 5000, 5000);

    }
    //$("#news .label").text()
    public void checkAuth(){
        Log.d("Auth","Start method");
        web.evaluateJavascript("$(\"#news .label\").text()",new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                s = s.replace("\"", "");
                Log.d("Auth","Result:"+s);
                if(s.equals("News")){
                    timer.cancel();
                    progress.dismiss();

                    web.evaluateJavascript("$(\"#footer-user .name span\").text();", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {
                            s = s.replace("\"", "");
                            Toast.makeText(context, "Hello, "+s, Toast.LENGTH_LONG).show();
                            auth = true;
                            Intent intent = new Intent(context, ListenActivity.class);
                            startActivity(intent);
                        }
                    });

                }else{

                    /*
                    WRONG AUTH
                     */
                }
            }
        });


    }

    public void auth() {
        //web= new WebView(this);
        //setContentView(web);
        progress.show();
        web.evaluateJavascript("(function(){$(\"#login\").click();})()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                web.evaluateJavascript("(function(){$(\"#email\").val(\"" + email.getText().toString() + "\")})()", null);
                web.evaluateJavascript("(function(){$(\"#password\").val(\"" + password.getText().toString() + "\")})()", null);
                web.evaluateJavascript("(function(){$(\".email-login button\").click()})()", null);

            }
        });
    }

    public void openListenActivity(View view) {
        Intent intent = new Intent(this, ListenActivity.class);
        startActivity(intent);
    }


}

