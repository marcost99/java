import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {
	
	// properties
	private Socket connection;
	
	// method construct
	public Client (Socket s) {
		connection =  s;
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		Socket s = new Socket("localhost", 8657);
		
		// initialized a new object of Client class
		Thread t = new Client (s);
		
		// start the thread
		t.start();
		
		// show message in console
		System.out.println("Cliente ativo!");
		
	}
	
	// implements the method run() of Thread class. This method is executed after the call of method start()
	public void run() {
		
		// try to run the code below
		try {
			
			//
			String message_typed;
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
			
			// show message on console
			System.out.println(name_client + " entrou do chat!");
			
			// variable that store an object that sends messages
			DataOutputStream output_server = new DataOutputStream(connection.getOutputStream());
			
			// variable that store an object that receive messages
			BufferedReader input_server = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			// sends a line for server
			output_server.writeBytes(name_client + '\n');
			
			// read a line of server
			message_received = input_server.readLine();
			
			// show the line read by server
			System.out.println("Servidor: " + message_received);
			
			// show message on console
			System.out.println("Informe um assunto: Economia | Entretenimento | Tecnologia");
			
			// variable that store an object started before. While none data be typed the code not continue
			subject = keyboard.readLine();
			
			// sends a line for server. Is necessary the '\n' for the code continue
			output_server.writeBytes(subject + '\n');
			
			// read a line of server
			message_received = input_server.readLine();
			
			// show the line read by server
			System.out.println("Servidor: " + message_received);
			
			// while the class be execution
			while (true) {
				
				// read a line of keyboard. While none data be typed the code not continue
				message_typed = keyboard.readLine();
				
				// verifies if the chat is must be terminated
				if (message_typed.startsWith("fim") == true)
				break;
				
				// sends a line for server. Is necessary the '\n' for the code continue
				output_server.writeBytes(message_typed + '\n');
				
				// read a line of server
				message_received = input_server.readLine();
				
				// show the line read by server
				System.out.println(message_received);
				
			}
			
			// close the connection of client object
			connection.close();
			
			// show message on console
			System.out.println(name_client + " saiu do chat!");
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}