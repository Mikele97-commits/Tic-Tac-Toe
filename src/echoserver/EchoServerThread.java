
package echoserver;


import java.net.*;
import java.io.*;

public class EchoServerThread implements Runnable
{
  protected Socket socket;
  public EchoServerThread(Socket clientSocket)
  {
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

    int size;
    //Creating game
      try {
          out.writeBytes("Give size\n");
          out.flush();
          line= brinp.readLine();
          size=Integer.parseInt(line);
          Game game = new Game(size);
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
      } catch (IOException e) {
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
