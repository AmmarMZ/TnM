package com.example.ammar.teacherandmev10.IdentifierClasses;

import android.os.Binder;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by Ammar on 2017-05-28.
 */

public class ObjectWrapperForBinder extends Binder {

    private final DatabaseReference mData;

    public ObjectWrapperForBinder(DatabaseReference data) {
        mData = data;
    }

    public Object getData() {
        return mData;
    }
}
