package com.example.liunan.sharedmemoryclient;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SharedMemory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.system.ErrnoException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.liunan.sharedmemoyserver.IMyMemoryService;

import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private IMyMemoryService binder;
    private SharedMemory memory;
    private int count;
    private ByteBuffer buffer;
    private Button bt_1;


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            binder = IMyMemoryService.Stub.asInterface(service);
            try {
                memory = binder.getMemory();
                buffer = memory.mapReadOnly();
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            } catch (ErrnoException e) {
                Log.e(TAG, e.toString());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
            memory.close();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent("com.example.liunan.sharedmemoyserver.shared_memory");
        intent.setComponent(new ComponentName("com.example.liunan.sharedmemoyserver", "com.example.liunan.sharedmemoyserver.MySharedMemoryService"));
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

        bt_1 = findViewById(R.id.button);
        bt_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    binder.writeByte(0, ((byte) count++));
                    bt_1.setText("增加到" + buffer.get(0));
                } catch (RemoteException e) {
                    Log.e(TAG, e.toString());
                }
            }
        });

    }
}
