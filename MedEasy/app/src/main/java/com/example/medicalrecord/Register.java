package com.example.medicalrecord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.medicalrecord.Res;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("  ");
    }
    public void register(View v)
    {
        mAuth = FirebaseAuth.getInstance();
        EditText email1=findViewById(R.id.email);
        EditText name1=findViewById(R.id.name);
        final String email=email1.getText().toString();
        final String name =name1.getText().toString();
        EditText password1=findViewById(R.id.pass);
        EditText Addr=findViewById(R.id.pass1);
        final String Ad=Addr.getText().toString();
        String password=password1.getText().toString();
        EditText ph=findViewById(R.id.pass2);
        final String phone=ph.getText().toString();
        EditText ag=findViewById(R.id.age);
        final String age=ag.getText().toString();
        //Log.d("Login ", " Starting ");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("users");

        if((TextUtils.isEmpty(email))||TextUtils.isEmpty(name)||TextUtils.isEmpty(password)||TextUtils.isEmpty(phone)||TextUtils.isEmpty(age)||TextUtils.isEmpty(Ad)||phone.length()<10||password.length()<8){
            if((TextUtils.isEmpty(email))||TextUtils.isEmpty(name)||TextUtils.isEmpty(password)||TextUtils.isEmpty(phone)||TextUtils.isEmpty(age)||TextUtils.isEmpty(Ad)){
                Toast.makeText(Register.this,"Please enter valid information in all the fields",Toast.LENGTH_LONG).show();
            }
            else if(password.length()<8){
                Toast.makeText(Register.this,"Please make a password that is at least 8 characters",Toast.LENGTH_LONG).show();
            }
            else if(phone.length()<10){
                Toast.makeText(Register.this,"Please enter a valid Phone Number",Toast.LENGTH_LONG).show();
            }
        }
        else{
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                String[] em=email.split("@");
                                Log.d("Login ", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Res r;
                                r = new Res(name,Ad,phone,age);
                                myRef.child(em[0]).setValue(r);
                               /* FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference  myRef1 = database.getReference("disease");
                                String id=myRef1.push().getKey();
                                ArrayList<String> arr = new ArrayList<String>(10);
                                Date date = new Date();
                                String med="Nothing to Show";
                                String str="Upload Data First";
                                arr.add(med);
                                arr.add(str);
                                //arr.add("dumy");
                                Artist artist=new Artist(arr);
                                //myRef.child(id).setValue("Avil5");
                                //myRef.setValue("Hello, World!");
                                // myRef.push().setValue("Avil5");
                                Log.d("Testing", "upload: Success "+myRef1.child(em[0]).child(date.toString()).setValue(artist));

                                */
                                Intent in=new Intent(getApplicationContext(), upload_fetch.class);
                                in.putExtra("user", em[0]);
                                startActivity(in);
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("Login", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(Register.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });
        }

    }
}
