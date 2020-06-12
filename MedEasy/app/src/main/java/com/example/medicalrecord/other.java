package com.example.medicalrecord;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.widget.Toast;

import com.google.zxing.WriterException;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static java.lang.Math.log;
import static java.lang.Math.random;

public class other extends AppCompatActivity {

    String user;
    DatabaseReference myRef;
    List<family> artistList;
    int count;
    String str;
    String TAG = "GenerateQRCode";
    EditText edtValue;
    ImageView qrImage;
    String inputValue;
    Button start;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        getSupportActionBar().setTitle("Share Information");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
         myRef= database.getReference("Family");
        Intent intent = getIntent();
        user = intent.getStringExtra("user");
        count=20;
        qrImage = (ImageView) findViewById(R.id.QR_Image);
        //start = (Button) findViewById(R.id.start);
        inputValue = user.trim();
        if (inputValue.length() > 0) {
            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerDimension = width < height ? width : height;
            smallerDimension = smallerDimension * 3 / 4;

            qrgEncoder = new QRGEncoder(
                    inputValue, null,
                    QRGContents.Type.TEXT,
                    smallerDimension);
            try {
                bitmap = qrgEncoder.encodeAsBitmap();
                qrImage.setImageBitmap(bitmap);
            } catch (WriterException e) {
                Log.v(TAG, e.toString());
            }
        } else {
            edtValue.setError("Required");
        }



    }
    public void allow(View v)
    {

        EditText ed=findViewById(R.id.edi);
        String[] str1=ed.getText().toString().split("@");
        str=str1[0];
        artistList = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                //artistList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    family artist = dataSnapshot1.getValue(family.class);
                    artistList.add(artist);


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
         count++;
         int s=(int)(Math.random()*1000000000);
         String add=String.valueOf(s);
         String[] ad=add.split(".");
       // Log.d("Value is", "allow: "+add);
        String fam="friends"+add;
        Log.d("Value is", "allow: "+add);
        ArrayList<String> arr = new ArrayList<String>(10);
        arr.add(user);
        myRef.child(str).child(fam).child("med").setValue(arr);
        ed.setText("");
    }





}
