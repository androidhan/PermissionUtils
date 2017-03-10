package com.hanshao.permission;

import java.util.List;

/**
 * AUTHOR: hanshao
 * DATE: 17/3/9.
 * ACTION:权限申请结果监听
 */

public interface OnRequestPermissionListener {

    void onRequestPermissionSuccess(int requestCode,List<String> permissions);

    void onRequestPermissionFailed(int requestCode,List<String> permissions);
}
