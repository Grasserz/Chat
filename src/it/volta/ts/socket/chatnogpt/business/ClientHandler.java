package it.volta.ts.socket.chatnogpt.business;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread{
	private Socket so;
	private PrintWriter    out;
	private BufferedReader in;
	private String         s = "";
	private ArrayList<ClientHandler> chList;
	private static int nCh = 1;
	private String nome = "Client";
	private boolean admin;
	
	public ClientHandler(Socket so, ArrayList<ClientHandler> ChList){
		nome = nome + nCh;
		this.so=so;
		this.chList = ChList;
		nCh++;
	}
	
	public void run() {
	    try {
	        out = new PrintWriter(so.getOutputStream(), true);
	        in = new BufferedReader(new InputStreamReader(so.getInputStream()));
	        
	        adminCheck();
	        wealcome();
	        clientJoined();

	        while (true) {    		
	            s = in.readLine();
	            if (s == null) {   break;  }	       
	     
	            switch (getCommand(s)) {
	            
        	    case "/kick:":
        	        if (admin) {
        	        	String[] splitted = s.split(":");
            	        if(splitted.length != 2) {
            	        	out.println("Formato del comando errato! Premi '?' per vedere la lista dei comandi");
            	        	break;
            	        }
        	            String toKick = splitted[1];
        	            kick(toKick);
        	            
        	        }else {
        	        	out.println("Non hai i permessi da amministratore!");
        	        }
        	        break;

        	    case "/list":
        	    	if(!s.equals("/list")) {
        	        	out.println("Formato del comando errato! Premi '?' per vedere la lista dei comandi");
        	        	break;
        	    	}
        	        showLista(s);
        	        break;

        	    case "/broadcast:":
        	        String[] broadcastMsg = s.split(":");
        	        if(broadcastMsg.length != 2) {
        	        	out.println("Formato del comando errato! Premi '?' per vedere la lista dei comandi");
        	        	break;
        	        }
        	        String toSend = broadcastMsg[1];
        	        broadCastMessage(toSend);
        	        break;

        	    case "/to:":
        	        String[] privateMsg = s.split(":");
        	        if(privateMsg.length != 3) {
        	        	out.println("Formato del comando errato! Premi '?' per vedere la lista dei comandi");
        	        	break;
        	        }
        	        if(!receiverCheck(privateMsg[1]) || privateMsg[1] == null) {
        	        	out.println("Inserisci un NomeUtente valido");
        	        	break;
        	        }	        
        	        String nome = privateMsg[1];       	            	        
        	        String message = privateMsg[2];    	        
        	        privateMessage(message, nome);
        	        break;
        	    
        	    case "?":
        	    	commandList();
        	    	break;
        	    
        	    case "":
        	    	break;
        	   
        	    case "/exit":
        	    	if(!s.equals("/exit")) {
        	        	out.println("Formato del comando errato! Premi '?' per vedere la lista dei comandi");    	        	
            	    	break;
        	    	}
        	    	clientExit();
		        	remove();
		        	return;
        	    	
        	    	
        	    default:
        	        out.println("Inserirsci un comando valido! Scrivi '?' per vedere la lista dei comandi.");
        	        break;
	            }
/*
		        if(s.equals("/exit")) {
		        	clientExit();
		        	remove();
		        	return;
		        }*/
	         }                       
	        
	    } catch (IOException e) { } finally {    
	        remove();           
	    }
	}	

	public void broadCastMessage(String s) {
		for(ClientHandler ch:chList) {
			if(!ch.nome.equals(nome)) {
				ch.out.println("[Broadcast] " + nome + "> " + s);
			}
		}
	}
	
	public void clientJoined() {
		for(ClientHandler ch:chList) {
			if(!ch.nome.equals(nome)) {
				ch.out.println("Server> " + nome + " si e' connesso alla chat");
			}
		}
	}
	
	public void clientExit() {
		for(ClientHandler ch:chList) {
			if(!ch.nome.equals(nome)) {
				ch.out.println("Server> " + nome + " si e' disconnesso dalla chat");
			}
		}
	}
	
	public void clientKicked(ClientHandler kicked) {
		for(ClientHandler ch:chList) {
			if(!ch.nome.equals(kicked.nome)) {
				ch.out.println("Server> " + kicked.nome + " e' stato rimosso dalla chat");
			}
		}
	}
	
	public void privateMessage(String s,String nome) {
		
		for(ClientHandler ch:chList) {
			if(ch.nome.equals(nome)) {
				ch.out.println(this.nome + "> "+ s);
			}
		}
	}
	
	public void showLista(String s) {
		for(ClientHandler ch:chList) {
			if(ch.nome.equals(nome))
			for(ClientHandler chandler:chList) {
				ch.out.println(chandler.toString());
			}
		}
	}
	
	public Socket getSo() {
		return so;
	}
	
	public String toString() {
		return nome;
	}
	
	private synchronized void adminCheck() {	
		String check = chList.get(0).toString();
		
		if(check.equals(nome)) {
			admin = true;
		}	
	}
	
	private synchronized void newAdmin() {	
		ClientHandler ch = chList.get(0);
		
		if(ch.isAdmin() == false) {
		ch.setAdmin(true);
		ch.out.println("Server> Ora sei l'amministratore della chat, premi '?' per vedere i comandi.");
		}
		
	}
		
	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public void kick(String nome) {
		
		ClientHandler toRemove=null;
		
		for(ClientHandler ch:chList) {
			if(ch.toString().equals(nome)) {
				if(nome.equals(this.nome)) {
					ch.out.println("Non puoi rimuovere te stesso.");
					return;
				}
				toRemove = ch;
				try {
					ch.out.println("Server> Sei stato rimosso dalla chat.");
					clientKicked(ch);
					ch.getSo().close();
				} catch (IOException e) {		
				}
			}
		}
		if(toRemove != null) {
			chList.remove(toRemove);
			
		}else {
			out.println("Utente non trovato.");
		}
		
	}
	
	private void remove() {
		try {
		out.println("Server> Ti sei disconnesso");
        chList.remove(this);
        newAdmin();
        if (so != null && !so.isClosed()) {
            so.close();
        } 
		}catch (IOException e) {
        }
	}

	private boolean receiverCheck(String toCheck) {
		for(ClientHandler ch:chList) {
			if(ch.nome.equals(toCheck)) {
				return true;
			}
		}
		return false;		
	}

	
	private String getCommand(String input) {
		
	    if (input.contains("/kick:")) {
	        return "/kick:";
	    }

	    if (input.contains("/list")) {
	        return "/list";
	    }

	    if (input.contains("/broadcast:")) {
	        return "/broadcast:";
	    }

	    if (input.contains("/to:")) {
	        return "/to:";
	    }

	    if (input.contains("/exit")) {
	    	return "/exit";
	    }
	    
	    if(input.contains("?")) {
	    	return "?";
	    }
	 
	    return input;
	}
	
	private void commandList() {

		out.println("Comandi:");
		if(admin) {
			out.println("- /kick: --> disconnetti utente (admin) [formato comando -> /kick:NomeUtente]");
		}
        out.println("- /list --> visualizza lista client");       
        out.println("- /to: --> invia un messaggio privato [formato comando ->  /to:NomeUtente:messaggio]");
        out.println("- /broadcast: --> invia un messaggio broadcast [formato comando ->  /broadcast:messaggio]");
        out.println("- /exit --> disconnettiti");
	}
	
	private void wealcome() {
		 String benvenuto = "Benvenuto " + nome + "!";
		 if(admin == true) {			 
			 out.println(benvenuto + " Sei un amministratore.\n");
	     }else {
	    	 out.println(benvenuto + "\n");
	     }
		 out.println("Scrivi '?' per vedere i comandi.");
	}
	
}
