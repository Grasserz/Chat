package it.volta.ts.socket.chatnogpt.business;

import java.io.BufferedReader;
import java.io.IOException;

public class ReceptionHandler extends Thread{
	
	private BufferedReader in;
    private String m = "";
		
    public	ReceptionHandler(BufferedReader in) {
			this.in = in;
	}
		
    public void run() {
        try {
            while (true) {
                m = in.readLine();
                if (m == null) {
                    break;
                }
                System.out.println(m);
            }
        } catch (IOException e) {
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
    }

}


