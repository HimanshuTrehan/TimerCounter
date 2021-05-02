package com.himanshu.timer;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.himanshu.timer.Modal.EventData;
import com.himanshu.timer.Services.YourService;
import com.himanshu.timer.Utils.Prefs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Intent mServiceIntent;
    private YourService mYourService;
    TextView timer;
    Button next,stop;
    String isrunning="0";
    String temp="";
    private EventBus bus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timer = findViewById(R.id.timer);
        next = findViewById(R.id.next);
        stop = findViewById(R.id.stop);
        stop.setOnClickListener(this);
        bus.register(this);
        next.setOnClickListener(this);

   //     timer.setText(count);
        mYourService = new YourService();
        mServiceIntent = new Intent(this, mYourService.getClass());
        if (!isMyServiceRunning(mYourService.getClass())) {
            if(isrunning.equalsIgnoreCase("1")) {
                Log.i("NOT","not running");
            }
            else {
                startService(mServiceIntent);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventData event){
        temp=String.valueOf(event.getData());
        timer.setText(temp);
     //   Toast.makeText(getApplicationContext(),event.getData(),Toast.LENGTH_LONG).show();
    }



//    @Override
//    protected void onDestroy() {
//        //stopService(mServiceIntent);
//        bus.unregister(this);
//        super.onDestroy();
//        String stop = Prefs.getKey(getApplicationContext(),"stop");
//            Intent broadcastIntent = new Intent();
//            broadcastIntent.setAction("restartservice");
//            broadcastIntent.setClass(this, Restarter.class);
//            this.sendBroadcast(broadcastIntent);
//
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.next:
                Intent intent = new Intent(this,ScreenB.class);
                startActivity(intent);
                break;

            case R.id.stop:
                if(isrunning.equalsIgnoreCase("0")) {
                    isrunning = "1";
                    stop.setText("Start");
                    timer.setText(temp);
                    bus.unregister(this);

                }
                else
                {
                    isrunning = "0";
                    stop.setText("Stop");
                    bus.register(this);
                    stopService(mServiceIntent);
                }
        }
    }
}