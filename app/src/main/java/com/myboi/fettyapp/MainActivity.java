package com.myboi.fettyapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String MIME_TYPE = "audio/mpeg";
    private static final int REQUEST_CODE_SHARE_TO_MESSENGER = 1;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    String PROJECT_NUMER = "";

    private ToggleButtonGroup soundButtons;
    private MusicPlayer player;
    private Snackbar bar;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PROJECT_NUMER = getResources().getString(R.string.gcm_sender_id);
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        GCMClientManager pushClientManager  = new GCMClientManager(this, PROJECT_NUMER);
        pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler(){
            @Override
            public void onSuccess(String registratoinId, boolean isNewRegistration) {
                Log.d("RegId", registratoinId);
            }

            @Override
            public void onFailure(String ex){
                super.onFailure(ex);
            }

        });

        this.addAllSoundButtons();
//        this.setupSendingFunctionality();

    }

    private void addAllSoundButtons() {
        player = new MusicPlayer(this);
        soundButtons = new ToggleButtonGroup();
        LinearLayout rL = (LinearLayout) findViewById(R.id.programLayout);
        String[] allSongs = getResources().getStringArray(R.array.fettyNoises);
        for (final String name : allSongs) {
            ToggleButton fettyButton = (ToggleButton) View.inflate(this.getApplication(), R.layout.sound_buttons, null);
            fettyButton.setText(name);
            fettyButton.setTextOn(name);
            fettyButton.setTextOff(name);
            fettyButton.setOnClickListener(new CompositeOnClickListener(soundButtons.add(fettyButton), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToggleButton clicked = (ToggleButton) v;
                    if(!clicked.isChecked()) return;
                    FettyNoise fettyNoise = new FettyNoise(name);
                    if (bar != null && bar.isShownOrQueued()) {
                        messageButtonClicked(clicked.getText().toString());
                        return;
                    }
                    if (!player.isPlaying()) {
                        player.findAndPlaySong(fettyNoise);
                    }
                }
            }));
            rL.addView(fettyButton);
        }
    }

//    private void setupSendingFunctionality() {
//
//        assert fab != null;
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (soundButtons.getCheckedButton() == null) {
//                    bar = Snackbar.make(view, "Select a beat to send!", Snackbar.LENGTH_INDEFINITE);
//                    bar.setAction("Action", null).show();
//                    return;
//                }
//                messageButtonClicked(soundButtons.getCheckedButton().getText().toString());
//            }
//        });
//    }

    private void messageButtonClicked(String fettyNoise) {
        Uri uri = Uri.parse("android.resource://" + this.getPackageName() + "/raw/" + fettyNoise);
        ShareToMessengerParams shareToMessengerParams =
                ShareToMessengerParams.newBuilder(uri, MIME_TYPE)
                        .build();
        if (bar != null) bar.dismiss();
        MessengerUtils.shareToMessenger(
                this,
                REQUEST_CODE_SHARE_TO_MESSENGER,
                shareToMessengerParams);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
