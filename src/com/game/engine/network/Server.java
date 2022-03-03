package com.game.engine.network;

import java.io.*;
import java.net.*;

public class Server  {
    private final static int SERVICE_PORT = 47261;
  

    
    public static void main(String[] args) throws IOException {
       
        try {
            DatagramSocket server = new DatagramSocket(SERVICE_PORT);

            byte[] receivingBuffer = new byte[1024];
            byte[] sendingBuffer = new byte[1024];
            for (int i = 0; i < 10000; i++) {
            DatagramPacket inputPacket = new DatagramPacket(receivingBuffer, receivingBuffer.length);
            

                // this part goes in run later
                server.receive(inputPacket);

                // Casts the received packet into a string
                String receivedData = new String(inputPacket.getData());
                System.out.println("Sent from the client: " + receivedData);

                sendingBuffer = (receivedData.toUpperCase().trim() + "|| Received by sam").getBytes();

                // Client address and port
                InetAddress senderAddress = inputPacket.getAddress();
                int senderPort = inputPacket.getPort();

                // Outpacket goes to client
                DatagramPacket outputPacket = new DatagramPacket(sendingBuffer, sendingBuffer.length, senderAddress,
                        senderPort);

                server.send(outputPacket);
                receivingBuffer = new byte[1024];
                sendingBuffer = new byte[1024];
                
            }

            server.close();

        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
    
}
