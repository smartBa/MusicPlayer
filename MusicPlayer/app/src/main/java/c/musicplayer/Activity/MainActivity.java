package c.musicplayer.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import c.musicplayer.R;
import c.musicplayer.Service.MusicService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,Runnable{
    private static boolean newStart=false;
    private NotificationManager manager;
    private LinearLayout local;
    private LinearLayout likeList;
    private LinearLayout backGround;
    private ImageView backMain;
    private ImageView search;
    private Thread mainThread=new Thread(this);
    private  long firstTime=0;
    private AlertDialog ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        Intent intent =new Intent(MainActivity.this,MusicService.class);
        startService(intent);
        setListener();
        if(!newStart){
            mainThread.start();
            newStart=true;
            getInfo();
        }
        //setInfo();
        //getInfo();
    }
    public void setInfo() {
        SharedPreferences share = getSharedPreferences("music", MODE_PRIVATE);
        SharedPreferences.Editor edit = share.edit();
        int num=MusicService.getLikeList().size();
        edit.putInt("num", num);
        for (int i = 0; i < num; i++) {
            edit.putInt("item"+i, (int)MusicService.getLikeList().get(i));
        }

//        else {
//            int num=musicDetail.getLikeList().size();
//            for(int i=0;i<num;i++){
//                int j=share.getInt("num",0)+i;
//                edit.putInt("item"+j,(int)musicDetail.getLikeList().get(i));
//            }
//            edit.putInt("num", num+share.getInt("num",0));
//        }
        edit.commit();
    }
    public void getInfo(){
        SharedPreferences share = getSharedPreferences("music", MODE_PRIVATE);
        if(share!=null) {
            int num = share.getInt("num", 0);
            for (int i = 0; i < num; i++) {
                int searchItem = share.getInt("item" + i, 0);
                MusicService.setLikeList(searchItem);
            }
        }
    }
    public void init(){
        local=(LinearLayout)findViewById(R.id.local);
        likeList=(LinearLayout)findViewById(R.id.mainLike);
        backMain=(ImageView)findViewById(R.id.backMain);
        search=(ImageView)findViewById(R.id.search);
        backGround=(LinearLayout)findViewById(R.id.backgroundMain);
        manager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
    public void setListener(){
        local.setOnClickListener(this);
        likeList.setOnClickListener(this);
        backMain.setOnClickListener(this);
        search.setOnClickListener(this);
        backGround.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.local:
                Intent intent=new Intent(MainActivity.this,MusicLocalList.class);
                startActivity(intent);
                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT) {
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
                break;
            case R.id.mainLike:
                Intent intent2=new Intent(MainActivity.this,MusicLikeList.class);
                startActivity(intent2);
                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT) {
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
                break;
            case R.id.backMain:
                onBackPressed();
                break;
            case R.id.search:
                Intent intent3=new Intent(MainActivity.this,MainSearch.class);
                startActivity(intent3);
                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT) {
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
                break;
            case R.id.backgroundMain:
                Intent intent4=new Intent(MainActivity.this,MainPicture.class);
                startActivity(intent4);
                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT) {
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
                break;
        }
    }
    @Override
    protected void onDestroy() {
       super.onDestroy();
        setInfo();
    }

    public void onBackPressed()
    {     //重写返回键，实现返回主界面
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        } else {
            Intent intent=new Intent(MainActivity.this,MusicService.class);
            stopService(intent);
            System.exit(0);  //两次按键小于2秒时，退出应用
        }

    }

    public android.os.Handler mainHandler=new android.os.Handler(){
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 1:
                    if(MusicService.getStart()){
                        setNo();
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    public void setNo(){
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        RemoteViews remoteViews=new RemoteViews(getPackageName(),R.layout.notification);
        builder.setTicker("通知");
        builder.setContentTitle("通知");
        builder.setContentText("hello");
        builder.setSmallIcon(R.drawable.music_player);
        remoteViews.setTextViewText(R.id.noName,MusicService.getMusicList().get(MusicService.getCount()).get("music_file_name").toString());
        remoteViews.setTextViewText(R.id.noAuthor,MusicService.getMusicList().get(MusicService.getCount()).get("music_author").toString());
        if(MusicService.getPlay()){
            remoteViews.setImageViewResource(R.id.playNo,R.drawable.show_pause);
        }
        else{
            remoteViews.setImageViewResource(R.id.playNo,R.drawable.show_play);
        }
        Intent intentPlay=new Intent("play");
        PendingIntent playIntent=PendingIntent.getBroadcast(this,0,intentPlay,0);
        remoteViews.setOnClickPendingIntent(R.id.playNo,playIntent);
        Intent intentNext=new Intent("next");
        PendingIntent nextIntent =PendingIntent.getBroadcast(this,0,intentNext,0);
        remoteViews.setOnClickPendingIntent(R.id.nextNo,nextIntent);
        Intent intent=new Intent(MainActivity.this,MusicLocalList.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(MainActivity.this,1,intent,PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pendingIntent);
        builder.setContent(remoteViews);
        builder.setOngoing(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        manager.notify(0,builder.build());
    }
    @Override
    public void run() {
        while(true){
            Message message = new Message();
            message.what=1;
            mainHandler.sendMessage(message);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
