/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpchat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TCPChat {
    private ServerSocket serverSocket;
    private  InputStream in;
    private OutputStream out;
     
    public TCPChat(int port) throws UnknownHostException, IOException{
           serverSocket= new ServerSocket(port);
    }
    
    private void waitConnection()
    {
        Scanner scan = new Scanner(System.in);
        String mess;
        
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                Socket socket1 = serverSocket.accept();
            
                SeverThread severThread= new SeverThread(socket,socket1);
                severThread.start();
                System.out.println("Server socket successful");
                GameBoard gameBoard= new GameBoard(8,8);
                
                severThread.send1("1");
                severThread.send("0");
               // mess=severThread.nhan();
                 //System.out.println(mess);
               
               while(true){
                mess=severThread.nhan();
               if (gameBoard.check(mess, 0)!="false"){
                   severThread.send1(mess);
                   severThread.send(mess);
                   
               }
               else {
                   severThread.send("false");
                   continue;
               }
                while (true) {                             
                mess=severThread.nhan1();
                if (gameBoard.check(mess, 1)!="false"){
                   severThread.send(mess);
                   severThread.send1(mess);
                   break;
               }
               else {
                   severThread.send1("false");
                   continue;
                   
               }
                    }
             
                }
            } catch (Exception e) {
                
            }
        }
    }    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
       
        try {
            TCPChat tCPChat= new TCPChat(1234);
            tCPChat.waitConnection();
            
          //  Scanner scan = new Scanner(System.in);
           
        } catch (Exception e) {
            System.out.println("Can't create server socket");
        }
    }
    
}
