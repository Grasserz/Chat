package it.volta.ts.socket.chatnogpt.business;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class NoChatGtpServer {

    ServerSocket   sso;
	Socket         cso;
	ClientHandler ch;
	ArrayList<ClientHandler> chList;
	
	public NoChatGtpServer() {
		try {
			sso = new ServerSocket(3333);
			chList = new ArrayList<>();
			while(true) {
				cso = sso.accept();
				ch = new ClientHandler(cso,chList);
				chList.add(ch);
				ch.start();
			}
		} catch (IOException e) {
		}
	}
}
