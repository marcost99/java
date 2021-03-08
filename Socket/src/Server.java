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
		
		// declares variables of control
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
			output_client.writeBytes("<Servidor> : <Olá " + name_client + "!> : <" + getDateTime() + ">\n");
			
			// read a name receive of client. While none data be typed the code not continue
			subject_client = input_client.readLine();
			
			// declares variables of control
			Integer i;
			Vector<DataOutputStream> v;
			
			// add the client in the vector and sets the vector by subject
			switch(subject_client) {
			  case "1":
				  vOutputEconomy.add(output_client);
				  v = vOutputEconomy;
				  subject_client = "Economia";
			    break;
			  case "2":
				  vOutputEntertainment.add(output_client);
				  v = vOutputEntertainment;
				  subject_client = "Entretenimento";
			    break;
			  case "3":
				  vOutputTechnology.add(output_client);
				  v = vOutputTechnology;
				  subject_client = "Tecnologia";
			    break;
			  default:
				  vOutputEntertainment.add(output_client);
				  v = vOutputEntertainment;
				  subject_client = "Entretenimento";
			}
			
			// sends the message received for all the clients of same subject
			i = 0;
			while (i < v.size()) {
				v.get(i).writeBytes("<Servidor> : <" + name_client + " entrou no chat [" + subject_client + "]!> : <" + getDateTime() + ">\n");
				i = i + 1;
			}
			
			// read a message receive of client. While none data be typed the code not continue
			message_received = input_client.readLine();

			// while the message received not be null or equal the finish
			while (message_received != null && !(message_received.trim().equals("")) && !(message_received.startsWith("fim"))) {
				
				// show message receive on console
				System.out.println(name_client + ": " + message_received);

				// creates message of return for client
				message_sended = " <" + name_client + "> : <" + subject_client + "> : <" + message_received + "> : <" + getDateTime() + ">\n";
				
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

			// sends the message received for all the clients of same subject
			i = 0;
			while (i < v.size()) {
				v.get(i).writeBytes("<Servidor> : <" + name_client + " saiu do chat [" + subject_client + "]!> : <" + getDateTime() + ">\n");
				i = i + 1;
			}
			
			// remove of vector the client that exit of chat
			i = 0;
			while (i < v.size()) {
				if(v.get(i) == output_client) {
					v.remove(v.get(i));
					System.out.println("Cliente desconectado!");
				}
				i = i + 1;
			}
			
			// close the connection of server object
			connection.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private static String getDateTime() { 
		DateFormat dateFormat = new SimpleDateFormat("HH:mm"); 
		Date date = new Date(); 
		return dateFormat.format(date); 
	}

}