import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class Server extends Thread {
	
	// properties
	// the variable of connection is not static because the your content not is shared between the various objects of class Server
	private Socket connection;
	private static Vector<DataOutputStream> vOutputEconomy = new Vector<DataOutputStream>();
	private static Vector<DataOutputStream> vOutputEntertainment = new Vector<DataOutputStream>();
	private static Vector<DataOutputStream> vOutputTechnology = new Vector<DataOutputStream>();
	
	// method construct
	public Server (Socket s) {
		connection =  s;
	}

	// method main. Is executed by default when the class is started
	public static void main(String[] args) throws IOException {

		// create the socket of communication with the clients on port 8657
		@SuppressWarnings("resource")
		ServerSocket server = new ServerSocket(8657);

		// while the class be execution
		while(true) {

			// show message in console
			System.out.println("Esperando cliente se conectar...");
			
			// if any client initialized a connection the Server class accept this connection through of an object of Socket class 
			Socket cn = server.accept();
			
			// initialized a new object of Server class
			Thread t = new Server (cn);
			
			// start the thread
			t.start();
			
			// show message in console
			System.out.println("Cliente conectado!");
		}

	}
	
	// implements the method run() of Thread class. This method is executed after the call of method start()
	public void run() {
		
		// declare variables of control
		String message_received;
		String message_sended;
		String name_client;
		String subject_client;
		
		// variable that store an object that receive messages
		BufferedReader input_client;
		
		// try to run the code below
		try {
			
			// why this code (before of while) is executed only in the first execution? Answer: The code get stuck inside of loop. When the code exit of loop the connection with the socket is finalized
			
			input_client = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			// variable that store an object that sends messages
			DataOutputStream output_client = new DataOutputStream(connection.getOutputStream());

			// read a name receive of client. While none data be typed the code not continue
			name_client = input_client.readLine();
			
			// sends return for client
			output_client.writeBytes("Olá " + name_client + "!\n");
			
			// read a name receive of client. While none data be typed the code not continue
			subject_client = input_client.readLine();
			
			// add the client in the vector
			switch(subject_client) {
			  case "Economia":
				  vOutputEconomy.add(output_client);
			    break;
			  case "Entretenimento":
				  vOutputEntertainment.add(output_client);
			    break;
			  case "Tecnologia":
				  vOutputTechnology.add(output_client);
			    break;
			  default:
				  vOutputEntertainment.add(output_client);
				  subject_client = "Entretenimento";
			}
			
			// sends return for client
			output_client.writeBytes("O assunto [" + subject_client + "] foi registrado!\n");
			
			// read a message receive of client. While none data be typed the code not continue
			message_received = input_client.readLine();
			
			Integer i;
			Vector<DataOutputStream> v;

			// while the message received not be null or equal the finish
			while (message_received != null && !(message_received.trim().equals("")) && !(message_received.startsWith("fim"))) {
				
				// show message receive on console
				System.out.println(name_client + ": " + message_received);

				// creates message of return for client
				message_sended = " <" + name_client + "> : <" + subject_client + "> : <" + message_received + "> : <" + getDateTime() + ">\n";
				
				// return the vector by subject
				switch(subject_client) {
				  case "Economia":
					  v = vOutputEconomy;
				    break;
				  case "Entretenimento":
					  v = vOutputEntertainment;
				    break;
				  case "Tecnologia":
					  v = vOutputTechnology;
				    break;
				  default:
					  v = vOutputEntertainment;
				}
				
				// sends the message received for all the clients except for the sender
				i = 0;
				while (i < v.size()) {
					if(v.get(i) != output_client) {				
						// sends return for client
						v.get(i).writeBytes(message_sended);
					}
					i = i + 1;
				}
				
				// waits a new message of client
				message_received = input_client.readLine();
				
			}

			// show message on console
			System.out.println("Cliente desconectado!");
			
			// close the connection of server object
			connection.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private static String getDateTime() { 
		//DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		DateFormat dateFormat = new SimpleDateFormat("HH:mm"); 
		Date date = new Date(); 
		return dateFormat.format(date); 
	}

}