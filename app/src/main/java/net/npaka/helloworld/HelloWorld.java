package net.npaka.helloworld;//(1)

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.EditText;
import android.database.Cursor;
import android.os.IBinder;



//HelloWorld
public class HelloWorld extends Activity implements AdapterView.OnItemClickListener {//(2)(3)
    private final static int REQUEST_TEXT=0;//テキストID
    private TextView textView;//テキストビュー
    //メニューアイテムID
    private static final int 
        MENU_ITEM0=0,
        MENU_ITEM1=1;
    
    private String items[];
    private int position_list = -1;
    private int position_max = -1;
    private SQLiteDatabase db;      //データベースオブジェクト
    private Intent     serviceIntent;
    private IMyService1 binder;
    
    @Override
    public void onCreate(Bundle bundle) {//(4)
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//(5)

        //データベースオブジェクトの取得(5)
        DBHelper dbHelper=new   DBHelper(this);
        db=dbHelper.getWritableDatabase();
        
        //要素の情報群の取得
        Cursor c=db.query(DBHelper.DB_TABLE,new String[]{"id","info"},
        		null,null,null,null,null);
            //if (c.getCount()==0) throw new Exception();
            c.moveToFirst();
            
            position_max = c.getCount();
            String tempStr[] = new String[position_max]; 
            for(int i=0; i<position_max;i++){
            	tempStr[i] = new String();
            	tempStr[i] =c.getString(1);
            	c.moveToNext();
            };

         c.close();
            
        //リストビューの生成
         items = tempStr;
         ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_list_item_1,items);      

         //リストビューの生成
         ListView listView=new ListView(this);
         listView.setScrollingCacheEnabled(false);
         listView.setBackgroundColor(Color.BLACK);
         listView.setAdapter(aa);
         //setListAdapter(aa);
         listView.setOnItemClickListener(this);
         //listView.setItemsCanFocus(false);
         //listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
         
         LinearLayout layout = (LinearLayout)getLayoutInflater().inflate(R.layout.main,
        		 null);
         
         layout.addView(listView);
         setContentView(layout);
         textView = (TextView)findViewById(R.id.textView);
         
         //サービスボタンの作成及びサービスインテントの生成
         setServiceUI(true);
         serviceIntent=new Intent(this,MyService1.class);
         
