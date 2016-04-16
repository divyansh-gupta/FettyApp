package com.myboi.fettyapp;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import java.io.IOException;
import java.net.URI;

/**
 * Created by Bhaanu Kaul on 4/14/2016.
 */
public class GcmMessageHandler extends GcmListenerService {
    public static final int MESSAGE_NOTIFICATION_ID = 435345;
    @Override
    public void onMessageReceived(String from, Bundle data){
        String message = data.getString("message");
        final MusicPlayer musicPlayer = new MusicPlayer(getApplication().getApplicationContext());
        musicPlayer.findAndPlaySong(new FettyNoise(message));
        Log.d("message received", message);
        createNotification(message);
    }

    private void createNotification(String body){
        final Intent intent = new Intent();
        Context ctx = getBaseContext();
        String bodyText = "";
        if (body.equals("aye_long")){
            bodyText = "Aaaayyyeeee";
        }
        else if(body.equals("aye_short")){
            bodyText = "Aye";
        }
        else if(body.equals("squaw")){
            bodyText = "SQUUAAWW";
        }
        else if (body.equals("seventeen")){
            bodyText = "1738";
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx)
                                                .setSmallIcon(R.mipmap.ic_fg)
                                                .setContentTitle("Oooh Baby ;) FettyGram")
                                                .setContentText(bodyText);
        mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        NotificationManager mNotificationManager = (NotificationManager) ctx
                .getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());

    }

}
