package echoserver;

/**
 *
 * @author dzelazny
 */
import echoserver.Game;

import java.net.*;
import java.io.*;

public class Client {

    public static void main(String args[]) {
        String host = "localhost";
        int port = 0;
        try {
            port = new Integer("6666").intValue();
        } catch (NumberFormatException e) {
            System.out.println("Nieprawidłowy argument: port");
            System.exit(-1);
        }
        //Inicjalizacja gniazda klienckiego
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(host, port);
        } catch (UnknownHostException e) {
            System.out.println("Nieznany host.");
            System.exit(-1);
        } catch (ConnectException e) {
            System.out.println("Połączenie odrzucone.");
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("Błąd wejścia-wyjścia: " + e);
            System.exit(-1);
        }
        System.out.println("Połączono z " + clientSocket);

        //Deklaracje zmiennych strumieniowych 
        String line = null;
        BufferedReader brSockInp = null;
        BufferedReader brLocalInp = null;
        DataOutputStream out = null;


        //Utworzenie strumieni
        try {
            out = new DataOutputStream(clientSocket.getOutputStream());
            brSockInp = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            brLocalInp = new BufferedReader(
                    new InputStreamReader(System.in));
        } catch (IOException e) {
            System.out.println("Błąd przy tworzeniu strumieni: " + e);
            System.exit(-1);
        }

        //Creating game
        try{
            System.out.println(brSockInp.readLine());
            out.writeBytes(brLocalInp.readLine()+"\n");
            out.flush();
            System.out.println(brSockInp.readLine());
            String boardLine;
            while(!(boardLine = brSockInp.readLine()).equals("END")) {
                System.out.println(boardLine);
            }
            System.out.println(brSockInp.readLine());
            while(true){
                out.writeBytes(brLocalInp.readLine()+"\n");
                out.flush();
                System.out.println(brSockInp.readLine());
                while(!(boardLine = brSockInp.readLine()).equals("END")) {
                    System.out.println(boardLine);
                }
                System.out.println(brSockInp.readLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //Pętla główna klienta
        /*while (true) {
            try {
                if (line != null) {
                    System.out.println("Wysyłam: " + line);
                    out.writeBytes(line + '\n');
                    out.flush();
                }
                if (line == null || "quit".equals(line)) {
                    System.out.println("Kończenie pracy...");
                    clientSocket.close();
                    System.exit(0);
                }
                brSockInp.readLine();
                System.out.println("Otrzymano: " + line);
            } catch (IOException e) {
                System.out.println("Błąd wejścia-wyjścia: " + e);
                System.exit(-1);
            }
        }*/
    }
}
