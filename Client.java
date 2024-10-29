import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/** @author Tejas Vaity **/
public class Client {

	public static final int SERVER_PORT = 3811;

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: client <Server IP Address>");
			System.exit(1);
		}

		String serverIp = args[0];
		//Auto Closeable try block so no need to close in catch or finally block for  all the BufferReader, PrintWriter, Socket
		try (Socket socket = new Socket(serverIp, SERVER_PORT);
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in))) {
			System.out.println("Enter command (ADD, DELETE, LIST, SHUTDOWN, QUIT) : ");
			String userInput;

			while ((userInput = consoleInput.readLine()) != null) {
				out.println(userInput);
				String serverResponse = in.readLine();
				System.out.println(serverResponse);

				if (userInput.equalsIgnoreCase("QUIT") || userInput.equalsIgnoreCase("SHUTDOWN")) {
					break;
				}

				// To Display multi line messages received from server
				while (in.ready()) {
					System.out.println(in.readLine());
				}
			}

		} catch (IOException e) {
			System.err.println("Client error: " + e.getMessage());
		}
	}
}
