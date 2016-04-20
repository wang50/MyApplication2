package com.example.v_wuwa.myapplication;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    SensorListenerTest sensorListenerTest;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.shuzi);
        sensorListenerTest = new SensorListenerTest(this);
        new DataThread().start();


        final TextView tx1 = (TextView) findViewById(R.id.TextView01);
        SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //从传感器管理器中获得全部的传感器列表
        List<Sensor> allSensors = sm.getSensorList(Sensor.TYPE_ALL);

        //显示有多少个传感器
        tx1.setText("经检测该手机有" + allSensors.size() + "个传感器，他们分别是：\n");

        //显示每个传感器的具体信息
        for (Sensor s : allSensors) {

            String tempString = "\n" + "  设备名称：" + s.getName() + "\n" + "  设备版本：" + s.getVersion() + "\n" + "  供应商："
                    + s.getVendor() + "\n";

            switch (s.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    tx1.setText(tx1.getText().toString() + s.getType() + " 加速度传感器accelerometer" + tempString);
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    tx1.setText(tx1.getText().toString() + s.getType() + " 陀螺仪传感器gyroscope" + tempString);
                    break;
                case Sensor.TYPE_LIGHT:
                    tx1.setText(tx1.getText().toString() + s.getType() + " 环境光线传感器light" + tempString);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    tx1.setText(tx1.getText().toString() + s.getType() + " 电磁场传感器magnetic field" + tempString);
                    break;
                case Sensor.TYPE_ORIENTATION:
                    tx1.setText(tx1.getText().toString() + s.getType() + " 方向传感器orientation" + tempString);
                    break;
                case Sensor.TYPE_PRESSURE:
                    tx1.setText(tx1.getText().toString() + s.getType() + " 压力传感器pressure" + tempString);
                    break;
                case Sensor.TYPE_PROXIMITY:
                    tx1.setText(tx1.getText().toString() + s.getType() + " 距离传感器proximity" + tempString);
                    break;
                case Sensor.TYPE_TEMPERATURE:
                    tx1.setText(tx1.getText().toString() + s.getType() + " 温度传感器temperature" + tempString);
                    break;
                default:
                    tx1.setText(tx1.getText().toString() + s.getType() + " 未知传感器" + tempString);
                    break;
            }
        }


    }

    // 添加OnResume和OnStop，注册和取消Sensor服务。
    @Override
    protected void onResume() {
        // 监听服务，在activity和Listener里面都要注册，这是一个难点。不要只在一个地方注册。
        // TODO Auto-generated method stub
        super.onResume();
        sensorListenerTest.enableSensor();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        sensorListenerTest.disableSensor();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            textView.setText((String)msg.obj);
        };
    };

    private class DataThread extends Thread {
        @Override
        public void run() {

            while(true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }

                String data = null;
                try {
                    data = sensorListenerTest.res();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // 只能在主线程中修改ui控件
                mHandler.sendMessage(mHandler.obtainMessage(0, data));
            }
        }
    }
}


