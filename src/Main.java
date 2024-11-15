public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        server.CreateServer(1111);
        User sender = new User("192.168.100.1", 1111);
    }
}