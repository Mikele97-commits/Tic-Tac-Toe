
package echoserver;

import java.net.*;
import java.io.*;

public class EchoServer {
    Game waitingGame= null;
    Player player;
    Game lastGame=null;
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
                player = joinGame(socket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            new Thread(new EchoServerThread(socket, lastGame, player )).start();
        }
    }


    public static void main(String[] args) {
       new EchoServer().startServer();

    }

    public synchronized Player joinGame(Socket socket) throws IOException {
        if(waitingGame == null) {
            waitingGame=new Game();
            lastGame=waitingGame;
            waitingGame.playerX=new Player(socket, Cell.X);
            return waitingGame.playerX;
        }else{
            Game game=waitingGame;
            if(game.state!=GameState.WAITING){
                System.out.println("Game full");
            }
            waitingGame.playerO=new Player(socket, Cell.O);
            lastGame=waitingGame;
            waitingGame=null;
            game.state=GameState.PLAYING;
            return game.playerO;
        }

    }


}
