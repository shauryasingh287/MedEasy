package com.example.medicalrecord;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class show extends AppCompatActivity {

    String user;
    DatabaseReference db;
    ListView list;
    List<family> artistList;
    ArrayList<String> arr;
    Button scanbtn;
    TextView result;
    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        getSupportActionBar().setTitle("View Shared Information");
        Intent in=getIntent();
        user = in.getStringExtra("user");
        artistList = new ArrayList<>();
        arr=new ArrayList<>();
        db = FirebaseDatabase.getInstance().getReference("Family").child(user);
        list = (ListView) findViewById(R.id.list1);

    }
    public void onStart() {
        super.onStart();


        db.addValueEventListener(new ValueEventListener() {


            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {


                artistList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    family artist = dataSnapshot1.getValue(family.class);

                    artistList.add(artist);
                    if(artist.getMed().get(0)!=null)
                        arr.add(artist.getMed().get(0));

                    //Log.d("Check", "getView: done");
                }
                new1s adapter = new new1s(show.this, artistList);
                list.setAdapter(adapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
       // Log.d("checking", "onItemClick: "+arr);

    }
    public void onResume() {
        super.onResume();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position1, long id) {
                // TODO Auto-generated method stub
                //String selected =((TextView)view.findViewById(R.id.list1[position1])).getText().toString();
               // Log.d("checking", "onItemClick: "+arr);
              //  Toast.makeText(getApplicationContext(), "Position "+arr.get(position1),   Toast.LENGTH_LONG).show();
                Intent in=new Intent(getApplicationContext(), fetch.class);
                in.putExtra("user",arr.get(position1) );
                startActivity(in);

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            if(data != null){
                final Barcode barcode = data.getParcelableExtra("barcode");
                result.post(new Runnable() {
                    @Override
                    public void run() {
                        result.setText(barcode.displayValue);
                    }
                });
            }
        }
    }
    public void scanit(View v)
    {
        scanbtn = (Button) findViewById(R.id.scanbtn);
        result = (TextView) findViewById(R.id.result);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }

        Intent intent = new Intent(show.this, scan.class);
        startActivityForResult(intent, REQUEST_CODE);
    }
    public void Goto(View v)
    {
        String val=result.getText().toString();
        if(val.length()>0)
        {
            Intent in=new Intent(getApplicationContext(), fetch.class);
            in.putExtra("user",val );
            startActivity(in);
        }
    }


}
