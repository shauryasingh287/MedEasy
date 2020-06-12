package com.example.medicalrecord;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class fetch extends AppCompatActivity {

    DatabaseReference db;
    ListView list;
    List<Artist> artistList;

    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch);
        getSupportActionBar().setTitle("Database");
        Intent intent=getIntent();
        user = intent.getStringExtra("user");
        db = FirebaseDatabase.getInstance().getReference("disease").child(user);
        list = (ListView) findViewById(R.id.list);
        artistList = new ArrayList<>();
    }

    public void onStart() {
        super.onStart();

        db.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int flag=0;
                artistList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Artist artist = dataSnapshot1.getValue(Artist.class);
                    flag++;
                    artistList.add(artist);

                }
                if(flag>0)
                {
                    news adapter = new news(fetch.this, artistList);
                    list.setAdapter(adapter);
                }
                else
                {
                    Artist artist=new Artist();
                    artistList.clear();
                    artistList.add(artist);
                    news adapter = new news(fetch.this, artistList);
                    list.setAdapter(adapter);
                }
                //news adapter = new news(fetch.this, artistList);
                //list.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Artist artist=new Artist();
                artistList.clear();
                artistList.add(artist);
                news adapter = new news(fetch.this, artistList);
                list.setAdapter(adapter);

            }
        });
    }
    public void Searchtag(View v)
    {
        EditText ed=findViewById(R.id.search);
        final String str=ed.getText().toString();
        //Log.d("search ", "Searchtag: "+str);

        if(str.length()>0)
        {
            db.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    artistList.clear();
                    int flag=0;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Artist artist = dataSnapshot1.getValue(Artist.class);
                        flag++;
                        if((artist.getMed().get(0).toLowerCase().trim()).equals(str.toLowerCase().trim()))
                             artistList.add(artist);

                    }
                    if(flag>0)
                    {
                        news adapter = new news(fetch.this, artistList);
                        list.setAdapter(adapter);
                    }
                    else
                    {
                        Artist artist=new Artist();
                        artistList.clear();
                        artistList.add(artist);
                        news adapter = new news(fetch.this, artistList);
                        list.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    Artist artist=new Artist();
                    artistList.clear();
                    artistList.add(artist);
                    news adapter = new news(fetch.this, artistList);
                    list.setAdapter(adapter);
                }
            });
        }
        else
        {
            db.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    int flag=0;
                    artistList.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Artist artist = dataSnapshot1.getValue(Artist.class);
                        flag++;
                        artistList.add(artist);

                    }
                    if(flag>0)
                    {
                        news adapter = new news(fetch.this, artistList);
                        list.setAdapter(adapter);
                    }
                    else
                    {
                        Artist artist=new Artist();
                        artistList.clear();
                        artistList.add(artist);
                        news adapter = new news(fetch.this, artistList);
                        list.setAdapter(adapter);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });
        }

    }
}
