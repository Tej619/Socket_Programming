import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/** @author TEJAS VAITY **/
public class Server {

	private static Map<Integer, String[]> addressBook = new HashMap<>(); // Created HashMap to store the details
	public static final int SERVER_PORT = 3811;
	public static final String ADDRESS_BOOK = "address.txt";
	public static final int MAX_RECORDS = 20;

	public static void main(String[] args) {
		try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
			System.out.println("Server started on port " + SERVER_PORT);

			loadAddressBook();

			while (true) {
				Socket clientSocket = serverSocket.accept(); // Waiting for a client connection to connect to server
				System.out.println("Client connected with IP Address: "+clientSocket.getInetAddress());
				handleClient(clientSocket);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Handle client requests
	private static void handleClient(Socket clientSocket) {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

			String request;
			while ((request = in.readLine()) != null) {
				System.out.println("Received command from Client: " + request);
				String[] parts = request.split(" ");
				String command = parts[0].toUpperCase(); // Making the ADD,DELETE,QUIT,SHUTDOWN,LIST case insensitive

				switch (command) {
				case "ADD":
					if (parts.length == 4) {
						addRecord(parts[1], parts[2], parts[3], out);
					} else {
						out.println("301 message format error");
					}
					break;
				case "DELETE":
					if (parts.length == 2) {
						deleteRecord(parts[1], out);
					} else {
						out.println("301 message format error");
					}
					break;
				case "LIST":
					listRecords(out);
					break;
				case "SHUTDOWN":
					out.println("200 OK");
					shutdownServer();
					break;
				case "QUIT":
					out.println("200 OK");
					return;
				default:
					out.println("300 invalid command");
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void addRecord(String firstName, String lastName, String phoneNumber, PrintWriter out) {
		if (addressBook.size() >= MAX_RECORDS) {
			out.println("403 Address book full");
			return;
		}
		// Checks for input validation with First,LastName less than 8 characters and
		// Mobile Number Less than 12 characters
		if (firstName.length() > 8 || lastName.length() > 8 || phoneNumber.length() > 12) {
			out.println("301 Wrong Input format. Try Again.");
			return;
		}
		int newId = generateRecordID(); // to generate unique ID.
		addressBook.put(newId, new String[] { firstName, lastName, phoneNumber });
		out.println("200 OK \nThe new Record ID is " + newId);
		saveAddressBook();
	}

	// Method to Delete record with Passing ID as input parameter
	private static void deleteRecord(String idStr, PrintWriter out) {
		try {
			int id = Integer.parseInt(idStr);
			if (addressBook.containsKey(id)) {
				addressBook.remove(id);
				out.println("200 OK");
				saveAddressBook();
			} else {
				out.println("403 The Record ID does not exist.");
			}
		} catch (NumberFormatException e) {
			out.println("301 message format error");
		}
	}

	// Method to display the List
	private static void listRecords(PrintWriter out) {
		if (addressBook.size() <= 0) {
			out.println("301 Address book empty");
			return;
		}
		StringBuilder sb = new StringBuilder("200 OK\nThe list of records in the book:\n");
		for (Map.Entry<Integer, String[]> entry : addressBook.entrySet()) {
			sb.append(entry.getKey() + "\t" + entry.getValue()[0] + " " + entry.getValue()[1] + "\t" + entry.getValue()[2]
					+ "\n");
		}
		out.println(sb.toString());
	}

	private static void loadAddressBook() {
		// Load the records from the file (if it exists)
		File addressFile = new File(ADDRESS_BOOK); // Creates new file if file does not exist
		try (BufferedReader reader = new BufferedReader(new FileReader(addressFile))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				int id = Integer.parseInt(parts[0]);
				addressBook.put(id, new String[] { parts[1], parts[2], parts[3] });
			}
		} catch (IOException e) {
			System.out.println("No address book file found, starting fresh.");
		}
	}

	// Method to save records in file
	private static void saveAddressBook() {
		try (PrintWriter writer = new PrintWriter(new FileWriter(ADDRESS_BOOK))) {
			for (Map.Entry<Integer, String[]> entry : addressBook.entrySet()) {
				writer.printf("%04d,%s,%s,%s%n", entry.getKey(), entry.getValue()[0], entry.getValue()[1],
						entry.getValue()[2]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Method to generate Record ID
	private static int generateRecordID() {
		int maxId = 1000;
		for (int id : addressBook.keySet()) {
			if (id > maxId) {
				maxId = id;
			}
		}
		return maxId + 1;
	}

	// Method to Shutdown the Server
	private static void shutdownServer() {
		saveAddressBook();
		System.out.println("Shutting down the server...");
		System.exit(0);
	}
}
