package c.musicplayer.Broad;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.RemoteViews;

import c.musicplayer.Activity.MainActivity;
import c.musicplayer.Activity.MusicLocalList;
import c.musicplayer.R;
import c.musicplayer.Service.MusicService;

/**
 * Created by hasee on 2017/3/19.
 */

public class MusicBroad extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
         String action=intent.getAction();
        if(action.equals("play")){
            MusicService.setPlay();
        }
        if(action.equals("next")){
            MusicService.nextMusic();
        }

    }

}