         //サービスとの接続(2)
         if (isServiceRunning("net.npaka.helloworld.MyService1")) {
             //bindService(serviceIntent,connection,BIND_AUTO_CREATE);
             setServiceUI(false);
         }

    }
    
    @Override
    public void onPause(){
    	super.onPause();
        if (isServiceRunning("net.npaka.helloworld.MyService1")) {
        	unbindService(connection);
        }
    }

    @Override
    public void onResume(){
    	super.onResume();
        if (isServiceRunning("net.npaka.helloworld.MyService1")) {
        	bindService(serviceIntent,connection,BIND_AUTO_CREATE);
        }
    }
    
    //ボタンクリック時に呼ばれる
    public void onClick1(View v) {
        //setContentView(R.layout.helloview);
        //EditText edittext = (EditText)findViewById(R.id.editText1);
        //edittext.setText(textView.getText().toString());
    	position_max += 1;
    	position_list = position_max-1;

        Intent intent=new Intent(this,
            net.npaka.helloworld.MyActivity.class);
            
        //インテントへのパラメータ指定(2)
        intent.putExtra("text","");
            
        //アクティビティの呼び出し(3)
        startActivityForResult(intent,REQUEST_TEXT);

    }
    
    public void onClick2(View v) {
    	EditText edittext = (EditText)findViewById(R.id.editText1);
        String str = edittext.getText().toString();
        setContentView(R.layout.main);
        
        textView = (TextView)findViewById(R.id.textView);
        textView.setText(str);
    }

    public void onClick3(View v) {
    	if( position_list == -1 ){
    		showDialog(this,"","リスト項目を選択してください");
    		return;
    	}
        //アプリ内のアクティビティを呼び出すインテントの生成(1)
        Intent intent=new Intent(this,
            net.npaka.helloworld.MyActivity.class);
            
        //インテントへのパラメータ指定(2)
        intent.putExtra("text",textView.getText().toString());
            
        //アクティビティの呼び出し(3)
        startActivityForResult(intent,REQUEST_TEXT);

    }
    
    
    //アクティビティ呼び出し結果の取得(4)
    @Override
    protected void onActivityResult(int requestCode,
        int resultCode,Intent intent) {
        if (requestCode==REQUEST_TEXT && resultCode==RESULT_OK) {
            //インテントからのパラメータ取得
            String text="";
            Bundle extras=intent.getExtras();
            if (extras!=null) text=extras.getString("text");
            
            try {
				this.writeDB(text,new String(""+position_list));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            String tempStr[] = new String[position_max]; 
            if(position_list == position_max-1){
	            for(int i=0; i<position_max-1;i++){
	            	tempStr[i] = new String();
	            	tempStr[i] = items[i];
	            };
            	tempStr[position_max-1] = new String(text);
            	items = tempStr;
            } else {
            	items[position_list] = new String(text);
            }
            //リストビューの生成
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_list_item_1,items);      

            //リストビューの生成
            ListView listView=new ListView(this);
            listView.setScrollingCacheEnabled(false);
            listView.setBackgroundColor(Color.BLACK);
            listView.setAdapter(aa);
            //setListAdapter(aa);
            listView.setOnItemClickListener(this);
            
            LinearLayout layout = (LinearLayout)getLayoutInflater().inflate(R.layout.main,
           		 null);
            
            layout.addView(listView);
            setContentView(layout);
            textView = (TextView)findViewById(R.id.textView);
            textView.setText(items[position_list]);
            
            //サービスへ値を渡す
            if (isServiceRunning("net.npaka.helloworld.MyService1")) {
	            try {
	            	binder.setMessage(textView.getText().toString());
	            } catch (Exception e) {
	    			e.printStackTrace();
	            }
            }
        }
        
    }    

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //オプションメニューへのアイテム0の追加
        MenuItem item0=menu.add(0,MENU_ITEM0,0,"追加");
        item0.setIcon(android.R.drawable.ic_menu_camera);
        
        //オプションメニューへのアイテム1の追加
        MenuItem item1=menu.add(0,MENU_ITEM1,0,"修正");
        item1.setIcon(android.R.drawable.ic_menu_call);

        return true;
    }
    
    
    //メニューアイテム選択イベントの処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM0:
            	position_max += 1;
            	position_list = position_max-1;

                Intent intent=new Intent(this,
                    net.npaka.helloworld.MyActivity.class);
                    
                //インテントへのパラメータ指定(2)
                intent.putExtra("text","");
                    
                //アクティビティの呼び出し(3)
                startActivityForResult(intent,REQUEST_TEXT);

                return true;
            case MENU_ITEM1:
            	if( position_list == -1 ){
            		showDialog(this,"","リスト項目を選択してください");
            		return true;
            	}
                //アプリ内のアクティビティを呼び出すインテントの生成(1)
                intent=new Intent(this,
                    net.npaka.helloworld.MyActivity.class);
                    
                //インテントへのパラメータ指定(2)
                intent.putExtra("text",textView.getText().toString());
                    
                //アクティビティの呼び出し(3)
                startActivityForResult(intent,REQUEST_TEXT);

                return true;
        }
        return true;
    }    
    
    //ダイアログの表示
    private static void showDialog(Context context,String title,String text) {
        AlertDialog.Builder ad=new AlertDialog.Builder(context);
        ad.setTitle(title);
        ad.setMessage(text);
        ad.setPositiveButton("OK",null);
        ad.show();
    }

    //データベースへの書き込み(6)
    private void writeDB(String info,String id) throws Exception {
        ContentValues values=new ContentValues();
        values.put("id",id);
        values.put("info",info);
        int colNum=db.update(DBHelper.DB_TABLE,values,null,null);
        if (colNum==0) db.insert(DBHelper.DB_TABLE,"",values);
    }

    //データベースからの読み込み(7)
    private String readDB() throws Exception {
        Cursor c=db.query(DBHelper.DB_TABLE,new String[]{"id","info"},
            "id='0'",null,null,null,null);
        if (c.getCount()==0) throw new Exception();
        c.moveToFirst();
        String str=c.getString(1);
        c.close();
        return str;
    }    
     
    public void onItemClick(AdapterView l, View v, int position, long id) {
        textView.setText(items[position]);
        position_list  = position;
        //サービスへ値を渡す
        if (isServiceRunning("net.npaka.helloworld.MyService1")) {
            try {
            	binder.setMessage(textView.getText().toString());
            } catch (Exception e) {
    			e.printStackTrace();
            }
        }
    }

    public void onClick4(View v) {
        setServiceUI(false);
        //サービスの開始(1)
        serviceIntent=new Intent(this,MyService1.class);
        
        //インテントへのパラメータ指定(2)
        serviceIntent.putExtra("text",textView.getText().toString());
        startService(serviceIntent);
        //サービスとの接続(2)
        bindService(serviceIntent,connection,BIND_AUTO_CREATE);

    }
    
    public void onClick5(View v) {
        setServiceUI(true);
        //サービスとの切断(4)
        unbindService(connection);
        //サービスの停止(5)
        //serviceIntent=new Intent(this,MyService1.class);
        stopService(serviceIntent);
    }
    
    //サービスが起動中かどうかを調べる(9)
    private boolean isServiceRunning(String className) {
        ActivityManager am=(ActivityManager)getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfos=
            am.getRunningServices(Integer.MAX_VALUE);
        for (int i=0;i<serviceInfos.size();i++) {
            if (serviceInfos.get(i).service.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }
    
    //サービスコネクションの生成(3)
    private ServiceConnection connection=new ServiceConnection() {
        //サービス接続時に呼ばれる
        public void onServiceConnected(ComponentName name,IBinder service) {
            binder=IMyService1.Stub.asInterface(service);
        }
        
        //サービス切断時に呼ばれる
        public void onServiceDisconnected(ComponentName name) {
            binder=null;
        }
    };
    
    //サービス操作の指定
    private void setServiceUI(boolean startable) {
    	Button button_id3 = (Button)findViewById(R.id.button3);
    	button_id3.setEnabled(startable);
    	Button button_id4 = (Button)findViewById(R.id.button4);
    	button_id4.setEnabled(!startable);
    }
}