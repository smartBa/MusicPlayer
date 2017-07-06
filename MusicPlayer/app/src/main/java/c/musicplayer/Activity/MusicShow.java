package c.musicplayer.Activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import c.musicplayer.R;
import c.musicplayer.Service.MusicService;


public class MusicShow extends AppCompatActivity implements View.OnClickListener,Runnable {
    private boolean run=false;
    private  ArrayList<HashMap<String, Object>> musicList=new ArrayList<HashMap<String, Object>>();
    private ImageView like;
    private ImageView likeList;
    private TextView musicName;
    private TextView musicAuthor;
    private ImageView back;
    private Thread showThread=new Thread(this);
    private ImageView backGround;
    private ImageView nextMusic;
    private ImageView backMusic;
    private ImageView playMusic;
    private ImageView playType;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_show2);
        init();
        setOnClick();
        setAll();
        run=true;
        showThread.start();
//        Log.e("test", musicDetail.getStart() + "");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public  void setAll(){
        int num=MusicService.getCount();
        musicAuthor.setText(musicList.get(num).get("music_author").toString());
        musicName.setText(musicList.get(num).get("music_file_name").toString());
        if(MusicService.getPlay()){
            playMusic.setImageResource(R.drawable.show_pause);
        }
        else{
            playMusic.setImageResource(R.drawable.show_play);
        }
        if(MusicService.getBackGround().equals("one")){
            backGround.setImageResource(R.drawable.background);
        }
        else if(MusicService.getBackGround().equals("two")){
            backGround.setImageResource(R.drawable.background2);
        }
        else if(MusicService.getBackGround().equals("three")){
            backGround.setImageResource(R.drawable.background3);
        }
        else {
            backGround.setImageResource(R.drawable.background4);
        }
       if(MusicService.getPlayType()==0){
            playType.setImageResource(R.drawable.show_loop);
        }
        else if(MusicService.getPlayType()==1){
           playType.setImageResource(R.drawable.show_rand);
        }
        else{
           playType.setImageResource(R.drawable.show_single);
        }
        for(int i=0;i<MusicService.getLikeList().size();i++){
            if((int)MusicService.getLikeList().get(i)==MusicService.getCount()){
                like.setImageResource(R.drawable.like);
                break;
            }
            if(i==MusicService.getLikeList().size()-1){
                like.setImageResource(R.drawable.disilike);
            }
        }
    }
    public void init(){
        likeList=(ImageView)findViewById(R.id.showList);
        playType=(ImageView)findViewById(R.id.showType);
        musicAuthor=(TextView)findViewById(R.id.showAuthor);
        musicName=(TextView)findViewById(R.id.showName);
        backMusic=(ImageView)findViewById(R.id.showBackforward);
        nextMusic=(ImageView)findViewById(R.id.showNext);
        playMusic=(ImageView)findViewById(R.id.showPlay);
        backGround=(ImageView)findViewById(R.id.backGround);
        progressBar=(ProgressBar)findViewById(R.id.showProgress);
        back=(ImageView)findViewById(R.id.showBack);
        like=(ImageView)findViewById(R.id.showLike);
        musicList=MusicService.getMusicList();
    }
    public void setOnClick(){
        like.setOnClickListener(this);
        backMusic.setOnClickListener(this);
        playMusic.setOnClickListener(this);
        nextMusic.setOnClickListener(this);
        back.setOnClickListener(this);
        playType.setOnClickListener(this);
        likeList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.showPlay:
                playMusic();
                break;
            case R.id.showNext:
                nextMusic();
                break;
            case R.id.showBackforward:
                backMusic();
                break;
            case R.id.showBack:
                onBackPressed();
                break;
            case R.id.showType:
                MusicService.setPlayType();
                setAll();
                break;
            case R.id.showLike:
                MusicService.setLikeList();
                setAll();
                break;
            case R.id.showList:
                Intent intent1=new Intent(MusicShow.this,MusicLikeList.class);
                startActivity(intent1);
                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT) {
                    overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
                }
                run=false;
                finish();
                break;
        }

    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(MusicShow.this, MusicLocalList.class);
        startActivity(intent);
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT) {
            overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
        }
        run=false;
        finish();
    }
    public void playMusic() {
        MusicService.setPlay();
        setAll();
    }
    public void nextMusic() {
        MusicService.nextMusic();
        setAll();
    }

    public void backMusic(){
        MusicService.backMusic();
        setAll();
    }
    public android.os.Handler showHandler=new android.os.Handler(){
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 1:
                    if(MusicService.getPlay()) {
                        progressBar.setMax(MusicService.getMax());
                        progressBar.setProgress(MusicService.getPosition());
                    }
                    setAll();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void run() {
            while (run) {
                Message message = new Message();
                message.what=1;
                showHandler.sendMessage(message);
//                Log.e("test", "执行成功");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


    }
}
