import at.favre.lib.crypto.bcrypt.BCrypt;

import java.net.*;
import java.io.*;
import java.util.Properties;

public class EchoServerThread implements Runnable
{

    protected Socket socket;
    private final Game game;
    private final Player player;
    User user;
    public EchoServerThread(Socket clientSocket, Game game, Player player) {
        this.game=game;
        this.player=player;
        this.socket = clientSocket;
    }
    public void run()
    {
        //Deklaracje zmiennych
        BufferedReader brinp;
        DataOutputStream out;
        String threadName = Thread.currentThread().getName();


        //inicjalizacja strumieni
        try{
            brinp = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()
                    )
            );
            out = new DataOutputStream(socket.getOutputStream());
        }
        catch(IOException e){
            System.out.println(threadName + "| Błąd przy tworzeniu strumieni " + e);
            return;
        }
        //Register/Login
        try {
            while(true) {
                game.sendMessage("Would you like to (L)ogin or (R)egister", player);
                String response = brinp.readLine();
                if (response.equals("L")) {
                    login(brinp);
                    break;
                } else {
                    register(brinp);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //Game
        try {
            while (true) {
                gameInit();
                gameLoop(brinp, out);

                if (player.symbol == Cell.X) {
                    game.sendBoth("Do you want to play again?");
                    game.playerXAnswer = game.playerX.input.readLine();
                    System.out.println("Player X answer: " + game.playerXAnswer);
                    synchronized (game) {game.notify();}
                    synchronized (game) {game.wait();}
                } else {
                    synchronized (game) {game.wait();}
                    game.playerOAnswer = game.playerO.input.readLine();
                    System.out.println("Player O answer: " + game.playerOAnswer);
                    synchronized (game) {game.notify();}
                }

                if (!(game.playerXAnswer.equalsIgnoreCase("y") && game.playerOAnswer.equalsIgnoreCase("y"))) {
                    game.sendBoth("No rematch :c. Bye bye");
                    break;
                }else {
                    if (player.symbol == Cell.O) {

                        game.sendMessage("REMATCH!",player);
                        game.sendMessage("you had symbol O, now you have symbol X", player);
                        player.symbol = Cell.X;
                        game.gameOver = false;
                        game.state=GameState.CREATING;
                        Player temp = game.playerX;
                        game.playerX = game.playerO;
                        game.playerO = temp;
                        synchronized (game) {
                            game.notify();
                        }

                    }else{
                        game.sendMessage("REMATCH!",player);
                        game.sendMessage("you had symbol X, now you have symbol O", player);
                        player.symbol = Cell.O;
                    }

                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void gameInit() throws IOException, InterruptedException {
        int size;
        System.out.println("Thread for " + player.symbol + " got game: " + game.hashCode());
        if(player.symbol==Cell.X){
            game.sendMessage("You are Player X", game.playerX);//Identity in client
            game.sendMessage("Enter size of board's side", game.playerX);
            size = Integer.parseInt(game.playerX.input.readLine());
            game.initField(size);
            game.sendMessage("Field created. Waiting for player", game.playerX);
            synchronized (game){
                game.state=GameState.CREATED;
                game.wait();
            }
        }else{ //If player O
            synchronized(game){
                game.sendMessage("You are Player O",game.playerO);//Identity in client
                while(game.state==GameState.CREATING){
                    System.out.println("waiting");
                    game.wait(1000); //Waiting for creation of board by Player X
                }
                game.notify();
                game.sendBoth("Both Players active, game on!");
                game.wait();
            }
        }
    }

    public void gameLoop(BufferedReader brinp, DataOutputStream out) throws IOException, InterruptedException {
        String line;

        while(true){
            if(player.symbol==Cell.X){
                game.sendBoth("Player X turn");
            } else{
                game.sendBoth("Player O turn");
            }//Sends currentPlayer

            game.displayBoard(game);



            if(player.symbol==Cell.X) {
                line = game.playerX.input.readLine();
                while (!game.makeMove(line, game.playerX)) {
                    game.sendMessage("Invalid move, make proper move!", game.playerX);
                    line = brinp.readLine();
                }

                game.displayBoard(game);

                if(CheckVictory.check(line,game.field)){
                    game.gameOver=true;
                    game.sendBoth("Player X wins!");
                    Database.addPoints(user.username, 3);
                    game.displayBoard(game);
                    synchronized(game){ game.notify();}
                    break;
                }

                if(CheckVictory.tie(game.field)){
                    game.sendBoth("TIE!");
                    game.displayBoard(game);
                    game.gameOver=true;
                    synchronized(game){ game.notify();}
                    break;
                }

                synchronized (game){game.notify();}
                synchronized (game){game.wait();}
            }

            if(player.symbol==Cell.O){
                line = game.playerO.input.readLine();
                while (!game.makeMove(line, game.playerO)) {
                    out.writeBytes("Invalid move, make proper move!");
                    out.flush();
                    line = brinp.readLine();
                }
                game.displayBoard(game);

                if(CheckVictory.check(line,game.field)){
                    game.gameOver=true;
                    game.sendBoth("Player O wins!");
                    Database.addPoints(user.username, 3);
                    game.displayBoard(game);
                    synchronized(game){ game.notify();}
                    break;
                }

                if(CheckVictory.tie(game.field)){
                    game.sendBoth("TIE!");
                    game.displayBoard(game);
                    game.gameOver=true;
                    synchronized(game){ game.notify();}
                    break;
                }
                synchronized (game){game.notify();}
                synchronized (game){game.wait();}
            }
            if(game.gameOver){
                Database.addPoints(user.username, -1);
                break;
            }

        }
    }

    public void login(BufferedReader brinp) throws IOException {
        game.sendMessage("Enter your Username", player);
        String username = brinp.readLine();
        while(true){
            if(!Database.userExists(username)){
                game.sendMessage("This username doesn't exist. Try different username", player);
                username = brinp.readLine();
            }else{
                game.sendMessage("Good", player);
                break;
            }
        }
        game.sendMessage("Enter your Password", player);
        String password = brinp.readLine();
        String passAndPoints=Database.giveProp(username);
        String[] split=passAndPoints.split("\\|");
        String pass=split[0];
        int points=Integer.parseInt(split[1]);
        byte[] passByte=pass.getBytes();
        while(true){
            if(BCrypt.verifyer().verify(password.toCharArray(),passByte).verified){
                game.sendMessage("Password correct", player);
                break;
            }else {
                game.sendMessage("Wrong password", player);
                password = brinp.readLine();
            }
        }
        user=new User(username,points);
        game.sendMessage(username+" logged in. You have "+user.getPoints()+" points.", player);
    }
    public void register(BufferedReader brinp) throws IOException {
        game.sendMessage("Enter your Username", player);
        String username = brinp.readLine();
        while(true){
            if(Database.userExists(username)){
                game.sendMessage("This username already registered. Try different username", player);
                username = brinp.readLine();
            }else{
                game.sendMessage("Good", player);
                break;
            }
        }

        game.sendMessage("Enter your Password", player);
        String password = brinp.readLine();
        Database.addUser(username, password);
        game.sendMessage("Account correctly created", player);
        user=new User(username,0);


    }
}
