import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Represents a chat client that connects to the server.
 */
public class Client {

    private String _name;                 // Name of the client
    private Socket _socket;               // Client's socket connection
    private BufferedReader _bufferedReader; // To receive messages
    private BufferedWriter _bufferedWriter; // To send messages

    /**
     * Initializes the client.
     *
     * @param _socket The socket connection to the server.
     * @param _name   The client's name.
     */
    public Client(Socket _socket, String _name) {
        try {
            this._socket = _socket;
            this._name = _name;
            this._bufferedWriter = new BufferedWriter(new OutputStreamWriter(_socket.getOutputStream()));
            this._bufferedReader = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
        } catch (IOException i) {
            CloseEverything();
        }
    }

    /**
     * Closes all resources associated with this client.
     */
    private void CloseEverything() {
        try {
            if (_bufferedWriter != null) _bufferedWriter.close();
            if (_bufferedReader != null) _bufferedReader.close();
            if (_socket != null) _socket.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    /**
     * Sends messages from the client to the server.
     */
    public void SendMessage() {
        try {
            _bufferedWriter.write(_name);
            _bufferedWriter.newLine();
            _bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (_socket.isConnected()) {
                String _messageToSend = scanner.nextLine();
                _bufferedWriter.write(_messageToSend);
                _bufferedWriter.newLine();
                _bufferedWriter.flush();
            }
        } catch (IOException i) {
            CloseEverything();
            System.out.println("Error writing message: " + i.getMessage());
        }
    }

    /**
     * Continuously listens for messages from the server.
     */
    public void ReceiveMessages() {
        Thread _thread = new Thread(() -> {
            try {
                while (_socket.isConnected()) {
                    String _message = _bufferedReader.readLine();
                    System.out.println(_message);
                }
            } catch (IOException i) {
                CloseEverything();
                System.out.println("Error reading message: " + i.getMessage());
            }
        });
        _thread.start();
    }

    /**
     * Entry point for the chat client.
     */

}
