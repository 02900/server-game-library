import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

// declaring required variables
private static ServerSocket serverSocket;
private static Socket clientSocket;
private static InputStreamReader inputStreamReader;
private static BufferedReader bufferedReader;
private static String message="";

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
	while (!message.equalsIgnoreCase("over")) {
		try {

			// the accept method waits for a new client connection
			// and and returns a individual socket for that connection
			clientSocket = serverSocket.accept();
			
			// get the inputstream from socket, which will have
			// the message from the clients
			inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
			bufferedReader = new BufferedReader(inputStreamReader);					
			
			// reading the message
			message = bufferedReader.readLine();
			
			// printing the message
			System.out.println(message);
			
			if (message.equals("init game library")) {
				Runtime.getRuntime().exec("C:\\Windows\\System32\\DisplaySwitch.exe /external");
				Runtime.getRuntime().exec("C:\\Users\\JUAN ORTIZ\\AppData\\Local\\Playnite\\Playnite.FullscreenApp.exe");
			}
			
			if (message.equals("reset DS4")) {
				Runtime.getRuntime().exec("powershell.exe taskkill /IM \"DS4Windows.exe\"");
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
}
