package com.ycw.nevercloseapp.nevercloseapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Random;

/**
 * Created by dell on 2016/6/24.
 */
public class MyService extends Service{
    private final static String TAG = "MyService";
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    private final class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
            super(looper);
        }


        @Override
        public void handleMessage(Message msg) {
            long endTime = System.currentTimeMillis()+5*1000;
            while(System.currentTimeMillis()<endTime){
                synchronized (this){
                    try{
                        wait(endTime - System.currentTimeMillis());
                    }catch (Exception e){

                    }
                }
            }
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        Log.e(TAG,"onCreate()");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        MyService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MyService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind()");
        return new LocalBinder();
    }


    @Override
    public void onDestroy() {
        Log.e(TAG,"onDestroy()");
        super.onDestroy();
    }

    // Random number generator
    private final Random mGenerator = new Random();

    /** method for clients */
    public int getRandomNumber() {
        return mGenerator.nextInt(100);
    }


     public void startForegrround(){
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
        startForeground(2, mBuilder.mNotification);
    }

}
