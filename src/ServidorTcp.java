import java.io.*;
import java.net.*;

public class ServidorTcp {

    private static final int port = 55555;
    private static final int size = 10;

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Servidor iniciado correctamente. Esperando jugadores. . .");

            while (true) {
                Socket player1Socket = serverSocket.accept();
                String playerName = "Jugador " + (1 + (int) (Math.random() * 2));
                System.out.println(playerName + " conectado");


                // Cada jugador con su respectivo hilo para un juego simultáneo
                new Thread(new ClienteHandler(player1Socket, playerName)).start();
            }

        } catch (IOException e) {
            System.out.println("Error al iniciar el servidor: " + e.getMessage());
        }
        }


    }

