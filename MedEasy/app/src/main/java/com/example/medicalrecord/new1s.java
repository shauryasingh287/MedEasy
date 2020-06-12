package com.example.medicalrecord;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.medicalrecord.family;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DibyenDu on 8/9/2017.
 */

public class new1s extends ArrayAdapter<family> {
    private Activity context;
    private List<family> news;
    public new1s(Activity context,List<family> news){
        super(context,R.layout.booking,news);
        this.context=context;
        this.news=news;


    }




    @Override
    public View getView(int position,View convertView,ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();
        View list=inflater.inflate(R.layout.book1,null,true);
        TextView sou=(TextView)list.findViewById(R.id.source1);
        family artist= news.get(position);
        ArrayList<String> arr=artist.getMed();
       // ArrayList<String> arr=artist.getMed();
        sou.setText(arr.get(0));
        //sou.setText(arr.get(1));
        //String arr=artist.getS();
        //sou.setText(arr);
        return list;
    }
}

