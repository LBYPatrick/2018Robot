package frc.team5181.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketControl {

        private Socket s;
        private Thread readThread;
        private SocketThread r;

        public SocketControl(String ip, int port, boolean isServer) {
            try {
                if(isServer) this.s = new ServerSocket(port).accept();
                else this.s = new Socket(ip,port);

                this.r = new SocketThread(this.s);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public SocketControl(int port, boolean isServer) {
            this("192.168.0.233",port,isServer);
        }

        public void start() {
            this.readThread = new Thread(r,"Read");
            readThread.start();
        }

        public String getData() {
            return this.r.dataBuffer;
        }
        public void writeData(String message) {
            new Thread(new SocketThread(this.s,message),"Write").start();
        }

        public void stop() {
            try {
                this.readThread.interrupt();
                this.s.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

    }
