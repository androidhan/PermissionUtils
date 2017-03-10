package com.permission.demo.permissiondemo;

import android.Manifest;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hanshao.permission.OnRequestPermissionListener;
import com.hanshao.permission.PermissionUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,OnRequestPermissionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //将权限申请结果绑定到PermissionUtils中
        PermissionUtils.onRequestPermissionsResult(this,requestCode,permissions,grantResults);
    }


    @Override
    public void onClick(View v) {

        PermissionUtils.requestPermission(this,"我们获取定位权限，不影响核心功能的适用",10,Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    public void onRequestPermissionSuccess(int requestCode, List<String> permissions) {
        Toast.makeText(this,"拥有了定位权限,开始做你的事情吧",Toast.LENGTH_LONG).show();
        HasPermissionActivity.startActivity(this);

    }

    @Override
    public void onRequestPermissionFailed(int requestCode, List<String> permissions) {
        Toast.makeText(this,"获取定位权限失败",Toast.LENGTH_LONG).show();
            PermissionUtils.checkRequestPermissionNeverHint(this,permissions,"假如没有这个权限，我不知道你的位置");
    }
}


