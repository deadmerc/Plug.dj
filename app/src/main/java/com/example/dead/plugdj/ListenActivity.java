package com.example.dead.plugdj;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ListenActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);

        Button roomChillout = (Button) findViewById(R.id.room_chillout);
        Button roomNightcore = (Button) findViewById(R.id.room_nighcore);
        Button roomEDM = (Button) findViewById(R.id.room_edm);

        roomChillout.setOnClickListener(this);
        roomNightcore.setOnClickListener(this);
        roomEDM.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        Intent intent = new Intent(this,RoomActivity.class);
        switch (v.getId()){
            case R.id.room_chillout:
                //chillout_room
                intent.putExtra("room_url", "https://plug.dj/the-chillout-room");
                break;
            case R.id.room_nighcore:
                //nighcore
                intent.putExtra("room_url", "https://plug.dj/nightcore-331");
                break;
            case R.id.room_edm:
                //edm
                intent.putExtra("room_url", "https://plug.dj/tastycat");
                break;
        }
        startActivity(intent);
    }
}
