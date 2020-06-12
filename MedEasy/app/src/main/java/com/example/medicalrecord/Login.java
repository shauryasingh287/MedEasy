package com.example.medicalrecord;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;



public class Login extends AppCompatActivity {
    private EditText email_id;
    private EditText password;
    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mdatabase;
    GoogleSignInClient mGoogleSignInClient;
    SignInButton signInButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("   ");
        mauth = FirebaseAuth.getInstance();
        email_id = (EditText) findViewById(R.id.editT1);
        password = (EditText) findViewById(R.id.editT2);

//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);
//        signInButton=(SignInButton)findViewById(R.id.googleBtn);
//
//        signInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//                startActivityForResult(signInIntent, 101);
//            }
//        });
    }
    public void sign(View view) {
        final String str=email_id.getText().toString().trim();
        final String str1=password.getText().toString();
    if((TextUtils.isEmpty(str))||TextUtils.isEmpty(str1)){
        Toast.makeText(Login.this,"Please Enter a Username and Password",Toast.LENGTH_LONG).show();
    }
    else{
        mauth.signInWithEmailAndPassword(str, str1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(Login.this,"Success",Toast.LENGTH_LONG);

                            FirebaseUser user = mauth.getCurrentUser();
                            Intent in=new Intent(getApplicationContext(), upload_fetch.class);
                            String[] em=str.split("@");
                            in.putExtra("user", em[0]);
                            startActivity(in);

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this,"Please enter a valid Username and Password",Toast.LENGTH_LONG).show();
                            //  Toast.makeText(Login.this,"Failure",Toast.LENGTH_LONG);
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    }
    public void register(View view)
    {
        Intent register =new Intent(Login.this, Register.class);
        startActivity(register);
    }


    private void checkUserExist()
    {
        final String user_id=mauth.getCurrentUser().getUid();
        Log.d("checking ", "checkUserExist: "+user_id);
        Toast.makeText(Login.this, "Set up account "+user_id, Toast.LENGTH_LONG).show();
        mdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id))
                {
                    Intent main =new Intent (Login.this, upload_fetch.class);
                    startActivity(main);
                }
                else
                {
                    Toast.makeText(Login.this, "Set up account", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
//    private void signIn() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, 101);
//    }



//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == 101) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                firebaseAuthWithGoogle(account);
//            } catch (ApiException e) {
//                // Google Sign In failed, update UI appropriately
//
//                // ...
//            }
//        }
//    }
//
//    private void firebaseAuthWithGoogle(GoogleSignInAccount account)
//    {
//        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//        mauth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
////                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = mauth.getCurrentUser();
//                            Intent i=new Intent(getApplicationContext(),upload_fetch.class);
//                            startActivity(i);
//                            finish();
//                            Toast.makeText(getApplicationContext(), "User logged in successfully", Toast.LENGTH_LONG).show();
//
//                        } else {
//                            Toast.makeText(getApplicationContext(), "Could not log in ", Toast.LENGTH_LONG).show();
//                            // If sign in fails, display a message to the user.
//////                            Log.w(TAG, "signInWithCredential:failure", task.getException());
////                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            ;
//                        }
//
//                        // ...
//                    }
//                });
//    }
}