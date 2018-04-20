package com.example.nguyenthanhxuan.appcaro.data;


import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Nguyen Thanh Xuan on 4/11/2018.
 */

public class ChuyenKetNoiData {
    public static boolean Gui(Boolean isConnect,String strAction,String strData)
    {
        try
        {
            Socket socReceive = new Socket(DataKetNoi.strHost, DataKetNoi.intPort);
            PrintWriter pwOut = new PrintWriter(socReceive.getOutputStream(),true);//ghi file
            pwOut.println(strAction);
            if(isConnect)
                pwOut.println("0");
            pwOut.println(strData);
            socReceive.close();
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
    public static String Nhan(Boolean isConnect)
    {
        try
        {
            Socket socReceive = new Socket(DataKetNoi.strHost,DataKetNoi.intPort);
            PrintWriter pwOut = new PrintWriter(socReceive.getOutputStream(),true);
            pwOut.println("0");
            if(isConnect)
                pwOut.println("1");
            InputStreamReader inStream = new InputStreamReader(socReceive.getInputStream());
            BufferedReader buffReader = new BufferedReader(inStream);//doc file
            String strData = buffReader.readLine();
            Log.d("Kết Quả",strData+"");
            return strData;
        }
        catch(Exception ex)
        {
            Log.d("Lỗi nhận được",ex.toString());
            return null;
        }
    }
}
