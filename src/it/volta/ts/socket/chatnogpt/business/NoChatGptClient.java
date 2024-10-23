package it.volta.ts.socket.chatnogpt.business;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class NoChatGptClient {
    Socket client;
    PrintWriter out;
    BufferedReader in;
	
    public NoChatGptClient(String serverIP) {
        try {
            client = new Socket(serverIP,3333);
            out = new PrintWriter(client.getOutputStream(),true);
            in  = new BufferedReader(new InputStreamReader(client.getInputStream()));
            new TransmissionHandler(out).start();
			new ReceptionHandler(in).start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
