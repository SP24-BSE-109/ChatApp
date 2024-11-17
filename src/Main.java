import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner _scanner = new Scanner(System.in);

        System.out.println("Hi! Welcome to the Chat application.");
        System.out.println("1. Start a server");
        System.out.println("2. Connect to a server as a client");

        int _choice;
        do {
            System.out.print("Enter your choice (1 or 2): ");
            _choice = _scanner.nextInt();
            _scanner.nextLine();
        } while (_choice != 1 && _choice != 2);

        if (_choice == 1) {
            StartServer(_scanner);
        }

        StartClient(_scanner);
    }

    private static void StartServer(Scanner _scanner) {
        System.out.print("Enter the port to start the server on: ");
        int _port = _scanner.nextInt();
        _scanner.nextLine();

        try {
            ServerSocket _serverSocket = new ServerSocket(_port);
            System.out.println("Server started on port " + _port);
            Server _server = new Server(_serverSocket);

            new Thread(_server::StartServer).start();
        } catch (IOException e) {
            System.out.println("Error starting server: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void StartClient(Scanner _scanner) {
        System.out.print("Enter the port to connect to the server: ");
        int _port = _scanner.nextInt();
        _scanner.nextLine();
        System.out.print("Enter your name: ");
        String _name = _scanner.nextLine();

        try {
            Socket _socket = new Socket("localhost", _port);
            Client _client = new Client(_socket, _name);

            _client.ReceiveMessages();
            _client.SendMessage();
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }
}
