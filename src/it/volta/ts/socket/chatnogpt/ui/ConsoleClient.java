package it.volta.ts.socket.chatnogpt.ui;

import it.volta.ts.socket.chatnogpt.business.NoChatGptClient;

public class ConsoleClient {
	  	NoChatGptClient client;
	    public ConsoleClient() {  }

	    public void runClient(String serverIP) {
	         client = new NoChatGptClient(serverIP);

	    }
}
