package c.musicplayer.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


/**
 * Created by hasee on 2017/3/16.
 */

public class MusicService extends Service {
    private AudioManager audioManager;
    private static boolean isStart=false;
    public static String backGround=new String();
    private static ArrayList likeList=new ArrayList();
    private int  voice;
    private Cursor cursor;
    private static ArrayList<HashMap<String, Object>> musicList=new ArrayList<HashMap<String, Object>>();
    private static MediaPlayer mediaPlayer = new MediaPlayer();
    private static int count = 0;
    private static int playType = 0;
    static private int COMMON_PLAY = 0;
    static private int RAND_PLAY = 1;
    static private int SINGLE_PLAY = 2;

    @Override
    public void onCreate() {
        super.onCreate();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //2 申请焦点
        audioManager.requestAudioFocus(mAudioFocusChange, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        Log.e("service","启动成功");
        musicList=scanAllAudioFiles();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d("service", "播放完毕");
                nextCount();
                rePlay();
            }
        });
    }

    public static void setLikeList(){
        Log.e("service", "setLikeList()" );
        if(likeList.size()==0){
            likeList.add(count);
            Log.e("service","add");
        }
        else{
            for(int i=0;i<likeList.size();i++){
                if(count==(int)likeList.get(i)){
                    Log.e("service","remove");
                    likeList.remove(i);
                    break;
                }
                if(i==likeList.size()-1){
                    Log.e("service","add2");
                    likeList.add(count);
                    break;
                }
            }
        }
    }
    public static void setBackGround(String num){             //设置背景
              backGround=num;
    }
    public static String getBackGround(){                     //得到背景
        return backGround;
    }
    public static void setLikeList(int position){
        Log.e("service", "setLikeList(111)" );
        if(likeList.size()==0){
            Log.e("service","add");
            likeList.add(position);
        }
        else{
            for(int i=0;i<likeList.size();i++){
                if(position==(int)likeList.get(i)){
                    Log.e("service","remove");
                    likeList.remove(i);
                    break;
                }
                if(i==likeList.size()-1){
                    Log.e("service","add2");
                    likeList.add(position);
                    break;
                }
            }
        }
    }
    public static ArrayList getLikeList(){
        return likeList;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("service","start");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e("service","destory");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }
    private static void rePlay(){
        Log.e("service", "replay" );
        if(!isStart){
            isStart=true;
        }
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(musicList.get(count).get("music_url").toString());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("service","Bind");
        return null;
    }
    public static void setPlay(){
        Log.e("service",mediaPlayer.isPlaying()+"");
        if (!isStart){
            rePlay();
        }
        else {
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            }
            else{
                mediaPlayer.start();
            }
        }

    }
    public  static ArrayList<HashMap<String, Object>> getMusicList(){
        return musicList;
    }
    public static void nextCount(){
        Log.e("11", "count");
        if(playType==COMMON_PLAY){
            count++;
            if(count==musicList.size()){
                count=0;
            }
        }
        else if(playType==RAND_PLAY){
            Random random=new Random();
            int rand=random.nextInt(musicList.size());
            count+=rand;
            if(count>=musicList.size()){
                count=count-musicList.size();
            }

        }
    }
    public static void nextMusic(){
        Log.e("service","next");
        if(playType==SINGLE_PLAY){
            count++;
            if(count==musicList.size()){
                count=0;
            }
        }
        else {
            nextCount();
        }

        rePlay();

    }
    public static void setCount(int position){
        Log.e("service","count");
        count=position;
        rePlay();
    }
    public static boolean getStart(){                    //是否开始
        return isStart;
    }
    public static int getPlayType(){                     //得到播放的模式
        return playType;
    }
    public static void setPlayType(){
         if(playType==COMMON_PLAY){
             playType=RAND_PLAY;
         }
         else if(playType==RAND_PLAY){
             playType=SINGLE_PLAY;
         }
         else{
             playType=COMMON_PLAY;
         }
    }
    public static int getMax(){
        return mediaPlayer.getDuration();
    }
    public static int getPosition(){
        return mediaPlayer.getCurrentPosition();
    }
    public static boolean getPlay(){
        return mediaPlayer.isPlaying();
    }
    public static void backMusic(){
        Log.e("service","back");
        if(playType==RAND_PLAY){
            nextCount();
        }
        else{
            count--;
            if(count<0){
                count=musicList.size()-1;
            }
        }
        rePlay();
    }
    private AudioManager.OnAudioFocusChangeListener mAudioFocusChange = new AudioManager.OnAudioFocusChangeListener() {         //监听是否来电去电声音变化
        @Override
        public void onAudioFocusChange(int focusChange) {
            Log.e("service", "改变了");
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS:
                    Log.d("service", "AUDIOFOCUS_LOSS");
                    mediaPlayer.pause();
                    audioManager.abandonAudioFocus(mAudioFocusChange);
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    //setPlay();
                    Log.e("service", "11111");
                    voice=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    if(mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    Log.e("service", "222");
                    voice=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)/2, AudioManager.FLAG_PLAY_SOUND);
                    break;
                case AudioManager.AUDIOFOCUS_GAIN:
                    Log.e("service", "3333");
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, voice, AudioManager.FLAG_PLAY_SOUND);
                    if(!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                    }
                    break;
            }
        }
    };
    public static int getCount(){
        return count;
    }
    public ArrayList<HashMap<String, Object>> scanAllAudioFiles(){
        ArrayList<HashMap<String, Object>> myList = new ArrayList<HashMap<String, Object>>();
        cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
                String like="dislike";
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                String tilte = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                String author = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                Long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                if(size>1024*800){
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("ArrayList<HashMap<String, Object>>music_list", id);
                    map.put("music_album",album);
                    map.put("music_file_name", tilte);
                    map.put("music_author",author);
                    map.put("music_url",url);
                    map.put("music_duration",duration);
                    myList.add(map);
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        return myList;
    }



}
