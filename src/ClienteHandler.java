import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteHandler  implements Runnable {

    private static final int size = 10;
    private Socket clientSocket;
    private String playerName;
    private char[][] tablero;

    public ClienteHandler(Socket clientSocket, String playerName) {
        this.clientSocket = clientSocket;
        this.playerName = playerName;
        this.tablero = new char[size][size];
        inicioTablero(this.tablero);
    }

    @Override
    public void run() {
        try {

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            out.println("¡Hola " + playerName + ", Bienvenido a hundir la flota !");
            enviarTablero(out, tablero);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void inicioTablero(char[][] tablero) {
        for (int i =0; i<size; i++) {
            for (int j=0; j <size; j++) {
                tablero[i][j] = '-';
            }
        }
    }

    private static void enviarTablero(PrintWriter out, char[][] board) {
        out.println("Tablero de juego");
        out.print("     ");
        for (int i = 0; i < size; i++) {
            if (i < 9) {
                out.print(i + 1 + "    ");
            } else {
                out.print(i + 1 + "   ");
            }
        }
        out.println();

        // Imprimir cada fila del tablero con letras (A-J) en el lateral izquierdo
        for (int i = 0; i < size; i++) {
            out.print((char)('A' + i) + "   | ");

            for (int j = 0; j < size; j++) {
                out.print(board[i][j] + "  | ");
            }
            out.println(); // Salto de línea después de cada fila
            out.println("    --------------------------------------------");
        }
    }



}
