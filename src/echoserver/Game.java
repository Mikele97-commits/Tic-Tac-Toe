package echoserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class Game {

    int tieCounter=0;
    int tileNumber;
    int lastX;
    int lastY;
    int Tiles;
    Cell[][] field;
    GameState state = GameState.WAITING;
    Player playerX=new Player(Cell.X);
    Player playerO=new Player(Cell.O);

    public Game(int Tiles) {
        this.Tiles = Tiles;
        tileNumber = Tiles*Tiles;
        field =new Cell[Tiles][Tiles];

    }

    public static void createNewField(Cell[][] field) {
        for (Cell[] cells : field) {
            Arrays.fill(cells, Cell.EMPTY);
        }
    }

    public boolean makeMove(String move, Player player) {
        if (move.length() != 2) {
            System.out.println("Invalid input");
            return false;
        }
        String xString = move.substring(0, 1);
        int x = xString.charAt(0)-65;
        if (x<0 || x>Tiles - 1) {
            System.out.println("Invalid input x, out of bounds");
            return false;
        }
        int y = Integer.parseInt(move.substring(1, 2))-1;
        if (y<0 || y>Tiles - 1) {
            System.out.println("Invalid input y, out of bounds");
            return false;
        }

        if (field[y][x] != Cell.EMPTY) {
            System.out.println("Cell is already occupied");
            return false;
        }

        field[y][x] = player.symbol;
        lastX=x;
        lastY=y;

        return true;
    }



    public void round(Socket socket, Game game){
        BufferedReader brinp = null;
        DataOutputStream out = null;
        String threadName = Thread.currentThread().getName();

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
        /*System.out.println("Give size of side");

        int x = sc.nextInt();*/
        Scanner sc = new Scanner(System.in);
        //Game game = new Game(x);
        createNewField(game.field);
        GameDisplay.display(game.field);
        while(true){
            System.out.println("Player X:");
            String move = sc.next();
            while (!game.makeMove(move, playerX)) {
                System.out.println("Invalid move, please try again");
                move = sc.next();
            }
            GameDisplay.display(game.field);
            if (CheckVictory.check(game.lastY, game.lastX, game.field )){
                System.out.println("Player X wins!");
                return;
            }
            game.tieCounter++;
            if (game.tieCounter==game.tileNumber){
                System.out.println("DRAW");
                return;
            }

            System.out.println("Player O:");
            move = sc.next();
            while (!game.makeMove(move, playerO)) {
                System.out.println("Invalid move, please try again");
                move = sc.next();
            }
            GameDisplay.display(game.field);
            if (CheckVictory.check(game.lastY, game.lastX, game.field )){
                System.out.println("Player Y wins!");
                return;
            }
            game.tieCounter++;
            if (game.tieCounter==game.tileNumber){
                System.out.println("DRAW");
                return;
            }
        }

    }
}

enum GameState {
    WAITING,
    PLAYING,
    FINISHED
}

