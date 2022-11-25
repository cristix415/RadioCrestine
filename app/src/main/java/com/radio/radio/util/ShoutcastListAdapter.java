package com.radio.radio.util;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.radio.radio.MainActivity;
import com.radio.radio.R;
import com.radio.radio.player.RadioManager;


import java.util.ArrayList;
import java.util.List;


public class ShoutcastListAdapter extends BaseAdapter {

    private MainActivity activity;

    private List<Shoutcast> shoutcasts = new ArrayList<>();

    public ShoutcastListAdapter(MainActivity activity, List<Shoutcast> shoutcasts) {
        this.activity = activity;
        this.shoutcasts = shoutcasts;
    }

    @Override
    public int getCount() {
        return shoutcasts.size();
    }

    @Override
    public Object getItem(int position) {
        return shoutcasts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = activity.getLayoutInflater();
        TextView textView;
        ViewHolder holder;



            view = inflater.inflate(R.layout.list_item, parent, false);
       // View child = getLayoutInflater().inflate(R.layout.child, item, false);

            holder = new ViewHolder(view);

            view.setTag(holder);



        Shoutcast shoutcast = (Shoutcast) getItem(position);
        if (shoutcast == null) {

            return view;

        }
        textView = (TextView) view;
        textView.setText(shoutcast.getName());
        textView.setTag(shoutcast);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.RefreshCelelalte();
                activity.radioManager.playOrPause(shoutcast);
//                if (RadioManager.getService().getStatus())
                    ((TextView) v).setText(( ((TextView) v).getText() + " selected"));
  //              else
    //                ((TextView) v).setText(shoutcast.getName());


                if (shoutcast == null) {

                    return;

                }

            //    textView.setText(shoutcast.getName());

                RadioManager.subPlayer.setVisibility(View.VISIBLE);

                //  streamURL = shoutcast.getUrl();

           //     activity.radioManager.playOrPause(shoutcast);
            }
        });

        return view;
    }

    static class ViewHolder {

        //   @BindView(R.id.text)
        TextView text;

        public ViewHolder(View view) {

            //   ButterKnife.bind(this, view);

        }
    }
}
