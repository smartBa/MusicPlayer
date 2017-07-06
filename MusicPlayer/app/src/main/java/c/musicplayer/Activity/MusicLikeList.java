package c.musicplayer.Activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import c.musicplayer.Adapter.LikeListAdapter;
import c.musicplayer.R;
import c.musicplayer.Service.MusicService;


public class MusicLikeList extends AppCompatActivity {
    private LinearLayout linearLayout;
    private ListView listView;
    private TextView textView;
    private ImageView backLike;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_like_list);
        findView();
        setListener();
        if(MusicService.getLikeList().size()==0){
            linearLayout.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
        else {
            LikeListAdapter like_Adapter = new LikeListAdapter(this);
            listView.setAdapter(like_Adapter);
        }
    }
    public void findView(){
        linearLayout=(LinearLayout)findViewById(R.id.likeLiner);
        textView=(TextView)findViewById(R.id.likeNone);
        listView = (ListView) findViewById(R.id.listLike);
        backLike=(ImageView)findViewById(R.id.backLike);
    }
    public void setListener(){
        backLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MusicService.setCount((int)MusicService.getLikeList().get(position));
                Intent intent =new Intent(MusicLikeList.this,MusicShow.class);
                startActivity(intent);
                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT) {
                    overridePendingTransition(R.anim.abc_slide_in_bottom,R.anim.abc_slide_out_bottom);
                }
                finish();
            }
        });
    }
    public void onBackPressed()
    {
        Intent i= new Intent(MusicLikeList.this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }

        finish();
    }



}
