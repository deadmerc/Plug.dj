package com.example.dead.plugdj;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaPlayer;
import android.media.TimedMetaData;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static android.R.id.input;
import static com.example.dead.plugdj.LoginActivity.auth;

public class RoomActivity extends Activity implements View.OnClickListener {

    WebView web, visual,playlist;
    Button time,btnWoot,btnGrab,btnMeh;
    Context context;
    ProgressDialog progress;
    String roomUrl;




    final Handler handler = new Handler();

    TimerTask myTimerTask = new TimerTask() {
        @Override
        public void run() {
            // post a runnable to the handler
            handler.post(new Runnable() {
                @Override
                public void run() {
                    getTrackName();
                    Log.d("Timer", "Update data");
                }
            });
        }
    };
    Timer timer = new Timer();


    Map<String, String> track = new HashMap<String, String>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        progress = new ProgressDialog(this);
        progress.setMessage("Plugging you in...");
        /*
        OPEN PAGE ROOM
         */
        //web = (WebView) findViewById(R.id.visual);
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
        context = this;
        /*
        START UPDATE INFO
         */
        timer.schedule(myTimerTask, 10000, 5000);

        /*
        GET PLAYLIST
         */
        playlist = new WebView(this);
        playlist.getSettings().setDomStorageEnabled(true);

        /*
        VISUALIZATION
         */
        /*
        visual = (WebView) findViewById(R.id.visual);
        visual.setVisibility(View.GONE);
        //visual.getSettings().setJavaScriptEnabled(true);
        //visual.getSettings().setAllowContentAccess(true);
        visual.setVerticalScrollBarEnabled(false);
        visual.setHorizontalScrollBarEnabled(false);
        visual.setScrollContainer(false);
        visual.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        visual.loadUrl("http://hackerswars.ru/spinner.html");
        //change background, no used now
        //document.getElementsByClassName("gr__plug_dj")[0].style.backgroundColor = "#212121"
        /*
        WORK WITH BTN
         */
        btnGrab = (Button) findViewById(R.id.button_grab);
        btnMeh = (Button) findViewById(R.id.button_meh);
        btnWoot = (Button) findViewById(R.id.button_woot);

        btnWoot.setOnClickListener(this);
        btnMeh.setOnClickListener(this);
        btnGrab.setOnClickListener(this);

    }
    public void onClick(View v){
        if(!auth){
            Toast.makeText(this,"Your need auth before actions", Toast.LENGTH_SHORT).show();
        }else{

            switch (v.getId()){
                case R.id.button_woot:
                    web.evaluateJavascript("$(\"#woot\").click();",null);
                    getTrackName();
                    break;
                case R.id.button_grab:

                    /*
                    get playlist
                    $.each($(".pop-menu .menu span"),function(i,v){
                        console.log(v);
                    });
                     */
                    //web.evaluateJavascript("API.setVolume(10)",null);
                    Toast.makeText(this,"Not work now :(", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.button_meh:
                    web.evaluateJavascript("$(\"#meh\").click();",null);
                    getTrackName();
                    break;
            }
        }


    }



    public void getTrackName() {
        Log.d("ShowMote", "Execute script");
        //document.getElementById('now-playing-media').textContent
        web.evaluateJavascript("$(\"#now-playing-media\").attr(\"title\")", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                //int res = Integer.parseInt(s);
                s = s.replace("\\", "");
                Log.d("ShowMote", "Now play from array before:" + track.get("name"));
                Log.d("ShowMote", "Now play:" + s);
                track.put("name", s);
                Log.d("ShowMote", "Now play from array:" + track.get("name"));
                if (s.length() > 4) {
                    progress.dismiss();
                    getWootCount();
                    getGrabCount();
                    getMehCount();
                    //sometimes volume are 0
                    web.evaluateJavascript("API.setVolume(50)",null);
                    TextView playNow = (TextView) findViewById(R.id.play_now);
                    playNow.setText(track.get("name"));
                }


            }
        });
    }

    public void getWootCount() {
        //visual.setVisibility(View.VISIBLE);
        Log.d("ShowMote", "Execute script woot grab");
        web.evaluateJavascript("$(\"#woot .value\").text()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                s = s.replace("\"", "");
                //int res = Integer.parseInt(s);
                track.put("woot", s);
                TextView wootCount = (TextView) findViewById(R.id.woot_count);
                wootCount.setText(s);
            }
        });
    }

    public void getGrabCount() {
        Log.d("ShowMote", "Execute script grab count");
        web.evaluateJavascript("$(\"#grab .value\").text()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                s = s.replace("\"", "");
                //int res = Integer.parseInt(s);
                track.put("grab", s);
                TextView grabCount = (TextView) findViewById(R.id.grab_count);
                grabCount.setText(s);
            }
        });
    }

    public void getMehCount() {
        Log.d("ShowMote", "Execute script meh count");
        web.evaluateJavascript("$(\"#meh .value\").text()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                s = s.replace("\"", "");
                //int res = Integer.parseInt(s);
                track.put("meh", s);
                TextView mehCount = (TextView) findViewById(R.id.meh_count);
                mehCount.setText(s);
            }
        });
    }



    public class myWebClient extends WebViewClient implements AudioTrack.OnPlaybackPositionUpdateListener {

        public void onPeriodicNotification(AudioTrack track){
            Log.d("Player","He:"+track.getAudioFormat());

        }
        public void onMarkerReached(AudioTrack track){
            Log.d("Player","NE:"+track.getAudioFormat());

        }

        public void onPageStarted(WebView web, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            Log.d("Progress", "Load it");
            progress.show();
            super.onPageStarted(web, url, favicon);

        }

        public void onPageFinished(WebView web, String url) {
            Log.d("Progress", "Dismiss it");
            getTrackName();
            //visual.setVisibility(View.VISIBLE);
            //progress.dismiss();
        }

    }


    public class myWebChromeClient extends WebChromeClient  {




        @Override
        public boolean onConsoleMessage(ConsoleMessage cm) {
            Log.d("ShowMote", cm.message() + " -- From line "
                    + cm.lineNumber() + " of "
                    + cm.sourceId());
            //getTrackName();
            return true;
        }


    }

}
