package com.example.ammar.teacherandmev10.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import com.example.ammar.teacherandmev10.IdentifierClasses.User;
import com.example.ammar.teacherandmev10.IdentifierClasses.ObjectWrapperForBinder;
import com.example.ammar.teacherandmev10.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//the page that is accessed after you login, it has 2 fragments(teacher and parent) clicking either opens the respective UI



public class RoleSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(R.string.nav);
        //remove title bar and replace with above title

    }

    //signs our the user and returns to the login screen
    @Override
    public void onBackPressed() {

            PopupMenu popupMenu = new PopupMenu(RoleSelection.this,findViewById(R.id.textView4));
            popupMenu.getMenu().add("Sign out?");
            popupMenu.show();

            MenuItem item = popupMenu.getMenu().getItem(0);
            {
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    return true;
                }
            });
        }

    }

    public void teacherButtonClick(View view)
    {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String refEmail = currentUser.getEmail();
        User temp = new User();

        final String referenceToData = temp.setUniqueID(refEmail);
        //used to convert email into key for respective database
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference courses = mDatabase.child(referenceToData).child("teacher"); //email-teacher

        final Bundle bundle = new Bundle();
        bundle.putBinder("dataBase", new ObjectWrapperForBinder(courses));
        //add the current hierarchy of databases to a bundle and pass it to the next activity
        //therefore accessing database will be quicker... i think
        startActivity(new Intent(this, TeacherView.class).putExtras(bundle));

    }

    //runs the parent screen
    public void parentButtonClick(View view)
    {
        Intent intent = new Intent(this,ParentView.class);
        startActivity(intent);
    }

    //runs the live chat
    public void liveChat(View view)
    {
        Intent intent = new Intent(this,LiveChat.class);
        startActivity(intent);
    }

}
