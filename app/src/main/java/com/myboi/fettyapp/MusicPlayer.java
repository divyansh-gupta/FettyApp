package com.myboi.fettyapp;

/**
 * Created by divy9677 on 3/27/16.
 */

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import java.io.IOException;
import android.app.Activity;

// -------------------------------------------------------------------------
/**
 * Subclass of MediaPlayer designed for easy music playback for activities.
 *
 * @author Divyansh Gupta (divyg)
 * @version 2016.25.03
 */
public class MusicPlayer extends MediaPlayer implements OnPreparedListener,
        OnCompletionListener {
    // ~ Fields ................................................................
    private Activity screen;
    private boolean paused;
    private boolean enabled;

    // ~ Constructors ..........................................................
    // ----------------------------------------------------------
    /**
     * Create a new MusicPlayer object.
     *
     * @param appScreen is the GameScreen upon which Music should be played.
     */
    public MusicPlayer(Activity appScreen) {
        screen = appScreen;
        paused = false;
        this.setOnPreparedListener(this);
        this.setOnCompletionListener(this);
    }

    // ~ Methods ...............................................................
    // ----------------------------------------------------------
    /**
     * Enables the GUI whenever the MediaPlayer is finished playing a Jingle.
     *
     * @param mp is the MediaPlayer that has just completed playing a Jingle.
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        enabled = true;
        this.stop();
        this.reset();
    }

    // ----------------------------------------------------------
    /**
     * Disables the GUI whenever the MediaPlayer begins playing a Jingle.
     *
     * @param mp is the MediaPlayer that has just been prepared to play a
     *            Jingle.
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        this.start();
        enabled = false;
    }

    // ----------------------------------------------------------
    /**
     * Play a Song that is passed in toPlay with the audio stored in the "raw"
     * resource folder.
     *
     * @param toPlay is the Song that will be played.
     */
    public void findAndPlaySong(Playable toPlay) {
        String path = toPlay.getRawResourcePath(screen.getApplicationContext());
        this.playSoundResource(path);
    }

    // ----------------------------------------------------------
    /**
     * Play any sound resource based on the path that is passed in.
     *
     * @param path is the location of the sound resource to play.
     */
    public void playSoundResource(String path) {
        try {
            this.setAudioStreamType(AudioManager.STREAM_MUSIC);
            this.setDataSource(screen.getApplicationContext(), Uri.parse(path));
            this.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ----------------------------------------------------------
    /**
     * Get if MusicPlayer has been paused or not.
     *
     * @return true if MusicPlayer has been paused. Otherwise, false.
     */
    public boolean isPaused() {
        return paused;
    }

    // ----------------------------------------------------------
    /**
     * Pauses the MusicPlayer, stopping it from playing a Song.
     */
    @Override
    public void pause() {
        super.pause();
        paused = true;
    }

    // ----------------------------------------------------------
    /**
     * Starts the MusicPlayer playing a Jingle.
     */
    @Override
    public void start() {
        super.start();
        paused = false;
    }

    // ----------------------------------------------------------
    /**
     * Return the value of enabled.
     *
     * @return true if the GUI is enabled, false if it is not
     */
    public boolean getEnabled() {
        return enabled;
    }
}
