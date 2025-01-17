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

    // Arreglo con tamaños de barcos predefinidos
    private int[] tamañosBarcos = {3, 4, 2, 5, 3};
    private int barcoActual = 0;  // Indica el índice del barco que se está colocando

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

            // Bucle para colocar los barcos
            while (barcoActual < tamañosBarcos.length) {
                out.println("Coloca el barco de tamaño " + tamañosBarcos[barcoActual] + " (Vertical):");
                out.println("Introduce las coordenadas (por ejemplo: A1):");

                String coordenada = in.readLine();
                int fila = coordenada.charAt(0) - 'A';  // Convierte la letra a índice de fila
                int columna = Integer.parseInt(coordenada.substring(1)) - 1;  // Convierte el número a índice de columna

                // Colocamos el barco
                colocarBarco(tablero, fila, columna, tamañosBarcos[barcoActual], true);  // Supongamos que todos los barcos son verticales

                // Enviamos el tablero actualizado
                enviarTablero(out, tablero);

                // Incrementamos el índice del siguiente barco
                barcoActual++;
            }

            out.println("¡Todos los barcos han sido colocados!");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void inicioTablero(char[][] tablero) {
        for (int i = 0; i<size; i++) {
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

    private void colocarBarco(char[][] tablero, int fila, int columna, int tamano, boolean vertical) {
        if (vertical) {
            for (int i=0; i<tamano; i++) {
                if (fila+i >= size) {
                    System.out.println("No hay espacio suficiente para el barco en esta posición");
                    return; // Sale si no hay espacio
                }
                if (tablero[fila +i][columna] != '-') {
                    System.out.println("Ya hay un barco en esta posición.");
                    return; // Sale si hay una colisión
                }
            }

            // Colocamos el barco
            for (int i = 0; i < tamano; i++) {
                tablero[fila + i][columna] = 'I';
            }

        } else {
            for (int i = 0; i < tamano; i++) {
                if (columna + i >= size) {
                    System.out.println("No hay espacio suficiente para el barco en esta posición");
                    return;
                }
                if (tablero[fila][columna + i] != '-') {
                    System.out.println("Ya hay un barco en esta posición");
                    return;
                }
            }

            // Colocamos el barco
            for (int i = 0; i < tamano; i++) {
                tablero[fila][columna + i] = 'I';
            }
        }
    }

}
