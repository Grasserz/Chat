package Main;

import it.volta.ts.socket.chatnogpt.ui.ConsoleClient;

public class MainClient {

	public static void main(String[] args) {
		new ConsoleClient().runClient(args[0]);
	}

}
