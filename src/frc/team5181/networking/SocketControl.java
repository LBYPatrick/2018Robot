package frc.team5181.networking;

import java.io.DataInput;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class SocketControl {
        private Socket s;
        private DataInputStream read;
        private DataOutputStream write;
        private String ip;
        private int port;
        private ReadThread readThread;
        private WriteThread writeThread;
        boolean isServer;

        public SocketControl(String ip, int port, boolean isServer) {
            this.ip = ip;
            this.port = port;
            this.isServer = isServer;
        }
        public SocketControl(int port, boolean isServer) {
            this("192.168.0.233",port,isServer);
        }

        public void init() {
            try {
                if(this.isServer) this.s = new ServerSocket(this.port).accept();
                else this.s = new Socket(this.ip,this.port);

                this.read = new DataInputStream(this.s.getInputStream());
                this.write = new DataOutputStream(this.s.getOutputStream());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void startReading() {
            this.readThread = new ReadThread(this.read);
            this.readThread.start();
        }

        public void write(String msg) {
            this.writeThread = new WriteThread(this.write, msg);
            this.readThread.start();
        }

        public String getData() { return this.readThread.getMessage(); }

        public void stop() {
            try {
                this.readThread.interrupt();
                this.writeThread.interrupt();
                this.s.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
