package com.game.engine.network;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public final static int SERVICE_PORT = 47261;

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);

        try {
            DatagramSocket client = new DatagramSocket();

            // This gets the ip address. Probably use a room code by casting to another base
            InetAddress IPAddress = InetAddress.getByName("localhost");

            System.out.println(InetAddress.getLocalHost());
            byte[] sendingBuffer = new byte[1024];
            byte[] receivingBuffer = new byte[1024];
            for (int i = 0; i < 10; i++) {

                String sentence = in.nextLine();
                sendingBuffer = sentence.getBytes();
                
                DatagramPacket sendingPacket = new DatagramPacket(sendingBuffer, sendingBuffer.length, IPAddress,
                        SERVICE_PORT);
                client.send(sendingPacket);
                System.out.println(new String(new DatagramPacket(sendingBuffer, sendingBuffer.length).getData()));
                long start = System.currentTimeMillis();

                // Receive confirmation
                DatagramPacket receivingPacket = new DatagramPacket(receivingBuffer, receivingBuffer.length);
                client.receive(receivingPacket);
                long end = System.currentTimeMillis();

                String receivedData = new String(receivingPacket.getData()).trim();
                System.out.println("Sent from the server: " + receivedData);
                System.out.println("Received in: " + (end - start) + "ms");
                receivingBuffer = new byte[1024];
                sendingBuffer = new byte[1024];
            }
            client.close();
            in.close();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
