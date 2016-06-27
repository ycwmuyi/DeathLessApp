package com.ycw.nevercloseapp.nevercloseapp;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {
    private final static String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mian2);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.test1)
    public void test1(View view){
        Log.e("MainActivity", "test1");
        showCustomizeNotification();
    }

    @OnClick(R.id.test2)
    public void test2(View view){
        Log.e("MainActivity","test1");
        showSimpleNotification();
    }



    @OnClick(R.id.test3)
    public void test3(View view){
        Log.e("MainActivity","updateNotification");
        updateNotification();
    }

    private MyService myService;

    @OnClick(R.id.test4)
    public void test4(View view){
        Log.e("MainActivity","updateNotification");
        showProgressNotification();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG,"onServiceConnected");
            MyService.LocalBinder mLocalBinder = (MyService.LocalBinder) service;
            myService = mLocalBinder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG,"onServiceDisconnected");

        }
    };

    @OnClick(R.id.test5)
    public void test5(View view){
        Log.e("MainActivity","updateNotification");
        Intent intent = new Intent(this,MyService.class);
//        startService(intent);
        bindService(intent,serviceConnection,Context.BIND_AUTO_CREATE);
    }

    @OnClick(R.id.test6)
    public void test6(View view){
        if(myService!=null){
            Log.e(TAG, myService.getRandomNumber() + "");
            myService.startForegrround();
        }
    }


    //自定义显示的通知 ，创建RemoteView对象
    private void showCustomizeNotification() {
        CharSequence title = "i am new";
        int icon = R.drawable.icon;
        long when = System.currentTimeMillis();
        Notification noti = new Notification(icon, title, when);
        noti.flags = Notification.FLAG_INSISTENT;
        // 1、创建一个自定义的消息布局 view.xml
         // 2、在程序代码中使用RemoteViews的方法来定义image和text。然后把RemoteViews对象传到contentView字段
        RemoteViews remoteView = new RemoteViews(this.getPackageName(),R.layout.notification);
        remoteView.setImageViewResource(R.id.image, R.drawable.icon);
        remoteView.setTextViewText(R.id.text , "通知类型为：自定义View");
        noti.contentView = remoteView;
        // 3、为Notification的contentIntent字段定义一个Intent(注意，使用自定义View不需要setLatestEventInfo()方法)
        //这儿点击后简单启动Settings模块
        PendingIntent contentIntent = PendingIntent.getActivity
                (MainActivity.this, 0,new Intent("android.settings.SETTINGS"), 0);
        noti.contentIntent = contentIntent;
        NotificationManager mnotiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mnotiManager.notify(0, noti);
    }



    // 默认显示的的Notification
    private void showDefaultNotification() {
        // 定义Notication的各种属性
        CharSequence title = "i am new";
        int icon = R.drawable.icon;
        long when = System.currentTimeMillis();
        Notification noti = new Notification(icon, title, when + 10000);
        noti.flags = Notification.FLAG_INSISTENT;
        // 创建一个通知
        Notification mNotification = new Notification();
        // 设置属性值
        mNotification.icon = R.drawable.icon;
        mNotification.tickerText = "NotificationTest";
        mNotification.when = System.currentTimeMillis(); // 立即发生此通知
        // 带参数的构造函数,属性值如上
        // Notification mNotification = = new Notification(R.drawable.icon,"NotificationTest", System.currentTimeMillis()));
        // 添加声音效果
        mNotification.defaults |= Notification.DEFAULT_SOUND;
        // 添加震动,后来得知需要添加震动权限 : Virbate Permission
        //mNotification.defaults |= Notification.DEFAULT_VIBRATE ;
        //添加状态标志
        //FLAG_AUTO_CANCEL          该通知能被状态栏的清除按钮给清除掉
        //FLAG_NO_CLEAR                 该通知能被状态栏的清除按钮给清除掉
        //FLAG_ONGOING_EVENT      通知放置在正在运行
        //FLAG_INSISTENT                通知的音乐效果一直播放
        mNotification.flags = Notification.FLAG_INSISTENT ;
        //将该通知显示为默认View
        PendingIntent contentIntent = PendingIntent.getActivity(MainActivity.this, 0,new Intent("android.settings.SETTINGS"), 0);
        //        mNotification.setLatestEventInfo(MainActivity.this, "通知类型：默认View", "一般般哟。。。。",contentIntent);
                mNotification.contentIntent = contentIntent;
        // 设置setLatestEventInfo方法,如果不设置会App报错异常
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //注册此通知
        // 如果该NOTIFICATION_ID的通知已存在，会显示最新通知的相关信息 ，比如tickerText 等
        mNotificationManager.notify(0, mNotification);
    }



    private void showSimpleNotification(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());
    }


    private NotificationCompat.Builder mNotifyBuilder;
    private int numMessages = 0;
    private void updateNotification(){
        NotificationManager  mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Sets an ID for the notification, so it can be updated
        int notifyID = 1;
        if(mNotifyBuilder==null){
            mNotifyBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle("New Message")
                    .setContentText("You've received new messages.")
                    .setSmallIcon(R.drawable.icon);
        }
        // Start of a loop that processes data and then notifies the user...
        mNotifyBuilder.setContentText("crruntmessage")
                .setNumber(++numMessages);
        // Because the ID remains unchanged, the existing notification is
        // updated.
        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
    }

    private void removeNotification() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // 取消的只是当前Context的Notification
        mNotificationManager.cancel(2);
    }


    NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private void showProgressNotification(){
        mNotifyManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Picture Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.icon);
        // Start a lengthy operation in a background thread
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        int incr;
                        // Do the "lengthy" operation 20 times
                        for (incr = 0; incr <= 100; incr+=5) {
                            // Sets the progress indicator to a max value, the
                            // current completion percentage, and "determinate"
                            // state
                            mBuilder.setProgress(100, incr, false);
                            // Displays the progress bar for the first time.
                            mNotifyManager.notify(0, mBuilder.build());
                            // Sleeps the thread, simulating an operation
                            // that takes time
                            try {
                                // Sleep for 5 seconds
                                Thread.sleep(5*1000);
                            } catch (InterruptedException e) {
                                Log.d(TAG, "sleep failure");
                            }
                        }
                        // When the loop is finished, updates the notification
                        mBuilder.setContentText("Download complete")
                                // Removes the progress bar
                                .setProgress(0,0,false);
                        mNotifyManager.notify(3, mBuilder.build());
                    }
                }
                // Starts the thread by calling the run() method in its Runnable
        ).start();

    }
}
