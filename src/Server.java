import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import static java.util.Map.entry; 

public class Server {

private static ServerSocket serverSocket;
private static Socket clientSocket;
private static InputStreamReader inputStreamReader;
private static BufferedReader bufferedReader;
private static String event = "";
private static String lastEvent = "";
private static String eventsPath = "C:\\Users\\JUAN ORTIZ\\Documents\\Events\\";

static Map<String, String> scripts = Map.ofEntries(
	    entry("PLAY", "play.bat"),
	    entry("RESET_DS4", "resetDS4.bat"),
	    entry("SLEEP", "sleep.bat"),
	    entry("RESTART", "restart.bat")
	);

public static void main(String[] args) {
	try {
		serverSocket = new ServerSocket(4444);
	} catch (IOException e) {
		System.out.println("Could not listen on port: 4444");
	}

	System.out.println("Server started. Listening to the port 4444");
	
	// we keep listening to the socket's
	// input stream until the message
	// "over" is encountered
	while (!event.equalsIgnoreCase("over")) {
		try {

			// the accept method waits for a new client connection
			// and and returns a individual socket for that connection
			clientSocket = serverSocket.accept();
			
			// get the input stream from socket, which will have
			// the message from the clients
			inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
			bufferedReader = new BufferedReader(inputStreamReader);					
		
			// reading the message
			lastEvent = bufferedReader.readLine();
			
			if(isEmptyString(lastEvent)) {
				inputStreamReader.close();
				clientSocket.close();
				continue;
			}
			
			event = lastEvent;
			String time = getTime();

			if (!isEmptyString(scripts.get(event))) {
				String script = scripts.get(event);
				System.out.println(time + event + " event has been triggered, " + script + " script will be executed");
				Runtime.getRuntime().exec("cmd /c start \"\" " + script, null, new File(eventsPath));
			}
			else {
				System.out.println(time + event + " event has been triggered");
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
	
	public static String getTime() {
		Date date = new Date();
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		return "[" + hour + ":" + minute + ":" + second + "] ";
	}
}
