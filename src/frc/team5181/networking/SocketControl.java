package frc.team5181.networking;

import java.io.DataInput;
import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.util.ArrayDeque;

public class SocketControl {
    private Socket s;
    private String ip;
    private int port;
    private ReadThread readThread;
    private DataOutputStream write;
    private
    boolean isServer;

    public SocketControl(String ip, int port, boolean isServer) {
        this.ip = ip;
        this.port = port;
        this.isServer = isServer;
    }
    public SocketControl(int port, boolean isServer) {
        this("192.168.0.233",port,isServer);
    }

    private void connect() {
        try {
            if(this.isServer) s = new ServerSocket(port).accept();
            else s = new Socket(ip,port);
        } catch (Exception e) {
            //Try to Reconnect
            if(e instanceof ConnectException) {
                connect();
                return;
            }
        }
    }

    public void init() {
        DataInputStream read;

        try {
            connect();
            read = new DataInputStream(this.s.getInputStream());
            write = new DataOutputStream(this.s.getOutputStream());
            readThread = new ReadThread(read);
            readThread.start();
        } catch (Exception e) {e.printStackTrace();}
        while(!isAlive()); //Block when the thread is not ready
    }

    public void write(String msg) {
        try {
            readThread.setPause(true);
            write.writeUTF(msg);
            readThread.setPause(false);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayDeque<String> read() {
        return readThread.getData();
    }

    public String readLatest() {
        return read().peekLast();
    }

    public boolean isAlive() {
        return readThread.isConnAlive();
    }

    public void stop() {
        try {
            this.readThread.shutdown();
            this.s.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
