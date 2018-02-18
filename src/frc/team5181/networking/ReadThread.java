package frc.team5181.networking;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class ReadThread extends Thread {

    private DataInputStream din;
    private String msg;

    public ReadThread(DataInputStream inputStream, String messageBuffer) {
        this.din = inputStream;
        this.msg  = messageBuffer;
    }

    @Override
    public void run() {
        try {
            while(true) {
                this.msg += this.din.readUTF();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
