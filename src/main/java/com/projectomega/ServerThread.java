package com.projectomega;

import com.projectomega.packets.PacketUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {

    private boolean running = true;
    private ServerSocket socket;


    public ServerThread() throws IOException {
        socket = new ServerSocket(25565);
        this.start();
    }

    @Override
    public void run() {
        PacketUtil.init();

        while(running){
            try {
                Socket accept = socket.accept();
                InputStream is = accept.getInputStream();
                PacketUtil.handlePacket(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
