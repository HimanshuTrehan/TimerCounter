package com.himanshu.timer;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.himanshu.timer.Modal.EventData;
import com.himanshu.timer.Services.YourService;
import com.himanshu.timer.Utils.Prefs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ScreenB extends AppCompatActivity implements View.OnClickListener {
    Intent mServiceIntent;
    private YourService mYourService;
    TextView timer;
    ImageView back;
    Button back_btn,next;
    public int couter=0;
    private EventBus bus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_b);
        timer = findViewById(R.id.timer);
        bus.register(this);
        //     timer.setText(count);
        next = findViewById(R.id.next);
        next.setOnClickListener(this);
        back = findViewById(R.id.back);
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);
        back.setOnClickListener(this);
        mYourService = new YourService();
        mServiceIntent = new Intent(this, mYourService.getClass());
        if (!isMyServiceRunning(mYourService.getClass())) {
            startService(mServiceIntent);
        }
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
        timer.setText(String.valueOf(event.getData()));
        //   Toast.makeText(getApplicationContext(),event.getData(),Toast.LENGTH_LONG).show();
    }



//    @Override
//    protected void onDestroy() {
//        //stopService(mServiceIntent);
//        Intent broadcastIntent = new Intent();
//        broadcastIntent.setAction("restartservice");
//        broadcastIntent.setClass(this, Restarter.class);
//        this.sendBroadcast(broadcastIntent);
//        bus.unregister(this);
//        super.onDestroy();
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back:
            case R.id.back_btn:
                finish();
                break;
            case R.id.next:
                Intent intent1 = new Intent(this,ScreenC.class);
                startActivity(intent1);
        }
    }
}