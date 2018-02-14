package frc.team5181.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

        private BufferedReader br = null;
        private PrintWriter pw = null;

        private Socket s;
        private String dataBuffer = new String();
        private String writeBuffer = new String();
        private boolean toWrite = false;
        private boolean toRead = true;

        public Server(int port) {
            try {
                this.s = new ServerSocket(port).accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {

            String readBuffer;
            try {

                /**
                 * Read
                 */
                if (this.toRead) {
                    this.br = new BufferedReader(new InputStreamReader(this.s.getInputStream())); //The original code sucks, so...
                    while ((readBuffer = this.br.readLine()) != null) {
                        this.dataBuffer += readBuffer;
                    }
                    this.s.shutdownInput();
                    this.br.close();
                }
                /**
                 * Write
                 */
                if (this.toWrite) {
                    this.pw = new PrintWriter(this.s.getOutputStream());
                    this.pw.write(writeBuffer);
                    this.pw.flush();
                    this.toWrite = false;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String getData() {
            String outputBuffer;
            if (!toRead) return null;
            else {
                this.toRead = false;
                outputBuffer = this.dataBuffer;
                this.dataBuffer = new String();
                this.toRead = true;
            }
            return outputBuffer;
        }

        public void writeData(String data) {
            this.toWrite = false;
            this.writeBuffer = data;
            this.toWrite = true;
        }

        public void stopRunning() {
            try {
                this.s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.interrupt();
        }

    }
