package echoserver;


import java.net.*;
import java.io.*;

public class Client {

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
            String identity=brSockInp.readLine();//Reading if player is X or O
            if(identity.equals("You are Player X")){
                System.out.println(identity);
                readMessage(brSockInp);//Enter size of board's side
                sendMessage(out, brLocalInp);
                readMessage(brSockInp);//Field created. Waiting for player

            }else{
                System.out.println(identity);
            }
            readMessage(brSockInp);//Both Players active, game on!
            if(identity.equals("You are Player O")){
                System.out.println("Player X turn");
            }
            String currentPlayer;
            //Main game loop
            while(true){
                currentPlayer=brSockInp.readLine();
                if( (currentPlayer.equals("Player X turn")&&identity.equals("You are Player X") )||
                        ( currentPlayer.equals("Player O turn")&&identity.equals("You are Player O") ) ){

                    readBoard(brSockInp);
                    System.out.println(currentPlayer);
                    sendMessage(out,brLocalInp);//Send move

                    line=checkValid(out, brSockInp,brLocalInp);//Check if move is valid
                    readBoard(line,brSockInp);

                String checkResult=brSockInp.readLine();
                    if(checkResult.equals("Player X wins!") || checkResult.equals("Player O wins!") || checkResult.equals("TIE!")){
                        readBoard(brSockInp);
                        System.out.println(checkResult);
                        break;
                    }

                    if(identity.equals("You are Player X")){
                        System.out.println("Player O turn");
                    } else {
                        System.out.println("Player X turn");
                    }

                } else if(currentPlayer.equals("Player X wins!") || currentPlayer.equals("Player O wins!") || currentPlayer.equals("TIE!")){
                    readBoard(brSockInp);
                    System.out.println(currentPlayer);
                    break; //For if other client's move leads to victory/tie
                }


            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    static String checkValid(DataOutputStream out, BufferedReader brSockInp, BufferedReader brLocalInp) throws IOException {
        String line=brSockInp.readLine();
        while(line.equals("Invalid move, make proper move!")){
            System.out.println(line);
            out.writeBytes(brLocalInp.readLine()+"\n");
            out.flush();
            line=brSockInp.readLine();
        }
        return line;
    }

    static String readMessage(BufferedReader brSockInp) throws IOException {
        String line=brSockInp.readLine();
        System.out.println(line);
        return line;
    }

    static void sendMessage(DataOutputStream out, BufferedReader brLocalInp) throws IOException {
        String line = brLocalInp.readLine();
        out.writeBytes(line+"\n");
        out.flush();//Giving size
    }
    static void readBoard(BufferedReader brSockInp) throws IOException {
        String boardLine = brSockInp.readLine();
        while(!(boardLine.equals("END"))) {
            System.out.println(boardLine);
            boardLine=brSockInp.readLine();
        }
    }

    static void readBoard(String line, BufferedReader brSockInp) throws IOException {
        String boardLine = line;
        while(!(boardLine.equals("END"))) {
            System.out.println(boardLine);
            boardLine=brSockInp.readLine();
        }
    }
}
