package com.radio.radio;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.radio.radio.databinding.ActivityMainBinding;
import com.radio.radio.player.PlaybackStatus;
import com.radio.radio.player.RadioManager;
import com.radio.radio.util.Shoutcast;
import com.radio.radio.util.ShoutcastHelper;
import com.radio.radio.util.ShoutcastListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    ListView listView;
    ImageButton trigger;
    String streamURL;
    Shoutcast shoutcast;

    TextView textView;


    View subPlayer;

    public RadioManager radioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "ALTCEVA", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View contentView = findViewById(R.id.continut);
        subPlayer = contentView.findViewById(R.id.sub_player);
        listView = contentView.findViewById(R.id.listview);
        trigger = contentView.findViewById(R.id.playTrigger);
        if (listView != null)
            Log.e("listview","not null");
        Log.e("trigger dupaaaaaaaa","trrrrrrrrrrrrrr");
        trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClicked();
            }
        });
        radioManager = RadioManager.with(this);
        radioManager.bind(shoutcast);
        RadioManager.subPlayer  = subPlayer;
        listView.setAdapter(new ShoutcastListAdapter(this, ShoutcastHelper.retrieveShoutcasts(this)));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("onItemClick list view","trrrrrrrrrrrrrr");
                onItemClickListView(parent, view, position, id);
            }
        });

/*
        StyledPlayerView playerView = findViewById(R.id.player_view);
        // Instantiate the player.
        ExoPlayer player = new ExoPlayer.Builder(getApplicationContext()).build();
// Attach player to the view.
        playerView.setPlayer(player);
// Set the media item to be played.
        MediaItem mediaItem = MediaItem.fromUri("https://listen.radioking.com/radio/494884/stream/551902");
        player.setMediaItem(mediaItem);
// Prepare the player.
        player.prepare();*/
     //   radioManager.playOrPause("http://bbcmedia.ic.llnwd.net/stream/bbcmedia_radio1_mf_p");
    }
 public void RefreshCelelalte(){
     for (int i = 0; i < listView.getCount(); ++i) {
         TextView txtview = (TextView)listView.getChildAt(i);
         txtview.setText(ShoutcastHelper.retrieveShoutcasts(getApplicationContext()).get(i).getName());
     }
 }
    @Override
    public void onStart() {

        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {

        EventBus.getDefault().unregister(this);

        super.onStop();
    }

    @Override
    protected void onDestroy() {

        radioManager.unbind();

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

      //  radioManager.bind(shoutcast);
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    @Subscribe
    public void onEvent(String status){
        Log.e("onEvent", status);
    //    subPlayer.setVisibility(View.VISIBLE);
        switch (status){

            case PlaybackStatus.LOADING:
                Log.e("loading", status);
                trigger.setImageResource(R.drawable.ic_loa);

                break;

            case PlaybackStatus.ERROR:
                trigger.setImageResource(R.drawable.ic_loa);
                Toast.makeText(this, R.string.no_stream, Toast.LENGTH_SHORT).show();

                break;
            case PlaybackStatus.PLAYING:
                trigger.setImageResource( R.drawable.ic_pause_black);
                Log.e("PPPPPLAYYYYYYYY", status);
                /*trigger.setImageResource(status.equals(PlaybackStatus.PLAYING)
                        ? R.drawable.ic_pause_black
                        : R.drawable.ic_play_arrow_black);*/
                break;


        }



    }


    public void onClicked(){
        Log.e("onCLicked buton jos", RadioManager.getService().shoutcast.getUrl());

        if(TextUtils.isEmpty(RadioManager.getService().shoutcast.getUrl())) return;
        RadioManager.subPlayer = subPlayer;
        radioManager.playOrPause(RadioManager.getService().shoutcast);
    }

    public void onItemClickListView(AdapterView<?> parent, View view, int position, long id){
        Log.e("onItemCLick", "click");

        Shoutcast shoutcast = (Shoutcast) parent.getItemAtPosition(position);
        if(shoutcast == null){

            return;

        }

        textView.setText("shoutcast.getName()");

    //    subPlayer.setVisibility(View.VISIBLE);

        streamURL = shoutcast.getUrl();

        radioManager.playOrPause(shoutcast);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        Log.e("OnCreateOption", "apasare meniu");

        AfisareUiRadio();
        return true;
    }

    private void AfisareUiRadio() {
        View contentView = findViewById(R.id.continut);
        listView = contentView.findViewById(R.id.listview);
    //    View fragHome = findViewById(R.id.hhhh);
//subPlayer.setVisibility(View.VISIBLE);
//subPlayer.bringToFront();
  //      View contentView = findViewById(R.id.continut);
      //  subPlayer = fragHome.findViewById(R.id.sub_player);
        listView = contentView.findViewById(R.id.listview);
        trigger = contentView.findViewById(R.id.playTrigger);
        if (listView != null)
            Log.e("listview","not null");
        Log.e("trigger dupaaaaaaaa","trrrrrrrrrrrrrr");
        trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClicked();
            }
        });
        if (listView != null)
    {
       // trigger = contentView.findViewById(R.id.playTrigger);
        radioManager = RadioManager.with(this);
        radioManager.bind(shoutcast);
        listView.setAdapter(new ShoutcastListAdapter(this, ShoutcastHelper.retrieveShoutcasts(getApplicationContext())));
        Log.e("Afisare RADIO", String.valueOf(radioManager.isPlaying()));

      //  if (radioManager.isPlaying())
        //    RadioManager.subPlayer.setVisibility(View.VISIBLE);

    }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}