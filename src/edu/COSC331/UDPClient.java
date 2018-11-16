package edu.COSC331;

import java.io.IOException;
import java.net.*;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Class which allows clients to connect to connect to a server
 * and exchange UDP-Base messages.
 * @author crcrowe0, emjetton0
 */
public class UDPClient {
    static DatagramSocket socket;
    static InetAddress addr;
    String IP;
    boolean firstTime;

    /**
     * Core method which runs the client.
     * @param args - none
     * @throws IOException - Input stream may not be found
     */
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.println("What host to connect to?");
        String hostName = in.nextLine();
        System.out.println("What port to connect to? (4000)");
        int port = in.nextInt();
        System.out.println("Port to use?");
        int localPort = in.nextInt();
        System.out.println("Username?");
        Scanner newer = new Scanner(System.in);
        String username = newer.nextLine();

        addr = InetAddress.getByName(hostName);
        socket = new DatagramSocket(localPort);

        Listener sender = new Listener(socket);
        sender.start();


        while(true){
            Scanner read = new Scanner(System.in);
            String line = username;
            line+=": "+read.nextLine();
            byte[] buffer = new byte[1024];
            buffer = line.getBytes();
            DatagramPacket s = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(hostName), port);

            socket.send(s);
        }
    }

}

/**
 * Listener class which is multithreaded. Runs in background
 * displaying received messages from server.
 */
class Listener extends Thread {
    DatagramSocket socket;

    /**
     * Constructor for a Listener.
     * @param socket2 - socket which to use
     */
    public Listener(DatagramSocket socket2){
        socket = socket2;
    }

    /**
     * The thread which receives packets
     */
    public void run() {
        while(true){
            byte[] buffer = new byte[1024];
            DatagramPacket p = new DatagramPacket(buffer, buffer.length);

            try {
                socket.receive(p);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String line = new String(p.getData(), 0, p.getLength());
            System.out.println(line);
        }

    }
}
