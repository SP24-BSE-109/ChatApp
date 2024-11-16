import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hi! Welcome to the Chat application.");
        System.out.println("1. Start a server");
        System.out.println("2. Connect to a server as a client");

        int choice;
        do {
            System.out.print("Enter your choice (1 or 2): ");
            choice = scanner.nextInt();
            scanner.nextLine();
        } while (choice != 1 && choice != 2);

        if (choice == 1) {
            startServer(scanner);
        }

        startClient(scanner);
    }

    private static void startServer(Scanner scanner) {
        System.out.print("Enter the port to start the server on: ");
        int port = scanner.nextInt();
        scanner.nextLine();

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
            Server server = new Server(serverSocket);

            new Thread(server::StartServer).start();
        } catch (IOException e) {
            System.out.println("Error starting server: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void startClient(Scanner scanner) {
        System.out.print("Enter the port to connect to the server: ");
        int port = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        try {
            Socket socket = new Socket("localhost", port);
            Client client = new Client(socket, name);

            client.ReceiveMessages();
            client.SendMessage();
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }

    private static void displayContacts() {
        System.out.println("Contacts list:");
        for (ClientHandler clientHandler : ClientHandler._clientsList) {
            System.out.println(" ---- " + clientHandler.GetClientName() + " ----");
        }
    }
}
