package net.npaka.helloworld;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

//サービスの定義(6)
public class MyService1 extends Service {
    private Handler handler=new Handler();
    private boolean running=false;
    private String  message1="Hello World, HelloWorld!";

    //サービス生成時に呼ばれる
    @Override
    public void onCreate() {
        super.onCreate();
    }
    
    //サービス開始時に呼ばれる
    @Override
    public void onStart(Intent intent,int startID) {
        super.onStart(intent,startID);
        
        //ノティフィケーションの表示
        showNotification(getApplicationContext(),R.drawable.ic_launcher,
            "HelloWoroldサービスを開始しました",
            "HelloWoroldサービス",
            "HelloWoroldサービスを操作します");
        
        //インテントからのパラメータ取得(5)
        Bundle extras=intent.getExtras();
        if (extras!=null) message1=extras.getString("text");

        //サービスの開始
        Thread thread=new Thread(){
            public void run() {
                running=true;
                while (running) {
                    handler.post(new Runnable(){
                        public void run() {
                            toast(MyService1.this,message1);
                        }
                    });
                    try {
                        Thread.sleep(5000);
                    } catch (Exception e) {
                    	e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }
    
    //サービス停止時に呼ばれる
    @Override
    public void onDestroy() {
        running=false;
        super.onDestroy();
    }
    
    //サービス接続時に呼ばれる
    @Override
    public IBinder onBind(Intent intent) {
        return IMyServiceBinder;
    }
    
    //ノティフィケーションの表示(8)
    private static void showNotification(Context context,
        int iconID,String ticker,String title,String message) {
        //ノティフィケーションオブジェクトの生成
        Notification notification=new Notification(iconID,
            ticker,System.currentTimeMillis());
        PendingIntent intent=PendingIntent.getActivity(context,0,
            new Intent(context,HelloWorld.class),0);
        notification.setLatestEventInfo(context,title,message,intent);
        
        //ノティフィケーションマネージャの取得
        NotificationManager nm=(NotificationManager)
            context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        //ノティフィケーションのキャンセル
        nm.cancel(0);
        
        //ノティフィケーションの表示
        nm.notify(0,notification);
    }

    //トーストの表示　
    private static void toast(Context context,String text) {
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }
    
    //バインダの生成(7)
    private final IMyService1.Stub IMyServiceBinder=new IMyService1.Stub() {
        public void setMessage(String msg) throws RemoteException {
            message1=msg;
        }
    };
}