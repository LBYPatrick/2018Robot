package frc.team5181.networking;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayDeque;
import java.util.ArrayList;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class ReadThread extends Thread {

    private DataInputStream din;
    private ArrayDeque<String> msg;
    private boolean isConnAlive;
    private boolean isPaused = false;
    private String msge;

    final private static String CONN_OK_MESSAGE = "<OK>";

    public ReadThread(DataInputStream inputStream) {
        din = inputStream;
        msg = new ArrayDeque<>();
    }

    @Override
    public void run() {

        String messageBuffer;
        isConnAlive = true;

        try {
            while(isConnAlive) {
                if(isPaused) continue;

                messageBuffer = din.readUTF();
                msg.add(messageBuffer);
            }
        } catch (Exception e) {
            if(e instanceof SocketException) return;
            e.printStackTrace();
        }
    }

    public synchronized ArrayDeque<String> getData() {
        isPaused = true;

        if(msg.size() == 0) return null;

        ArrayDeque<String> returnBuffer = msg;
        msg = new ArrayDeque<>();
        isPaused = false;
        return returnBuffer;
    }

    public synchronized void setPause(boolean value) {
        isPaused = value;
    }

    public boolean isConnAlive() {
        return isConnAlive;
    }

    public void shutdown() {
        isConnAlive = false;
    }
}