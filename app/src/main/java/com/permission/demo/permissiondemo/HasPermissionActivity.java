package com.permission.demo.permissiondemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;



public class HasPermissionActivity extends AppCompatActivity {


    public static void startActivity(Activity activity){
        Intent intent = new Intent(activity,HasPermissionActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_has_permission);
    }
}
