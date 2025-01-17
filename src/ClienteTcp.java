import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClienteTcp {
    public static void main(String[] args) {
        String ipServidor = "localhost";
        int port = 55555;

        try {
            Socket socket = new Socket(ipServidor, port);
            System.out.println("Conexión establecida con " + ipServidor + " en el puerto " + port);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            //Leer mensaje del servidor
            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                System.out.println(serverMessage);
                if (serverMessage.isEmpty()) {
                    break;
                }
            }

            // Mandamos al cliente a la consola y envíamos su mensae
            String userMessage = scanner.nextLine();
            out.println(userMessage );

            // Leer mensaje del servidor
            String response = in.readLine();
            System.out.println("Servidor respondió: " + response);

            socket.close();
            scanner.close();
        } catch (IOException e) {
            System.out.println("Error al conectar con el servidor " + e.getMessage());
        }
    }
}
