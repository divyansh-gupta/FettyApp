package com.myboi.fettyapp;

import android.app.NotificationManager;
import android.content.Context;
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
        Uri uri = Uri.parse("android.resource://" + this.getPackageName() + "/raw/" + message);
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(getApplicationContext(), uri);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("message received", message);
        createNotification(from, message);
    }

    private void createNotification(String title, String body){
        Context ctx = getBaseContext();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx)
                                                .setContentTitle(title)
                                                .setContentText(body);

        NotificationManager mNotificationManager = (NotificationManager) ctx
                .getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());

    }

}
