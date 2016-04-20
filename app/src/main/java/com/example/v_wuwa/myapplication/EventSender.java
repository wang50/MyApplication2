package com.example.v_wuwa.myapplication;

import android.util.Log;

import java.io.IOException;
import java.nio.charset.*;
import java.util.*;
import java.util.concurrent.ExecutionException;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.*;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by v-wuwa on 4/12/2016.
 */
public class EventSender {
    //sastoken C#程序生成  https://msdn.microsoft.com/en-us/library/mt652140.aspx
    //rest api 文档：https://msdn.microsoft.com/en-us/library/dn790664.aspx
    static String sastoken = "SharedAccessSignature sr=wanderhub-ns.servicebus.chinacloudapi.cn&sig=UJyDmc4eTAw3NKbTrsjk13UqM0h5MRQSZNgd31tkL60%3d&se=1492073270&skn=ReceiveRule";
    static String host = "wanderhub-ns.servicebus.chinacloudapi.cn";
    static String huburl = "https://wanderhub-ns.servicebus.chinacloudapi.cn/wanderhub/messages?timeout=60&api-version=2014-01";

    public static String PostEventHub(String msg) throws IOException{
         return PostMessage(huburl,null,msg);
    }

    public static String PostMessage(String urlStr,Map urlParam,String msg) throws IOException{

        if(urlParam!=null && !urlParam.isEmpty()){
            String urlParamStr = "?";
            Iterator ups = urlParam.entrySet().iterator();
            while(ups.hasNext()){
                Map.Entry MUPS = (Map.Entry)ups.next();
                urlParamStr += MUPS.getKey() + "=" + MUPS.getValue().toString().trim() + "&";
            }
            urlParamStr = urlParamStr.substring(0, urlParamStr.length() - 1);
            urlStr = urlStr + urlParamStr;
        }

        URL url = new URL(urlStr);

        HttpURLConnection  connet = (HttpURLConnection) url.openConnection();

        connet.setRequestMethod("POST");
        connet.setRequestProperty("Authorization", sastoken);
        connet.setRequestProperty("Content-Type", "application/atom+xml;type=entry;charset=utf-8");
        connet.setRequestProperty("Host", host);
        connet.setRequestProperty("Alert", "Strong Wind");
        connet.setDoOutput(true);
        connet.setDoInput(true);
        connet.setUseCaches(false);
        connet.connect();

        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        out = new PrintWriter(connet.getOutputStream());
        out.print(msg);
        out.flush();

        in = new BufferedReader(
                new InputStreamReader(connet.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
            result += line;
        }

        out.close();
        in.close();

        return result;
    }
}
