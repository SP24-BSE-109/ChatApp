import java.io.*;
import java.net.*;

public class Server {

    private final ServerSocket _serverSocket;

    public Server(ServerSocket _socket) {
        this._serverSocket = _socket;
    }

    public void StartServer() {
        try {
            while (!_serverSocket.isClosed()) {
                Socket socket = _serverSocket.accept();
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
    public void CloseServerSocket(){
        if(_serverSocket != null){
            try {
                _serverSocket.close();
            } catch (IOException i) {
                i.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ServerSocket _serverSocket = null;
        try {
            _serverSocket = new ServerSocket(1234);
            System.out.println("Server started on port " + _serverSocket.getLocalPort());
            Server _server = new Server(_serverSocket);
            _server.StartServer();
        } catch (IOException e) {
            System.err.println("Could not listen on port 1234");
            System.exit(1);
        }
    }
}
