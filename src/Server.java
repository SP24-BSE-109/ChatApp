import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Represents the chat server that accepts and handles client connections.
 */
public class Server {

    private final ServerSocket _serverSocket; // The server's listening socket

    /**
     * Initializes the server with a listening socket.
     *
     * @param _socket The server socket.
     */
    public Server(ServerSocket _socket) {
        this._serverSocket = _socket;
    }

    /**
     * Starts the server and listens for incoming connections.
     */
    public void StartServer() {
        try {
            while (!_serverSocket.isClosed()) {
                Socket socket = _serverSocket.accept();
                System.out.println("New client connected");
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the server socket.
     */
    public void CloseServerSocket() {
        if (_serverSocket != null) {
            try {
                _serverSocket.close();
            } catch (IOException i) {
                i.printStackTrace();
            }
        }
    }

    /**
     * Entry point for the chat server.
     */
    public static void main(String[] args) {
        try {
            ServerSocket _serverSocket = new ServerSocket(1234);
            System.out.println("Server started on port " + _serverSocket.getLocalPort());
            Server _server = new Server(_serverSocket);
            _server.StartServer();
        } catch (IOException e) {
            System.err.println("Could not listen on port 1234");
            System.exit(1);
        }
    }
}
