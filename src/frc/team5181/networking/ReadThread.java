package frc.team5181.networking;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.SocketException;

public class ReadThread extends Thread {

    private DataInputStream din;
    private String msg;

    public ReadThread(DataInputStream inputStream) {
        this.din = inputStream;
    }

    @Override
    public void run() {
        try {
            while(true) {
                try{
                    this.msg = this.din.readUTF();
                } catch (SocketException e) {break;} //Quit if Connection reset
            }
        }
        catch(Exception e) {

            e.printStackTrace();
        }
    }

    public String getMessage() {
        return this.msg;
    }

}
