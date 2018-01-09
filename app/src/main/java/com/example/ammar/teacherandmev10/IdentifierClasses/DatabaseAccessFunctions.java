package com.example.ammar.teacherandmev10.IdentifierClasses;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Ammar on 2018-01-08.
 */

public class DatabaseAccessFunctions
{
    public ArrayList<String> getChildrenOfDatabase(Iterator<DataSnapshot> iterator, ArrayList<String> toUpdate)
    {
        if(toUpdate.size() > 0)
            toUpdate.clear();

        while (iterator.hasNext())
        {
            toUpdate.add(String.valueOf(iterator.next().getKey()));
        }
        return toUpdate;
    }

    public void addChild(final DatabaseReference db, final Object toAdd, String name, Context context) //called when enroll students is clicked
    {
        final Map<String,Object> childrenToAdd = new HashMap<>();
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Add a " + name );
        alert.setMessage("Enter the name of the " + name + " you'd like to add");
        final EditText input = new EditText(context);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String name = input.getText().toString();
                childrenToAdd.put(name, toAdd);
                db.getRef().updateChildren(childrenToAdd);

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }
}
