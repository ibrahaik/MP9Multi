import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorTcp {
    private static final int PORT = 55555;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor iniciado correctamente. Esperando jugadores...");
            while (true) {
                Socket socket1 = serverSocket.accept();
                System.out.println("Jugador 1 conectado.");
                ClienteHandler jugador1 = new ClienteHandler(socket1, "Jugador 1");

                Socket socket2 = serverSocket.accept();
                System.out.println("Jugador 2 conectado.");
                ClienteHandler jugador2 = new ClienteHandler(socket2, "Jugador 2");

                boolean replay = true;
                while (replay) {
                    jugador1.resetBoard();
                    jugador2.resetBoard();

                    Thread t1 = new Thread(() -> {
                        try {
                            jugador1.colocarBarcos();
                        } catch (IOException e) {
                            System.out.println("Error en la colocación de barcos de " + jugador1.getPlayerName());
                        }
                    });

                    Thread t2 = new Thread(() -> {
                        try {
                            jugador2.colocarBarcos();
                        } catch (IOException e) {
                            System.out.println("Error en la colocación de barcos de " + jugador2.getPlayerName());
                        }
                    });

                    t1.start();
                    t2.start();
                    t1.join();
                    t2.join();

                    Partida partida = new Partida(jugador1, jugador2);
                    replay = partida.iniciarPartida();
                }

                // CIERRE DE SESION
                jugador1.getOut().println("La partida ha terminado. ¡Hasta luego!");
                jugador2.getOut().println("La partida ha terminado. ¡Hasta luego!");
                jugador1.getClientSocket().close();
                jugador2.getClientSocket().close();
                System.out.println("Partida finalizada. Esperando nuevos jugadores...");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error en el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

