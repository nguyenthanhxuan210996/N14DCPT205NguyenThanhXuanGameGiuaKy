/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient extends Thread {
    private InputStream in;
    private OutputStream out;
    private Socket socket;
    
   public TCPClient(String severAddress, int severPort) throws IOException{
      socket= new Socket(severAddress, severPort);
      in = socket.getInputStream();
      out = socket.getOutputStream();
      
   }
   //gui tin nhan
   public void send(String mess){
       try {
           out.write(mess.getBytes());
       } catch (IOException e) {
           System.out.println("Can't send");
       }
   }
    public static void main(String[] args) {
        TCPClient client;
        try {
            client= new  TCPClient("192.168.43.204", 1234);
            Scanner scan = new Scanner(System.in);
            client.start();
            
          while(true){
                String mess = scan.nextLine();
                client.send(mess);
            }
        } catch (Exception e) {
        }
        
    }
}
