package com.example.ammar.teacherandmev10.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ammar.teacherandmev10.IdentifierClasses.User;
import com.example.ammar.teacherandmev10.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//Activity for registering, accessed through the register button on the MainActivity (Login page)
public class Register extends AppCompatActivity {

    private static FirebaseAuth mAuth;
    private static final String TAG = "Register";
    //dont delete this despite the warning
    private static FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle(R.string.reg_title);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                }
                else
                {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

    }

    //clicking the register button on the register page
    public void registerButton(View view)
    {
        TextView firstPass = (TextView) findViewById(R.id.editText6);
        String fPass = firstPass.getText().toString().trim();
        //getting the password entered into the first password field

        TextView secondPass = (TextView) findViewById(R.id.editText7);
        String sPass = secondPass.getText().toString().trim();
        //second password field

        TextView user = (TextView) findViewById(R.id.editText5);
        String email = user.getText().toString().trim();
        //email field

        boolean passMatch = true;
        //variable to see passwords match

        //if password or email are not entered
        if (email.length() == 0 || sPass.length() == 0 || fPass.length() == 0)
        {
            Toast.makeText(Register.this, R.string.no_email_pass,
                    Toast.LENGTH_SHORT).show();
            passMatch = false;
        }

        //if both passwords don't match
        if (!fPass.equals(sPass))
        {
            Toast.makeText(Register.this, R.string.no_pass_match,
                    Toast.LENGTH_SHORT).show();
            passMatch = false;
        }

        //if password isnt between 8-16 characters
        if (fPass.length() < 8 || fPass.length() > 16)
        {
            Toast.makeText(Register.this, R.string.pass_length_limit,
                    Toast.LENGTH_SHORT).show();
        }

        final Intent intent = new Intent(this, InsertInfo.class);
        //next activitiy to insert information declared final since it is accessed within an inner class

        if (passMatch)
        {
            mAuth.createUserWithEmailAndPassword(email, fPass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(Register.this, R.string.auth_failed_or_exists,
                                        Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(Register.this, R.string.reg_succeeded,
                                        Toast.LENGTH_SHORT).show();

                                User user = new User();
                                System.out.println("--------UID ---------");
                                System.out.println(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                user.setUserID(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(user.setUniqueID(currentUser.getEmail()));
                                myRef.setValue(user);

                                startActivity(intent);
                            }
                        }
                    });
        }
    }
}
