
package echoserver;

import java.net.*;
import java.io.*;

public class EchoServer {
    Game waitingGame= null;
    Player player;
    Game lastGame=null;
    Lobby lobby=new Lobby();
    Game game;
    public void startServer() {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
             serverSocket = new ServerSocket(6666);
        } catch (IOException e) {
            System.out.println(
                    "Błąd przy tworzeniu gniazda serwerowego " + e);
            System.exit(-1);
        }
        System.out.println("Inicjalizacja gniazda zakończona...");
        System.out.println("Parametry gniazda: " + serverSocket);
        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("Błąd wejścia-wyjścia: " + e);
            }
            System.out.println("Nadeszło połączenie...");
            System.out.println("Parametry połączenia: " + socket);
            Player player = null;
            try {
                player = lobby.joinGame(socket);
                game = lobby.games.get(player.roomID);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            new Thread(new EchoServerThread(socket, game, player )).start();
        }
    }


    public static void main(String[] args) {
       new EchoServer().startServer();

    }
}
