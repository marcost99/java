import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {
	
	// properties
	private BufferedReader input;
	// the variable of connection is static because the your content is shared between the various objects of class Client of a same console
	private static String message_typed;
	
	// method construct
	public Client (BufferedReader i) {
		// sets the property that store the object that receive messages of class Server
		input = i;
		// sets the property for not permit that loop be finalized
		message_typed = "Not Null";
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		// declares variables of control
		String message_received;
		String name_client;
		String subject;
		
		// why this code (before of while) is executed only in the first execution? Answer: The code get stuck inside of loop. When the code exit of loop the connection with the socket is finalized
		
		// variable that store an object that management the data receive of keyboard
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		
		// show message on console
		System.out.println("Informe o nome do cliente:");
		
		// variable that store an object started before. While none data be typed the code not continue
		name_client = keyboard.readLine();
		
		// variable that store an object the create and management the socket of access to the server on port 8657
		Socket conn = new Socket("localhost", 8657);
		
		// variable that store an object that sends messages
		DataOutputStream output_server = new DataOutputStream(conn.getOutputStream());
		
		// variable that store an object that receive messages
		BufferedReader input_server = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		
		// sends a line for server
		output_server.writeBytes(name_client + '\n');
		
		// read a line of server
		message_received = input_server.readLine();
		
		// show the line read by server
		System.out.println(message_received);
		
		// show message on console
		System.out.println("Informe um assunto: 1- Economia | 2- Entretenimento | 3- Tecnologia");
		
		// variable that store an object started before. While none data be typed the code not continue
		subject = keyboard.readLine();
		
		// sends a line for server. Is necessary the '\n' for the code continue
		output_server.writeBytes(subject + '\n');
		
		// read a line of server
		message_received = input_server.readLine();
		
		// show the line read by server
		System.out.println(message_received);
		
		// initializes a new object of Client class for receive messages instant
		Thread t = new Client (input_server);
		
		// start the thread
		t.start();
		
		// while the class be execution
		while (true) {
			
			// read a line of keyboard. While none data be typed the code not continue
			message_typed = keyboard.readLine();
			
			// sends a line for server. Is necessary the '\n' for the code continue
			output_server.writeBytes(message_typed + '\n');
			
			// verifies if the chat is must be terminated
			if (message_typed.startsWith("fim") == true)
			break;
			
		}
		
		// read a line of server
		message_received = input_server.readLine();
		
		// show the line read by server
		if (message_received != null) {
			System.out.println(message_received);
		}
		
		// close the connection of client object
		conn.close();
		
	}
	
	public void run() {
		try {
			// verifies if the chat is must be terminated
			while (message_typed != null && !(message_typed.trim().equals("")) && !(message_typed.startsWith("fim"))) {
				System.out.println(input.readLine());
			}
			// shut down the object of class Client
			System.exit(0);
		} catch (IOException e) {
			// in case of error shut down the object of class Client
			System.exit(0);
		}
	}
	
}