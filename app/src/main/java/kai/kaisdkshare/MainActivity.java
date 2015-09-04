package kai.kaisdkshare;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.HashMap;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;

public class MainActivity extends AppCompatActivity {
    String url = "https://www.google.ca/images/nav_logo100633543.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ShareSDK.initSDK(this);

    }

    public void share(View view) {
        cn.sharesdk.onekeyshare.OnekeyShare oks = new cn.sharesdk.onekeyshare.OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);    }

    public void smshare(View view) {
        ShortMessage.ShareParams sp = new ShortMessage.ShareParams();
        sp.setText("hello world!");
        sp.setImageData(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher));
//        sp.setImagePath("/mnt/sdcard/测试分享的图片.jpg");

        Platform sm = ShareSDK.getPlatform(ShortMessage.NAME);
        sm.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.v("kai", "error");
            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        }); // 设置分享事件回调
        // 执行图文分享
        sm.share(sp);
    }

    public void authorize(View view) {
//        Platform platform = ShareSDK.getPlatform(this, SinaWeibo.NAME);
        Platform platform = ShareSDK.getPlatform(this, QQ.NAME);
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.v("kai", "login");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.v("kai", "cancel");
            }
        });
        platform.authorize();
        //移除授权
        //weibo.removeAccount();
    }

    public void unauthorize(View view) {
        Platform qq = ShareSDK.getPlatform(this, QQ.NAME);
        if (qq.isValid ()) {
            qq.removeAccount();
        }
        qq.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.v("kai", "delete");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        qq.authorize();
        //isValid和removeAccount不开启线程，会直接返回。

    }

    public void fblogin(View view) {
        final Platform fb = ShareSDK.getPlatform(this, Facebook.NAME);

        fb.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.v("kai", "login");
                Log.v("kai", "token: " + fb.getDb().getToken());
                Log.v("kai", "token sec: " + fb.getDb().getTokenSecret());
                Log.v("kai", "name: " + fb.getDb().getUserName());
                Log.v("kai", "icon: " + fb.getDb().getUserIcon());
                Log.v("kai", "expire time: " + fb.getDb().getExpiresTime());
                Log.v("kai", "gender: " + fb.getDb().getUserGender());

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });

        fb.authorize();

    }

    public void fbshare(View view) {
        Facebook.ShareParams fbs = new Facebook.ShareParams();
        fbs.setText("hello world!");
        fbs.setImageUrl(url);
//        fbs.setImageData(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
//        sp.setImagePath("/mnt/sdcard/测试分享的图片.jpg");

        Platform fb = ShareSDK.getPlatform(Facebook.NAME);
//        fb.
        fb.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.v("kai", "shared");

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.v("kai", "error");
            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        }); // 设置分享事件回调

        fb.share(fbs);
    }

}
