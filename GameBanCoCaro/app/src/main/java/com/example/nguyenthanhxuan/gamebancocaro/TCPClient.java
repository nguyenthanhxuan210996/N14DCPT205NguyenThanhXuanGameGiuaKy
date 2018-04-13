package com.example.nguyenthanhxuan.gamebancocaro;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Nguyen Thanh Xuan on 4/12/2018.
 */

public class TCPClient extends Thread {
    private InputStream in;//luông đầu vào
    private OutputStream out;//luồng ghi ra màn hình
    private Socket socket;//biểu diễn kết nối giữa server và client
    private String mess;

    public TCPClient (String serverAddress, int serverPort) throws IOException{
        socket=new Socket(serverAddress,serverPort);
        in=socket.getInputStream();
        out=socket.getOutputStream();
    }
    public String getMess(){
        return mess;
    }
    public void setMess(){
        this.mess=mess;
    }
    //gui tin nhan
    public void send(String mess){
        try{
            out.write(mess.getBytes());
        } catch (IOException e){
            System.out.println("abc");
        }
    }

    @Override
    public void run() {
        byte[] buff=new byte[1024];
        try{
            while (true){
                int receivedBytes=in.read(buff);
                if(receivedBytes<1) break;
                mess=new String(buff,0,receivedBytes);
            }
        }catch (IOException e){
            System.out.println("abc");
        }
        super.run();
    }
}
