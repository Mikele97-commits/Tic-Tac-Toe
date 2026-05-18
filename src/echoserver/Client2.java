package echoserver;

import java.net.*;
import java.io.*;

public class Client2 {

    public static void main(String args[]) {
        String host = "localhost";
        int port = 0;
        try {
            port = 6666;
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
            String identity=brSockInp.readLine();
            if(identity.equals("You are Player X")){
                System.out.println(identity);
                System.out.println(brSockInp.readLine());//Give size
                out.writeBytes(brLocalInp.readLine()+"\n");
                out.flush();//Giving size
                System.out.println(brSockInp.readLine());//Waiting
            }else{
                System.out.println(identity);
            }

            System.out.println(brSockInp.readLine());



            String boardLine;
            while(!(boardLine = brSockInp.readLine()).equals("END")) {
                System.out.println(boardLine);
            }//Initial display board

            //Main game loop
            while(true){
                line=brSockInp.readLine();
                System.out.println(line);//Display Player X turn

                out.writeBytes(brLocalInp.readLine()+"\n");
                out.flush();//Send move

                line=brSockInp.readLine();
                while(line.equals("Invalid move, make proper move!")){
                    System.out.println(line);
                    out.writeBytes(brLocalInp.readLine()+"\n");
                    out.flush();
                    line=brSockInp.readLine();
                }
                boardLine=line;
                while(!(boardLine.equals("END"))) {
                    System.out.println(boardLine);
                    boardLine=brSockInp.readLine();
                }//Display field

                String checkVictory = brSockInp.readLine();
                if(checkVictory.equals("Player X wins!") || checkVictory.equals("Player O wins!")){
                    System.out.println(checkVictory);
                    return;
                }
                String checkTie = brSockInp.readLine();
                if(checkTie.equals("TIE!")){
                    System.out.println(checkTie);
                    return;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

