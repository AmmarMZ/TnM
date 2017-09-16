package com.example.ammar.teacherandmev10.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ammar.teacherandmev10.IdentifierClasses.Parent;
import com.example.ammar.teacherandmev10.IdentifierClasses.Teacher;
import com.example.ammar.teacherandmev10.IdentifierClasses.User;
import com.example.ammar.teacherandmev10.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

//Activity that comes right after registering, can also be called when editing information

public class InsertInfo extends AppCompatActivity {
    private static final String TAG = "InsertInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_info);

        Toast.makeText(InsertInfo.this, R.string.verify_email,
                Toast.LENGTH_SHORT).show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // verify email by sending an email to them

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });

        TextView tempView = (TextView) findViewById(R.id.editText11);
        tempView.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        //sets the email field as the inputted email
    }

    public void proceed(View view)  //function that is called when user information is filled
    {
        TextView fname = (TextView) findViewById(R.id.editText8);
        String firstName = fname.getText().toString().trim();

        TextView lname = (TextView) findViewById(R.id.editText9);
        String lastName = lname.getText().toString().trim();

        TextView pNumber = (TextView) findViewById(R.id.editText10);
        String phoneNum = pNumber.getText().toString().trim();

        TextView em = (TextView) findViewById(R.id.editText11);
        String email = em.getText().toString().trim();

        CheckBox teacherCheckBox = (CheckBox) findViewById(R.id.checkBox);
        CheckBox parentCheckBox = (CheckBox) findViewById(R.id.checkBox2);

        boolean formsCorrect = true;
        //var to check if forms are filled in properly

        //test if forms aren't empty
        if (firstName.length() == 0 || lastName.length() == 0 || phoneNum.length() == 0 || email.length() == 0 )
        {
            Toast.makeText(InsertInfo.this, R.string.names_phone_missing,
                    Toast.LENGTH_SHORT).show();
            formsCorrect = false;
        }

        if (phoneNum.length() < 10)
        {
            Toast.makeText(InsertInfo.this, R.string.phone_num_short,
                    Toast.LENGTH_SHORT).show();
            formsCorrect = false;
        }

        char [] temp = new char[phoneNum.length()];
        temp = phoneNum.toCharArray();
        //convert phone number into char array and test fields so no weird characters
        //the phoneNumber field is numeric only therefore letters do not need to be checked

        boolean check = false;
        //check used to verify phoneNumber characters

        for (int i = 0; i < phoneNum.length(); i++)
        {
            if (temp[i] == ',' || temp[i] == '+' || temp[i] == ';'
                    || temp[i] == '/' || temp[i] == 'N' || temp[i] == '.' || temp[i] =='#' || temp[i] == '*')
            {
                formsCorrect = false;
                check = true;
            }
        }

        if (check)
        {
            Toast.makeText(InsertInfo.this, R.string.invalid_phone,
                    Toast.LENGTH_SHORT).show();
        }

        if (firstName.length() < 2 || lastName.length() < 2)
        {
            Toast.makeText(InsertInfo.this, R.string.name_short,
                    Toast.LENGTH_SHORT).show();
            formsCorrect = false;
        }
        if (!email.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
        {
            Toast.makeText(InsertInfo.this, R.string.email_changed,
                    Toast.LENGTH_SHORT).show();
            formsCorrect = false;
        }

        if (!teacherCheckBox.isChecked() && !parentCheckBox.isChecked())
        {
            Toast.makeText(InsertInfo.this, R.string.checkbox_empty,
                    Toast.LENGTH_SHORT).show();
            formsCorrect = false;
        }

        if (formsCorrect)
        {
            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhoneNumber(phoneNum);
            user.setUserAccount(FirebaseAuth.getInstance().getCurrentUser());

            if (teacherCheckBox.isChecked())
            {
                Teacher teacher = new Teacher(UUID.randomUUID().toString());
                user.setTeacher(teacher);
            }
            if (parentCheckBox.isChecked())
            {
                Parent parent = new Parent(UUID.randomUUID().toString());
                user.setParent(parent);
            }

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(user.setUniqueID(currentUser.getEmail()));
            myRef.setValue(user);

            Intent intent = new Intent(this,RoleSelection.class); //intent after login screen
            intent.putExtra("caller",TAG);
            startActivity(intent);
            System.out.println(UUID.randomUUID().toString());
        }

    }

    @Override
    public void onBackPressed() {

            PopupMenu popupMenu = new PopupMenu(InsertInfo.this,findViewById(R.id.textView9));
            popupMenu.getMenu().add(R.string.sign_wout_reg);
            popupMenu.show();

            MenuItem item = popupMenu.getMenu().getItem(0);
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    return true;
                }
            });


    }


}
