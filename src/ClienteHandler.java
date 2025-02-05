import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteHandler {
    public static final int SIZE = 10;
    private Socket clientSocket;
    private String playerName;
    private char[][] board;
    private int[] barcosSizes = {3, 4, 2}; // 3 barcos
    private PrintWriter out;
    private BufferedReader in;

    public ClienteHandler(Socket clientSocket, String playerName) throws IOException {
        this.clientSocket = clientSocket;
        this.playerName = playerName;
        this.board = new char[SIZE][SIZE];
        resetBoard();
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out.println("¡Bienvenido " + playerName + "!");
        enviarTablero();
    }

    public String getPlayerName() {
        return playerName;
    }

    public char[][] getBoard() {
        return board;
    }

    public PrintWriter getOut() {
        return out;
    }

    public BufferedReader getIn() {
        return in;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void resetBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = '-';
            }
        }
    }

    public void enviarTablero() {
        out.println("Tu Tablero de Juego:");
        StringBuilder sb = new StringBuilder();
        sb.append("     ");
        for (int i = 0; i < SIZE; i++) {
            sb.append((i + 1) + "    ");
        }
        out.println(sb.toString());
        for (int i = 0; i < SIZE; i++) {
            sb = new StringBuilder();
            sb.append((char) ('A' + i) + "   | ");
            for (int j = 0; j < SIZE; j++) {
                sb.append(board[i][j] + "  | ");
            }





            out.println(sb.toString());
            out.println("    --------------------------------------------");
        }
    }

    public void colocarBarcos() throws IOException {
        out.println("Inicia la colocación de tus barcos (verticalmente). Debes colocar 3 barcos.");
        enviarTablero();
        for (int i = 0; i < barcosSizes.length; i++) {
            int shipSize = barcosSizes[i];


            while (true) {
                out.println("Coloca el barco de tamaño " + shipSize + ". Introduce la coordenada (Ej: A1):");
                String coordenada = in.readLine();
                if (coordenada == null || coordenada.length() < 2) {
                    out.println("Entrada inválida. Intenta de nuevo.");
                    continue;
                }
                int fila = coordenada.charAt(0) - 'A';

                int columna;
                try {
                    columna = Integer.parseInt(coordenada.substring(1)) - 1;
                } catch (NumberFormatException e) {
                    out.println("Formato inválido. Intenta de nuevo.");
                    continue;
                }
                if (fila < 0 || fila >= SIZE || columna < 0 || columna >= SIZE) {
                    out.println("Coordenada fuera de rango. Intenta de nuevo.");
                    continue;
                }
                // ESPACIO BARCO VERTICALMENTE
                if (fila + shipSize > SIZE) {
                    out.println("No hay espacio suficiente para el barco. Intenta otra coordenada.");
                    continue;
                }
                // NO BARCOS REPETIDOS
                boolean canPlace = true;
                for (int j = 0; j < shipSize; j++) {
                    if (board[fila + j][columna] != '-') {
                        canPlace = false;
                        break;
                    }
                }
                if (!canPlace) {
                    out.println("El barco se superpone con otro. Intenta de nuevo.");
                    continue;
                }
                // Colocar el barco marcándolo con 'I'
                for (int j = 0; j < shipSize; j++) {
                    board[fila + j][columna] = 'I';
                }
                enviarTablero();
                break;
            }
        }
        out.println("¡Todos los barcos han sido colocados!");
    }
}
