import java.io.*;
import java.net.*;
import java.util.Scanner;


public class Server {

    ServerSocket serverSocket = null;
    public void CreateServer(int port){
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server is listening on port " + port);
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());
            String line = "";
            while (!line.equals("Close")) {
                Scanner scanner = new Scanner(System.in);
                line = scanner.nextLine();
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + port);

        }

    }

}
