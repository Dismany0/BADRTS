package com.game.engine.network;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;
import java.net.*;

public class ClientFull implements Runnable {
    int time;
    ExecutorService executor = Executors.newFixedThreadPool(2);
    DatagramSocket client;
    InetAddress IPAddress;
    Runnable worker;
    Thread thread;
    Boolean running = false;

    ClientReceive clientR;
    ClientSend clientS;

    private final double UPDATE_CAP = 1.0 / 60.0;
    public static void main(String args[]) throws SocketException, UnknownHostException {
        ClientFull client = new ClientFull();
        client.start();
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    public ClientFull() throws SocketException, UnknownHostException {
        time = 0;
        client = new DatagramSocket();
        IPAddress = InetAddress.getByName("localhost");
    }

    public void run() {
        double time1 = 0;
        double time2 = System.nanoTime() / 1e9; 
        double passedTime = 0;
        //unprocessed time
        double processedTime = 0;

        //Keep track of frames per second
        double frameTime = 0;
        int frames = 0;
        int fps = 0;

        running = true;
        while (running) {
            time1 = System.nanoTime() / 1e9;
            passedTime = time1 - time2;
            time2 = time1;

            processedTime += passedTime;

            frameTime += passedTime;

            while (processedTime >= UPDATE_CAP) {
                processedTime -= UPDATE_CAP;

                frames++;
                if(frameTime >= 1.0){
                    frameTime = 0;
                    fps = frames;
                    frames = 0;
                    System.out.println("FPS is " + fps);
                }
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            try {
                worker = new ClientSend(time, client, IPAddress);
                executor.execute(worker);

                worker = new ClientReceive(time, client, IPAddress);
                executor.execute(worker);
                time++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

class ClientReceive extends Thread {
    public final static int SERVICE_PORT = 47261;
    int time;
    DatagramSocket client;
    InetAddress IPAddress;

    public ClientReceive(int time, DatagramSocket client, InetAddress IPAddress) throws IOException {
        this.time = time;
        this.client = client;
        this.IPAddress = IPAddress;

    }

    @Override
    public void run() {
        try {
            byte[] receivingBuffer = new byte[1024];
            DatagramPacket receivingPacket = new DatagramPacket(receivingBuffer, receivingBuffer.length);
            client.receive(receivingPacket);

            String receivedData = new String(receivingPacket.getData()).trim();
            System.out.println(receivedData + " || received on frame " + time);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.stop();
    }
}

class ClientSend extends Thread {
    public final static int SERVICE_PORT = 47261;
    int time;
    DatagramSocket client;
    InetAddress IPAddress;

    public ClientSend(int time, DatagramSocket client, InetAddress IPAddress) throws IOException {
        this.time = time;
        this.client = client;
        this.IPAddress = IPAddress;

    }

    public void run() {
        try {
            byte[] sendingBuffer = new byte[1024];

            String sentence = "Frame " + time;
            sendingBuffer = sentence.getBytes();

            DatagramPacket sendingPacket = new DatagramPacket(sendingBuffer, sendingBuffer.length, IPAddress,
                    SERVICE_PORT);
            client.send(sendingPacket);
            System.out.println("Sending " + time + "th frame");
            sendingBuffer = new byte[1024];
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.stop();
    }
}