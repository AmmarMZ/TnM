package com.example.ammar.teacherandmev10.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ammar.teacherandmev10.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//first screen that is shown when app starts, contains login screen

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static String TAG = "MainActivity";

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle(R.string.login);
        //set title of activity

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    //goes to account recovery page
    public void pass_forget(View view)
    {
        Intent intent = new Intent(this,ForgotPass.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        //do nothing
    }

    public void loginMain(View view)
    {
        TextView username = (TextView)findViewById(R.id.editText); //get text from username field
        String user = username.getText().toString().trim();


        TextView password = (TextView)findViewById(R.id.editText3); //get text from password field
        String pass = password.getText().toString().trim();


        boolean formsFill = true;
        //var to check if fields are filled in or not

        //test for fields being filled
        if (user.length() == 0 || pass.length() ==0)
        {
            Toast.makeText(MainActivity.this, R.string.user_pass_missing,
                    Toast.LENGTH_SHORT).show();
            formsFill = false;
            //set check to false
        }

        final Intent intent = new Intent(this, RoleSelection.class);
        //intent after login screen

        //login using fireabase authentication
        if (formsFill)
        {
            mAuth.signInWithEmailAndPassword(user, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                                Toast.makeText(MainActivity.this, R.string.auth_failed,
                                        Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                startActivity(intent);
                                //start roleSelection Acitivtty
                            }

                        }
                    });
        }
    }

    //open the registration page activity
    public void registerMain(View view)
    {
        Intent intent = new Intent(this,Register.class);
        startActivity(intent);
    }



}
