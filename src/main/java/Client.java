import java.net.*;
import java.io.*;

public class Client {

    public static void main(String[] args) {
        User user;
        String host;
        if(args.length > 0){
            System.out.println("args found, host: " + args[0]);
            host = args[0];
        }else {
            System.out.println("No host specified");
            host = "localhost";
        }
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
        String line;
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

        //Register/login

        try {

            while (true) {
                readMessage(brSockInp);//Login/register?
                String answer = brLocalInp.readLine();
                if (!(answer.equals("L") || answer.equals("R"))) {
                    System.out.println("Wrong input");
                } else {
                    out.writeBytes(answer + "\n");
                    out.flush();
                    if (answer.equals("L")) {
                        login(brSockInp, brLocalInp, out);
                        break;
                    } else {
                        register(brSockInp, brLocalInp, out);
                    }

                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        //Game
        try{
            //Session loop
            while(true) {
                System.out.println("Beginning of session loop");
                String identity = brSockInp.readLine();//Reading if player is X or O
                if (identity.equals("You are Player X")) {
                    System.out.println(identity);
                    readMessage(brSockInp);//Enter size of board's side
                    sendMessage(out, brLocalInp);
                    readMessage(brSockInp);//Field created. Waiting for player

                } else {
                    System.out.println(identity+"\nWaiting for game creation");
                }
                readMessage(brSockInp);//Both Players active, game on!
                if (identity.equals("You are Player O")) {
                    System.out.println("Player X turn");
                }
                String currentPlayer;

                //Main game loop
                while (true) {
                    currentPlayer = brSockInp.readLine();
                    if(currentPlayer.equals("Player X wins!")||currentPlayer.equals("Player O wins!")||currentPlayer.equals("TIE!")) {
                        readBoard(brSockInp);
                        System.out.println(currentPlayer);
                        break;
                    }
                    if ((currentPlayer.equals("Player X turn") && identity.equals("You are Player X")) ||
                            (currentPlayer.equals("Player O turn") && identity.equals("You are Player O"))) {

                        readBoard(brSockInp);
                        System.out.println(currentPlayer);
                        sendMessage(out, brLocalInp);//Send move

                        line = checkValid(out, brSockInp, brLocalInp);//Check if move is valid
                        readBoard(line, brSockInp);



                        if (identity.equals("You are Player X")) {
                            System.out.println("Player O turn");
                        } else {
                            System.out.println("Player X turn");
                        }

                    }

                }

                readMessage(brSockInp);//Ask for rematch
                sendMessage(out, brLocalInp);
                String rematch= brSockInp.readLine();
                if(rematch.equals("No rematch :c. Bye bye")) {
                    System.out.println(rematch);
                    break;
                }
                readMessage(brSockInp);//Read new symbol
                System.out.println(rematch);

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
    static void login(BufferedReader brSockInp, BufferedReader brLocalInp, DataOutputStream out) throws IOException {
        readMessage(brSockInp);//"Enter your username"
        while (true) {
            sendMessage(out, brLocalInp);
            String correct = brSockInp.readLine();
            if (correct.equals("Good")) {
                break;
            }else{
                System.out.println(correct);
            }
        }
        readMessage(brSockInp);//"Enter password"
        sendMessage(out, brLocalInp);
        while (true) {
            String line= brSockInp.readLine();
            System.out.println(line);
            if (line.equals("Password correct")) {
                break;
            }else {
                sendMessage(out, brLocalInp);
            }
        }
        readMessage(brSockInp);

    }

    static void register(BufferedReader brSockInp, BufferedReader brLocalInp, DataOutputStream out) throws IOException {
        readMessage(brSockInp);//"Enter your username"
        while (true) {
            sendMessage(out, brLocalInp);
            String correct = brSockInp.readLine();
            if (correct.equals("Good")) {
                break;
            }else{
                System.out.println(correct);
            }
        }
        readMessage(brSockInp);//"Enter password"
        sendMessage(out, brLocalInp);
        readMessage(brSockInp);//Account correctly created
    }
}