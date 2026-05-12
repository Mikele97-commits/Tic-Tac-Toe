/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
          Game.createNewField(game.field);
          out.writeBytes("Created game of size "+size+"\n");
          out.flush();
          String board = GameDisplay.display(game.field);
          out.writeBytes(board);
          out.writeBytes("END\n");
          out.flush();
          out.writeBytes("Game starts. Player X turn\n");
          out.flush();

          while (true){
              line = brinp.readLine();
              if(game.makeMove(line,game.playerX)){
                  board = GameDisplay.display(game.field);
                  out.writeBytes(board);
                  out.writeBytes("END\n");
                  out.flush();
              }
              out.writeBytes("Player O turn");
              line = brinp.readLine();
              if(game.makeMove(line,game.playerO)){
                  board = GameDisplay.display(game.field);
                  out.writeBytes(board);
                  out.writeBytes("END\n");
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
