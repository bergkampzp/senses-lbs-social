package com.example.lokal;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;

public class Login2 extends Activity {
    TextView textView;
    MyHandler myHandler;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.handlertest);

        //实现创建handler并与looper绑定。这里没有涉及looper与
          //线程的关联是因为主线程在创建之初就已有looper
        myHandler=new MyHandler(Login2.this.getMainLooper());
        //textView = (textView) findViewById(R.id.textView);
       
        MyThread m = new MyThread();
        new Thread(m).start();
    }


    class MyHandler extends Handler {
        public MyHandler() {
        }

        public MyHandler(Looper L) {
            super(L);
        }

        // 必须重写这个方法，用于处理message
        @Override
        public void handleMessage(Message msg) {
            // 这里用于更新UI
            Bundle b = msg.getData();
            String color = b.getString("color");
            Login2.this.textView.setText(color);
        }
    }

    class MyThread implements Runnable {
        public void run() {
            //从消息池中取出一个message
            Message msg = myHandler.obtainMessage();
            //Bundle是message中的数据
            Bundle b = new Bundle();
            b.putString("color", "我的");
            msg.setData(b);
            //传递数据
            myHandler.sendMessage(msg); // 向Handler发送消息,更新UI
        }
    }
}