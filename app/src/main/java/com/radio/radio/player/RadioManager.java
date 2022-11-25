package com.radio.radio.player;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.radio.radio.util.Shoutcast;

import org.greenrobot.eventbus.EventBus;

public class RadioManager {

    private static RadioManager instance = null;

    private static RadioService service;
    public static View subPlayer;

    private Context context;

    private boolean serviceBound;

    private RadioManager(Context context) {
        this.context = context;
        serviceBound = false;
    }

    public static RadioManager with(Context context) {

        if (instance == null)
            instance = new RadioManager(context);
Log.e("daaaa","daaaa");
        return instance;
    }

    public static RadioService getService(){
        return service;
    }


    public void playOrPause(Shoutcast shoutcast){

        service.playOrPause(shoutcast);
    }

    public boolean isPlaying() {

        return service.isPlaying();
    }

    public void bind(Shoutcast shoutcast) {

        Intent intent = new Intent(context, RadioService.class);
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
Log.e("bind","gggggg");
        if(service != null)
        {
            EventBus.getDefault().post(service.getStatus());

    }}

    public void unbind() {

        context.unbindService(serviceConnection);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder binder) {
Log.e("service","hhh");
            service = ((RadioService.LocalBinder) binder).getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

            serviceBound = false;
        }
    };

}
