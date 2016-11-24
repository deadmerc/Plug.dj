package com.example.dead.plugdj;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.R.id.input;

public class RoomActivity extends AppCompatActivity {

    WebView web;
    Listen listen;
    Button time;
    Context context;
    static ProgressDialog progress;
    String roomUrl;


    Map<String, String> track = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        progress = new ProgressDialog(this);
        progress.setMessage("Plugging you in...");
        //web = (WebView) findViewById(R.id.web);
        web = new WebView(this);
        web.setWebViewClient(new myWebClient());
        web.setWebChromeClient(new myWebChromeClient());

        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setAllowContentAccess(true);
        web.getSettings().setDatabaseEnabled(true);
        web.getSettings().setDomStorageEnabled(true);
        web.getSettings().setDisplayZoomControls(true);
        web.getSettings().setMediaPlaybackRequiresUserGesture(false);


        web.getSettings().setUserAgentString("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.90 Safari/537.36");

        roomUrl = getIntent().getExtras().getString("room_url");
        web.loadUrl(roomUrl);
        Log.d("ShowMote", "Loading page");
        /*
        time = (Button) findViewById(R.id.time);

        View.OnClickListener timeRemain = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTrackName();
                //web.loadUrl("javascript:console.log(API.getVolume());");
                //web.loadUrl("javascript:API.getVolume();");
            }
        };
        time.setOnClickListener(timeRemain);
        */
        context = this;
        //setContentView(web);
    }

    public void getTrackName() {
        Log.d("ShowMote", "Execute script");
        //document.getElementById('now-playing-media').textContent
        web.evaluateJavascript("$(\"#now-playing-media\").attr(\"title\")", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                //int res = Integer.parseInt(s);
                s = s.replace("\\","");
                Log.d("ShowMote", "Now play from array before:" + track.get("name"));
                Log.d("ShowMote", "Now play:" + s);
                track.put("name", s);
                Log.d("ShowMote", "Now play from array:" + track.get("name"));
                if(s.length() > 4){
                    progress.dismiss();
                    getWootCount();
                    getGrabCount();
                    getMehCount();
                    TextView playNow = (TextView) findViewById(R.id.play_now);
                    playNow.setText(track.get("name"));
                }


            }
        });
    }
    public void getWootCount(){
        Log.d("ShowMote", "Execute script woot grab");
        web.evaluateJavascript("$(\"#woot .value\").text()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                s = s.replace("\"","");
                //int res = Integer.parseInt(s);
                track.put("woot", s);
                TextView wootCount = (TextView) findViewById(R.id.woot_count);
                wootCount.setText(s);
            }
        });
    }
    public void getGrabCount(){
        Log.d("ShowMote", "Execute script grab count");
        web.evaluateJavascript("$(\"#grab .value\").text()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                s = s.replace("\"","");
                //int res = Integer.parseInt(s);
                track.put("grab", s);
                TextView grabCount = (TextView) findViewById(R.id.grab_count);
                grabCount.setText(s);
            }
        });
    }
    public void getMehCount(){
        Log.d("ShowMote", "Execute script meh count");
        web.evaluateJavascript("$(\"#meh .value\").text()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                s = s.replace("\"","");
                //int res = Integer.parseInt(s);
                track.put("meh", s);
                TextView mehCount = (TextView) findViewById(R.id.meh_count);
                mehCount.setText(s);
            }
        });
    }


    public class myWebClient extends WebViewClient {
        public void onPageStarted(WebView web, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            Log.d("Progress", "Load it");
            progress.show();
            super.onPageStarted(web, url, favicon);

        }

        public void onPageFinished(WebView web, String url) {
            Log.d("Progress", "Dismiss it");
            getTrackName();
            //progress.dismiss();
        }

    }



    static public void hideProgress() {
        progress.dismiss();
    }


    public class myWebChromeClient extends WebChromeClient implements MediaCodec.OnFrameRenderedListener {

        @Override
        public void onFrameRendered(MediaCodec codec, long presentationTimeUs, long nanoTime) {
            Log.d("Player", "PresentTime:" + presentationTimeUs);
            Log.d("Player", "NanoTime:" + nanoTime);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage cm) {
            Log.d("ShowMote", cm.message() + " -- From line "
                    + cm.lineNumber() + " of "
                    + cm.sourceId());
            getTrackName();
            return true;
        }


    }

    class Listen extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... url) {

            return null;
        }
    }

}
