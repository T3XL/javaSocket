package com.example.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class ReadThread extends Thread{
    private static final Logger logger = Logger.getLogger(ReadThread.class.getName());
    private Socket socket;
    public ReadThread(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        while(true)
        {
            try{
                InputStream is = socket.getInputStream();
                BufferedReader br = new BufferedReader(new java.io.InputStreamReader(is));
                String message = br.readLine();
                System.out.println(message);
            }
            catch(IOException e)
            {
                logger.info(e.getMessage());
            }
        }
    }
}
