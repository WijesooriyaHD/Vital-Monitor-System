

//  E number  :     E/17/407
//  Name      :     Wijesooriya H.D

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.net.DatagramSocket;
import java.io.*;


// gateway class
// this discovers the vital monitors and receives data from them
public class Gateway{

    // initialize a port to handle broadcast messages from the vital monitors
    private final int SERVICE_PORT=6000;

    // a method to initiate UDP and TCP connections with vital monitors
    public void gatewayConnection() throws IOException{


      // a variable to establish UDP connections
        DatagramSocket datagramSocket=null;

      // a variable to store the monitor data
        Monitor receivedMonitor=null;

     // an array list to keep track of the connected monitors
        List <String> monitorIds=new ArrayList<>();


        try{


            // dealing with UDP broadcast messages
            //inisiate a new DatagramSocket to receive infromation from the client
            datagramSocket = new DatagramSocket(SERVICE_PORT);
            
            // Creating a buffer to receiving data.
            byte[] receivingDataBuffer = new byte[65535];

            DatagramPacket inputPacket=null;

            System.out.println("Waiting for connections .......");
            System.out.println(" ");

            while(true){
               
                /* initiate a UDP packet to store the 
                client data using the buffer for receiving data*/
                inputPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
                
                // receive data from the client and store in inputPacket
                datagramSocket.receive(inputPacket);

                //System.out.println("client connected (UDP - broadcast)");
                

                // convert byte type data to monitor
                receivedMonitor=convertByteDataToMonitor(inputPacket.getData());

                //System.out.println("Sent from the client : "+receivedMonitor.monitor_str());


                // add  monitor id to the list
                String monitor_id=receivedMonitor.getMonitorID();
                monitorIds.add(monitor_id);
                
                // initiate TCP connections with the newly connected vital monitors
                if(monitorIds.size()==1 || !(monitorIds.contains(monitor_id))){
                    // a thread to initiate tcp connections
                    TcpThread tcpConnection=new TcpThread(receivedMonitor);
                    Thread mytThread= new Thread(tcpConnection);
                    mytThread.start();

                    System.out.println("a new vital monitor connected :-    "+receivedMonitor.monitor_str());
                }


            }


        // exception handling
        }catch (IOException e){
            e.printStackTrace();

        }

    }

    //-------------------------------------------------------------------------//
    

    // a method to create a monitor object using received data from
    // the udp connection
    private static Monitor convertByteDataToMonitor(byte[] data){
    
        // initialize a variable
        Monitor monitor = null;

        ByteArrayInputStream byteData = new ByteArrayInputStream(data);

        // convert byte type data to monitor type
        try{
            ObjectInputStream objectData = new ObjectInputStream(byteData);
            monitor =  (Monitor)objectData.readObject(); 
     
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
      
        return monitor;
    }


    //--------------------------------------------------------------------------//
    

    // main method
    public static void main(String args[]){

        // creating an object from the Gateway class
        Gateway myGatewayServer=new Gateway();

        try {
            // invoke the method to initiate udp & tcp connections
            myGatewayServer.gatewayConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //--------------------------------------------------------------------------//
 
    
}
