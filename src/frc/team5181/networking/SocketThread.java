package frc.team5181.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketThread implements Runnable {

    private Socket s;
    private String readBuffer;
    private BufferedReader br;
    public String dataBuffer;
    private String writeBuffer;
    private boolean isRead;
    private PrintWriter pw;

    public SocketThread(Socket socket) {
        this.s = socket;
        this.isRead = true;
    }

    public SocketThread(Socket socket,String message) {
        this.s = socket;
        this.isRead = false;
        this.writeBuffer = message;
    }

    public void run() {
        try {
            if (this.isRead) {
                while (true) {
                    this.br = new BufferedReader(new InputStreamReader(this.s.getInputStream())); //The original code sucks, so...
                    while ((this.readBuffer = this.br.readLine()) != null) {
                        this.dataBuffer += readBuffer;
                    }
                    this.br.close();
                }
            }
            else if (!this.isRead) {
                this.pw = new PrintWriter(this.s.getOutputStream());
                this.pw.write(this.writeBuffer);
                this.pw.flush();
            }
        } catch (IOException e) { e.printStackTrace(); }

    }

    public void closeSocket() {
        try {
            this.s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
