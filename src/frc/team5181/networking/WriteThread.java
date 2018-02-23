package frc.team5181.networking;

import java.io.DataOutputStream;
import java.io.IOException;

public class WriteThread extends Thread {

    private DataOutputStream dout;
    private String msg;

    public WriteThread(DataOutputStream outputStream, String message) {
        this.dout = outputStream;
        this.msg  = message;
    }

    @Override
    public void run() {
        try {
            this.dout.writeUTF(this.msg);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
