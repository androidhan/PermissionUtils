#权限适配工具

##功能
- 支持权限申请的成功与失败回调监听。
- 支持第一次申请权限失败再次申请权限的弹窗友好说明提示。
- 支持系统禁止该权限授权的检测并跳转应用设置详情来进行设置权限。

##Download

###使用Gradle
```
dependencies {
    compile 'com.hanshao:universallibrary:1.1.0'
}
repositories {
    maven{
        url "https://dl.bintray.com/hanshaofengs/maven/"
    }
}
```

##使用

###1.当前Activity或Fragment实现OnRequestPermissionListener监听接口
```
public class YourActivity implements OnRequestPermissionListener {
	......
	    /**
     * 权限申请成功
     * @param requestCode 申请权限时的请求码
     * @param permissions 申请成功的权限
     */
    @Override
    public void onRequestPermissionSuccess(int requestCode, List<String> permissions) {
    }

    /**
     * 权限申请失败
     * @param requestCode 申请权限时的请求码
     * @param permissions 申请失败的权限
     */
    @Override
    public void onRequestPermissionFailed(int requestCode, List<String> permissions) {
    }
}

```

###2.进行申请权限
```
PermissionUtils.requestPermission(this,"权限申请说明，用于第一次被拒绝之后，再次发起权限申请的友好说明",10,Manifest.permission.ACCESS_FINE_LOCATION);
```

###3.与Activity或Fragment的onRequestPermissionsResult()方法进行绑定

```
   @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
 		......
        PermissionUtils.onRequestPermissionsResult(this,requestCode,permissions,grantResults);
    }

```

##其他

PermissionUtils具体实现过程参考博客链接:<http://blog.xiaohanshao.cn/2017/03/10/permission>

有BUG或者有其他意见改进的地方可以联系我
我的博客:<http://blog.xiaohanshao.cn>
