import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientName;

    public ClientHandler(Socket socket){
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientName = bufferedReader.readLine();
            clients.add(this);
            broadcastMessage("Server: " + clientName + " has connected");
        } catch (IOException e) {
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter!= null) {
                bufferedWriter.close();
            }
            if (socket!= null) {
                socket.close();
            }
        } catch(IOException i) {
            i.printStackTrace();
        }
    }


    @Override
    public void run() {
        String message;
        while(socket.isConnected()) {
            try {
                message = bufferedReader.readLine();
                broadcastMessage(message);
                if(message.equalsIgnoreCase("/quit")) {
                    removeClientHandler();
                    break;
                }
            } catch (IOException i) {
                removeClientHandler();
                break;
            }
        }

    }

    private void broadcastMessage(String message) {
        for (ClientHandler client : clients) {
            try {
                if (client == this) continue;
                client.bufferedWriter.write(message);
                client.bufferedWriter.newLine();
                client.bufferedWriter.flush();
            } catch (IOException i) {
                System.out.println("Error broadcasting message: " + i.getMessage());
            }
        }
    }

    private void removeClientHandler(){
        closeEverything(socket, bufferedReader, bufferedWriter);
        clients.remove(this);
        broadcastMessage("Server: " + clientName + " has disconnected");
    }
}
