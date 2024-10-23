package it.volta.ts.socket.chatnogpt.business;

import java.io.PrintWriter;
import java.util.Scanner;

public class TransmissionHandler extends Thread{
	private PrintWriter out;
	Scanner sc;
	public	TransmissionHandler(PrintWriter out) {
		this.out = out;
	}
	
	public void run() {
		  	sc = new Scanner(System.in);
	        String m = "";
	        for (;;) {
	            m = sc.nextLine();
	            out.println(m);
	        }
	}
	
	public void closeScanner() {
		sc.close();
	}

}
