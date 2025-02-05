import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClienteTcp2 {
    public static void main(String[] args) {
        String ipServidor = "localhost";
        int port = 55555;


        try (Socket socket = new Socket(ipServidor, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                System.out.println(serverMessage);
                if (serverMessage.contains("Introduce") ||
                        serverMessage.contains("Coloca") ||
                        serverMessage.contains("¿Quieres jugar de nuevo?")) {
                    String userInput = scanner.nextLine();
                    out.println(userInput);
                }
            }
        } catch (IOException e) {
            System.out.println("Error en el cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
