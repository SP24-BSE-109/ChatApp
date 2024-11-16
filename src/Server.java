import java.io.*;
import java.net.*;

public class Server {

    private final ServerSocket serverSocket;

    public Server(ServerSocket socket) {
        this.serverSocket = socket;
    }

    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public void closeServerSocket(){
        if(serverSocket != null){
            try {
                serverSocket.close();
            } catch (IOException i) {
                i.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(1234);
            System.out.println("Server started on port " + serverSocket.getLocalPort());
            Server server = new Server(serverSocket);
            server.startServer();
        } catch (IOException e) {
            System.err.println("Could not listen on port 1234");
            System.exit(1);
        }
    }
}
