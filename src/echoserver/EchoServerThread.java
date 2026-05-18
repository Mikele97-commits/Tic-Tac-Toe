
package echoserver;


import java.net.*;
import java.io.*;

public class EchoServerThread implements Runnable
{
  protected Socket socket;
  private Game game;
  private Player player;
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
          int size;
          if(player.symbol==Cell.X){
              game.playerX.out.writeBytes("You are Player X\n");
              game.playerX.out.flush();
              game.playerX.out.writeBytes("Enter size of board's side\n");
              game.playerX.out.flush();
              size =Integer.parseInt(game.playerX.input.readLine());
              game.initField(size);
              game.playerX.out.writeBytes("Field created. Waiting for player\n");
              game.playerX.out.flush();
              synchronized (game){
                  game.wait();
              }
          }else{
              synchronized(game){
                  game.notify();
              }
              game.playerO.out.writeBytes("You are Player O\n");
              game.playerO.out.flush();
          }

          game.sendBoth("Both Players active, game on!\n");

          line= brinp.readLine();
          size=Integer.parseInt(line);
          Game.createNewField(game.field);//Taking size, creating game, filling field with EMPTY

          out.writeBytes("Created game of size "+size+"\n");
          out.flush();
          String board = GameDisplay.display(game.field);
          out.writeBytes(board);
          out.writeBytes("END\n");
          out.flush();//Initial display of field
          out.writeBytes("Game starts.");
          //Main game loop
          while (true){
              out.writeBytes("Player X turn\n");
              out.flush();

              line = brinp.readLine();//Receive coordinates

              while(!game.makeMove(line,game.playerX)){
                 out.writeBytes("Invalid move, make proper move!\n");
                 out.flush();
                 line = brinp.readLine();
              }



              board = GameDisplay.display(game.field);
              out.writeBytes(board);
              out.writeBytes("END\n");
              out.flush();//Display board

              if(CheckVictory.check(line,game.field)){
                  out.writeBytes("Player X wins!\n");
                  out.flush();
              }else {
                  out.writeBytes("NOT\n");
                  out.flush();
              }

              if(CheckVictory.tie(game.field)){
                  out.writeBytes("TIE!\n");
                  out.flush();
              }else {
                  out.writeBytes("NOT\n");
                  out.flush();
              }

              out.writeBytes("Player O turn\n");
              out.flush();//Send Player O turn

              line = brinp.readLine();
              while(!game.makeMove(line,game.playerO)){
                  out.writeBytes("Invalid move, make proper move!\n");
                  out.flush();
                  line = brinp.readLine();
              }

              board = GameDisplay.display(game.field);
              out.writeBytes(board);
              out.writeBytes("END\n");
              out.flush();

              if(CheckVictory.check(line,game.field)){
                  out.writeBytes("Player O wins!\n");
                  out.flush();
              }else {
                  out.writeBytes("NOT\n");
                  out.flush();
              }

              if(CheckVictory.tie(game.field)){
                  out.writeBytes("TIE!\n");
                  out.flush();
              }else {
                  out.writeBytes("NOT\n");
                  out.flush();
              }
          }
      } catch (IOException | InterruptedException e) {
          throw new RuntimeException(e);
      }


      /*while(true){
      try{
        line = brinp.readLine();
        System.out.println(threadName + "| Odczytano linię: " + line);
        
        //badanie warunku zakończenia pracy
        if((line == null) || "quit".equals(line)){
          System.out.println(threadName + "| Zakończenie pracy z klientem: " + socket);
          socket.close();
          return;
        }
        else{ //odesłanie danych do klienta
          out.writeBytes(line + "\n\r");
          System.out.println(threadName + "| Wysłano linię: " + line);
        }
      }
      catch(IOException e){
        System.out.println(threadName + "| Błąd wejścia-wyjścia." + e);
        return;
      }
    }*/
  }

}
