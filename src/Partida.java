import java.io.IOException;

public class Partida {
    private ClienteHandler jugador1;
    private ClienteHandler jugador2;

    public Partida(ClienteHandler jugador1, ClienteHandler jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
    }

    public boolean iniciarPartida() throws IOException {

        jugador1.getOut().println("La partida comienza. Eres " + jugador1.getPlayerName() + ".");

        jugador2.getOut().println("La partida comienza. Eres " + jugador2.getPlayerName() + ".");
        // El Jugador 1 inicia el turno
        ClienteHandler currentPlayer = jugador1;
        ClienteHandler opponent = jugador2;
        boolean turnoExtra = false; // Si impacta, se mantiene turno
        boolean partidaActiva = true;

        while (partidaActiva) {

            // Mostrar tablero propio
            currentPlayer.enviarTablero();
            currentPlayer.getOut().println("Tu turno. Introduce coordenadas para disparar (Ej: A5):");
            String input = currentPlayer.getIn().readLine();
            if (input == null || input.length() < 2) {
                currentPlayer.getOut().println("Entrada inválida. Intenta de nuevo.");
                continue;
            }

            int fila = input.charAt(0) - 'A';
            int columna;
            try {
                columna = Integer.parseInt(input.substring(1)) - 1;
            } catch (NumberFormatException e) {
                currentPlayer.getOut().println("Formato inválido. Intenta de nuevo.");
                continue;
            }
            if (fila < 0 || fila >= ClienteHandler.SIZE || columna < 0 || columna >= ClienteHandler.SIZE) {
                currentPlayer.getOut().println("Coordenada fuera de rango. Intenta de nuevo.");
                continue;
            }

            // Verificar si ya se ha disparado en esa celda en el tablero del oponente
            char cell = opponent.getBoard()[fila][columna];

            if (cell == 'X' || cell == '*') {

                currentPlayer.getOut().println("Ya has atacado esta posición. Intenta de nuevo.");
                continue;
            }
            //disparo
            if (cell == 'I') {
                opponent.getBoard()[fila][columna] = 'X';
                currentPlayer.getOut().println("¡Impacto!");
                opponent.getOut().println("Tu rival ha impactado en " + input + ". ¡Has sido alcanzado!");
                turnoExtra = true;
            }            else {
                opponent.getBoard()[fila][columna] = '*';
                currentPlayer.getOut().println("Agua.");
                opponent.getOut().println("Tu rival ha disparado en " + input + " y ha fallado.");
                turnoExtra = false;
            }

            opponent.enviarTablero();

            if (verificarDerrota(opponent.getBoard())) {
                currentPlayer.getOut().println("¡Victoria! Has vencido a tu rival.");
                opponent.getOut().println("¡Derrota! Todos tus barcos han sido hundidos.");
                partidaActiva = false;
                break;
            }

            if (!turnoExtra) {
                ClienteHandler temp = currentPlayer;
                currentPlayer = opponent;
                opponent = temp;
            }
        }

        // Preguntar a ambos jugadores si desean jugar de nuevo
        jugador1.getOut().println("¿Quieres jugar de nuevo? (si/no)");
        jugador2.getOut().println("¿Quieres jugar de nuevo? (si/no)");
        String resp1 = jugador1.getIn().readLine();
        String resp2 = jugador2.getIn().readLine();
        boolean replay = (resp1 != null && resp1.trim().equalsIgnoreCase("si") &&
                resp2 != null && resp2.trim().equalsIgnoreCase("si"));
        return replay;
    }

    private boolean verificarDerrota(char[][] board) {
        for (int i = 0; i < ClienteHandler.SIZE; i++) {
            for (int j = 0; j < ClienteHandler.SIZE; j++) {
                if (board[i][j] == 'I') {
                    return false;
                }
            }
        }
        return true;
    }
}
