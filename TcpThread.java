
//  E number  :     E/17/407
//  Name      :     Wijesooriya H.D

import java.net.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.InetAddress;

// a class to initiate TCP connections
//this is a multithreaded class
public class TcpThread implements Serializable, Runnable{

    // client socket
    private Socket clientSocket=null;
    
    // a variable to store monitor details
    private Monitor connectedMonitor = null;


    //constructor of the class
    public TcpThread(Monitor connectedMonitor){
        this.connectedMonitor = connectedMonitor;
    }

    // run method of the multi thread class
    // a method to initiate TCP connections with the vital monitors
    @Override
    public void run() {

        try {

            // get ip address and the port of the connected monitor
            InetAddress ip=connectedMonitor.getIp();
            int port = connectedMonitor.getPort();

            // open a socket
            clientSocket = new Socket(ip,port);
            
            while(true){

                // receive data from the vital monitors
                BufferedReader inputData = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String message = inputData.readLine();
                System.out.println("Message from vital monitor :-       "+message);
            }

        // error handling
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        
    }
}
