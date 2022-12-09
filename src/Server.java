import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import static java.util.Map.entry; 

public class Server {

// declaring required variables
private static ServerSocket serverSocket;
private static Socket clientSocket;
private static InputStreamReader inputStreamReader;
private static BufferedReader bufferedReader;
private static String eventId = "";
private static String preEventId = "";

static Map<String, String> ewents = Map.ofEntries(
	    entry("init game library", "test.bat")
	);

public static void main(String[] args) {
	try {
		// creating a new ServerSocket at port 4444
		serverSocket = new ServerSocket(4444);

	} catch (IOException e) {
		System.out.println("Could not listen on port: 4444");
	}

	System.out.println("Server started. Listening to the port 4444");
	
	// we keep listening to the socket's
	// input stream until the message
	// "over" is encountered
	while (!eventId.equalsIgnoreCase("over")) {
		try {

			// the accept method waits for a new client connection
			// and and returns a individual socket for that connection
			clientSocket = serverSocket.accept();
			
			// get the inputstream from socket, which will have
			// the message from the clients
			inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
			bufferedReader = new BufferedReader(inputStreamReader);					
		
			// reading the message
			preEventId = bufferedReader.readLine();
			
			if(isEmptyString(preEventId)) {
				inputStreamReader.close();
				clientSocket.close();
				continue;
			}
			
			eventId = preEventId;

			if (!isEmptyString(ewents.get(eventId))) {
				String event = ewents.get(eventId);
				System.out.println(event);
				Runtime.getRuntime().exec("cmd /c start \"\" " + event, null, new File("C:\\Users\\JUAN ORTIZ\\Documents\\Events\\"));
			}

			// finally it is very important
			// that you close the sockets
			inputStreamReader.close();
			clientSocket.close();

			} catch (IOException ex) {
				System.out.println("Problem in message reading");
			}
		}
	}

	public static boolean isEmptyString(String str) {
		return str == null || str.trim().isEmpty();
	}
}
