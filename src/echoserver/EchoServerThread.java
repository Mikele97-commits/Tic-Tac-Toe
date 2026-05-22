
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
    String line = null;

    //Creating game
      try {
          int size=0;
          System.out.println("Thread for " + player.symbol + " got game: " + game.hashCode());

          if(player.symbol==Cell.X){
              game.sendMessage("You are Player X", game.playerX);
              game.sendMessage("Enter size of board's side", game.playerX);
              size = Integer.parseInt(game.playerX.input.readLine());
              game.initField(size);
              game.sendMessage("Field created. Waiting for player", game.playerX);
              synchronized (game){
                  game.wait();
              }
          }else{
              synchronized(game){
                  game.notify();
                  game.sendMessage("You are Player O",game.playerO);
                  game.sendBoth("Both Players active, game on!\n");
                  game.wait();
              }
          }


          //Main game loop
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
                     game.sendBoth("Player X wins!\n");
                     game.displayBoard(game);
                     return;
                  }

                if(CheckVictory.tie(game.field)){
                    synchronized(game){ game.notify();}
                    game.sendBoth("TIE!\n");
                    game.displayBoard(game);
                    return;
                }else {
                    game.sendMessage("NOT", game.playerX);
                }

                  synchronized (game){game.notify();}
                  synchronized (game){game.wait();}
              }

              if(player.symbol==Cell.O){
                  line = game.playerO.input.readLine();
                  while (!game.makeMove(line, game.playerO)) {
                      out.writeBytes("Invalid move, make proper move!\n");
                      out.flush();
                      line = brinp.readLine();
                  }
                  game.displayBoard(game);

                  if(CheckVictory.check(line,game.field)){
                      synchronized(game){ game.notify();}
                      game.sendBoth("Player X wins!\n");
                      game.displayBoard(game);
                      return;
                  }

                  if(CheckVictory.tie(game.field)){
                      synchronized(game){ game.notify();}
                      game.sendBoth("TIE!\n");
                      game.displayBoard(game);
                      return;
                  }else {
                      game.sendMessage("NOT", game.playerX);
                  }

                  synchronized (game){game.notify();}
                  synchronized (game){game.wait();}
              }

          }

      } catch (IOException | InterruptedException e) {
          throw new RuntimeException(e);
      }



  }

}
