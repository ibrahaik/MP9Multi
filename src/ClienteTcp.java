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

            boolean seguirEsperando = true;  // Controlamos el flujo del juego

            // Leer mensaje de servidor
            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                System.out.println(serverMessage);

                // Imprimir el tablero cuando el servidor lo envíe
                if (serverMessage.contains("Tablero de juego")) {
                    System.out.println(serverMessage);
                }

                // Colocar barco
                if (serverMessage.contains("Coloca el primer barco") || serverMessage.contains("Coloca el barco")) {
                    System.out.println("Introduce la coordenada para colocar el barco:");
                    String coordenada = scanner.nextLine();
                    out.println(coordenada); // coordenada al servidor
                }

                // Si el servidor indica que el juego ha terminado, cambiar el flujo
                if (serverMessage.contains("Juego terminado")) {
                    System.out.println("Juego finalizado.");
                    seguirEsperando = false;  // Cambiamos el flujo a false para terminar
                    break;
                }
            }

            socket.close();
            scanner.close();
        } catch (IOException e) {
            System.out.println("Error al conectar con el servidor " + e.getMessage());
        }
    }
}
