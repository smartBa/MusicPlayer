package c.musicplayer.Activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import c.musicplayer.R;
import c.musicplayer.Service.MusicService;

public class MainPicture extends AppCompatActivity implements View.OnClickListener {
    private ImageView back;
    private ImageView imageViewOne;
    private ImageView imageViewTwo;
    private ImageView imageViewThree;
    private ImageView imageViewFour;
    private ImageView rightOne;
    private ImageView rightTwo;
    private ImageView rightThree;
    private ImageView rightFour;
    private static String num=new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_picture);
        init();
        setOnClick();
        setRight();
    }

    public void init() {
        imageViewOne = (ImageView) findViewById(R.id.imageOne);
        imageViewTwo = (ImageView) findViewById(R.id.imageTwo);
        imageViewThree = (ImageView) findViewById(R.id.imageThree);
        imageViewFour = (ImageView) findViewById(R.id.imageFour);
        rightOne= (ImageView) findViewById(R.id.rightOne);
        rightTwo= (ImageView) findViewById(R.id.rightTwo);
        rightThree= (ImageView) findViewById(R.id.rightThree);
        rightFour= (ImageView) findViewById(R.id.rightFour);
        back=(ImageView)findViewById(R.id.pictureBack);
    }
    public void setOnClick() {
        imageViewOne.setOnClickListener(this);
        imageViewTwo.setOnClickListener(this);
        imageViewThree.setOnClickListener(this);
        imageViewFour.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    public void setRight(){
        if(num.equals("one")){
            rightOne.setVisibility(View.VISIBLE);
            rightTwo.setVisibility(View.GONE);
            rightThree.setVisibility(View.GONE);
            rightFour.setVisibility(View.GONE);
        }
        else if(num.equals("two")){
            rightOne.setVisibility(View.GONE);
            rightTwo.setVisibility(View.VISIBLE);
            rightThree.setVisibility(View.GONE);
            rightFour.setVisibility(View.GONE);
        }
        else if(num.equals("three")){
            rightOne.setVisibility(View.GONE);
            rightTwo.setVisibility(View.GONE);
            rightThree.setVisibility(View.VISIBLE);
            rightFour.setVisibility(View.GONE);
        }
        else{
            rightOne.setVisibility(View.GONE);
            rightTwo.setVisibility(View.GONE);
            rightThree.setVisibility(View.GONE);
            rightFour.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageOne:
                num="one";
                MusicService.setBackGround(num);
                rightOne.setVisibility(View.VISIBLE);
                rightTwo.setVisibility(View.GONE);
                rightThree.setVisibility(View.GONE);
                rightFour.setVisibility(View.GONE);
                break;
            case R.id.imageTwo:
                num="two";
                MusicService.setBackGround(num);
                rightOne.setVisibility(View.GONE);
                rightTwo.setVisibility(View.VISIBLE);
                rightThree.setVisibility(View.GONE);
                rightFour.setVisibility(View.GONE);
                break;
            case R.id.imageThree:
                num="three";
                MusicService.setBackGround(num);
                rightOne.setVisibility(View.GONE);
                rightTwo.setVisibility(View.GONE);
                rightThree.setVisibility(View.VISIBLE);
                rightFour.setVisibility(View.GONE);
                break;
            case R.id.imageFour:
                num="four";
                MusicService.setBackGround(num);
                rightOne.setVisibility(View.GONE);
                rightTwo.setVisibility(View.GONE);
                rightThree.setVisibility(View.GONE);
                rightFour.setVisibility(View.VISIBLE);
                break;
            case R.id.pictureBack:
                onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(MainPicture.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT) {
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }
        finish();
    }
}
