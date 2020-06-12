package com.example.medicalrecord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.app.PendingIntent;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.util.Calendar;

import java.util.ArrayList;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class ocr extends AppCompatActivity {
    EditText e;
    ImageView i;
    private static final int CAMERA_REQUEST_CODE=200;
    private static final int STORAGE_REQUEST_CODE=400;
    private static final int IMAGE_PICK_GALLERY_CODE=1000;
    private static final int IMAGE_PICK_CAMERA_CODE=1001;
    String result12;
    String cameraPermission[];
    String storagePermission[];
    Calendar calendar;
    ArrayAdapter adapter;
    Uri image_uri;
    ListView lv;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);
        getSupportActionBar().setTitle("OCR");
        ActionBar a=getSupportActionBar();

        lv=findViewById(R.id.listv);
        i=findViewById(R.id.image);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        cameraPermission=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        result12="Nothing to Show";
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if(id==R.id.addimage)
        {
            //Log.e("not working", "onOptionsItemSelected:"  );
            showImageimportDialog();
        }

        return super.onOptionsItemSelected(item);
    }
    private void showImageimportDialog()
    {
        String [] items={"Camera","Gallery"};
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("Select image");
        dialog.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                //e.setText("sameena");
                if(which==0)
                {
                    if(!checkCameraPermission())
                    {
                        requestCameraPermission();
                    }
                    else
                    {
                        pickCamera();
                    }
                }
                if(which ==1)
                {
                    if(!checkStoragePermission())
                    {
                        requestStoragePermission();
                    }
                    else
                    {
                        pickGallery();
                    }

                }
            }
        });

        AlertDialog alert= dialog.create();
        alert.show();
    }

    private void pickGallery() {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);



    }

    private void pickCamera() {

        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"NewPic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Image to text");
        image_uri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);

    }

    private void requestStoragePermission() {

        ActivityCompat.requestPermissions(this,storagePermission,STORAGE_REQUEST_CODE);


    }

    private boolean checkStoragePermission() {
        boolean result= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestCameraPermission()
    {
        ActivityCompat.requestPermissions(this,cameraPermission,CAMERA_REQUEST_CODE);
    }
    private boolean checkCameraPermission()
    {
        boolean result= ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        boolean result1= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }
    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults )
    {
        switch(requestCode)
        {
            case CAMERA_REQUEST_CODE:
                if(grantResults.length>0)
                {
                    boolean cameraAccepted=grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted=grantResults[0]== PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && writeStorageAccepted)
                        pickCamera();
                    else
                    {
                        Toast.makeText(this,"permission denied",Toast.LENGTH_SHORT).show();

                    }

                }
                break;
            case STORAGE_REQUEST_CODE:
                if(grantResults.length>0)
                {
                    boolean writeStorageAccepted=grantResults[0]== PackageManager.PERMISSION_GRANTED;
                    if( writeStorageAccepted)
                        pickGallery();
                    else
                    {
                        Toast.makeText(this,"permission denied",Toast.LENGTH_SHORT).show();

                    }

                }
                break;

        }
    }
    public void Back(View v)
    {
        Intent in=new Intent(getApplicationContext(), upload_fetch.class);
        in.putExtra("med", result12);
        setResult(19034,in);
        finish();
    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data)

    {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK)
        {

            if(requestCode==IMAGE_PICK_GALLERY_CODE)
            {
                CropImage.activity(data.getData()).setGuidelines(CropImageView.Guidelines.ON).start(this);

            }
            if(requestCode==IMAGE_PICK_CAMERA_CODE)
            {

                CropImage.activity(image_uri).setGuidelines(CropImageView.Guidelines.ON).start(this);

            }
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK)
            {
                Uri resulturi=result.getUri();
                i.setImageURI(resulturi);
                BitmapDrawable bitmapDrawable=(BitmapDrawable)i.getDrawable();
                Bitmap bitmap=bitmapDrawable.getBitmap();
                TextRecognizer recognizer=new TextRecognizer.Builder(getApplicationContext()).build();
                if(!recognizer.isOperational())
                {
                    Toast.makeText(this,"ERROR",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Frame frame=new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items=recognizer.detect(frame);
                    StringBuilder sb= new StringBuilder();
                    ArrayList<String> arr = new ArrayList<>();
                    for(int j=0;j<items.size();j++)
                    {
                        TextBlock myItem=items.valueAt(j);
                        String [] str = myItem.getValue().split("\n");
                        for(int i=0;i<str.length;i++) {
                            arr.add(str[i]);
                            Log.d("ok",str[i]);
                        }

                    }
                    for(int j=0;j<arr.size();j++)
                    {
                        sb.append(String.valueOf(j+1)+") "+arr.get(j).trim());
                        sb.append("\n");
                    }

                    if(sb.length()>0)
                    {
                        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.remlist,arr);
                        lv.setAdapter(adapter);
                        //  e.setText(sb.toString());
                        result12=sb.toString();
                       /* Intent in=new Intent(getApplicationContext(), ocr.class);
                        in.putExtra("med", sb.toString());
                        startActivity(in);

                        */
                    }
                    else
                    {
                        e.setText("No Text Found");
                        result12="Nothing was Selected";
                    }
                }
            }
            else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error =result.getError();
                Toast.makeText(this,""+error,Toast.LENGTH_SHORT).show();
            }
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    // TODO Auto-generated method stub
                    AlertDialog.Builder dialog=new AlertDialog.Builder(ocr.this);
                    String [] items={"Set Reminder"};

                    dialog.setItems(items, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            if(which==0)
                            {
                                Calendar mcurrentTime = Calendar.getInstance();
                                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                                int minute = mcurrentTime.get(Calendar.MINUTE);
                                TimePickerDialog mTimePicker;

                                Log.d("aqq", "zz");
                                mTimePicker = new TimePickerDialog(ocr.this, new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                        long time;
                                        Log.d("ada", "aaa");
                                        calendar = Calendar.getInstance();
                                        calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                                        calendar.set(Calendar.MINUTE, selectedMinute);
                                        Intent intent = new Intent(ocr.this, AlarmReceiver.class);
                                        pendingIntent = PendingIntent.getBroadcast(ocr.this, 0, intent, 0);

                                        time=(calendar.getTimeInMillis()-(calendar.getTimeInMillis()%60000));
                                        Log.d("y",time+"");
                                        if(System.currentTimeMillis()>time)
                                        {
                                            if (calendar.AM_PM == 0)
                                                time = time + (1000*60*60*12);
                                            else
                                                time = time + (1000*60*60*24);
                                        }

                                        //  alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 10000, pendingIntent);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            Log.d("new","ok");
                                            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent);
                                        } else if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.KITKAT) {
                                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
                                        } else {
                                            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
                                        }
                                    }
                                }, hour, minute, true);//Yes 24 hour time
                                mTimePicker.setTitle("Select Time");
                                mTimePicker.show();
                            }

                        }
                    });
                    AlertDialog alert= dialog.create();
                    alert.show();

                }
            });

        }
    }
}

