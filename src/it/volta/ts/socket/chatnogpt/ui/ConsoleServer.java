package it.volta.ts.socket.chatnogpt.ui;

import it.volta.ts.socket.chatnogpt.business.NoChatGtpServer;

public class ConsoleServer {

    NoChatGtpServer server;

    public ConsoleServer() { }

    public void run() {
         server = new NoChatGtpServer();
    }

}
