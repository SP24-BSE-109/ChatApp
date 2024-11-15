import java.io.*;
import java.net.*;

public class Server {

    private ServerSocket socket = null;
    private DataInputStream in = null;

    public void CreateServer(int port){
        try {
            socket = new ServerSocket(port);
            System.out.println("Server is listening on port " + port);
            Socket clientSocket = socket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());
            String line = "";
            while (!line.equals("Over")) {
                try {
                    line = in.readUTF();
                    System.out.println(line);
                } catch(IOException i) {
                    System.out.println(i);
                }
            }
            System.out.println("Closing connection");
            socket.close();
            in.close();
        } catch (IOException e) {
            System.err.println("Could not listen on port " + port);

        }

    }

}
