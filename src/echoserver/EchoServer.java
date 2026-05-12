
package echoserver;

import java.net.*;
import java.io.*;

public class EchoServer {


    public static void main(String[] args) {
        Game game=null;
        ServerSocket serverSocket = null;
        Socket socket = null;
        Player player = joinGame(socket);
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
            new Thread(new EchoServerThread(socket, game, )).start();
        }
    }

    public void joinGame(Socket socket) {

    }
}
