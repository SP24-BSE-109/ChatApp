import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> _clientsList = new ArrayList<ClientHandler>();

    private Socket _socket;
    private BufferedReader _bufferedReader;
    private BufferedWriter _bufferedWriter;
    private String _clientName;

    public ClientHandler(Socket _socket) {
        try {
            this._socket = _socket;
            this._bufferedWriter = new BufferedWriter(new OutputStreamWriter(_socket.getOutputStream()));
            this._bufferedReader = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            this._clientName = _bufferedReader.readLine();
            _clientsList.add(this);
            BroadcastMessage("Server: " + _clientName + " has joined the chat");
        } catch (IOException e) {
            CloseEverything(_socket, _bufferedReader, _bufferedWriter);
        }
    }

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

    private String FormatMessage(String _message) {
        String _timestamp = DateTime.GetDateTime();
        return "[" + _timestamp + "] " + _message;
    }

    private void RemoveClientHandler() {
        _clientsList.remove(this);
        BroadcastMessage("Server: " + _clientName + " has left the chat");
        CloseEverything(_socket, _bufferedReader, _bufferedWriter);
    }

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
