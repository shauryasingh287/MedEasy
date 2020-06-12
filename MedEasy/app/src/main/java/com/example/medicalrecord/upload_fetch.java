package com.example.medicalrecord;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class upload_fetch extends AppCompatActivity {

    String user,med;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_fetch);
        getSupportActionBar().setTitle("Home Screen");
        Intent intent = getIntent();
        user = intent.getStringExtra("user");
        med="Upload Medicine Name ->";


    }
    public void upload(View v)
    {

      //  {
            TextView ed1=findViewById(R.id.ed);
            String st= ed1.getText().toString();
            EditText ed2=findViewById(R.id.ed1);
            String str= ed2.getText().toString();

         if(med.equals("Upload Medicine Name ->") || str.length()<=0 )
         {
             Intent in=new Intent(getApplicationContext(), ocr.class);
             //in.putExtra("user", user);
             //startActivity(in);
             startActivityForResult(in,19034);
         }
         else
         {
             FirebaseDatabase database = FirebaseDatabase.getInstance();
             DatabaseReference  myRef = database.getReference("disease");
             String id=myRef.push().getKey();
             ArrayList<String> arr = new ArrayList<String>(10);
             Date date = new Date();
             arr.add(str);
             arr.add(med);
             //arr.add("dumy");
             Artist artist=new Artist(arr);
             //myRef.child(id).setValue("Avil5");
             //myRef.setValue("Hello, World!");
             // myRef.push().setValue("Avil5");
             Log.d("Testing", "upload: Success "+myRef.child(user).child(date.toString()).setValue(artist));
             ed1.setText("Upload Medicine Name ->");
             ed2.setText("");
             //Toast.makeText(this,"Uploaded Sucessfully",Toast.LENGTH_LONG);


         }

        }


        //myRef.setValue(st);
       /* myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                String value = dataSnapshot.getValue(String.class);
                Log.d("Read ", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Read Fail", "Failed to read value.", error.toException());
            }
        });

        */


    public void change(View v)
    {
        Intent in=new Intent(getApplicationContext(), fetch.class);
        in.putExtra("user", user);
        startActivity(in);

    }
    public void show(View v)
    {
        Intent in=new Intent(getApplicationContext(), show.class);
        in.putExtra("user", user);
        startActivity(in);
    }
    public void share(View v)
    {
        Intent in=new Intent(getApplicationContext(), other.class);
        in.putExtra("user", user);
        startActivity(in);
    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 19034) {
            med = data.getStringExtra("med");
            TextView ed1 = findViewById(R.id.ed);
            ed1.setText(med);
        }
    }
}
