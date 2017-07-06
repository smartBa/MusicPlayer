package c.musicplayer.Activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import c.musicplayer.Adapter.LocalListAdapter;
import c.musicplayer.R;
import c.musicplayer.Service.MusicService;

public class MusicLocalList extends AppCompatActivity implements View.OnClickListener ,Runnable{
    private  ArrayList<HashMap<String, Object>> musicList=new ArrayList<HashMap<String, Object>>();
    private boolean run=false;
    private ImageView list;
    private ProgressBar progressBar;
    private ListView listView;
    private ImageView imageView;
    private  Thread thread=new Thread(this);
    private LocalListAdapter localListAdapter;
    private TextView noneMusic;
    private ImageView nextMusic;
    private ImageView backMusic;
    private TextView num;
    private  TextView musicName;
    private  TextView musicAuthor;
    private LinearLayout linearLayout;
    private RelativeLayout relativeLayout;
    private RelativeLayout playAll;
    private ImageView home;
    private ImageView play;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_local_list);
        init();
        run=true;
        if(musicList.size()!=0){
            num.setText(String.valueOf("(共"+musicList.size()+"首)"));
            localListAdapter=new LocalListAdapter(this);
            listView.setAdapter(localListAdapter);
            setAll();
            thread.start();
        }
        if(musicList.size()==0) {
            linearLayout.setVisibility(View.GONE);
            noneMusic.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
        }
        setListener();

    }
    public  void setAll(){
        int num=MusicService.getCount();
        musicAuthor.setText(musicList.get(num).get("music_author").toString());
        musicName.setText(musicList.get(num).get("music_file_name").toString());
        if(MusicService.getPlay()){
            play.setImageResource(R.drawable.stop);
        }
        else{
            play.setImageResource(R.drawable.play);
        }
    }
    public  void setListener(){
        imageView.setOnClickListener(this);
        home.setOnClickListener(this);
        play.setOnClickListener(this);
        playAll.setOnClickListener(this);
        nextMusic.setOnClickListener(this);
        backMusic.setOnClickListener(this);
        list.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                musicName.setText(musicList.get(position).get("music_file_name").toString());
                musicAuthor.setText(musicList.get(position).get("music_author").toString());
                MusicService.setCount(position);
                setAll();
            }
        });
    }
   public void close(){
       run=false;
       finish();
   }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.showLocal:
                Intent intent=new Intent(MusicLocalList.this,MusicShow.class);
                startActivity(intent);
                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT) {
                    overridePendingTransition(R.anim.abc_slide_in_bottom,R.anim.abc_slide_out_bottom);
                }
                close();
                break;
            case R.id.backLocal:
                onBackPressed();
                break;
            case R.id.musicPlayLocal:
                MusicService.setPlay();
                break;
            case R.id.musicNextLocal:
                MusicService.nextMusic();
                break;
            case R.id.musicBackLocal:
                MusicService.backMusic();
                break;
            case R.id.playAllLocal:
                MusicService.nextMusic();
                break;
            case R.id.likeListLocal:
                Intent intent1=new Intent(MusicLocalList.this,MusicLikeList.class);
                startActivity(intent1);
                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT) {
                    overridePendingTransition(R.anim.abc_slide_in_bottom,R.anim.abc_slide_out_bottom);
                }
                close();
                break;
            default:break;
        }
    }
    public  void init(){
        linearLayout=(LinearLayout) findViewById(R.id.musicLocalList);
        relativeLayout=(RelativeLayout)findViewById(R.id.playDetail);
        list=(ImageView)findViewById(R.id.likeListLocal);
        listView=(ListView) findViewById(R.id.listLocal);
        num=(TextView)findViewById(R.id.numMusic);
        musicName=(TextView)findViewById(R.id.musicNameLocal);
        musicAuthor=(TextView)findViewById(R.id.musicAuthorLocal);
        noneMusic=(TextView) findViewById(R.id.noneMusic);
        imageView=(ImageView)findViewById(R.id.showLocal);
        home=(ImageView)findViewById(R.id.backLocal);
        play=(ImageView)findViewById(R.id.musicPlayLocal);
        nextMusic=(ImageView)findViewById(R.id.musicNextLocal);
        backMusic=(ImageView)findViewById(R.id.musicBackLocal);
        progressBar=(ProgressBar)findViewById(R.id.progressLocal);
        playAll=(RelativeLayout)findViewById(R.id.playAllLocal);
        musicList=MusicService.getMusicList();
    }

    @Override
    public void onBackPressed()
    {     //重写返回键，实现返回主界面
        Intent intent=new Intent(MusicLocalList.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT) {
            overridePendingTransition(R.anim.abc_popup_enter,R.anim.abc_popup_exit);
        }
        close();
    }



    @Override
    public void run() {
        while (run) {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public android.os.Handler handler=new android.os.Handler(){
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

}
