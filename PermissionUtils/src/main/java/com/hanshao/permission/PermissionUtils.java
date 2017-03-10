package com.hanshao.permission;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * AUTHOR: hanshao
 * DATE: 17/3/9.
 * ACTION:权限适配工具
 */
public class PermissionUtils {



    private static final int REQUEST_CODE = 200;


    public static void requestPermission(Activity activity,String reason,int requestCode, String... params ){

        realRequestPermission(activity,reason,"确定","取消",requestCode,params);
    }


    public static void requestPermission(Activity activity,String reason,String positive,String negative,int requestCode, String... params  ){
        realRequestPermission(activity,reason,positive,negative,requestCode,params);
    }

    public static void requestPermission(Fragment fragment,String reason, int requestCode, String... params){
        realRequestPermission(fragment,reason,"确定","取消",requestCode,params);
    }

    public static void requestPermission(Fragment fragment,String reason,String positive,String negative,int requestCode, String... params){
        realRequestPermission(fragment,reason,positive,negative,requestCode,params);
    }

    public static void requestPermission(android.app.Fragment fragment,String reason, int requestCode, String... params){
        realRequestPermission(fragment,reason,"确定","取消",requestCode,params);
    }


    public static void requestPermission(android.app.Fragment fragment,String reason,String positive,String negative, int requestCode, String... params){
        realRequestPermission(fragment,reason,positive,negative,requestCode,params);
    }


    private static void realRequestPermission(Object object,String reason, String positive,String negative,int requestCode, String... permissions){

        boolean isEnable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
        if(!isEnable){
            return;
        }

        if(!(object instanceof  OnRequestPermissionListener)){
            throw new RuntimeException("activity and fragment must implements OnRequestPermissionListener");
        }

        doRequestPermission(object,reason,positive,negative,requestCode,permissions);

    }

    private static void doRequestPermission(Object object,String reason, String positive,String negative,int requestCode, String... permissions) {

        if(!checkContainerPermissions(getActivity(object),permissions)){

            boolean isShouldShowRequestPermiision = isShouldShowRequestPermissionRationale(object,Arrays.asList(permissions));

            if(isShouldShowRequestPermiision){
                showRequestPermissionReasonDialog(object,reason,positive,negative,requestCode,permissions);
            }else{
                doingRequestPermission(object,requestCode,permissions);
            }
        }else{
            ((OnRequestPermissionListener)object).onRequestPermissionSuccess(requestCode, Arrays.asList(permissions));
        }
    }

