package com.example.medicalrecord;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DibyenDu on 8/9/2017.
 */

public class news extends ArrayAdapter<Artist> {
    private Activity context;
    private List<Artist> news;
    public news(Activity context,List<Artist> news){
        super(context,R.layout.booking,news);
        this.context=context;
        this.news=news;


    }



    @Override
    public View getView(int position,View convertView,ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();
        View list=inflater.inflate(R.layout.booking,null,true);
        TextView sou=(TextView)list.findViewById(R.id.source);
        TextView des=(TextView)list.findViewById(R.id.destination);
        Artist artist =news.get(position);
        ArrayList<String> arr=artist.getMed();
        sou.setText(arr.get(0));
        des.setText(arr.get(1));
        return list;
    }
}

