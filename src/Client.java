import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private String name;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;


    public Client( Socket socket, String name) {
        try{
            this.socket = socket;
            this.name = name;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (IOException i){
            closeEverything();
        }
    }

    private void closeEverything() {
        try {
            if(bufferedWriter!=null) bufferedWriter.close();
            if(bufferedReader!=null) bufferedReader.close();
            if(socket!=null) socket.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }
    public void sendMessage() {
        try {
            bufferedWriter.write(name);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()){
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(name + ": " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException i) {
            closeEverything();
            System.out.println("Error writing message: " + i.getMessage());
        }
    }
    public void receiveMessages() {
        Thread thread = new Thread(() -> {
            try {
                while (socket.isConnected()){
                    String message = bufferedReader.readLine();
                    System.out.println(message);
                }
            } catch (IOException i) {
                closeEverything();
                System.out.println("Error reading message: " + i.getMessage());
            }
        });
        thread.start();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        Socket socket = null;
        try {
            socket = new Socket("localhost", 1234);
            Client client = new Client(socket, name);
            client.receiveMessages();
            client.sendMessage();
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }
}
