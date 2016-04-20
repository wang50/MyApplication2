package com.example.v_wuwa.myapplication;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by v-wuwa on 4/8/2016.
 */
public class SensorListenerTest implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor sensor;
    private Context mContext;

    private ConcurrentHashMap monitor =  new ConcurrentHashMap<String,JSONObject>();;

    public SensorListenerTest(Context context) {
        mContext = context;
        // 在构造函数里面注册Sensor服务
        enableSensor();
    }

    // 注册传感器服务，在本java和Activity里面都要注册，但是取消注册的时候，只在activity里面取消注册即可。
    public void enableSensor() {
        // 在这里真正注册Service服务
        mSensorManager = (SensorManager) mContext
                .getSystemService(Context.SENSOR_SERVICE);
        sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (mSensorManager == null) {
            Log.v("sensor..", "Sensors not supported");
        }

        mSensorManager.registerListener(this, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);

    }

    // 取消注册传感器服务
    public void disableSensor() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
            mSensorManager = null;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        if (event.sensor == null) {
            return;
        }
        JSONObject jsonObj = new JSONObject();
        //加速度
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float mLastX = event.values[0];
            float mLastY = event.values[1];
            float mLastZ = event.values[2];

            Log.v("mLastX==", String.valueOf(mLastX));
            Log.v("mLastY==", String.valueOf(mLastY));
            Log.v("mLastZ==", String.valueOf(mLastY));

            try {
                jsonObj.put("x_acc",mLastX);
                jsonObj.put("y_acc",mLastX);
                jsonObj.put("z_acc",mLastX);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            monitor.put("accelerometer",jsonObj);
        }
        //电磁场
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            float mg = event.values[0];

            try {
                jsonObj.put("magnetic",mg);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            monitor.put("magnetic",jsonObj);
        }
        //方向
        else if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float o1 = event.values[0];
            float o2 = event.values[1];
            float o3 = event.values[2];

            try {
                jsonObj.put("azimuth",o1);
                jsonObj.put("pitch",o2);
                jsonObj.put("roll",o3);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            monitor.put("orientation",jsonObj);
        }
        //温度
        else if (event.sensor.getType() == Sensor.TYPE_TEMPERATURE) {
            float o1 = event.values[0];

            try {
                jsonObj.put("temperature",o1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            monitor.put("temperature",jsonObj);
        }
        //距离
        else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {

        }
        //环境光线
        else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float o1 = event.values[0];

            try {
                jsonObj.put("light",o1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            monitor.put("light",jsonObj);
        }
        //压力
        else if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            float yali = event.values[0];

            try {
                jsonObj.put("pressure",yali);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            monitor.put("pressure",jsonObj);
        }
        //OTHERS
        else{
            float s = event.values[0];
            System.out.println(s);
        }

        try{
            Log.e("eee",jsonObj.toString());
            EventSender.PostEventHub(jsonObj.toString());
        }catch ( Exception e){
        }

    }


    public String res() throws JSONException {
        String res="";
        Iterator iter = monitor.entrySet().iterator();
        while (iter.hasNext()){
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            JSONObject obj = (JSONObject)entry.getValue();
            res = res + key + ":\n";
            Iterator it = obj.keys();
            while (it.hasNext()){
                String name = (String) it.next();
                String value = obj.getString(key);
                res = res+name+": "+value+"\t";
            }
            res = res+"\n";
        }
        res = monitor.toString();
        Log.e("e",res);
        return res;

    }
    private int index = 0;
    public String test(){
        //return String.valueOf(++index);
        return monitor.toString();
    }
}
