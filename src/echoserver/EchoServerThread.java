
package echoserver;


import java.net.*;
import java.io.*;

public class EchoServerThread implements Runnable
{
  protected Socket socket;
  private Game game;
  private Player player;
  private Player currentPlayer;
  public EchoServerThread(Socket clientSocket, Game game, Player player) {
    this.game=game;
    this.player=player;
    this.socket = clientSocket;
  }
  public void run()
  {
    //Deklaracje zmiennych
    BufferedReader brinp = null;
    DataOutputStream out = null;
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

    //Creating game
      try {
        while (true) {
            gameInit();
            gameLoop(brinp, out);


            if (player.symbol == Cell.X) {
                game.sendBoth("Do you want to play again?");
                game.playerXAnswer = game.playerX.input.readLine();
                synchronized (game) {
                    game.notify();
                }
                synchronized (game) {
                    game.wait();
                }
            } else {
                synchronized (game) {
                    game.wait();
                }
                game.playerOAnswer = game.playerO.input.readLine();
                synchronized (game) {
                    game.notify();
                }
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
                    synchronized (game) {
                        game.wait();
                    }
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
        int size=0;
        System.out.println("Thread for " + player.symbol + " got game: " + game.hashCode());
        if(player.symbol==Cell.X){
            game.sendMessage("You are Player X", game.playerX);
            game.sendMessage("Enter size of board's side", game.playerX);
            size = Integer.parseInt(game.playerX.input.readLine());
            game.initField(size);
            game.sendMessage("Field created. Waiting for player", game.playerX);
            synchronized (game){
                game.state=GameState.CREATED;
                game.wait();
            }
        }else{
            synchronized(game){
                while(game.state==GameState.CREATING){
                    System.out.println("waiting");
                    game.wait(1000);
                }
                game.notify();
                game.sendMessage("You are Player O",game.playerO);
                game.sendBoth("Both Players active, game on!");
                game.wait();
            }
        }
    }

    public void gameLoop(BufferedReader brinp, DataOutputStream out) throws IOException, InterruptedException {
        String line = null;

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
                    synchronized(game){ game.notify();}
                    game.sendBoth("Player X wins!");
                    game.displayBoard(game);
                    game.gameOver=true;
                    break;
                }

                if(CheckVictory.tie(game.field)){
                    synchronized(game){ game.notify();}
                    game.sendBoth("TIE!");
                    game.displayBoard(game);
                    game.gameOver=true;
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
                    synchronized(game){ game.notify();}
                    game.sendBoth("Player O wins!");
                    game.displayBoard(game);
                    game.gameOver=true;
                    break;
                }

                if(CheckVictory.tie(game.field)){
                    synchronized(game){ game.notify();}
                    game.sendBoth("TIE!");
                    game.displayBoard(game);
                    game.gameOver=true;
                    break;
                }
                synchronized (game){game.notify();}
                synchronized (game){game.wait();}
            }
            if(game.gameOver){
                break;
            }

        }
    }

}
