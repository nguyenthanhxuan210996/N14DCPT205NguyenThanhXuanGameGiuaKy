/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpchat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import javax.xml.ws.Service;

/**
 *
 * @author SONY
 */
public class SeverThread extends Thread{
  final  private PlayerSocket player;
 final private PlayerSocket player1;
   
    
    public SeverThread(Socket socket, Socket socket1) throws IOException{
       player= new PlayerSocket(socket);
       player1= new PlayerSocket(socket1);
      
       
    }
    //gui tin nhan
      void send(String mess){
       try {
           player.getOut().write(mess.getBytes());
           
  
       } catch (IOException e) {
           System.out.println("Can't send");
       }
       }
      
       void send1(String mess){
       try {
           player1.getOut().write(mess.getBytes());
           
  
       } catch (IOException e) {
           System.out.println("Can't send");
       }
       }
       
       public String nhan(){
           String mess;
           byte[] buff = new byte[2048];
        try {
            while (true) {
            int receivedBytes = player.getIn().read(buff);
            if(receivedBytes<1) break;
                 mess = new String(buff,0,receivedBytes);
              return mess;
            }
        } catch (Exception e) {
            System.out.println("Can't read");
        }
       return null;
       }
       public String nhan1(){
           String mess;
           byte[] buff = new byte[2048];
        try {
            while (true) {
            int receivedBytes = player1.getIn().read(buff);
            if(receivedBytes<1) break;
                 mess = new String(buff,0,receivedBytes);
              return mess;
            }
        } catch (Exception e) {
            System.out.println("Can't read");
        }
       return null;
}
    //nhan
    @Override
    public void run(){
        int index;
        int rowIndex;
        int colIndex;
        byte[] buff = new byte[2048];
        try {
            while (true) {
            int receivedBytes = player.getIn().read(buff);
            if(receivedBytes<1) break;
                String mess = new String (buff,0,receivedBytes);
                System.out.println(mess);
              
            }
        } catch (Exception e) {
            System.out.println("Can't read");
        }
    }
    
    
}
