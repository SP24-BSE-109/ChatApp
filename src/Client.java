import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private String _name;
    private Socket _socket;
    private BufferedReader _bufferedReader;
    private BufferedWriter _bufferedWriter;


    public Client(Socket _socket, String _name) {
        try{
            this._socket = _socket;
            this._name = _name;
            this._bufferedWriter = new BufferedWriter(new OutputStreamWriter(_socket.getOutputStream()));
            this._bufferedReader = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
        }
        catch (IOException i){
            CloseEverything();
        }
    }

    private void CloseEverything() {
        try {
            if(_bufferedWriter !=null) _bufferedWriter.close();
            if(_bufferedReader !=null) _bufferedReader.close();
            if(_socket !=null) _socket.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }
    public void SendMessage() {
        try {
            _bufferedWriter.write(_name);
            _bufferedWriter.newLine();
            _bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (_socket.isConnected()){
                String _messageToSend = scanner.nextLine();
                _bufferedWriter.write(_name + ": " + _messageToSend);
                _bufferedWriter.newLine();
                _bufferedWriter.flush();
            }
        } catch (IOException i) {
            CloseEverything();
            System.out.println("Error writing message: " + i.getMessage());
        }
    }
    public void ReceiveMessages() {
        Thread _thread = new Thread(() -> {
            try {
                while (_socket.isConnected()){
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String _name = scanner.nextLine();
        Socket _socket = null;
        try {
            _socket = new Socket("localhost", 1234);
            Client client = new Client(_socket, _name);
            client.ReceiveMessages();
            client.SendMessage();
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }
}
