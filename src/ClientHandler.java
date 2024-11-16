import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Handles individual client connections and manages communication between the client and server.
 */
public class ClientHandler implements Runnable {

    // Static list to maintain all connected clients
    public static ArrayList<ClientHandler> _clientsList = new ArrayList<>();

    private Socket _socket;                     // The client's socket
    private BufferedReader _bufferedReader;     // To read incoming messages from the client
    private BufferedWriter _bufferedWriter;     // To send messages to the client
    private String _clientName;                 // Name of the client

    /**
     * Constructor to initialize the ClientHandler for a connected client.
     *
     * @param _socket The client's socket connection.
     */
    public ClientHandler(Socket _socket) {
        try {
            this._socket = _socket;
            this._bufferedWriter = new BufferedWriter(new OutputStreamWriter(_socket.getOutputStream()));
            this._bufferedReader = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            this._clientName = _bufferedReader.readLine(); // Get client's name from the first message
            _clientsList.add(this);
            BroadcastMessage("Server: " + _clientName + " has joined the chat");
        } catch (IOException e) {
            CloseEverything(_socket, _bufferedReader, _bufferedWriter);
        }
    }

    /**
     * Continuously listens for messages from the client and handles them.
     */
    @Override
    public void run() {
        String _message;
        while (_socket.isConnected()) {
            try {
                _message = _bufferedReader.readLine();
                if (_message.equalsIgnoreCase("/quit")) {
                    RemoveClientHandler();
                    break;
                }
                BroadcastMessage(FormatMessage(_message));
            } catch (IOException e) {
                RemoveClientHandler();
                break;
            }
        }
    }

    /**
     * Sends a message to all connected clients except the sender.
     *
     * @param message The message to broadcast.
     */
    private void BroadcastMessage(String message) {
        for (ClientHandler _client : _clientsList) {
            try {
                if (_client == this) continue; // Skip the sender
                _client._bufferedWriter.write(message);
                _client._bufferedWriter.newLine();
                _client._bufferedWriter.flush();
            } catch (IOException e) {
                CloseEverything(_socket, _bufferedReader, _bufferedWriter);
            }
        }
    }

    /**
     * Formats the message with a timestamp.
     *
     * @param _message The message to format.
     * @return Formatted message.
     */
    private String FormatMessage(String _message) {
        String _timestamp = DateTime.GetDateTime();
        return "[" + _timestamp + "] " + _clientName + ": " + _message;
    }

    /**
     * Removes this client from the list of active clients and notifies others.
     */
    private void RemoveClientHandler() {
        _clientsList.remove(this);
        BroadcastMessage("Server: " + _clientName + " has left the chat");
        CloseEverything(_socket, _bufferedReader, _bufferedWriter);
    }

    /**
     * Closes all resources associated with this client.
     *
     * @param socket The client's socket.
     * @param bufferedReader The reader to close.
     * @param bufferedWriter The writer to close.
     */
    private void CloseEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