    private static void showRequestPermissionReasonDialog(final Object object, String reason,String positive,String negative, final int requestCode, final String... permissions) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity(object))
                .setMessage(reason)
                .setPositiveButton(positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doingRequestPermission(object,requestCode, permissions);
                    }
                })
                .setNegativeButton(negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((OnRequestPermissionListener) object).onRequestPermissionFailed(requestCode, Arrays.asList(permissions));
                    }
                }).create();
        dialog.show();
    }


    private static void doingRequestPermission(Object object,int requestCode,String[] permissions){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(object instanceof  Activity){
                ((Activity)object).requestPermissions(permissions,requestCode);
            }else if(object instanceof Fragment){
                ((Fragment)object).requestPermissions(permissions, requestCode);
            }else{
                ((android.app.Fragment)object).requestPermissions(permissions,requestCode);
            }
        }
    }

    private static boolean checkContainerPermissions(Context context,String... permissions){

        for (String permission : permissions) {
            boolean isHasPermission = (ContextCompat.checkSelfPermission(context, permission) ==
                    PackageManager.PERMISSION_GRANTED);

            if (!isHasPermission) {
                return false;
            }
        }
        return true;
    }

    private static Activity getActivity(Object object){

        if(object instanceof Activity){
            return ((Activity) object);
        }else if(object instanceof Fragment){
            return ((Fragment) object).getActivity();
        }else {
            return ((android.app.Fragment)object).getActivity();
        }
    }


    private static boolean isShouldShowRequestPermissionRationale(Object object,List<String> permissions){

        for (String permission: permissions) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(shouldShowRequestPermissionRationale(object,permission)){
                    return true;
                }
            }
        }
        return false;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private static boolean shouldShowRequestPermissionRationale(Object object, String permission){
        if(object instanceof Activity){
            return ((Activity)object).shouldShowRequestPermissionRationale(permission);
        }
        else if(object instanceof Fragment){
            return ((Fragment)object).shouldShowRequestPermissionRationale(permission);
        }else{
            return ((android.app.Fragment)object).shouldShowRequestPermissionRationale(permission);
        }
    }


    public static void onRequestPermissionsResult(Object object,int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){

        if(!(object instanceof  OnRequestPermissionListener)){
            throw new RuntimeException("activity and fragment must implements OnRequestPermissionListener");
        }

        List<String> resfusePermissions = new ArrayList<>();
        List<String> grantPermissions = new ArrayList<>();

        for (int i = 0; i <permissions.length ; i++) {

            String permission = permissions[i];
            if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                grantPermissions.add(permission);
            }else{
                resfusePermissions.add(permission);
            }
        }
        if (grantPermissions.size() != 0) {
            ((OnRequestPermissionListener) object).onRequestPermissionSuccess(requestCode, grantPermissions);
        }
        if (resfusePermissions.size() != 0) {
            ((OnRequestPermissionListener) object).onRequestPermissionFailed(requestCode, resfusePermissions);
        }
    }


    public static boolean checkRequestPermissionNeverHint(Activity activity, List<String> refusePermissions, @NonNull String reason){
        return realCheckRequestPermissionNeverHint(activity,refusePermissions,reason,"去设置","取消",null);
    }


    public static boolean checkRequestPermissionNeverHint(Fragment fragment, List<String> refusePermissions,  @NonNull String reason){
        return realCheckRequestPermissionNeverHint(fragment,refusePermissions,reason,"去设置","取消",null);
    }

    public static boolean checkRequestPermissionNeverHint(android.app.Fragment fragment, List<String> refusePermissions, @NonNull String reason){
        return realCheckRequestPermissionNeverHint(fragment,refusePermissions,reason,"去设置","取消",null);
    }


    public static boolean checkRequestPermissionNeverHint(Activity activity, List<String> refusePermissions,  @NonNull String reason ,@NonNull String positive, @NonNull String nagative
    ,DialogInterface.OnClickListener onNegativeClickListener){
        return realCheckRequestPermissionNeverHint(activity,refusePermissions,reason,positive,nagative,onNegativeClickListener);
    }



    public static boolean checkRequestPermissionNeverHint(Fragment activity, List<String> refusePermissions, @NonNull String reason , @NonNull String positive, @NonNull String nagative
    ,DialogInterface.OnClickListener onNegativeClickListener){

        return realCheckRequestPermissionNeverHint(activity,refusePermissions,reason,positive,nagative,onNegativeClickListener);
    }



    public static boolean checkRequestPermissionNeverHint(android.app.Fragment activity, List<String> refusePermissions,  @NonNull String reason ,@NonNull String positive, @NonNull String nagative
    ,DialogInterface.OnClickListener onNegativeClickListener){
        return realCheckRequestPermissionNeverHint(activity,refusePermissions,reason,positive,nagative,onNegativeClickListener);
    }




    private static boolean realCheckRequestPermissionNeverHint(final Object object, List<String> refusePermissions, @NonNull String reason , @NonNull String positive, @NonNull String negative
    , DialogInterface.OnClickListener onNegativeClickListener){

        if(!(object instanceof  OnRequestPermissionListener)){
            throw new RuntimeException("activity and fragment must implements OnRequestPermissionListener");
        }

        boolean isHint = !isShouldShowRequestPermissionRationale(object,refusePermissions);

        if(isHint){
            AlertDialog dialog = new AlertDialog.Builder(getActivity(object))
                    .setMessage(reason)
                    .setPositiveButton(positive, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getActivity(object).getPackageName(), null);
                            intent.setData(uri);
                            getActivity(object).startActivityForResult(intent,REQUEST_CODE);

                        }
                    })
                    .setNegativeButton(negative,onNegativeClickListener)
                    .create();
            dialog.show();

            return true;

        }else{

            return false;
        }

    }



}
